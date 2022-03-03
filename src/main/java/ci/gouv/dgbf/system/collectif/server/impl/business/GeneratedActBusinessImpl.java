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
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
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
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActExpenditureImpl;

@ApplicationScoped
public class GeneratedActBusinessImpl extends AbstractSpecificBusinessImpl<GeneratedAct> implements GeneratedActBusiness,Serializable {

	@Inject EntityManager entityManager;
	@Inject GeneratedActPersistence persistence;
	@Inject GeneratedActExpenditurePersistence generatedActExpenditurePersistence;
	@Inject LegislativeActPersistence legislativeActPersistence;
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
		String auditIdentifier = generateAuditIdentifier();
		
		Collection<GeneratedActImpl> generatedActs = entityManager.createNamedQuery(GeneratedActImpl.QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIIFER,GeneratedActImpl.class).setParameter("legislativeActVersionIdentifier", legislativeActVersionIdentifier)
				.getResultList();
		Collection<GeneratedActExpenditureImpl> generatedActExpenditures = entityManager.createNamedQuery(GeneratedActExpenditureImpl.QUERY_READ_BY_ACT_IDENTIIFERS,GeneratedActExpenditureImpl.class)
				.setParameter("actIdentifiers", FieldHelper.readSystemIdentifiersAsStrings(generatedActs)).getResultList();
		
		if(CollectionHelper.isNotEmpty(generatedActExpenditures))
			generatedActExpenditures.forEach(generatedActExpenditure -> {
				audit(generatedActExpenditure,auditIdentifier, DELETE_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_AUDIT_IDENTIFIER, auditWho, auditWhen);
				entityManager.remove(generatedActExpenditure);
			});
		
		if(CollectionHelper.isNotEmpty(generatedActs))
			generatedActs.forEach(generatedAct -> {
				audit(generatedAct,auditIdentifier, DELETE_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_AUDIT_IDENTIFIER, auditWho, auditWhen);
				entityManager.remove(generatedAct);
			});
		
