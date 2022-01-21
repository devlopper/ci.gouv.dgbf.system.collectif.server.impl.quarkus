package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.business.server.EntityCreator;

import ci.gouv.dgbf.system.collectif.server.api.business.GeneratedActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.GeneratedActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;

@ApplicationScoped
public class GeneratedActBusinessImpl extends AbstractSpecificBusinessImpl<GeneratedAct> implements GeneratedActBusiness,Serializable {

	@Inject EntityCreator entityCreator;
	@Inject GeneratedActPersistence persistence;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;

	@Override
	public Result generateByLegislativeActVersionIdentifier(String legislativeActVersionIdentifier, String auditWho) {
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		ValidatorImpl.GeneratedAct.validateGenerateInputs(legislativeActVersionIdentifier, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		LegislativeActVersion legislativeActVersion = legislativeActVersionPersistence.readOne(legislativeActVersionIdentifier, List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_CODE,LegislativeActVersionImpl.FIELD_NAME));
		throwablesMessages.addIfTrue("La version du collectif budgétaire est requis", legislativeActVersion == null);
		throwablesMessages.throwIfNotEmpty();
		
		GeneratedAct act = generateAct(legislativeActVersion);
		throwablesMessages.addIfTrue("Impossible de généré l'acte principal", act == null);
		throwablesMessages.throwIfNotEmpty();
		
		Collection<GeneratedAct> generatedActs = new ArrayList<>();
		generatedActs.add(act);
		/*
		Collection<GeneratedAct> cancellationsActs = generateCancellationsActs(legislativeActVersion);
		throwablesMessages.addIfTrue("Au moins un acte d'annulation généré est requis", CollectionHelper.isEmpty(cancellationsActs));
		throwablesMessages.throwIfNotEmpty();
		
		generatedActs.addAll(cancellationsActs);
		*/
		
		throwablesMessages.addIfTrue("Au moins un acte généré est requis", CollectionHelper.isEmpty(generatedActs));
		throwablesMessages.throwIfNotEmpty();
		
		entityCreator.createMany(CollectionHelper.cast(Object.class, generatedActs));
		
		//1 - load adjusted expenditures
		//Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION)
		//		.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,identifier));
		
		
		Result result = new Result().open();
		//1 - Récupération des actes de gestion inclus
		//Collection<RegulatoryAct> regulatoryActs = regulatoryActPersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(RegulatoryActImpl.FIELD_IDENTIFIER,RegulatoryActImpl.FIELD_CODE,RegulatoryActImpl.FIELD_NAME)
		//		.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,identifier));
		
		
		// Return of message
		result.close().setName(String.format("Génération de %s acte(s) de la version du collectif %s par %s",generatedActs.size(),legislativeActVersionIdentifier,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre d'actes générés : %s", generatedActs.size()));
		return result;
	}
	
	private GeneratedAct generateAct(LegislativeActVersion legislativeActVersion) {
		GeneratedActImpl act = new GeneratedActImpl();
		act.setIdentifier(legislativeActVersion.getIdentifier());
		act.setActSourceIdentifier(legislativeActVersion.getIdentifier());
		act.setApplied(Boolean.FALSE);
		act.setCode(legislativeActVersion.getCode());
		act.setName(legislativeActVersion.getName());
		return act;
	}
	
	private Collection<GeneratedAct> generateCancellationsActs(LegislativeActVersion legislativeActVersion) {
		return null;
	}
	
}