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
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.server.query.ReaderByCollection;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EntryAuthorizationImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplAvailableMonitorableIsNotFalseReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImportableView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.PaymentCreditImpl;

@ApplicationScoped
public class ExpenditureBusinessImpl extends AbstractExpenditureResourceBusinessImpl<Expenditure> implements ExpenditureBusiness,Serializable {

	@Override
	void __listenPostConstruct__() {
		entityClass = Expenditure.class;
		entityViewClass = ExpenditureView.class;
		entityImportableClass = ExpenditureImportableView.class;
		super.__listenPostConstruct__();
	}
	
	@Override @Transactional
	public Result adjust(Map<String, Long[]> adjustments,String auditWho) {
		return adjust(adjustments,generateAuditIdentifier(), auditWho, ADJUST_AUDIT_IDENTIFIER);
	}
	
	private Result adjust(Map<String, Long[]> adjustments,String auditIdentifier,String auditWho,String auditFunctionality) {
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
			audit(expenditure,auditIdentifier,auditFunctionality,auditWho,auditWhen);
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
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> new Long[] {entry.getValue(),entry.getValue()})),generateAuditIdentifier(),auditWho,ADJUST_BY_ENTRY_AUTHORIZATIONS_AUDIT_IDENTIFIER);
	}
	
	@Override @Transactional
	public Result copy(String legislativeActVersionIdentifier, String legislativeActVersionSourceIdentifier,String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.Expenditure.validateCopyAdjustmentsInputs(legislativeActVersionIdentifier, legislativeActVersionSourceIdentifier, auditWho, throwablesMessages, entityManager);
		throwablesMessages.throwIfNotEmpty();		
		Integer count = copy((LegislativeActVersionImpl)instances[0], (LegislativeActVersionImpl)instances[1],generateAuditIdentifier(),COPY_ADJUSTMENTS_AUDIT_IDENTIFIER, auditWho,LocalDateTime.now());		
		// Return of message
		result.close().setName(String.format("Copie de %s ajustement(s) de %s vers %s par %s",count,legislativeActVersionSourceIdentifier,legislativeActVersionIdentifier,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre d'ajustements copiés : %s", count));
		return result;
	}
	
	public Integer copy(LegislativeActVersionImpl legislativeActVersion, LegislativeActVersionImpl legislativeActVersionSource,String auditIdentifier,String auditWho,String auditFunctionality,LocalDateTime auditWhen) {
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
				audit((ExpenditureImpl)expenditure,auditIdentifier, auditFunctionality, auditWho, auditWhen);
			});
			
			ExecutorService executorService = Executors.newFixedThreadPool(configuration.copy().processing().executor().thread().count());
			List<List<Expenditure>> batches = CollectionHelper.getBatches(expenditures, configuration.copy().processing().batch().size());
			batches.forEach(batch -> {
				executorService.execute(() -> {
					updateBatch(batch, EntityManagerGetter.getInstance().get(), Boolean.TRUE, null);
				});
			});
			shutdownExecutorService(executorService,configuration.copy().processing().executor().timeout().duration(),configuration.copy().processing().executor().timeout().unit());
		}
		return CollectionHelper.getSize(arrays);
	}
	
	/* Import */
	
	@Override
	Expenditure instantiateForImport(LegislativeActVersion legislativeActVersion, Object[] array) {
		return new ExpenditureImpl().setIdentifier((String)array[0]).setActVersion(legislativeActVersion).setActivityIdentifier((String)array[1]).setEconomicNatureIdentifier((String)array[2])
				.setFundingSourceIdentifier((String)array[3]).setLessorIdentifier((String)array[4]).setEntryAuthorization(new EntryAuthorizationImpl()).setPaymentCredit(new PaymentCreditImpl());
	}
}