		// Return of message
		result.close().setName(String.format("Suppression de %s acte(s) et %s dépense(s) de la version du collectif %s par %s",generatedActs.size(),generatedActExpenditures.size(),legislativeActVersionIdentifier,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre d'actes supprimés : %s", generatedActs.size()));
		return result;
	}
	
	@Override @Transactional
	public Result generateByLegislativeActVersionIdentifier(String legislativeActVersionIdentifier, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		Object[] instances = ValidatorImpl.GeneratedAct.generateByLegislativeActVersionIdentifierInputs(legislativeActVersionIdentifier, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) instances[0];
		ValidatorImpl.GeneratedAct.generateByLegislativeActVersionIdentifier(legislativeActVersion, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		LegislativeActImpl legislativeAct = (LegislativeActImpl) legislativeActPersistence.readOne(legislativeActVersion.getActIdentifier(), List.of(LegislativeActImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_ACT_GENERATION_MODE));
		
		LocalDateTime auditWhen = LocalDateTime.now();
		String auditIdentifier = generateAuditIdentifier();
		
		Collection<RegulatoryAct> regulatoryActs = regulatoryActPersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier()
				,Parameters.REGULATORY_ACT_INCLUDED,Boolean.TRUE));
		LogHelper.log(String.format("%s acte(s) de gestion inclus dans %s", CollectionHelper.getSize(regulatoryActs),legislativeActVersion.getName()),Result.getLogLevel(), getClass());
		Collection<String> regulatoryActsIdentifiers = FieldHelper.readSystemIdentifiersAsStrings(regulatoryActs);
		Collection<RegulatoryActExpenditure> regulatoryActExpenditures = CollectionHelper.isEmpty(regulatoryActsIdentifiers) ? null 
				: regulatoryActExpenditurePersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.REGULATORY_ACT_IDENTIFIERS
				,regulatoryActsIdentifiers).addProjectionsFromStrings(RegulatoryActExpenditureImpl.FIELD_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ACT_IDENTIFIER
						,RegulatoryActExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER
						,RegulatoryActExpenditureImpl.FIELD_LESSOR_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ENTRY_AUTHORIZATION_AMOUNT,RegulatoryActExpenditureImpl.FIELD_PAYMENT_CREDIT_AMOUNT));
		
		if(regulatoryActs != null)
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
		
		Integer count = generateActsByMode(legislativeActVersion, regulatoryActs, regulatoryActExpenditures, legislativeAct.getActGenerationMode(), throwablesMessages,auditIdentifier, GENERATE_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_AUDIT_IDENTIFIER
				, auditWho, auditWhen);		
		throwablesMessages.throwIfNotEmpty();
		
		if(count == null)
			count = 0;
		// Return of message
		result.close().setName(String.format("Génération de %s acte(s) de la version du collectif %s par %s",count,legislativeActVersionIdentifier,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre d'actes générés : %s", count));
		return result;
	}
	
	/* Acts Generation Methods */
	
	private Integer generateActsByMode(LegislativeActVersionImpl legislativeActVersion,Collection<RegulatoryAct> regulatoryActs,Collection<RegulatoryActExpenditure> regulatoryActExpenditures,LegislativeAct.ActGenerationMode generationMode
			,ThrowablesMessages throwablesMessages,String auditIdentifier,String auditFunctionality,String auditWho,LocalDateTime auditWhen) {
		if(CollectionHelper.isEmpty(regulatoryActs) || CollectionHelper.isEmpty(regulatoryActExpenditures))
			return null;
		if(generationMode == null)
			generationMode = LegislativeAct.ActGenerationMode.DEFAULT;
		if(LegislativeAct.ActGenerationMode.CANCELATION_ADJUSTMENT.equals(generationMode)) {
			generateCancelationsActs(legislativeActVersion, regulatoryActs, regulatoryActExpenditures, throwablesMessages,auditIdentifier, auditFunctionality, auditWho, auditWhen);
			generateCollectiveAct(legislativeActVersion, regulatoryActs, regulatoryActExpenditures, throwablesMessages,auditIdentifier, auditFunctionality, auditWho, auditWhen);
			return NumberHelper.getInteger(NumberHelper.add(CollectionHelper.getSize(regulatoryActs),1));
		}else if(LegislativeAct.ActGenerationMode.CANCELATION_REPOSITIONING_ADJUSTMENT.equals(generationMode)) {
			generateCancelationsRepositioningsActs(legislativeActVersion, regulatoryActs, regulatoryActExpenditures, throwablesMessages,auditIdentifier, auditFunctionality, auditWho, auditWhen);
			generateAdjustmentAct(legislativeActVersion, throwablesMessages,auditIdentifier, auditFunctionality, auditWho, auditWhen);
		}else if(LegislativeAct.ActGenerationMode.ADJUSTMENT.equals(generationMode)) {
			throwablesMessages.add(String.format("%s pas encore implémenté",generationMode.getValue()));
		}
		return null;
	}
	
	private void generateCancelationsActs(LegislativeActVersionImpl legislativeActVersion,Collection<RegulatoryAct> regulatoryActs,Collection<RegulatoryActExpenditure> regulatoryActExpenditures
			,ThrowablesMessages throwablesMessages,String auditIdentifier,String auditFunctionality,String auditWho,LocalDateTime auditWhen) {
		regulatoryActs.forEach(regulatoryAct -> {
			GeneratedActImpl act = new GeneratedActImpl();
			act.setIdentifier(String.format(CANCELATION_ACT_IDENTIFIER_FORMAT,legislativeActVersion.getIdentifier(),regulatoryAct.getIdentifier()));
			act.setLegislativeActVersion((LegislativeActVersionImpl) legislativeActVersion);
			act.setType(GeneratedAct.Type.CANCELATION);
			act.setActSourceIdentifier(regulatoryAct.getIdentifier());
			act.setApplied(Boolean.FALSE);
			act.setCode(String.format(CANCELATION_ACT_CODE_FORMAT,legislativeActVersion.getCode(),regulatoryAct.getCode()));
			act.setName(String.format(CANCELATION_ACT_NAME_FORMAT, GeneratedAct.Type.CANCELATION.getValue(),regulatoryAct.getName()));
			audit(act, auditIdentifier,auditFunctionality, auditWho, auditWhen);
			entityManager.persist(act);
			Collection<RegulatoryActExpenditureImpl> actRegulatoryActExpenditures = CollectionHelper.cast(RegulatoryActExpenditureImpl.class
					, regulatoryActExpenditures.stream().filter(regulatoryActExpenditure -> regulatoryActExpenditure.getActIdentifier().equals(regulatoryAct.getIdentifier())).collect(Collectors.toList()));
			if(CollectionHelper.isNotEmpty(actRegulatoryActExpenditures)) {
				actRegulatoryActExpenditures.forEach(regulatoryActExpenditure -> {
					GeneratedActExpenditureImpl generatedActExpenditure = new GeneratedActExpenditureImpl().setIdentifier(act.getIdentifier()+"_"+regulatoryActExpenditure.getIdentifier()).setAct(act)
							.setActivityIdentifier(regulatoryActExpenditure.getActivityIdentifier()).setEconomicNatureIdentifier(regulatoryActExpenditure.getEconomicNatureIdentifier())
							.setFundingSourceIdentifier(regulatoryActExpenditure.getFundingSourceIdentifier()).setLessorIdentifier(regulatoryActExpenditure.getLessorIdentifier())
							.setEntryAuthorizationAmount(regulatoryActExpenditure.getEntryAuthorizationAmount()).setPaymentCreditAmount(regulatoryActExpenditure.getPaymentCreditAmount());
					audit(generatedActExpenditure, auditIdentifier,auditFunctionality, auditWho, auditWhen);
					entityManager.persist(generatedActExpenditure);
				});
			}
		});	
	}
	
	private Integer generateCollectiveAct(LegislativeActVersionImpl legislativeActVersion,Collection<RegulatoryAct> regulatoryActs,Collection<RegulatoryActExpenditure> regulatoryActExpenditures
			,ThrowablesMessages throwablesMessages,String auditIdentifier,String auditFunctionality,String auditWho,LocalDateTime auditWhen) {
		Long count = expenditurePersistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier()
				,Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO,Boolean.TRUE));
		LogHelper.log(String.format("Nombre de dépenses ajustées ou ayant leur mouvements inclus : %s", count),Result.getLogLevel(), getClass());
		if(NumberHelper.isEqualToZero(count))
			return null;
		GeneratedActImpl act = new GeneratedActImpl();
		act.setIdentifier(legislativeActVersion.getIdentifier());
		act.setLegislativeActVersion((LegislativeActVersionImpl) legislativeActVersion);
		act.setType(GeneratedAct.Type.ADJUSTMENT);
		act.setActSourceIdentifier(legislativeActVersion.getIdentifier());
		act.setApplied(Boolean.FALSE);
		act.setCode(legislativeActVersion.getCode());
		act.setName(legislativeActVersion.getName());
		audit(act, auditIdentifier,auditFunctionality, auditWho, auditWhen);
		entityManager.persist(act);
		
		Integer batchSize = 100;
		List<Integer> batchSizes = NumberHelper.getProportions(count.intValue(), batchSize);		
		LogHelper.log(String.format("Traitement par lot de %s. Nombre de lot = %s", batchSize,batchSizes.size()),Result.getLogLevel(), getClass());
		for(Integer index =0; index < batchSizes.size(); index = index + 1) {
			Collection<ExpenditureImpl> expenditures = CollectionHelper.cast(ExpenditureImpl.class, expenditurePersistence.readMany(new QueryExecutorArguments()
					.addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,ExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER,ExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER
							,ExpenditureImpl.FIELD_LESSOR_IDENTIFIER).addProcessableTransientFieldsNames(ExpenditureImpl.FIELDS_AMOUNTS)
					.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier(),Parameters.ADJUSTMENTS_EQUAL_ZERO,Boolean.FALSE,Parameters.GENERATED_ACT_EXPENDITURE_EXISTS,Boolean.FALSE)
					.setNumberOfTuples(batchSizes.get(index))));
			LogHelper.log(String.format("Traitement du lot %s/%s | %s",index+1,batchSizes.size(),CollectionHelper.getSize(expenditures)),Result.getLogLevel(), getClass());
			if(CollectionHelper.isEmpty(expenditures))
				break;
			expenditures.forEach(expenditure -> {
				if(expenditure.getEntryAuthorization() == null || expenditure.getPaymentCredit() == null) {
					LogHelper.logWarning(String.format("%s %s doit avoir AE(%s) et CP(%s)", Expenditure.NAME,expenditure.getIdentifier(),expenditure.getEntryAuthorization() != null,expenditure.getPaymentCredit() != null), getClass());
					return;
				}
				GeneratedActExpenditureImpl generatedActExpenditure = new GeneratedActExpenditureImpl().setIdentifier(act.getIdentifier()+"_"+expenditure.getIdentifier()).setAct(act).setActivityIdentifier(expenditure.getActivityIdentifier())
						.setEconomicNatureIdentifier(expenditure.getEconomicNatureIdentifier()).setFundingSourceIdentifier(expenditure.getFundingSourceIdentifier()).setLessorIdentifier(expenditure.getLessorIdentifier())
						.setEntryAuthorizationAmount(expenditure.getEntryAuthorization().getActualMinusMovementIncludedPlusAdjustment())
						.setPaymentCreditAmount(expenditure.getPaymentCredit().getActualMinusMovementIncludedPlusAdjustment());
				audit(generatedActExpenditure,auditIdentifier, auditFunctionality, auditWho, auditWhen);
				entityManager.persist(generatedActExpenditure);
			});
		}
		
		return 1;
	}
	
	private void generateAdjustmentAct(LegislativeActVersion legislativeActVersion,ThrowablesMessages throwablesMessages,String auditIdentifier,String auditFunctionality,String auditWho,LocalDateTime auditWhen) {
		Long adjustedCount = expenditurePersistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier(),Parameters.ADJUSTMENTS_EQUAL_ZERO,Boolean.FALSE));
		LogHelper.log(String.format("Nombre de dépenses ajustées : %s", adjustedCount),Result.getLogLevel(), getClass());
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
		audit(act,auditIdentifier, auditFunctionality, auditWho, auditWhen);
		entityManager.persist(act);
		
		Integer batchSize = 100;
		List<Integer> batchSizes = NumberHelper.getProportions(adjustedCount.intValue(), batchSize);		
		LogHelper.log(String.format("Traitement par lot de %s. Nombre de lot = %s", batchSize,batchSizes.size()),Result.getLogLevel(), getClass());
		for(Integer index =0; index < batchSizes.size(); index = index + 1) {
			Collection<ExpenditureImpl> expenditures = CollectionHelper.cast(ExpenditureImpl.class, expenditurePersistence.readMany(new QueryExecutorArguments()
					.addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,ExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER,ExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER
							,ExpenditureImpl.FIELD_LESSOR_IDENTIFIER).addProcessableTransientFieldsNames(ExpenditureImpl.FIELDS_AMOUNTS)
					.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier(),Parameters.ADJUSTMENTS_EQUAL_ZERO,Boolean.FALSE,Parameters.GENERATED_ACT_EXPENDITURE_EXISTS,Boolean.FALSE)
					.setNumberOfTuples(batchSizes.get(index))));
			LogHelper.log(String.format("Traitement du lot %s/%s | %s",index+1,batchSizes.size(),CollectionHelper.getSize(expenditures)),Result.getLogLevel(), getClass());
			if(CollectionHelper.isEmpty(expenditures))
				break;
			expenditures.forEach(expenditure -> {
				GeneratedActExpenditureImpl generatedActExpenditure = new GeneratedActExpenditureImpl().setIdentifier(act.getIdentifier()+"_"+expenditure.getIdentifier()).setAct(act).setActivityIdentifier(expenditure.getActivityIdentifier())
						.setEconomicNatureIdentifier(expenditure.getEconomicNatureIdentifier()).setFundingSourceIdentifier(expenditure.getFundingSourceIdentifier()).setLessorIdentifier(expenditure.getLessorIdentifier())
						.setEntryAuthorizationAmount(expenditure.getEntryAuthorization().getActualMinusMovementIncludedPlusAdjustment())
						.setPaymentCreditAmount(expenditure.getPaymentCredit().getActualMinusMovementIncludedPlusAdjustment());
				audit(generatedActExpenditure,auditIdentifier, auditFunctionality, auditWho, auditWhen);
				entityManager.persist(generatedActExpenditure);
			});
		}
	}
	
	private void generateCancelationsRepositioningsActs(LegislativeActVersion legislativeActVersion,Collection<RegulatoryAct> regulatoryActs,Collection<RegulatoryActExpenditure> regulatoryActExpenditures
			,ThrowablesMessages throwablesMessages,String auditIdentifier,String auditFunctionality,String auditWho,LocalDateTime auditWhen) {
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
				audit(act,auditIdentifier, auditFunctionality, auditWho, auditWhen);
				entityManager.persist(act);
				Collection<RegulatoryActExpenditureImpl> actRegulatoryActExpenditures = CollectionHelper.cast(RegulatoryActExpenditureImpl.class
						, regulatoryActExpenditures.stream().filter(regulatoryActExpenditure -> regulatoryActExpenditure.getActIdentifier().equals(regulatoryAct.getIdentifier())).collect(Collectors.toList()));
				if(CollectionHelper.isNotEmpty(actRegulatoryActExpenditures)) {
					actRegulatoryActExpenditures.forEach(regulatoryActExpenditure -> {
						GeneratedActExpenditureImpl generatedActExpenditure = new GeneratedActExpenditureImpl().setIdentifier(act.getIdentifier()+"_"+regulatoryActExpenditure.getIdentifier()).setAct(act)
								.setActivityIdentifier(regulatoryActExpenditure.getActivityIdentifier()).setEconomicNatureIdentifier(regulatoryActExpenditure.getEconomicNatureIdentifier())
								.setFundingSourceIdentifier(regulatoryActExpenditure.getFundingSourceIdentifier()).setLessorIdentifier(regulatoryActExpenditure.getLessorIdentifier())
								.setEntryAuthorizationAmount(regulatoryActExpenditure.getEntryAuthorizationAmount()).setPaymentCreditAmount(regulatoryActExpenditure.getPaymentCreditAmount());
						audit(generatedActExpenditure,auditIdentifier, auditFunctionality, auditWho, auditWhen);
						entityManager.persist(generatedActExpenditure);
					});
				}
			});
		}
	}
	
	private static final String CANCELATION_ACT_IDENTIFIER_FORMAT = "A_%s_%s";
	private static final String CANCELATION_ACT_CODE_FORMAT = CANCELATION_ACT_IDENTIFIER_FORMAT;
	private static final String CANCELATION_ACT_NAME_FORMAT = "%s %s";
}