package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.entity.EntityLifeCycleListenerImpl;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.hibernate.annotation.Hibernate;
import org.cyk.utility.persistence.server.query.ReaderByCollection;
import org.cyk.utility.persistence.server.view.MaterializedViewManager;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EntryAuthorizationImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplAvailableMonitorableIsNotFalseReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImportableView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.PaymentCreditImpl;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class ExpenditureBusinessImpl extends AbstractExpenditureResourceBusinessImpl<Expenditure> implements ExpenditureBusiness,Serializable {

	@Inject ExpenditurePersistence persistence;
	@Inject LegislativeActPersistence legislativeActPersistence;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;
	@Inject @Hibernate MaterializedViewManager materializedViewManager;

	@Override @Transactional
	public Result adjust(Map<String, Long[]> adjustments,String auditWho) {
		return adjust(adjustments, auditWho, ADJUST_AUDIT_IDENTIFIER);
	}
	
	private Result adjust(Map<String, Long[]> adjustments,String auditWho,String auditFunctionality) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		ValidatorImpl.Expenditure.validateAdjust(adjustments, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		// Validation of adjustments
		Collection<String> providedIdentifiers = adjustments.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
		
		Collection<Object[]> availablesMonitorables = new ExpenditureImplAvailableMonitorableIsNotFalseReader().readByIdentifiers(new ArrayList<String>(adjustments.keySet()), null);
		Collection<String> availablesMonitorablesIdentifiers = CollectionHelper.isEmpty(availablesMonitorables) ? null : availablesMonitorables.stream().map(array -> (String)array[0]).collect(Collectors.toList());
		Collection<Object[]> arrays = CollectionHelper.isEmpty(availablesMonitorablesIdentifiers) ? null : new ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader().readByIdentifiers(providedIdentifiers, null);
		
		//Collection<String> identifiers = CollectionHelper.isEmpty(arrays) ? null : arrays.stream().map(array -> (String)array[0]).collect(Collectors.toList());	
		//ValidatorImpl.validateIdentifiers(providedIdentifiers, identifiers, throwablesMessages);
		ValidatorImpl.Expenditure.validateAdjustmentsAvailable(adjustments, arrays, ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader.ENTRY_AUTHORIZATION_AVAILABLE_INDEX
				, ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader.PAYMENT_CREDIT_AVAILABLE_INDEX, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		// Validation of objects
		Collection<ExpenditureImpl> expenditures = entityManager.createNamedQuery(ExpenditureImpl.QUERY_READ_BY_IDENTIIFERS, ExpenditureImpl.class)
				.setParameter("identifiers", adjustments.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
				.getResultList();
		ValidatorImpl.validateIdentifiers(providedIdentifiers, FieldHelper.readSystemIdentifiersAsStrings(expenditures), throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		// Persist of objects
		LocalDateTime auditWhen = LocalDateTime.now();
		expenditures.forEach(expenditure -> {
			expenditure.getEntryAuthorization(Boolean.TRUE).setAdjustment(adjustments.get(expenditure.getIdentifier())[0]);
			expenditure.getPaymentCredit(Boolean.TRUE).setAdjustment(adjustments.get(expenditure.getIdentifier())[1]);
			audit(expenditure,auditFunctionality,auditWho,auditWhen);
			entityManager.merge(expenditure);
		});

		// Return of message
		result.close().setName(String.format("Ajustement de %s %s(s) par %s",expenditures.size(),Expenditure.NAME,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre de %s mise à jour : %s",Expenditure.NAME, expenditures.size()));
		return result;
	}
	
	@Override @Transactional
	public Result adjustByEntryAuthorizations(Map<String, Long> entryAuthorizations,String auditWho) {
		return adjust(entryAuthorizations == null ? null : Optional.ofNullable(entryAuthorizations).get().entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> new Long[] {entry.getValue(),entry.getValue()})),auditWho,ADJUST_BY_ENTRY_AUTHORIZATIONS_AUDIT_IDENTIFIER);
	}
	
	@Override @Transactional
	public Result import_(String legislativeActVersionIdentifier,Boolean throwIfRunning, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.Expenditure.validateImportInputs(legislativeActVersionIdentifier,auditWho, throwablesMessages,entityManager);
		throwablesMessages.throwIfNotEmpty();
		
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) instances[0];
		ValidatorImpl.Expenditure.validateImport(legislativeActVersion,importRunning, auditWho, throwablesMessages, entityManager);
		throwablesMessages.throwIfNotEmpty();
		
		Long count = persistence.count();
		import_(legislativeActVersion, auditWho, IMPORT_AUDIT_IDENTIFIER, LocalDateTime.now(),throwIfRunning,entityManager);
		throwablesMessages.throwIfNotEmpty();
		count = NumberHelper.getLong(NumberHelper.subtract(persistence.count(),count));
		
		// Return of message
		result.close().setName(String.format("Importation de %s %s(s) dans %s par %s",count,Expenditure.NAME,legislativeActVersion.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Nombre de %s importée : %s",Expenditure.NAME, count));
		return result;
	}
	
	@Override @Transactional
	public Result import_(String legislativeActVersionIdentifier, String auditWho) {
		return import_(legislativeActVersionIdentifier, Boolean.TRUE, auditWho);
	}
	/*
	@Override @SuppressWarnings("unchecked") 
	public void import_(LegislativeActVersionImpl legislativeActVersion, String auditWho, String auditFunctionality,LocalDateTime auditWhen,Boolean throwIfRunning, EntityManager entityManager) {
		String finalAuditFunctionality = StringHelper.isBlank(auditFunctionality) ? IMPORT_AUDIT_IDENTIFIER : auditFunctionality;
		LocalDateTime finalAuditWhen = auditWhen == null ? LocalDateTime.now() : auditWhen;
		synchronized(IMPORT_RUNNING) {
			if(ValidatorImpl.Expenditure.isImportRunning(legislativeActVersion, entityManager)) {
				String message = ValidatorImpl.Expenditure.formatMessageImportIsRunning(legislativeActVersion);
				if(Boolean.TRUE.equals(throwIfRunning))
					throw new RuntimeException(message);
				else
					LogHelper.logWarning(message, getClass());
				return;
			}
			IMPORT_RUNNING.add(legislativeActVersion.getIdentifier());
		}
		try {
			materializedViewManager.actualize(ExpenditureView.class);
			Long count = entityManager.createNamedQuery(ExpenditureImportableView.QUERY_COUNT_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER, Long.class).setParameter("legislativeActVersionIdentifier", legislativeActVersion.getIdentifier()).getSingleResult();
			List<Integer> batchSizes = NumberHelper.getProportions(count.intValue(), importBatchSize);		
			if(CollectionHelper.isNotEmpty(batchSizes)) {
				LogHelper.logInfo(String.format("Importation de %s. Traitement par lot de %s. Nombre de lot = %s", count,importBatchSize,batchSizes.size()), getClass());
				
				List<Object[]> arrays = entityManager.createNamedQuery(ExpenditureImportableView.QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER)
						.setParameter("legislativeActVersionIdentifier", legislativeActVersion.getIdentifier()).getResultList();

				List<Expenditure> expenditures = arrays.stream().map(
						array -> new ExpenditureImpl().setIdentifier((String)array[0]).setActVersion(legislativeActVersion).setActivityIdentifier((String)array[1]).setEconomicNatureIdentifier((String)array[2])
						.setFundingSourceIdentifier((String)array[3]).setLessorIdentifier((String)array[4]).setEntryAuthorization(new EntryAuthorizationImpl().setInitial((Long)array[5]).setActual((Long)array[6]))
						.setPaymentCredit(new PaymentCreditImpl().setInitial((Long)array[7]).setActual((Long)array[8])).set__auditWho__(auditWho).set__auditFunctionality__(finalAuditFunctionality).set__auditWhen__(finalAuditWhen)
						).collect(Collectors.toList());
							
				List<Object[]> lists = new ArrayList<>();
				for(Integer index =0; index < batchSizes.size(); index = index + 1)
					lists.add(new Object[] {new ArrayList<>(expenditures.subList(index*importBatchSize, index*importBatchSize+batchSizes.get(index))),index+1,batchSizes.size()});
				expenditures.clear();
				expenditures = null;
				ExecutorService executorService = Executors.newFixedThreadPool(importExecutorThreadCount);
				lists.forEach(array -> {
					executorService.execute(() -> {
						EntityManager __entityManager__ = EntityManagerGetter.getInstance().get();
						createBatch(new ArrayList<>((List<Expenditure>)array[0]),__entityManager__,Boolean.TRUE,null);
					});
				});
				shutdownExecutorService(executorService, importExecutorTimeoutDuration, importExecutorTimeoutUnit);
			}
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}finally {
			IMPORT_RUNNING.remove(legislativeActVersion.getIdentifier());
		}
	}
*/
	@Scheduled(cron = "{cyk.expenditure.import.cron}")
	void importAutomatically() {
		Collection<LegislativeActImpl> legislativeActs = CollectionHelper.cast(LegislativeActImpl.class,legislativeActPersistence.readMany(new QueryExecutorArguments()
				.addProjectionsFromStrings(LegislativeActImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_NAME).addProcessableTransientFieldsNames(LegislativeActImpl.FIELD_DEFAULT_VERSION_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_IN_PROGRESS,Boolean.TRUE)));
		if(CollectionHelper.isEmpty(legislativeActs))
			return;
		for(LegislativeActImpl legislativeAct : legislativeActs) {
			if(StringHelper.isBlank(legislativeAct.getDefaultVersionIdentifier())) {
				LogHelper.logWarning(String.format("Aucune %s ne peut être automatiquement importée dans %s car aucune version par défaut n'a été définie",Expenditure.NAME, legislativeAct.getName()), getClass());
				continue;
			}
			import_(legislativeAct.getDefaultVersionIdentifier(),Boolean.FALSE, EntityLifeCycleListenerImpl.SYSTEM_USER_NAME);
		}
	}

	@Override @Transactional
	public Result copy(String legislativeActVersionIdentifier, String legislativeActVersionSourceIdentifier,String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.Expenditure.validateCopyAdjustmentsInputs(legislativeActVersionIdentifier, legislativeActVersionSourceIdentifier, auditWho, throwablesMessages, entityManager);
		throwablesMessages.throwIfNotEmpty();		
		Integer count = copy((LegislativeActVersionImpl)instances[0], (LegislativeActVersionImpl)instances[1],COPY_ADJUSTMENTS_AUDIT_IDENTIFIER, auditWho,LocalDateTime.now());		
		// Return of message
		result.close().setName(String.format("Copie de %s ajustement(s) de %s vers %s par %s",count,legislativeActVersionSourceIdentifier,legislativeActVersionIdentifier,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre d'ajustements copiés : %s", count));
		return result;
	}
	
	public Integer copy(LegislativeActVersionImpl legislativeActVersion, LegislativeActVersionImpl legislativeActVersionSource,String auditWho,String auditFunctionality,LocalDateTime auditWhen) {
		@SuppressWarnings("unchecked")
		List<Object[]> arrays = entityManager.createNamedQuery(ExpenditureImpl.QUERY_READ_FOR_COPY_BY_ACT_VERSION_IDENTIFIER_BY_SOURCE_ACT_VERSION_IDENTIFIER).setParameter("legislativeActVersionIdentifier", legislativeActVersion.getIdentifier())
				.setParameter("legislativeActVersionSourceIdentifier", legislativeActVersionSource.getIdentifier()).getResultList();
		if(CollectionHelper.isNotEmpty(arrays)) {
			List<Expenditure> expenditures = (List<Expenditure>) new ReaderByCollection.AbstractImpl<String,Expenditure>() {
				@Override
				protected Collection<Expenditure> __read__(Collection<String> identifiers) {
					return CollectionHelper.cast(Expenditure.class, entityManager.createNamedQuery(ExpenditureImpl.QUERY_READ_BY_IDENTIIFERS, ExpenditureImpl.class).setParameter("identifiers", identifiers).getResultList());
				}
			}.read(arrays.stream().map(array -> (String)array[0]).collect(Collectors.toList()));
			
			expenditures.forEach(expenditure -> {
				Object[] array = arrays.stream().filter(a -> expenditure.getIdentifier().equals(a[0])).findFirst().get();
				((ExpenditureImpl)expenditure).getEntryAuthorization(Boolean.TRUE).setAdjustment((Long)array[1]);
				((ExpenditureImpl)expenditure).getPaymentCredit(Boolean.TRUE).setAdjustment((Long)array[2]);
				audit((ExpenditureImpl)expenditure, auditFunctionality, auditWho, auditWhen);
			});
			
			ExecutorService executorService = Executors.newFixedThreadPool(copyExecutorThreadCount);
			List<List<Expenditure>> batches = CollectionHelper.getBatches(expenditures, copyBatchSize);
			batches.forEach(batch -> {
				executorService.execute(() -> {
					updateBatch(batch, EntityManagerGetter.getInstance().get(), Boolean.TRUE, null);
				});
			});
			shutdownExecutorService(executorService, copyExecutorTimeoutDuration, copyExecutorTimeoutUnit);
		}
		return CollectionHelper.getSize(arrays);
	}
	
	/**/
	
	@Override
	Boolean isImportRunning(LegislativeActVersion legislativeActVersion, EntityManager entityManager) {
		return ValidatorImpl.Expenditure.isImportRunning(legislativeActVersion,importRunning, entityManager);
	}

	@Override
	String formatMessageImportIsRunning(LegislativeActVersion legislativeActVersion) {
		return ValidatorImpl.Expenditure.formatMessageImportIsRunning(legislativeActVersion);
	}
	
	@Override
	Expenditure instantiate(LegislativeActVersion legislativeActVersion, Object[] array) {
		return new ExpenditureImpl().setIdentifier((String)array[0]).setActVersion(legislativeActVersion).setActivityIdentifier((String)array[1]).setEconomicNatureIdentifier((String)array[2])
				.setFundingSourceIdentifier((String)array[3]).setLessorIdentifier((String)array[4]).setEntryAuthorization(new EntryAuthorizationImpl().setInitial((Long)array[5]).setActual((Long)array[6]))
				.setPaymentCredit(new PaymentCreditImpl().setInitial((Long)array[7]).setActual((Long)array[8]));
	}
	
	@Override
	Class<?> getImportableClass() {
		return ExpenditureImportableView.class;
	}
	
	@Override
	Class<?> getViewClass() {
		return ExpenditureView.class;
	}
}