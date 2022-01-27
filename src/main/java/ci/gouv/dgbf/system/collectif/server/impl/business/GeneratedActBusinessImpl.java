package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.collectif.server.api.business.GeneratedActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActExpenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.GeneratedActExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.GeneratedActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActExpenditureImpl;

@ApplicationScoped
public class GeneratedActBusinessImpl extends AbstractSpecificBusinessImpl<GeneratedAct> implements GeneratedActBusiness,Serializable {

	@Inject EntityManager entityManager;
	@Inject GeneratedActPersistence persistence;
	@Inject GeneratedActExpenditurePersistence generatedActExpenditurePersistence;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject RegulatoryActPersistence regulatoryActPersistence;
	@Inject RegulatoryActExpenditurePersistence regulatoryActExpenditurePersistence;

	private LegislativeActVersion validate(String legislativeActVersionIdentifier, String auditWho,Boolean generate,ThrowablesMessages throwablesMessages) {
		ValidatorImpl.GeneratedAct.validateDeleteInputs(legislativeActVersionIdentifier, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		LegislativeActVersion legislativeActVersion = legislativeActVersionPersistence.readOne(legislativeActVersionIdentifier, List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_CODE,LegislativeActVersionImpl.FIELD_NAME));
		throwablesMessages.addIfTrue("La version du collectif budgétaire est requis", legislativeActVersion == null);
		throwablesMessages.throwIfNotEmpty();
		
		Long generatedCount = persistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier()));
		
		if(Boolean.TRUE.equals(generate) && NumberHelper.isGreaterThanZero(generatedCount))
			throwablesMessages.addIfTrue(String.format("L'acte de la version du collectif %s a déja été généré", legislativeActVersion.getCode()),NumberHelper.isGreaterThanZero(generatedCount));
		else if(!Boolean.TRUE.equals(generate) && NumberHelper.isEqualToZero(generatedCount))
			throwablesMessages.addIfTrue(String.format("La version du collectif %s n'a aucun actes générés", legislativeActVersion.getCode()),generatedCount == 0);
		throwablesMessages.throwIfNotEmpty();
		return legislativeActVersion;
	}
	
	@Override @Transactional
	public Result deleteByLegislativeActVersionIdentifier(String legislativeActVersionIdentifier, String auditWho) {
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		validate(legislativeActVersionIdentifier, auditWho,Boolean.FALSE, throwablesMessages);
		
		Result result = new Result().open();
		LocalDateTime auditWhen = LocalDateTime.now();
		Collection<GeneratedActImpl> generatedActs = entityManager.createNamedQuery(GeneratedActImpl.QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIIFER,GeneratedActImpl.class).setParameter("legislativeActVersionIdentifier", legislativeActVersionIdentifier)
				.getResultList();
		Collection<GeneratedActExpenditureImpl> generatedActExpenditures = entityManager.createNamedQuery(GeneratedActExpenditureImpl.QUERY_READ_BY_ACT_IDENTIIFERS,GeneratedActExpenditureImpl.class)
				.setParameter("actIdentifiers", FieldHelper.readSystemIdentifiersAsStrings(generatedActs)).getResultList();
		
		if(CollectionHelper.isNotEmpty(generatedActExpenditures))
			generatedActExpenditures.forEach(generatedActExpenditure -> {
				audit(generatedActExpenditure, DELETE_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_AUDIT_IDENTIFIER, auditWho, auditWhen);
				entityManager.remove(generatedActExpenditure);
			});
		
		if(CollectionHelper.isNotEmpty(generatedActs))
			generatedActs.forEach(generatedAct -> {
				audit(generatedAct, DELETE_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_AUDIT_IDENTIFIER, auditWho, auditWhen);
				entityManager.remove(generatedAct);
			});
		
		// Return of message
		result.close().setName(String.format("Suppression de %s acte(s) et %s dépense(s) de la version du collectif %s par %s",generatedActs.size(),generatedActExpenditures.size(),legislativeActVersionIdentifier,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre d'actes supprimés : %s", generatedActs.size()));
		return result;
	}
	
	@Override @Transactional
	public Result generateByLegislativeActVersionIdentifier(String legislativeActVersionIdentifier, String auditWho) {
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		LegislativeActVersion legislativeActVersion = validate(legislativeActVersionIdentifier, auditWho,Boolean.TRUE, throwablesMessages);
		
		Result result = new Result().open();
		
		LocalDateTime auditWhen = LocalDateTime.now();
		generateAdjustmentAct(legislativeActVersion,throwablesMessages,GENERATE_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_AUDIT_IDENTIFIER,auditWho,auditWhen);
		throwablesMessages.throwIfNotEmpty();
		
		Long includedRegulatoryActCount = regulatoryActPersistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier()
				,Parameters.REGULATORY_ACT_INCLUDED,Boolean.TRUE));
		LogHelper.logInfo(String.format("Nombre d'actes inclus : %s", includedRegulatoryActCount), getClass());
		if(NumberHelper.isGreaterThanZero(includedRegulatoryActCount)) {
			Collection<RegulatoryAct> regulatoryActs = regulatoryActPersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier()
					,Parameters.REGULATORY_ACT_INCLUDED,Boolean.TRUE));
			Collection<RegulatoryActExpenditure> regulatoryActExpenditures = regulatoryActExpenditurePersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.REGULATORY_ACT_IDENTIFIERS
					,FieldHelper.readSystemIdentifiersAsStrings(regulatoryActs)).addProjectionsFromStrings(RegulatoryActExpenditureImpl.FIELD_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ACT_IDENTIFIER
							,RegulatoryActExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER
							,RegulatoryActExpenditureImpl.FIELD_LESSOR_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ENTRY_AUTHORIZATION_AMOUNT,RegulatoryActExpenditureImpl.FIELD_PAYMENT_CREDIT_AMOUNT));
			for(RegulatoryAct regulatoryAct : regulatoryActs) {
				Boolean regulatoryActExpenditureExist = null;
				if(regulatoryActExpenditures != null)
					for(RegulatoryActExpenditure regulatoryActExpenditure : regulatoryActExpenditures) {
						if(regulatoryActExpenditure.getActIdentifier().equals(regulatoryAct.getIdentifier())) {
							regulatoryActExpenditureExist = Boolean.TRUE;
							break;
						}
					}
				if(regulatoryActExpenditureExist == null)
					throwablesMessages.add(String.format("L'acte de gestion %s n'a pas de %s", regulatoryAct.getName(),Expenditure.NAME));
			}
			throwablesMessages.throwIfNotEmpty();
			
			generateCancelationsRepositioningsActs(legislativeActVersion, regulatoryActs,regulatoryActExpenditures, throwablesMessages, auditWho, auditWho, auditWhen);
		}
		
		Long count = includedRegulatoryActCount*2+1;
		// Return of message
		result.close().setName(String.format("Génération de %s acte(s) de la version du collectif %s par %s",count,legislativeActVersionIdentifier,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre d'actes générés : %s", count));
		return result;
	}
	
	private void generateAdjustmentAct(LegislativeActVersion legislativeActVersion,ThrowablesMessages throwablesMessages,String auditFunctionality,String auditWho,LocalDateTime auditWhen) {
		Long adjustedCount = expenditurePersistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier(),Parameters.ADJUSTMENTS_EQUAL_ZERO,Boolean.FALSE));
		LogHelper.logInfo(String.format("Nombre de dépenses ajustées : %s", adjustedCount), getClass());
		if(NumberHelper.isEqualToZero(adjustedCount))
			return;
		GeneratedActImpl act = new GeneratedActImpl();
		act.setIdentifier(legislativeActVersion.getIdentifier());
		act.setLegislativeActVersion((LegislativeActVersionImpl) legislativeActVersion);
		act.setType(GeneratedAct.Type.ADJUSTMENT);
		act.setActSourceIdentifier(legislativeActVersion.getIdentifier());
		act.setApplied(Boolean.FALSE);
		act.setCode(legislativeActVersion.getCode());
		act.setName(legislativeActVersion.getName());
		audit(act, auditFunctionality, auditWho, auditWhen);
		entityManager.persist(act);
		
		Integer batchSize = 100;
		List<Integer> batchSizes = NumberHelper.getProportions(adjustedCount.intValue(), batchSize);		
		LogHelper.logInfo(String.format("Traitement par lot de %s. Nombre de lot = %s", batchSize,batchSizes.size()), getClass());
		for(Integer index =0; index < batchSizes.size(); index = index + 1) {
			Collection<ExpenditureImpl> expenditures = CollectionHelper.cast(ExpenditureImpl.class, expenditurePersistence.readMany(new QueryExecutorArguments()
					.addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,ExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER,ExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER
							,ExpenditureImpl.FIELD_LESSOR_IDENTIFIER).addProcessableTransientFieldsNames(ExpenditureImpl.FIELDS_AMOUNTS)
					.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier(),Parameters.ADJUSTMENTS_EQUAL_ZERO,Boolean.FALSE,Parameters.GENERATED_ACT_EXPENDITURE_EXISTS,Boolean.FALSE)
					.setNumberOfTuples(batchSizes.get(index))));
			LogHelper.logInfo(String.format("Traitement du lot %s/%s | %s",index+1,batchSizes.size(),CollectionHelper.getSize(expenditures)), getClass());
			if(CollectionHelper.isEmpty(expenditures))
				break;
			expenditures.forEach(expenditure -> {
				GeneratedActExpenditureImpl generatedActExpenditure = new GeneratedActExpenditureImpl().setIdentifier(act.getIdentifier()+"_"+expenditure.getIdentifier()).setAct(act).setActivityIdentifier(expenditure.getActivityIdentifier())
						.setEconomicNatureIdentifier(expenditure.getEconomicNatureIdentifier()).setFundingSourceIdentifier(expenditure.getFundingSourceIdentifier()).setLessorIdentifier(expenditure.getLessorIdentifier())
						.setEntryAuthorizationAmount(expenditure.getEntryAuthorization().getActualMinusMovementIncludedPlusAdjustment())
						.setPaymentCreditAmount(expenditure.getPaymentCredit().getActualMinusMovementIncludedPlusAdjustment());
				audit(generatedActExpenditure, auditFunctionality, auditWho, auditWhen);
				entityManager.persist(generatedActExpenditure);
			});
		}
	}
	
	private void generateCancelationsRepositioningsActs(LegislativeActVersion legislativeActVersion,Collection<RegulatoryAct> regulatoryActs,Collection<RegulatoryActExpenditure> regulatoryActExpenditures
			,ThrowablesMessages throwablesMessages,String auditFunctionality,String auditWho,LocalDateTime auditWhen) {
		for(GeneratedAct.Type type : GeneratedAct.Type.CANCELATION_REPOSITIONING) {
			regulatoryActs.forEach(regulatoryAct -> {
				GeneratedActImpl act = new GeneratedActImpl();
				act.setIdentifier(String.format("%s%s_%s", GeneratedAct.Type.CANCELATION.equals(type) ? "A_" : "",legislativeActVersion.getIdentifier(),regulatoryAct.getIdentifier()));
				act.setLegislativeActVersion((LegislativeActVersionImpl) legislativeActVersion);
				act.setType(type);
				act.setActSourceIdentifier(regulatoryAct.getIdentifier());
				act.setApplied(Boolean.FALSE);
				act.setCode(String.format("%s%s_%s", GeneratedAct.Type.CANCELATION.equals(type) ? "A_" : "",legislativeActVersion.getCode(),regulatoryAct.getCode()));
				act.setName(String.format("%s%s", GeneratedAct.Type.CANCELATION.equals(type) ? type.getValue() : "",regulatoryAct.getName()));
				audit(act, auditFunctionality, auditWho, auditWhen);
				entityManager.persist(act);
				Collection<RegulatoryActExpenditureImpl> actRegulatoryActExpenditures = CollectionHelper.cast(RegulatoryActExpenditureImpl.class
						, regulatoryActExpenditures.stream().filter(regulatoryActExpenditure -> regulatoryActExpenditure.getActIdentifier().equals(regulatoryAct.getIdentifier())).collect(Collectors.toList()));
				if(CollectionHelper.isNotEmpty(actRegulatoryActExpenditures)) {
					actRegulatoryActExpenditures.forEach(regulatoryActExpenditure -> {
						GeneratedActExpenditureImpl generatedActExpenditure = new GeneratedActExpenditureImpl().setIdentifier(act.getIdentifier()+"_"+regulatoryActExpenditure.getIdentifier()).setAct(act)
								.setActivityIdentifier(regulatoryActExpenditure.getActivityIdentifier()).setEconomicNatureIdentifier(regulatoryActExpenditure.getEconomicNatureIdentifier())
								.setFundingSourceIdentifier(regulatoryActExpenditure.getFundingSourceIdentifier()).setLessorIdentifier(regulatoryActExpenditure.getLessorIdentifier())
								.setEntryAuthorizationAmount(regulatoryActExpenditure.getEntryAuthorizationAmount()).setPaymentCreditAmount(regulatoryActExpenditure.getPaymentCreditAmount());
						audit(generatedActExpenditure, auditFunctionality, auditWho, auditWhen);
						entityManager.persist(generatedActExpenditure);
					});
				}
			});
		}
	}
}