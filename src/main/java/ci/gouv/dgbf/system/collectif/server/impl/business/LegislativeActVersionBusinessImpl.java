package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.cyk.quarkus.extension.core_.configuration.When;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.business.RegulatoryActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.business.ResourceBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.Configuration;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import io.vertx.core.eventbus.EventBus;

@ApplicationScoped
public class LegislativeActVersionBusinessImpl extends AbstractSpecificBusinessImpl<LegislativeActVersion> implements LegislativeActVersionBusiness,Serializable {

	@Inject EntityManager entityManager;
	@Inject LegislativeActVersionPersistence persistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ExpenditureBusiness expenditureBusiness;
	@Inject ResourceBusiness resourceBusiness;
	@Inject RegulatoryActBusiness regulatoryActBusiness;
	@Inject Configuration configuration;
	@Inject EventBus eventBus;
	
	@Override
	public Result create(String code, String name, Byte number, String legislativeActIdentifier, String auditWho) {
		Result result = createInTransaction(code, name, number, legislativeActIdentifier, auditWho);
		if(When.AFTER.equals(configuration.legislativeActVersion().creation().whenRegulatoryActIncluded()))
			eventBus.publish(RegulatoryActBusinessImpl.EVENT_CHANNEL_INCLUDE_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER, new RegulatoryActBusinessImpl.EventMessage(((LegislativeActVersion)result.getMapValueByKey(LegislativeActVersion.class)).getIdentifier()
					,auditWho));
		if(When.AFTER.equals(configuration.legislativeActVersion().creation().whenExpenditureImported()))
			eventBus.publish(ExpenditureBusinessImpl.EVENT_CHANNEL_IMPORT, new ExpenditureBusinessImpl.EventMessage(((LegislativeActVersion)result.getMapValueByKey(LegislativeActVersion.class)).getIdentifier(),auditWho));
		if(When.AFTER.equals(configuration.legislativeActVersion().creation().whenResourceImported()))
			eventBus.publish(ResourceBusinessImpl.EVENT_CHANNEL_IMPORT, new ResourceBusinessImpl.EventMessage(((LegislativeActVersion)result.getMapValueByKey(LegislativeActVersion.class)).getIdentifier(),auditWho));
		return result;
	}
	
	@Transactional
	Result createInTransaction(String code, String name, Byte number, String legislativeActIdentifier, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.LegislativeActVersion.validateCreateInputs(code, name, number,legislativeActIdentifier, auditWho, throwablesMessages,entityManager);
		throwablesMessages.throwIfNotEmpty();
		//All inputs are fine
		LegislativeActVersionImpl legislativeActVersion = create(code, name, number, (LegislativeActImpl) instances[0],generateAuditIdentifier(), auditWho, null, null, entityManager);
		result.map(LegislativeActVersion.class, legislativeActVersion);
		// Return of message
		result.close().setName(String.format("Création de %s par %s",legislativeActVersion.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Création de %s",legislativeActVersion.getName()));
		//((RegulatoryActBusinessImpl)regulatoryActBusiness).actualizeExpenditureIncludedMovementView();
		return result;
	}
	
	public LegislativeActVersionImpl create(String code, String name, Byte number, LegislativeActImpl legislativeAct,String auditIdentifier, String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager) {
		if(StringHelper.isBlank(auditFunctionality))
			auditFunctionality = CREATE_AUDIT_IDENTIFIER;
		if(auditWhen == null)
			auditWhen = LocalDateTime.now();
		//Instantiate legislative act version
		LegislativeActVersionImpl legislativeActVersion = new LegislativeActVersionImpl().setCode(code).setName(name).setNumber(number).setAct(legislativeAct);
		//Derive blank attributes
		if(legislativeActVersion.getNumber() == null)
			legislativeActVersion.setNumber(NumberHelper.get(Byte.class,persistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_IDENTIFIER,legislativeAct.getIdentifier())),Byte.valueOf("0")));
		legislativeActVersion.setNumber(NumberHelper.get(Byte.class,legislativeActVersion.getNumber()+Byte.valueOf("1")));
		if(StringHelper.isBlank(legislativeActVersion.getCode()))
			legislativeActVersion.setCode(String.format(CODE_FORMAT, legislativeActVersion.getAct().getCode(),legislativeActVersion.getNumber()));		
		if(StringHelper.isBlank(legislativeActVersion.getName()))
			legislativeActVersion.setName(String.format(NAME_FORMAT, legislativeActVersion.getNumber(),legislativeActVersion.getAct().getName()));
		legislativeActVersion.setIdentifier(legislativeActVersion.getCode());
		audit(legislativeActVersion,auditIdentifier, auditFunctionality, auditWho, auditWhen);
		//Persist instance
		entityManager.persist(legislativeActVersion);
		//entityManager.flush();
		
		legislativeActVersion.setActIdentifier(legislativeAct.getIdentifier());
		
		if(When.WHILE.equals(configuration.legislativeActVersion().creation().whenRegulatoryActIncluded()))
			((RegulatoryActBusinessImpl)regulatoryActBusiness).includeByLegislativeActVersionIdentifier(legislativeActVersion,auditIdentifier, auditWho, auditFunctionality, auditWhen, entityManager);
		if(When.WHILE.equals(configuration.legislativeActVersion().creation().whenExpenditureImported()))
			((ExpenditureBusinessImpl)expenditureBusiness).import_(legislativeActVersion,auditIdentifier, auditWho,auditFunctionality,auditWhen,null,entityManager,Boolean.FALSE);
		if(When.WHILE.equals(configuration.legislativeActVersion().creation().whenResourceImported()))
			((ResourceBusinessImpl)resourceBusiness).import_(legislativeActVersion,auditIdentifier, auditWho,auditFunctionality,auditWhen,null,entityManager,Boolean.FALSE);
		return legislativeActVersion;
	}
	
