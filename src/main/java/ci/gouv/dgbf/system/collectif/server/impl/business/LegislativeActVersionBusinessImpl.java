package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.business.RegulatoryActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;

@ApplicationScoped
public class LegislativeActVersionBusinessImpl extends AbstractSpecificBusinessImpl<LegislativeActVersion> implements LegislativeActVersionBusiness,Serializable {

	@Inject EntityManager entityManager;
	@Inject LegislativeActVersionPersistence persistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ExpenditureBusiness expenditureBusiness;
	@Inject RegulatoryActBusiness regulatoryActBusiness;

	@Override @Transactional
	public Result create(String code, String name, Byte number, String legislativeActIdentifier, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.LegislativeActVersion.validateCreateInputs(code, name, number,legislativeActIdentifier, auditWho, throwablesMessages,entityManager);
		throwablesMessages.throwIfNotEmpty();
		//All inputs are fine
		LegislativeActVersionImpl legislativeActVersion = create(code, name, number, (LegislativeActImpl) instances[0], auditWho, null, null, entityManager);
		// Return of message
		result.close().setName(String.format("Création de %s par %s",legislativeActVersion.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Création de %s",legislativeActVersion.getName()));
		return result;
	}
	
	public LegislativeActVersionImpl create(String code, String name, Byte number, LegislativeActImpl legislativeAct, String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager) {
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
		audit(legislativeActVersion, auditFunctionality, auditWho, auditWhen);
		//Persist instance
		entityManager.persist(legislativeActVersion);
		entityManager.flush();
		
		legislativeActVersion.setActIdentifier(legislativeAct.getIdentifier());
		((RegulatoryActBusinessImpl)regulatoryActBusiness).includeByLegislativeActVersionIdentifier(legislativeActVersion, auditWho, auditFunctionality, auditWhen, entityManager);
		
		//entityManager.clear();
		//Import expenditures
		((ExpenditureBusinessImpl)expenditureBusiness).import_(legislativeActVersion, auditWho,auditFunctionality,auditWhen,null,entityManager);
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
		
		copy(legislativeActVersionSource, legislativeActVersionTarget, options, auditWho, COPY_AUDIT_IDENTIFIER, LocalDateTime.now(), entityManager);
		
		// Return of message
		result.close().setName(String.format("Copie de %s vers %s par %s",legislativeActVersionSource.getName(),legislativeActVersionTarget.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Copie de %s vers %s",legislativeActVersionSource.getName(),legislativeActVersionTarget.getName()));
		return result;
	}
	
	public void copy(LegislativeActVersionImpl source, LegislativeActVersionImpl destination, CopyOptions options,String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager) {
		Long count = expenditurePersistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,source.getIdentifier()));
		copyByOverWrite(source, destination, options, auditWho, auditFunctionality, auditWhen, entityManager, count, 1000);
	}
	
	public void copyByOverWrite(LegislativeActVersionImpl source, LegislativeActVersionImpl destination, CopyOptions options,String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager,Long count,Integer batchSize) {
		((ExpenditureBusinessImpl)expenditureBusiness).copyAdjustments(destination, source, auditWho,auditFunctionality,auditWhen);
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
		LegislativeActVersionImpl legislativeActVersionSource = (LegislativeActVersionImpl) instances[0];
		LegislativeActVersionImpl legislativeActVersionDestination = create(null, null, null, entityManager.find(LegislativeActImpl.class, legislativeActVersionSource.getActIdentifier()), auditWho, DUPLICATE_AUDIT_IDENTIFIER, auditWhen, entityManager);
		
		copy(legislativeActVersionSource, legislativeActVersionDestination, null, auditWho, DUPLICATE_AUDIT_IDENTIFIER, auditWhen, entityManager);
		
		// Return of message
		result.close().setName(String.format("Duplication de %s par %s",legislativeActVersionSource.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Duplication de %s",legislativeActVersionSource.getName()));
		return result;
	}
	
	private static final String CODE_FORMAT = "%s_%s";
	private static final String NAME_FORMAT = "Version %s %s";
}