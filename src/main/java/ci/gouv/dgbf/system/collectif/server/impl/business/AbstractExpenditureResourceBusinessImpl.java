package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.klass.ClassHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.object.marker.AuditableWhoDoneWhatWhen;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.SpecificPersistence;
import org.cyk.utility.persistence.entity.EntityLifeCycleListenerImpl;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.hibernate.annotation.Hibernate;
import org.cyk.utility.persistence.server.view.MaterializedViewManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureResourceBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import io.quarkus.scheduler.Scheduled;

public abstract class AbstractExpenditureResourceBusinessImpl<ENTITY> extends AbstractSpecificBusinessImpl<ENTITY> implements ExpenditureResourceBusiness<ENTITY>,Serializable{

	@Inject EntityManager entityManager;
	@Inject LegislativeActPersistence legislativeActPersistence;
	@Inject @Hibernate MaterializedViewManager materializedViewManager;
	
	@ConfigProperty(name = "cyk.import.batch.size",defaultValue = "2000")
	Integer importBatchSize;
	@ConfigProperty(name = "cyk.import.executor.thread.count",defaultValue = "4")
	Integer importExecutorThreadCount;
	@ConfigProperty(name = "cyk.import.executor.timeout.duration",defaultValue = "5")
	Long importExecutorTimeoutDuration;
	@ConfigProperty(name = "cyk.import.executor.timeout.unit",defaultValue = "MINUTES")
	TimeUnit importExecutorTimeoutUnit;
	
	@ConfigProperty(name = "cyk.copy.batch.size",defaultValue = "2000")
	Integer copyBatchSize;
	@ConfigProperty(name = "cyk.copy.executor.thread.count",defaultValue = "4")
	Integer copyExecutorThreadCount;
	@ConfigProperty(name = "cyk.copy.executor.timeout.duration",defaultValue = "5")
	Long copyExecutorTimeoutDuration;
	@ConfigProperty(name = "cyk.copy.executor.timeout.unit",defaultValue = "MINUTES")
	TimeUnit copyExecutorTimeoutUnit;
	
	final Set<String> importRunning = new HashSet<>();
	
	String entityName;
	Class<?> entityImportableClass,entityViewClass;
	String countImportableByLegislativeActIdentifierQueryIdentifier,readImportableByLegislativeActIdentifierQueryIdentifier;
	
	@PostConstruct
	void listenPostConstruct() {
		__listenPostConstruct__();
	}
	
	@SuppressWarnings("unchecked")
	void __listenPostConstruct__() {
		if(entityClass == null)
			entityClass = (Class<ENTITY>) ClassHelper.getParameterAt(getClass(), 0);
		if(StringHelper.isBlank(entityName))
			entityName = (String) FieldHelper.readStatic(entityClass, "NAME");
		if(StringHelper.isBlank(countImportableByLegislativeActIdentifierQueryIdentifier))
			countImportableByLegislativeActIdentifierQueryIdentifier = (String) FieldHelper.readStatic(entityImportableClass, "QUERY_COUNT_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER");
		if(StringHelper.isBlank(readImportableByLegislativeActIdentifierQueryIdentifier))
			readImportableByLegislativeActIdentifierQueryIdentifier = (String) FieldHelper.readStatic(entityImportableClass, "QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER");
	}
	
	@Override @Transactional
	public Result import_(String legislativeActVersionIdentifier,Boolean throwIfRunning, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = validateImportInputs(legislativeActVersionIdentifier,auditWho, throwablesMessages,entityManager);
		throwablesMessages.throwIfNotEmpty();
		
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) instances[0];
		validateImport(legislativeActVersion, auditWho, throwablesMessages, entityManager);
		throwablesMessages.throwIfNotEmpty();
		
		String auditIdentifier = generateAuditIdentifier();
		LocalDateTime auditWhen = LocalDateTime.now();
		