	@Override @Transactional
	public Result copy(String sourceIdentifier, String destinationIdentifier, CopyOptions options,String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.LegislativeActVersion.validateCopyInputs(sourceIdentifier,destinationIdentifier, auditWho, throwablesMessages,entityManager);
		throwablesMessages.throwIfNotEmpty();
		//All inputs are fine
		LegislativeActVersionImpl legislativeActVersionSource = (LegislativeActVersionImpl) instances[0];
		LegislativeActVersionImpl legislativeActVersionTarget = (LegislativeActVersionImpl) instances[1];
		
		copy(legislativeActVersionSource, legislativeActVersionTarget, options,generateAuditIdentifier(), auditWho, COPY_AUDIT_IDENTIFIER, LocalDateTime.now(), entityManager);
		
		// Return of message
		result.close().setName(String.format("Copie de %s vers %s par %s",legislativeActVersionSource.getName(),legislativeActVersionTarget.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Copie de %s vers %s",legislativeActVersionSource.getName(),legislativeActVersionTarget.getName()));
		//((RegulatoryActBusinessImpl)regulatoryActBusiness).actualizeExpenditureIncludedMovementView();
		return result;
	}
	
	public void copy(LegislativeActVersionImpl source, LegislativeActVersionImpl destination, CopyOptions options,String auditIdentifier,String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager) {
		Long count = expenditurePersistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,source.getIdentifier()));
		copyByOverWrite(source, destination, options,auditIdentifier, auditWho, auditFunctionality, auditWhen, entityManager, count, 1000);
	}
	
	public void copyByOverWrite(LegislativeActVersionImpl source, LegislativeActVersionImpl destination, CopyOptions options,String auditIdentifier,String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager,Long count,Integer batchSize) {
		((ExpenditureBusinessImpl)expenditureBusiness).copy(destination, source,auditIdentifier, auditWho,auditFunctionality,auditWhen);
	}
	
	public void copyByMerge(LegislativeActVersionImpl source, LegislativeActVersionImpl destination, CopyOptions options,String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager) {
		
	}
	
	@Override @Transactional
	public Result duplicate(String identifier, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.LegislativeActVersion.validateDuplicateInputs(identifier, auditWho, throwablesMessages,entityManager);
		throwablesMessages.throwIfNotEmpty();
		//All inputs are fine
		LocalDateTime auditWhen = LocalDateTime.now();
		String auditIdentifier = generateAuditIdentifier();
		LegislativeActVersionImpl legislativeActVersionSource = (LegislativeActVersionImpl) instances[0];
		LegislativeActVersionImpl legislativeActVersionDestination = create(null, null, null, entityManager.find(LegislativeActImpl.class, legislativeActVersionSource.getActIdentifier()),auditIdentifier, auditWho, DUPLICATE_AUDIT_IDENTIFIER
				, auditWhen, entityManager);
		
		copy(legislativeActVersionSource, legislativeActVersionDestination, null,auditIdentifier, auditWho, DUPLICATE_AUDIT_IDENTIFIER, auditWhen, entityManager);
		
		// Return of message
		result.close().setName(String.format("Duplication de %s par %s",legislativeActVersionSource.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Duplication de %s",legislativeActVersionSource.getName()));
		//((RegulatoryActBusinessImpl)regulatoryActBusiness).actualizeExpenditureIncludedMovementView();
		return result;
	}
	
	private static final String CODE_FORMAT = "%s_%s";
	private static final String NAME_FORMAT = "Version %s %s";
}