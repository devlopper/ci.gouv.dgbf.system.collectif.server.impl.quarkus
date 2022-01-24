package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.business.server.EntityCreator;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.collectif.server.api.business.GeneratedActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActExpenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.GeneratedActExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.GeneratedActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;

@ApplicationScoped
public class GeneratedActBusinessImpl extends AbstractSpecificBusinessImpl<GeneratedAct> implements GeneratedActBusiness,Serializable {

	@Inject EntityCreator entityCreator;
	@Inject GeneratedActPersistence persistence;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;

	@Override
	public Result generateByLegislativeActVersionIdentifier(String legislativeActVersionIdentifier, String auditWho) {
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		ValidatorImpl.GeneratedAct.validateGenerateInputs(legislativeActVersionIdentifier, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		LegislativeActVersion legislativeActVersion = legislativeActVersionPersistence.readOne(legislativeActVersionIdentifier, List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_CODE,LegislativeActVersionImpl.FIELD_NAME));
		throwablesMessages.addIfTrue("La version du collectif budgétaire est requis", legislativeActVersion == null);
		throwablesMessages.throwIfNotEmpty();
		
		generateAdjustmentAct(legislativeActVersion,throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		/*
		Collection<GeneratedAct> cancellationsActs = generateCancellationsActs(legislativeActVersion);
		throwablesMessages.addIfTrue("Au moins un acte d'annulation généré est requis", CollectionHelper.isEmpty(cancellationsActs));
		throwablesMessages.throwIfNotEmpty();
		
		generatedActs.addAll(cancellationsActs);
		*/
		
		//throwablesMessages.addIfTrue("Au moins un acte généré est requis", CollectionHelper.isEmpty(generatedActs));
		//throwablesMessages.throwIfNotEmpty();
		
		//entityCreator.createMany(CollectionHelper.cast(Object.class, generatedActs));
		
		//1 - load adjusted expenditures
		//Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION)
		//		.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,identifier));
		
		
		Result result = new Result().open();
		//1 - Récupération des actes de gestion inclus
		//Collection<RegulatoryAct> regulatoryActs = regulatoryActPersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(RegulatoryActImpl.FIELD_IDENTIFIER,RegulatoryActImpl.FIELD_CODE,RegulatoryActImpl.FIELD_NAME)
		//		.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,identifier));
		
		
		// Return of message
		//result.close().setName(String.format("Génération de %s acte(s) de la version du collectif %s par %s",generatedActs.size(),legislativeActVersionIdentifier,auditWho)).log(getClass());
		//result.addMessages(String.format("Nombre d'actes générés : %s", generatedActs.size()));
		return result;
	}
	
	private void generateAdjustmentAct(LegislativeActVersion legislativeActVersion,ThrowablesMessages throwablesMessages) {
		Long generatedCount = persistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier()));
		if(NumberHelper.isGreaterThanZero(generatedCount)) {
			throwablesMessages.add(String.format("L'acte de la version du collectif %s a déja été généré", legislativeActVersion.getCode()));
			return;
		}
		
		Long adjustedCount = expenditurePersistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier()
				,Parameters.ADJUSTMENTS_EQUAL_ZERO,Boolean.FALSE));
		LogHelper.logInfo(String.format("Nombre de dépenses ajustées : %s", adjustedCount), getClass());
		if(NumberHelper.isEqualToZero(adjustedCount)) {
			return;
		}
		GeneratedActImpl act = new GeneratedActImpl();
		act.setIdentifier(legislativeActVersion.getIdentifier());
		act.setLegislativeActVersion((LegislativeActVersionImpl) legislativeActVersion);
		act.setType(GeneratedAct.Type.ADJUSTMENT);
		act.setActSourceIdentifier(legislativeActVersion.getIdentifier());
		act.setApplied(Boolean.FALSE);
		act.setCode(legislativeActVersion.getCode());
		act.setName(legislativeActVersion.getName());
		entityCreator.createMany(act);
		
		//TODO use pagination
		Collection<ExpenditureImpl> expenditures = CollectionHelper.cast(ExpenditureImpl.class, expenditurePersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier()
				,Parameters.ADJUSTMENTS_EQUAL_ZERO,Boolean.FALSE,Parameters.GENERATED_ACT_EXPENDITURE_EXISTS,Boolean.FALSE)));
		
		if(CollectionHelper.isEmpty(expenditures)) {
			throwablesMessages.add(String.format("Aucune dépense ajustée et non encore générée trouvée dans la version du collectif %s", legislativeActVersion.getCode()));
			return;
		}
		Collection<GeneratedActExpenditure> generatedActExpenditures = 
				expenditures.stream().map(expenditure -> new GeneratedActExpenditureImpl().setIdentifier(act.getIdentifier()+"_"+expenditure.getIdentifier()).setAct(act).setExpenditure(expenditure)).collect(Collectors.toList());
		entityCreator.createMany(CollectionHelper.cast(Object.class, generatedActExpenditures));
	}
	
	private Collection<GeneratedAct> generateCancelationsActs(LegislativeActVersion legislativeActVersion) {
		return null;
	}
	
	private Collection<GeneratedAct> generateRepositioningsActs(LegislativeActVersion legislativeActVersion) {
		return null;
	}
	
}