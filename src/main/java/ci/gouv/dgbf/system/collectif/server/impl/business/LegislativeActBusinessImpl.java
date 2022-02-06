package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExercisePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExerciseImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;

@ApplicationScoped
public class LegislativeActBusinessImpl extends AbstractSpecificBusinessImpl<LegislativeAct> implements LegislativeActBusiness,Serializable {

	@Inject EntityManager entityManager;
	@Inject LegislativeActPersistence persistence;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;
	@Inject ExercisePersistence exercisePersistence;
	@Inject LegislativeActVersionBusiness legislativeActVersionBusiness;
	
	@Override @Transactional
	public Result create(String code, String name, String exerciseIdentifier, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.LegislativeAct.validateCreateInputs(code, name, exerciseIdentifier, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		//All inputs are fine
		LegislativeActImpl legislativeAct = new LegislativeActImpl().setCode(code).setName(name).setExerciseIdentifier(exerciseIdentifier).setInProgress(Boolean.FALSE).setExercise((ExerciseImpl) instances[0]);
		if(StringHelper.isBlank(legislativeAct.getCode())) {
			Long count = persistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.EXERCISE_YEAR,legislativeAct.getExercise().getYear()));
			if(count == null)
				count = 0l;
			legislativeAct.setCode(String.format(CODE_FORMAT, legislativeAct.getExercise().getYear(),++count));
		}
		if(StringHelper.isBlank(legislativeAct.getName())) {
			legislativeAct.setName(String.format(NAME_FORMAT, legislativeAct.getExercise().getYear()));
		}
		legislativeAct.setIdentifier(legislativeAct.getCode());
		String auditFunctionality = CREATE_AUDIT_IDENTIFIER;
		LocalDateTime auditNow = LocalDateTime.now();
		audit(legislativeAct , auditWho,auditFunctionality, auditNow);
		entityManager.persist(legislativeAct);
		
		legislativeActVersionBusiness.create(null, null, null, legislativeAct.getIdentifier(), auditWho,auditFunctionality, auditNow,entityManager);
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) legislativeActVersionPersistence
				.readOne(new QueryExecutorArguments().setEntityManager(entityManager).addProjectionsFromStrings(LegislativeActVersionImpl.FIELD_IDENTIFIER).addFilterField(Parameters.LEGISLATIVE_ACT_IDENTIFIER, legislativeAct.getIdentifier()));
		legislativeAct.setDefaultVersion(legislativeActVersion);
		Long inProgressCount = persistence.count(new QueryExecutorArguments().setEntityManager(entityManager).addFilterField(Parameters.LEGISLATIVE_ACT_IN_PROGRESS, Boolean.TRUE));
		if(inProgressCount == null || inProgressCount == 0)
			legislativeAct.setInProgress(Boolean.TRUE);
		entityManager.merge(legislativeAct);
		
		// Return of message
		result.close().setName(String.format("Création de %s par %s",legislativeAct.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Création de %s",legislativeAct.getName()));
		return result;
	}
	
	@Override @Transactional
	public Result updateDefaultVersion(String legislativeActVersionIdentifier, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.LegislativeAct.validateUpdateDefaultVersionInputs(legislativeActVersionIdentifier,auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) instances[0];
		LegislativeActImpl legislativeAct = legislativeActVersion.getAct();	
		legislativeAct.setDefaultVersion(legislativeActVersion);
		audit(legislativeAct, UPDATE_DEFAULT_VERSION_AUDIT_IDENTIFIER, auditWho, LocalDateTime.now());
		entityManager.merge(legislativeAct);
		// Return of message
		result.close().setName(String.format("Mise à jour de la version par défaut de %s avec %s par %s",legislativeAct.getName(),legislativeActVersion.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Version par défaut de %s : %s",legislativeAct.getName(),legislativeActVersion.getName()));
		return result;
	}
	
	@Override @Transactional
	public Result updateInProgress(String legislativeActIdentifier,Boolean inProgress, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		ValidatorImpl.LegislativeAct.validateUpdateInProgressInputs(legislativeActIdentifier,inProgress,auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		LegislativeActImpl legislativeAct = validateAndReturnUsingNamedQueryReadByIdentifierLegislativeAct(legislativeActIdentifier,throwablesMessages);
		
		throwablesMessages.addIfTrue(String.format("%s %sest %s en cours",legislativeAct.getName(),inProgress ? "" : "n'",inProgress ? "déja" : "pas"), legislativeAct.getInProgress() != null && inProgress == legislativeAct.getInProgress());
		throwablesMessages.throwIfNotEmpty();
		
		legislativeAct.setInProgress(inProgress);
		audit(legislativeAct, UPDATE_DEFAULT_VERSION_AUDIT_IDENTIFIER, auditWho, LocalDateTime.now());
		entityManager.merge(legislativeAct);
		// Return of message
		result.close().setName(String.format("Mise à jour de <<en cours>> de %s avec %s par %s",LegislativeActVersion.NAME,legislativeAct.getName(),inProgress,auditWho)).log(getClass());
		result.addMessages(String.format("<<en cours>> de %s : %s",legislativeAct.getName(),inProgress ? "Oui" : "Non"));
		return result;
	}
	
	LegislativeActImpl validateAndReturnUsingNamedQueryReadByIdentifierLegislativeAct(String legislativeActIdentifier,ThrowablesMessages throwablesMessages) {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) persistence.readUsingNamedQueryReadByIdentifier(legislativeActIdentifier);
		throwablesMessages.addIfTrue(String.format("%s identifiée par %s n'existe pas",LegislativeAct.NAME, legislativeActIdentifier),legislativeAct == null);
		return legislativeAct;
	}
	
	private static final String CODE_FORMAT = "%s_%s";
	private static final String NAME_FORMAT = LegislativeAct.NAME+" %s";
}