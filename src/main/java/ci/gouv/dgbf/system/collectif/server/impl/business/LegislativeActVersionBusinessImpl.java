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
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;

@ApplicationScoped
public class LegislativeActVersionBusinessImpl extends AbstractSpecificBusinessImpl<LegislativeActVersion> implements LegislativeActVersionBusiness,Serializable {

	@Inject EntityManager entityManager;
	@Inject LegislativeActVersionPersistence persistence;

	@Override @Transactional
	public Result create(String code, String name, Byte number, String legislativeActIdentifier, String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.LegislativeActVersion.validateCreateInputs(code, name, number,legislativeActIdentifier, auditWho, throwablesMessages,entityManager);
		throwablesMessages.throwIfNotEmpty();
		//All inputs are fine
		LegislativeActVersionImpl legislativeActVersion = new LegislativeActVersionImpl().setCode(code).setName(name).setNumber(number).setAct((LegislativeActImpl) instances[0]);
		if(legislativeActVersion.getNumber() == null)
			legislativeActVersion.setNumber(NumberHelper.get(Byte.class,persistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_IDENTIFIER,legislativeActIdentifier)),Byte.valueOf("0")));
		legislativeActVersion.setNumber(NumberHelper.get(Byte.class,legislativeActVersion.getNumber()+Byte.valueOf("1")));
		if(StringHelper.isBlank(legislativeActVersion.getCode()))
			legislativeActVersion.setCode(String.format(CODE_FORMAT, legislativeActVersion.getAct().getCode(),legislativeActVersion.getNumber()));		
		if(StringHelper.isBlank(legislativeActVersion.getName()))
			legislativeActVersion.setName(String.format(NAME_FORMAT, legislativeActVersion.getNumber(),legislativeActVersion.getAct().getName()));
		legislativeActVersion.setIdentifier(legislativeActVersion.getCode());
		audit(legislativeActVersion, ValueHelper.defaultToIfBlank(auditFunctionality, CREATE_AUDIT_IDENTIFIER), auditWho, ValueHelper.defaultToIfBlank(auditWhen, LocalDateTime.now()));
		if(entityManager == null)
			entityManager = this.entityManager;
		entityManager.persist(legislativeActVersion);
		// Return of message
		result.close().setName(String.format("Création de %s par %s",legislativeActVersion.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Création de %s",legislativeActVersion.getName()));
		return result;
	}
	
	@Override
	public Result create(String code, String name, Byte number, String legislativeActIdentifier, String auditWho) {
		return create(code, name, number, legislativeActIdentifier, auditWho, CREATE_AUDIT_IDENTIFIER, LocalDateTime.now(),entityManager);
	}
	
	private static final String CODE_FORMAT = "%s_%s";
	private static final String NAME_FORMAT = "Version %s %s";
}