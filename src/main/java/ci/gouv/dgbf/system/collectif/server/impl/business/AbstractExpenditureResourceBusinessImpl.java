package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.klass.ClassHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.object.marker.AuditableWhoDoneWhatWhen;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.SpecificPersistence;
import org.cyk.utility.persistence.entity.EntityLifeCycleListenerImpl;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.SpecificPersistenceGetter;
import org.cyk.utility.persistence.server.view.MaterializedViewActualizer;
import org.cyk.utility.report.GenericReportService;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureResourceBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.Configuration;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;
import io.quarkus.scheduler.Scheduled;

public abstract class AbstractExpenditureResourceBusinessImpl<ENTITY> extends AbstractSpecificBusinessImpl<ENTITY> implements ExpenditureResourceBusiness<ENTITY>,Serializable{

	@Inject EntityManager entityManager;
	@Inject SpecificPersistenceGetter specificPersistenceGetter;
	SpecificPersistence<ENTITY> persistence;
	@Inject LegislativeActPersistence legislativeActPersistence;
	@Inject MaterializedViewActualizer materializedViewActualizer;
	@Inject GenericReportService genericReportService;
	
	@Inject Configuration configuration;
	
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
		
		if(entityClass != null)
			persistence = specificPersistenceGetter.get(entityClass);
	}
	
	@Override
	public Result import_(String legislativeActVersionIdentifier,Boolean throwIfRunning, String auditWho) {
		return import_(legislativeActVersionIdentifier, throwIfRunning, auditWho, entityManager, Boolean.FALSE);
	}
	
	public Result import_(String legislativeActVersionIdentifier,Boolean throwIfRunning, String auditWho,EntityManager entityManager,Boolean isUserTransaction) {
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
		if(Boolean.TRUE.equals(isUserTransaction))
			entityManager.getTransaction().begin();
		import_(legislativeActVersion,auditIdentifier, auditWho, getImportAuditIdentifier(), auditWhen,throwIfRunning,entityManager,Boolean.TRUE);
		if(Boolean.TRUE.equals(isUserTransaction))
			entityManager.getTransaction().commit();
		Long count = persistence.count(new QueryExecutorArguments().setEntityManager(entityManager).addFilterFieldsValues(Parameters.__AUDIT_IDENTIFIER__,auditIdentifier));
		
		// Return of message
		result.close().setName(String.format("Importation de %s %s(s) dans %s par %s",count,entityName,legislativeActVersion.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Nombre de %s importée : %s",entityName, count));
		return result;
	}

	//abstract Object[] validateImportInputs(String legislativeActVersionIdentifier, String auditWho,ThrowablesMessages throwablesMessages, EntityManager entityManager);
	//abstract void validateImport(LegislativeActVersionImpl legislativeActVersion,String auditWho, ThrowablesMessages throwablesMessages, EntityManager entityManager);

	Object[] validateImportInputs(String legislativeActVersionIdentifier, String auditWho,ThrowablesMessages throwablesMessages, EntityManager entityManager) {
		return ValidatorImpl.validateImportInputs(legislativeActVersionIdentifier,auditWho, throwablesMessages,entityManager);
	}
	
	void validateImport(LegislativeActVersionImpl legislativeActVersion, String auditWho,ThrowablesMessages throwablesMessages, EntityManager entityManager) {
		ValidatorImpl.validateImport(legislativeActVersion,entityClass,importRunning, auditWho, throwablesMessages, entityManager);
	}
	
	@SuppressWarnings("unchecked")
	public void import_(LegislativeActVersionImpl legislativeActVersion,String auditIdentifier, String auditWho, String auditFunctionality,LocalDateTime auditWhen,Boolean throwIfRunning, EntityManager entityManager,Boolean threadable) {
		String finalAuditFunctionality = StringHelper.isBlank(auditFunctionality) ? getImportAuditIdentifier() : auditFunctionality;
		LocalDateTime finalAuditWhen = auditWhen == null ? LocalDateTime.now() : auditWhen;
		synchronized(AbstractExpenditureResourceBusinessImpl.class) {
			if(isImportRunning(legislativeActVersion, entityManager)) {
				String message = formatMessageImportIsRunning(legislativeActVersion);
				if(Boolean.TRUE.equals(throwIfRunning))
					throw new RuntimeException(message);
				LogHelper.logWarning(message, getClass());
				return;
			}
			importRunning.add(legislativeActVersion.getIdentifier());
		}
		try {
			materializedViewActualizer.execute(null,entityViewClass);
			Long count = countImportable(legislativeActVersion,entityManager);
			LogHelper.log(String.format("%s %s à importer",count,entityName),Result.getLogLevel(), getClass());
			List<Integer> batchSizes = NumberHelper.getProportions(count.intValue(),configuration.importation().processing().batch().size());
			if(CollectionHelper.isNotEmpty(batchSizes)) {
				LogHelper.log(String.format("Importation de %s. Traitement par lot de %s. Nombre de lot = %s", count,configuration.importation().processing().batch().size(),batchSizes.size()),Result.getLogLevel(), getClass());
				
				List<Object[]> arrays = readImportable(legislativeActVersion,entityManager);

				List<ENTITY> instances = instantiateForImport(legislativeActVersion, arrays,auditIdentifier, auditWho, finalAuditFunctionality, finalAuditWhen);
							
				List<Object[]> lists = new ArrayList<>();
				for(Integer index =0; index < batchSizes.size(); index = index + 1)
					lists.add(new Object[] {new ArrayList<>(instances.subList(index*configuration.importation().processing().batch().size(), index*configuration.importation().processing().batch().size()+batchSizes.get(index))),index+1,batchSizes.size()});
				instances.clear();
				instances = null;
				if(Boolean.TRUE.equals(threadable)) {
					ExecutorService executorService = Executors.newFixedThreadPool(configuration.importation().processing().executor().thread().count());
					lists.forEach(array -> {
						executorService.execute(() -> {
							EntityManager __entityManager__ = EntityManagerGetter.getInstance().get();
							createBatch(new ArrayList<>((List<ENTITY>)array[0]),__entityManager__,Boolean.TRUE,null);
						});
					});
					shutdownExecutorService(executorService,configuration.importation().processing().executor().timeout().duration(), configuration.importation().processing().executor().timeout().unit());
				}else {
					lists.forEach(array -> {
						List<ENTITY> list = (List<ENTITY>) array[0];
						list.forEach(entity -> {
							entityManager.persist(entity);
						});
					});
				}				
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
	
	abstract ENTITY instantiateForImport(LegislativeActVersion legislativeActVersion,Object[] array);
	
	List<ENTITY> instantiateForImport(LegislativeActVersion legislativeActVersion, List<Object[]> arrays,String auditIdentifier, String auditWho,String auditFunctionality, LocalDateTime auditWhen) {
		if(CollectionHelper.isEmpty(arrays))
			return null;
		List<ENTITY> list = new ArrayList<>();
		arrays.forEach(array -> {
			ENTITY entity = instantiateForImport(legislativeActVersion, array);
			if(entity == null)
				return;
			((AuditableWhoDoneWhatWhen)entity).set__auditIdentifier__(auditIdentifier).set__auditWho__(auditWho).set__auditFunctionality__(auditFunctionality).set__auditWhen__(auditWhen);
			list.add(entity);
		});
		return list;
	}
	
	Long countImportable(LegislativeActVersion legislativeActVersion,EntityManager entityManager) {
		return entityManager.createNamedQuery(countImportableByLegislativeActIdentifierQueryIdentifier, Long.class).setParameter("legislativeActVersionIdentifier", legislativeActVersion.getIdentifier()).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	List<Object[]> readImportable(LegislativeActVersion legislativeActVersion,EntityManager entityManager) {
		return entityManager.createNamedQuery(readImportableByLegislativeActIdentifierQueryIdentifier).setParameter("legislativeActVersionIdentifier", legislativeActVersion.getIdentifier()).getResultList();
	}

	abstract String getReadAdjustmentIsNotZeroReportIdentifierQueryName();
	
	@Override
	public Result buildAdjustmentIsNotZeroReportResponse(Map<String,String> parameters,String fileType,Boolean isContentInline,String auditWho) {
		if(parameters == null)
			throw new RuntimeException("Le filtre est obligatoire");
		String legislativeActVersionIdentifier = parameters.get(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER);
		if(StringHelper.isBlank(legislativeActVersionIdentifier))
			throw new RuntimeException(String.format("L'identifiant de %s est obligatoire",LegislativeActVersion.NAME));
		Result result = new Result();
		String identifier = null;
		String queryName = getReadAdjustmentIsNotZeroReportIdentifierQueryName();
		if(StringHelper.isNotBlank(queryName))
			try {
				identifier = entityManager.createNamedQuery(queryName, String.class).setParameter("legislativeActVersionIdentifier", legislativeActVersionIdentifier).getSingleResult();
			} catch (NoResultException exception) {
				identifier = null;
			}
		org.cyk.utility.report.configuration.Report configuration = getAdjustmentIsNotZeroReportConfiguration();
		if(StringHelper.isBlank(identifier))
			identifier = configuration.identifier();
		//Change identifier to code
		parameters.entrySet().forEach(entry -> {
			if(Parameters.SECTION_IDENTIFIER.equals(entry.getKey()))
				entry.setValue(entityManager.createQuery(String.format("SELECT t.code FROM %s t WHERE t.identifier = :identifier",SectionImpl.ENTITY_NAME),String.class).setParameter("identifier", entry.getValue()).getSingleResult());
		});
		//Parameters name must match Report parameters name
		parameters = parameters.entrySet().stream().collect(Collectors.toMap(x -> {
			org.cyk.utility.report.configuration.Report.Parameter parameter = org.cyk.utility.report.configuration.Report.getParameterByName(configuration, x.getKey());
			return parameter == null ? x.getKey() : parameter.mappedTo();
		}, x -> x.getValue()));
		String parametersAsJson = ValueHelper.defaultToIfBlank(JsonbBuilder.create().toJson(parameters),configuration.parametersAsJson());
		fileType = ValueHelper.defaultToIfBlank(fileType, configuration.fileType());
		isContentInline = isContentInline == null ? configuration.isContentInline() : isContentInline;
		result.setValue(genericReportService.get(identifier, parametersAsJson, fileType, isContentInline));
		return result;
	}
	
	@Override
	public Result buildAdjustmentIsNotZeroReportStream(Map<String,String> parameters,String fileType,String auditWho) {
		Result result = new Result();
		Result temp = buildAdjustmentIsNotZeroReportResponse(parameters,fileType,Boolean.TRUE, auditWho);
		Response response = (Response) temp.getValue();
		InputStream inputStream = null;
		if(response.getEntity() instanceof InputStream)
			inputStream = (InputStream) response.getEntity();
		else if(response.getEntity() instanceof byte[])
			inputStream = new ByteArrayInputStream((byte[])response.getEntity());
		result.setValue(inputStream);
		return result;
	}
	
	abstract org.cyk.utility.report.configuration.Report getAdjustmentIsNotZeroReportConfiguration();
}