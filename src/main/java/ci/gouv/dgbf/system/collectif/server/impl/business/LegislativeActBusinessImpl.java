package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;

@ApplicationScoped
public class LegislativeActBusinessImpl extends AbstractSpecificBusinessImpl<LegislativeAct> implements LegislativeActBusiness,Serializable {

	@Inject EntityManager entityManager;
	@Inject LegislativeActPersistence persistence;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;
	
	@Override @Transactional
	public Result updateDefaultVersion(String legislativeActIdentifier,String legislativeActVersionIdentifier, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		ValidatorImpl.LegislativeAct.validateUpdateDefaultVersionInputs(legislativeActIdentifier,legislativeActVersionIdentifier,auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		LegislativeActImpl legislativeAct;
		try {
			legislativeAct = entityManager.createNamedQuery(LegislativeActImpl.QUERY_READ_BY_IDENTIIFER,LegislativeActImpl.class).setParameter("identifier", legislativeActIdentifier).getSingleResult();
		}catch(NoResultException exception) {
			legislativeAct = null;
		}	
		throwablesMessages.addIfTrue(String.format("%s identifiée par %s n'existe pas",LegislativeAct.NAME, legislativeActIdentifier),legislativeAct == null);
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) legislativeActVersionPersistence.readOne(legislativeActVersionIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_NAME));
		throwablesMessages.addIfTrue(String.format("%s identifiée par %s n'existe pas",LegislativeActVersion.NAME, legislativeActVersionIdentifier),legislativeActVersion == null);
		throwablesMessages.addIfTrue("La version par défaut existe déja",legislativeAct.getDefaultVersion() != null && legislativeActVersionIdentifier.equals(legislativeAct.getDefaultVersion().getIdentifier()));
		throwablesMessages.throwIfNotEmpty();
		
		legislativeAct.setDefaultVersion(legislativeActVersion);
		audit(legislativeActVersion, UPDATE_DEFAULT_VERSION_AUDIT_IDENTIFIER, auditWho, LocalDateTime.now());
		entityManager.merge(legislativeAct);
		// Return of message
		result.close().setName(String.format("Mise à jour de la version par défaut de %s avec %s par %s",LegislativeActVersion.NAME,legislativeAct.getName(),legislativeActVersion.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Version par défaut de %s : %s",legislativeAct.getName(),legislativeActVersion.getName()));
		return result;
	}
	
}