		import_(legislativeActVersion,auditIdentifier, auditWho, getImportAuditIdentifier(), auditWhen,throwIfRunning,entityManager);
		throwablesMessages.throwIfNotEmpty();
		Long count = getPersistence().count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.__AUDIT_IDENTIFIER__,auditIdentifier));
		
		// Return of message
		result.close().setName(String.format("Importation de %s %s(s) dans %s par %s",count,entityName,legislativeActVersion.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Nombre de %s importée : %s",entityName, count));
		return result;
	}

	abstract Object[] validateImportInputs(String legislativeActVersionIdentifier, String auditWho,ThrowablesMessages throwablesMessages, EntityManager entityManager);
	abstract void validateImport(LegislativeActVersionImpl legislativeActVersion,String auditWho, ThrowablesMessages throwablesMessages, EntityManager entityManager);

	@SuppressWarnings("unchecked")
	public void import_(LegislativeActVersionImpl legislativeActVersion,String auditIdentifier, String auditWho, String auditFunctionality,LocalDateTime auditWhen,Boolean throwIfRunning, EntityManager entityManager) {
		String finalAuditFunctionality = StringHelper.isBlank(auditFunctionality) ? getImportAuditIdentifier() : auditFunctionality;
		LocalDateTime finalAuditWhen = auditWhen == null ? LocalDateTime.now() : auditWhen;
		synchronized(AbstractExpenditureResourceBusinessImpl.class) {
			if(isImportRunning(legislativeActVersion, entityManager)) {
				String message = formatMessageImportIsRunning(legislativeActVersion);
				if(Boolean.TRUE.equals(throwIfRunning))
					throw new RuntimeException(message);
				else
					LogHelper.logWarning(message, getClass());
				return;
			}
			importRunning.add(legislativeActVersion.getIdentifier());
		}
		try {
			updateMaterializedView();
			Long count = countImportable(legislativeActVersion);
			List<Integer> batchSizes = NumberHelper.getProportions(count.intValue(), importBatchSize);		
			if(CollectionHelper.isNotEmpty(batchSizes)) {
				LogHelper.logInfo(String.format("Importation de %s. Traitement par lot de %s. Nombre de lot = %s", count,importBatchSize,batchSizes.size()), getClass());
				
				List<Object[]> arrays = readImportable(legislativeActVersion);

				List<ENTITY> instances = instantiate(legislativeActVersion, arrays,auditIdentifier, auditWho, finalAuditFunctionality, finalAuditWhen);
							
				List<Object[]> lists = new ArrayList<>();
				for(Integer index =0; index < batchSizes.size(); index = index + 1)
					lists.add(new Object[] {new ArrayList<>(instances.subList(index*importBatchSize, index*importBatchSize+batchSizes.get(index))),index+1,batchSizes.size()});
				instances.clear();
				instances = null;
				ExecutorService executorService = Executors.newFixedThreadPool(importExecutorThreadCount);
				lists.forEach(array -> {
					executorService.execute(() -> {
						EntityManager __entityManager__ = EntityManagerGetter.getInstance().get();
						createBatch(new ArrayList<>((List<ENTITY>)array[0]),__entityManager__,Boolean.TRUE,null);
					});
				});
				shutdownExecutorService(executorService, importExecutorTimeoutDuration, importExecutorTimeoutUnit);
			}
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}finally {
			importRunning.remove(legislativeActVersion.getIdentifier());
		}
	}
	
	@Override @Transactional
	public Result import_(String legislativeActVersionIdentifier, String auditWho) {
		return import_(legislativeActVersionIdentifier, Boolean.TRUE, auditWho);
	}
	
	@Scheduled(cron = "{cyk.import.cron}")
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
	
	String getImportAuditIdentifier() {
		return (String) FieldHelper.readStatic(getClass(), "IMPORT_AUDIT_IDENTIFIER");
	}
	
	Boolean isImportRunning(LegislativeActVersion legislativeActVersion,EntityManager entityManager) {
		return importRunning.contains(legislativeActVersion.getIdentifier());
	}
	
	String formatMessageImportIsRunning(LegislativeActVersion legislativeActVersion) {
		return String.format("%s de %s en cours d'importation", entityName,legislativeActVersion.getName());
	}
	
	void updateMaterializedView() {
		materializedViewManager.actualize(entityViewClass);
	}
	
	abstract ENTITY instantiate(LegislativeActVersion legislativeActVersion,Object[] array);
	
	List<ENTITY> instantiate(LegislativeActVersion legislativeActVersion, List<Object[]> arrays,String auditIdentifier, String auditWho,String auditFunctionality, LocalDateTime auditWhen) {
		if(CollectionHelper.isEmpty(arrays))
			return null;
		List<ENTITY> list = new ArrayList<>();
		arrays.forEach(array -> {
			ENTITY entity = instantiate(legislativeActVersion, array);
			if(entity == null)
				return;
			((AuditableWhoDoneWhatWhen)entity).set__auditIdentifier__(auditIdentifier).set__auditWho__(auditWho).set__auditFunctionality__(auditFunctionality).set__auditWhen__(auditWhen);
			list.add(entity);
		});
		return list;
	}
	
	Long countImportable(LegislativeActVersion legislativeActVersion) {
		return entityManager.createNamedQuery(countImportableByLegislativeActIdentifierQueryIdentifier, Long.class).setParameter("legislativeActVersionIdentifier", legislativeActVersion.getIdentifier()).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	List<Object[]> readImportable(LegislativeActVersion legislativeActVersion) {
		return entityManager.createNamedQuery(readImportableByLegislativeActIdentifierQueryIdentifier).setParameter("legislativeActVersionIdentifier", legislativeActVersion.getIdentifier()).getResultList();
	}
	
	abstract SpecificPersistence<ENTITY> getPersistence();
}