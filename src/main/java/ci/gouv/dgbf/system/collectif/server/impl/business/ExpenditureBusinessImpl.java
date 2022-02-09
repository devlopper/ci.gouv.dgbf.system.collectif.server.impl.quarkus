package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import org.cyk.utility.__kernel__.time.TimeHelper;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.entity.EntityLifeCycleListener;
import org.cyk.utility.persistence.entity.EntityLifeCycleListenerImpl;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class ExpenditureBusinessImpl extends AbstractSpecificBusinessImpl<Expenditure> implements ExpenditureBusiness,Serializable {

	@Inject EntityManager entityManager;
	@Inject ExpenditurePersistence persistence;
	@Inject LegislativeActPersistence legislativeActPersistence;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;
	
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
		Collection<Object[]> arrays = new ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader().readByIdentifiers(providedIdentifiers, null);
		Collection<String> identifiers = CollectionHelper.isEmpty(arrays) ? null : arrays.stream().map(array -> (String)array[0]).collect(Collectors.toList());	
		ValidatorImpl.validateIdentifiers(providedIdentifiers, identifiers, throwablesMessages);
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
	public Result import_(String legislativeActVersionIdentifier, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		ValidatorImpl.Expenditure.validateImportInputs(legislativeActVersionIdentifier,auditWho, throwablesMessages,entityManager);
		LegislativeActVersionImpl legislativeActVersion = entityManager.find(LegislativeActVersionImpl.class, legislativeActVersionIdentifier);
		
		throwablesMessages.addIfTrue(String.format("%s identifiée par %s n'existe pas",LegislativeActVersion.NAME, legislativeActVersionIdentifier),legislativeActVersion == null);
		throwablesMessages.throwIfNotEmpty();
		
		throwablesMessages.addIfTrue(String.format("%s de %s en cours d'importation",Expenditure.NAME ,legislativeActVersion.getName()), INPORT_RUNNING.contains(legislativeActVersionIdentifier));
		throwablesMessages.throwIfNotEmpty();
		
		Long count = persistence.count();
		import_(legislativeActVersion, auditWho, IMPORT_AUDIT_IDENTIFIER, LocalDateTime.now(), entityManager);
		count = NumberHelper.getLong(NumberHelper.subtract(persistence.count(),count));
		
		// Return of message
		result.close().setName(String.format("Importation de %s %s(s) dans %s par %s",count,Expenditure.NAME,legislativeActVersion.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Nombre de %s importée : %s",Expenditure.NAME, count));
		return result;
	}
	
	public void import_(LegislativeActVersionImpl legislativeActVersion, String auditWho, String auditFunctionality,LocalDateTime auditWhen, EntityManager entityManager) {
		if(StringHelper.isBlank(auditFunctionality))
			auditFunctionality = IMPORT_AUDIT_IDENTIFIER;
		if(auditWhen == null)
			auditWhen = LocalDateTime.now();
		INPORT_RUNNING.add(legislativeActVersion.getIdentifier());
		persistence.import_(legislativeActVersion.getIdentifier(),auditWho, auditFunctionality, EntityLifeCycleListener.Event.CREATE.getValue(), new java.sql.Date(TimeHelper.toMillisecond(auditWhen)),entityManager);
		INPORT_RUNNING.remove(legislativeActVersion.getIdentifier());
	}
	
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
			import_(legislativeAct.getDefaultVersionIdentifier(), EntityLifeCycleListenerImpl.SYSTEM_USER_NAME);
		}
	}
	
	/**/
	
	private static final Set<String> INPORT_RUNNING = new HashSet<>();
}