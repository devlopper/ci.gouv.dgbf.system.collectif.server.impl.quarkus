package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.klass.ClassHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.object.marker.AuditableWhoDoneWhatWhen;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.server.hibernate.annotation.Hibernate;
import org.cyk.utility.persistence.server.view.MaterializedViewManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureResourceBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EntryAuthorizationImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.PaymentCreditImpl;

public abstract class AbstractExpenditureResourceBusinessImpl<ENTITY> extends AbstractSpecificBusinessImpl<ENTITY> implements ExpenditureResourceBusiness<ENTITY>,Serializable{

	@Inject EntityManager entityManager;
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
	
	Class<ENTITY> entityClass;
	String countImportableByLegislativeActIdentifierQueryIdentifier,readImportableByLegislativeActIdentifierQueryIdentifier;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	void listenPostConstruct() {
		entityClass = (Class<ENTITY>) ClassHelper.getParameterAt(getClass(), 0);
		countImportableByLegislativeActIdentifierQueryIdentifier = (String) FieldHelper.readStatic(getImportableClass(), "QUERY_COUNT_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER");
		readImportableByLegislativeActIdentifierQueryIdentifier = (String) FieldHelper.readStatic(getImportableClass(), "QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER");
	}
	
	@SuppressWarnings("unchecked")
	public void import_(LegislativeActVersionImpl legislativeActVersion, String auditWho, String auditFunctionality,LocalDateTime auditWhen,Boolean throwIfRunning, EntityManager entityManager) {
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

				List<ENTITY> instances = instantiate(legislativeActVersion, arrays, auditWho, finalAuditFunctionality, finalAuditWhen);
							
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
	
	String getImportAuditIdentifier() {
		return (String) FieldHelper.readStatic(getClass(), "IMPORT_AUDIT_IDENTIFIER");
	}
	
	abstract Boolean isImportRunning(LegislativeActVersion legislativeActVersion,EntityManager entityManager);
	abstract String formatMessageImportIsRunning(LegislativeActVersion legislativeActVersion);
	
	void updateMaterializedView() {
		materializedViewManager.actualize(getViewClass());
	}
	
	abstract ENTITY instantiate(LegislativeActVersion legislativeActVersion,Object[] array);
	
	List<ENTITY> instantiate(LegislativeActVersion legislativeActVersion, List<Object[]> arrays, String auditWho,String auditFunctionality, LocalDateTime auditWhen) {
		if(CollectionHelper.isEmpty(arrays))
			return null;
		List<ENTITY> list = new ArrayList<>();
		arrays.forEach(array -> {
			ENTITY entity = instantiate(legislativeActVersion, array);
			if(entity == null)
				return;
			((AuditableWhoDoneWhatWhen)entity).set__auditWho__(auditWho).set__auditFunctionality__(auditFunctionality).set__auditWhen__(auditWhen);
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
	
	abstract Class<?> getImportableClass();
	
	abstract Class<?> getViewClass();
}