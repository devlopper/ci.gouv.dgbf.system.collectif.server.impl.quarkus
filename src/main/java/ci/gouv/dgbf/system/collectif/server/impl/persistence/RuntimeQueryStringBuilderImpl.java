package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.cyk.utility.persistence.query.Language.parenthesis;
import static org.cyk.utility.persistence.query.Language.Where.or;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.query.string.LikeStringBuilder;
import org.cyk.utility.persistence.server.query.string.LikeStringValueBuilder;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder.Arguments;
import org.cyk.utility.persistence.server.query.string.RuntimeQueryStringBuilder;
import org.cyk.utility.persistence.server.query.string.WhereStringBuilder.Predicate;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ActionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExercisePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.FundingSourcePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LessorPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourcePersistence;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @ci.gouv.dgbf.system.collectif.server.api.System @Unremovable
public class RuntimeQueryStringBuilderImpl extends RuntimeQueryStringBuilder.AbstractImpl implements Serializable {

	@Inject ExercisePersistence exercisePersistence;
	@Inject LegislativeActPersistence legislativeActPersistence;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	@Inject ActionPersistence actionPersistence;
	@Inject ActivityPersistence activityPersistence;
	@Inject ResourceActivityPersistence resourceActivityPersistence;
	@Inject EconomicNaturePersistence economicNaturePersistence;
	@Inject FundingSourcePersistence fundingSourcePersistence;
	@Inject LessorPersistence lessorPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ResourcePersistence resourcePersistence;
	@Inject RegulatoryActPersistence regulatoryActPersistence;
	@Inject RegulatoryActExpenditurePersistence regulatoryActExpenditurePersistence;
	@Inject GeneratedActPersistence generatedActPersistence;
	@Inject GeneratedActExpenditurePersistence generatedActExpenditurePersistence;
	
	@Override
	protected void setTuple(QueryExecutorArguments arguments, Arguments builderArguments) {
		super.setTuple(arguments, builderArguments);
		if(Boolean.TRUE.equals(legislativeActPersistence.isProcessable(arguments))) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",LegislativeActImpl.ENTITY_NAME));
			String versionIdentifier = (String) arguments.getFilterFieldValue(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER);
			if(StringHelper.isNotBlank(versionIdentifier)) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s bav ON bav.%s = t AND bav.identifier = '%s'",LegislativeActVersionImpl.ENTITY_NAME
						,LegislativeActVersionImpl.FIELD_ACT,versionIdentifier));
				arguments.removeFilterFields(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER);
			}
			if((arguments.getFilterBackup() != null && arguments.getFilterBackup().getFieldValue(Parameters.LATEST_LEGISLATIVE_ACT) != null) || arguments.getFilterField(Parameters.EXERCISE_YEAR) != null) {
				builderArguments.getTuple().addJoins(String.format("LEFT JOIN %s exercise ON exercise.%s = t.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
			}
		}
		
		if(Boolean.TRUE.equals(legislativeActVersionPersistence.isProcessable(arguments))) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",LegislativeActVersionImpl.ENTITY_NAME));
			
			if((arguments.getFilterBackup() != null && arguments.getFilterBackup().getFieldValue(Parameters.DEFAULT_LEGISLATIVE_ACT_VERSION_IN_LATEST_LEGISLATIVE_ACT) != null)) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s la ON la = t.%s AND la.%s = t",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_DEFAULT_VERSION));
				builderArguments.getTuple().addJoins(String.format("LEFT JOIN %s exercise ON exercise.%s = la.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
				//builderArguments.getTuple().addJoins(String.format("JOIN %s dv ON dv = t AND dv.%s = la",LegislativeActVersionImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT));
			}
		}
		
		if(Boolean.TRUE.equals(expenditurePersistence.isProcessable(arguments))) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",ExpenditureImpl.ENTITY_NAME));
			/*if(ExpenditureQueryStringBuilder.Join.isJoinedToRegulatoryActLegislativeActVersionAndAvailable(arguments, builderArguments)) {
				ExpenditureQueryStringBuilder.Join.joinRegulatoryActLegislativeActVersionAndAvailable(builderArguments);
				builderArguments.getGroup(Boolean.TRUE).add("t.identifier");
			}else {*/
				if(arguments.getFilterField(Parameters.INCLUDED_MOVEMENT_NOT_EQUAL_ZERO) != null || arguments.getFilterField(Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO) != null) {
					/*
					builderArguments.getTuple().addJoins(String.format("JOIN %s lav ON lav = t.%s",LegislativeActVersionImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION));
					builderArguments.getTuple().addJoins(String.format("JOIN %s la ON la = lav.%s",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT));
					builderArguments.getTuple().addJoins(String.format("JOIN %s exercise ON exercise.%s = la.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));	
					*/
					ExpenditureQueryStringBuilder.Tuple.joinAmounts(builderArguments,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
					//if(!arguments.getQuery().getIdentifier().equals(expenditurePersistence.getQueryIdentifierCountDynamic()))
					//	builderArguments.getGroup(Boolean.TRUE).add("t.identifier");
				}
				
				if(arguments.getFilterField(Parameters.AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_LESS_THAN_ZERO) != null) {
					ExpenditureQueryStringBuilder.Tuple.joinAmounts(builderArguments,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
					//ExpenditureQueryStringBuilder.Join.joinRegulatoryActLegislativeActVersionAndAvailable(builderArguments);
					//builderArguments.getGroup(Boolean.TRUE).add("t.identifier");
				}
				
				/*String identifier = (String) arguments.getFilterFieldValue(Parameters.SECTION_IDENTIFIER);
				if(StringHelper.isNotBlank(identifier)) {
					builderArguments.getTuple().addJoins(String.format("JOIN %s v ON v.identifier = t.identifier AND v.sectionIdentifier = '%s'",ExpenditureView.ENTITY_NAME
							,identifier));
					arguments.removeFilterFields(Parameters.SECTION_IDENTIFIER);
				}
				*/
				if(Boolean.TRUE.equals(isExpenditureJoinedToView(arguments, builderArguments))) {
					builderArguments.getTuple().addJoins(String.format("JOIN %s ev ON ev.identifier = t.identifier",ExpenditureView.ENTITY_NAME));
				}
			//}
		}else if(Boolean.TRUE.equals(resourcePersistence.isProcessable(arguments))) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",ResourceImpl.ENTITY_NAME));
			if(Boolean.TRUE.equals(isResourceJoinedToView(arguments, builderArguments))) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s v ON v.identifier = t.identifier",ResourceView.ENTITY_NAME));
			}
		}
	}
	/*
	@Override
	protected Boolean isGroupedByIdentifier(QueryExecutorArguments arguments) {
		if(arguments.getFilterField(Parameters.INCLUDED_MOVEMENT_NOT_EQUAL_ZERO) != null || arguments.getFilterField(Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO) != null)
			return Boolean.TRUE;
		return super.isGroupedByIdentifier(arguments);
	}*/
	
	@Override
	protected void populatePredicate(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		super.populatePredicate(arguments, builderArguments, predicate, filter);
		if(Boolean.TRUE.equals(expenditurePersistence.isProcessable(arguments)))
			populatePredicateExpenditure(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(resourcePersistence.isProcessable(arguments)))
			populatePredicateResource(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(legislativeActPersistence.isProcessable(arguments)))
			populatePredicateLegislativeAct(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(legislativeActVersionPersistence.isProcessable(arguments)))
			populatePredicateLegislativeActVersion(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(budgetSpecializationUnitPersistence.isProcessable(arguments)))
			populatePredicateBudgetSpecializationUnit(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(actionPersistence.isProcessable(arguments)))
			populatePredicateAction(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(activityPersistence.isProcessable(arguments)))
			populatePredicateActivity(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(resourceActivityPersistence.isProcessable(arguments)))
			populatePredicateResourceActivity(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(economicNaturePersistence.isProcessable(arguments)))
			populatePredicateEconomicNature(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(fundingSourcePersistence.isProcessable(arguments)))
			populatePredicateFundingSource(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(lessorPersistence.isProcessable(arguments)))
			populatePredicateLessor(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(regulatoryActPersistence.isProcessable(arguments)))
			populatePredicateRegulatoryAct(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(regulatoryActExpenditurePersistence.isProcessable(arguments)))
			populatePredicateRegulatoryActExpenditure(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(generatedActPersistence.isProcessable(arguments)))
			populatePredicateGeneratedAct(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(generatedActExpenditurePersistence.isProcessable(arguments)))
			populatePredicateGeneratedActExpenditure(arguments, builderArguments, predicate, filter);
	}
	
	@Override
	protected void setOrder(QueryExecutorArguments arguments, Arguments builderArguments) {
		if(expenditurePersistence.getQueryIdentifierReadDynamic().equals(arguments.getQuery().getIdentifier())) {
			if(builderArguments.getOrder() == null || CollectionHelper.isEmpty(builderArguments.getOrder().getStrings())) {
				if(Boolean.TRUE.equals(isExpenditureJoinedToView(arguments, builderArguments)))
					builderArguments.getOrder(Boolean.TRUE).asc("ev", ExpenditureView.FIELD_SECTION_CODE/*, ExpenditureView.FIELD_ADMINISTRATIVE_UNIT_CODE*/, ExpenditureView.FIELD_NATURE_CODE
							, ExpenditureView.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE, ExpenditureView.FIELD_ACTION_CODE, ExpenditureView.FIELD_ACTIVITY_CODE
							, ExpenditureView.FIELD_ECONOMIC_NATURE_CODE, ExpenditureView.FIELD_FUNDING_SOURCE_CODE, ExpenditureView.FIELD_LESSOR_CODE);
				else
					builderArguments.getOrder(Boolean.TRUE).asc("t", "identifier");
			}
		}else if(resourcePersistence.getQueryIdentifierReadDynamic().equals(arguments.getQuery().getIdentifier())) {
			if(builderArguments.getOrder() == null || CollectionHelper.isEmpty(builderArguments.getOrder().getStrings())) {
				if(Boolean.TRUE.equals(isResourceJoinedToView(arguments, builderArguments)))
					builderArguments.getOrder(Boolean.TRUE).asc("v", ResourceView.FIELD_SECTION_CODE, ResourceView.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE, ResourceView.FIELD_ACTIVITY_CODE, ResourceView.FIELD_ECONOMIC_NATURE_CODE);
				else
					builderArguments.getOrder(Boolean.TRUE).asc("t", "identifier");
			}
		}else if(legislativeActVersionPersistence.isProcessable(arguments)) {
			if(!legislativeActVersionPersistence.getQueryIdentifierCountDynamic().equals(arguments.getQuery().getIdentifier())) {
				Boolean latest = arguments.getFilterFieldValueAsBoolean(null,Parameters.LATEST_LEGISLATIVE_ACT_VERSION);
				if(Boolean.TRUE.equals(latest)) {
					builderArguments.getOrder(Boolean.TRUE).desc("t", LegislativeActVersionImpl.FIELD_NUMBER);
					arguments.setNumberOfTuples(1);
				}else {
					if(builderArguments.getOrder() == null || CollectionHelper.isEmpty(builderArguments.getOrder().getStrings())) {
						builderArguments.getOrder(Boolean.TRUE).desc("t", "code");
					}
				}
				
				Boolean defaultLegislativeActVersionInLatestLegislativeAct = arguments.getFilterFieldValueAsBoolean(null,Parameters.DEFAULT_LEGISLATIVE_ACT_VERSION_IN_LATEST_LEGISLATIVE_ACT);
				if(Boolean.TRUE.equals(defaultLegislativeActVersionInLatestLegislativeAct)) {
					builderArguments.getOrder(Boolean.TRUE).desc("exercise", ExerciseImpl.FIELD_YEAR).desc("la", LegislativeActImpl.FIELD_NUMBER);
					arguments.setNumberOfTuples(1);
				}
			}
			arguments.removeFilterFields(Parameters.LATEST_LEGISLATIVE_ACT_VERSION);
			arguments.removeFilterFields(Parameters.DEFAULT_LEGISLATIVE_ACT_VERSION_IN_LATEST_LEGISLATIVE_ACT);
		}else if(legislativeActPersistence.isProcessable(arguments)) {
			if(!legislativeActPersistence.getQueryIdentifierCountDynamic().equals(arguments.getQuery().getIdentifier())) {
				Boolean latest = arguments.getFilterFieldValueAsBoolean(null,Parameters.LATEST_LEGISLATIVE_ACT);
				if(Boolean.TRUE.equals(latest)) {
					builderArguments.getOrder(Boolean.TRUE).desc("exercise", ExerciseImpl.FIELD_YEAR).desc("t", LegislativeActImpl.FIELD_NUMBER);
					arguments.setNumberOfTuples(1);
				}
			}
			arguments.removeFilterFields(Parameters.LATEST_LEGISLATIVE_ACT);
			
			if(legislativeActPersistence.getQueryIdentifierReadDynamic().equals(arguments.getQuery().getIdentifier())) {
				if(builderArguments.getOrder() == null || CollectionHelper.isEmpty(builderArguments.getOrder().getStrings())) {
					builderArguments.getOrder(Boolean.TRUE).desc("t", "code");
				}
			}
		}else if(exercisePersistence.getQueryIdentifierReadDynamic().equals(arguments.getQuery().getIdentifier())) {
			if(builderArguments.getOrder() == null || CollectionHelper.isEmpty(builderArguments.getOrder().getStrings())) {
				builderArguments.getOrder(Boolean.TRUE).desc("t", ExerciseImpl.FIELD_YEAR);
			}
		}
		
		super.setOrder(arguments, builderArguments);
	}
	
	/**/
	
	public static void populatePredicateExpenditure(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_IDENTIFIER,"t"
				,FieldHelper.join(ExpenditureImpl.FIELD_ACT_VERSION,LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_IDENTIFIER));
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"t"
				,FieldHelper.join(ExpenditureImpl.FIELD_ACT_VERSION,LegislativeActImpl.FIELD_IDENTIFIER));
		
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.SECTION_IDENTIFIER,"ev"
				,ExpenditureView.FIELD_SECTION_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.EXPENDITURE_NATURE_IDENTIFIER,"ev"
				,ExpenditureView.FIELD_NATURE_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ADMINISTRATIVE_UNIT_IDENTIFIER,"ev"
				,ExpenditureView.FIELD_ADMINISTRATIVE_UNIT_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,"ev"
				,ExpenditureView.FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ACTION_IDENTIFIER,"ev"
				,ExpenditureView.FIELD_ACTION_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ACTIVITY_IDENTIFIER,"ev"
				,ExpenditureView.FIELD_ACTIVITY_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ECONOMIC_NATURE_IDENTIFIER,"ev"
				,ExpenditureView.FIELD_ECONOMIC_NATURE_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.FUNDING_SOURCE_IDENTIFIER,"ev"
				,ExpenditureView.FIELD_FUNDING_SOURCE_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LESSOR_IDENTIFIER,"ev"
				,ExpenditureView.FIELD_LESSOR_IDENTIFIER);
		
		if(arguments.getFilterFieldValue(Parameters.ACTIVITIES_IDENTIFIERS) != null) {
			predicate.add("t.activityIdentifier IN :activitiesIdentifiers");
			filter.addField("activitiesIdentifiers", arguments.getFilterFieldValue(Parameters.ACTIVITIES_IDENTIFIERS));
		}
		
		if(arguments.getFilterFieldValue(Parameters.ECONOMIC_NATURES_IDENTIFIERS) != null) {
			predicate.add("t.economicNatureIdentifier IN :economicNaturesIdentifiers");
			filter.addField("economicNaturesIdentifiers", arguments.getFilterFieldValue(Parameters.ECONOMIC_NATURES_IDENTIFIERS));
		}
		
		if(arguments.getFilterFieldValue(Parameters.FUNDING_SOURCES_IDENTIFIERS) != null) {
			predicate.add("t.fundingSourceIdentifier IN :fundingSourcesIdentifiers");
			filter.addField("fundingSourcesIdentifiers", arguments.getFilterFieldValue(Parameters.FUNDING_SOURCES_IDENTIFIERS));
		}
		
		if(arguments.getFilterFieldValue(Parameters.LESSORS_IDENTIFIERS) != null) {
			predicate.add("t.lessorIdentifier IN :lessorsIdentifiers");
			filter.addField("lessorsIdentifiers", arguments.getFilterFieldValue(Parameters.LESSORS_IDENTIFIERS));
		}
		
		Boolean adjustmentsNotEqualZero = arguments.getFilterFieldValueAsBoolean(null,Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO);
		if(adjustmentsNotEqualZero != null)
			predicate.add(buildPredicateExpenditureAdjustmentsEqualZeroPredicate(!adjustmentsNotEqualZero));
		
		Boolean includedMovementNotEqualZero = arguments.getFilterFieldValueAsBoolean(null,Parameters.INCLUDED_MOVEMENT_NOT_EQUAL_ZERO);
		if(includedMovementNotEqualZero != null)
			predicate.add(ExpenditureQueryStringBuilder.Predicate.getMovementIncludedEqualZero(!includedMovementNotEqualZero) /*buildPredicateExpenditureMovementIncludedEqualZeroPredicate(!includedMovementNotEqualZero)*/);
			//builderArguments.getHaving(Boolean.TRUE).add(Language.Where.notIfTrue(ExpenditureQueryStringBuilder.Predicate.getMovementIncludedEqualZero(), includedMovementNotEqualZero)  /*buildPredicateExpenditureMovementIncludedEqualZeroPredicate(!includedMovementNotEqualZero)*/);
		
		Boolean adjustmentsNotEqualZeroOrIncludedMovementNotEqualZero = arguments.getFilterFieldValueAsBoolean(null,Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO);
		if(adjustmentsNotEqualZeroOrIncludedMovementNotEqualZero != null) {
			predicate.add(ExpenditureQueryStringBuilder.Predicate.getAdjustmentNotEqualZeroOrMovementIncludedNotEqualZero(adjustmentsNotEqualZeroOrIncludedMovementNotEqualZero));
			//predicate.add(Language.parenthesis(Language.Where.or(buildPredicateExpenditureAdjustmentsEqualZeroPredicate(!adjustmentsNotEqualZeroOrIncludedMovementNotEqualZero)
			//		,buildPredicateExpenditureMovementIncludedEqualZeroPredicate(!adjustmentsNotEqualZeroOrIncludedMovementNotEqualZero))));
		}
				
		ExpenditureQueryStringBuilder.Predicate.populate(arguments, builderArguments, predicate, filter);

		/*
		addPredicateExpenditureAdjustmentsEqualZero(predicate,  adjustmentsNotEqualZeroOrIncludedMovementNotEqualZero == null ? null : !adjustmentsNotEqualZeroOrIncludedMovementNotEqualZero);
		
		addPredicateExpenditureAdjustmentsEqualZero(predicate,  arguments.getFilterFieldValueAsBoolean(null,Parameters.ADJUSTMENTS_EQUAL_ZERO));
		
		addPredicateExpenditureMovementIncludedEqualZero(predicate, adjustmentsNotEqualZeroOrIncludedMovementNotEqualZero == null ? null : !adjustmentsNotEqualZeroOrIncludedMovementNotEqualZero);
		*/
		/*Boolean adjustmentEqualZero = arguments.getFilterFieldValueAsBoolean(null,Parameters.ADJUSTMENTS_EQUAL_ZERO);
		if(adjustmentEqualZero != null) {
			predicate.add(String.format("(t.%1$s.%3$s %4$s 0 %5$s t.%2$s.%3$s %4$s 0)",ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION,ExpenditureImpl.FIELD_PAYMENT_CREDIT,AbstractExpenditureAmountsImpl.FIELD_ADJUSTMENT
					,adjustmentEqualZero ? "=" : "<>",adjustmentEqualZero ? "AND" : "OR"));
		}*/
		
		Boolean generatedActExpenditureExists = arguments.getFilterFieldValueAsBoolean(null,Parameters.GENERATED_ACT_EXPENDITURE_EXISTS);
		if(generatedActExpenditureExists != null) {
			predicate.add(String.format("%1$sEXISTS(SELECT p.identifier FROM %2$s p WHERE p.%3$s = t.%3$s AND p.%4$s = t.%4$s AND p.%5$s = t.%5$s AND p.%6$s = t.%6$s)"
					,generatedActExpenditureExists ? "" : "NOT ",GeneratedActExpenditureImpl.ENTITY_NAME,GeneratedActExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,GeneratedActExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER
							,GeneratedActExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER,GeneratedActExpenditureImpl.FIELD_LESSOR_IDENTIFIER));
		}
		
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.__AUDIT_IDENTIFIER__,"t",ExpenditureImpl.FIELD___AUDIT_IDENTIFIER__);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.__AUDIT_WHO__,"t",ExpenditureImpl.FIELD___AUDIT_WHO__);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.__AUDIT_WHEN__,"t",ExpenditureImpl.FIELD___AUDIT_WHEN__);
	}
	
	private static String buildPredicateExpenditureAdjustmentsEqualZeroPredicate(Boolean isEqualToZero) {
		return String.format("(t.%1$s.%3$s %4$s 0 %5$s t.%2$s.%3$s %4$s 0)",ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION,ExpenditureImpl.FIELD_PAYMENT_CREDIT,AbstractExpenditureAmountsImpl.FIELD_ADJUSTMENT
				,isEqualToZero ? "=" : "<>",isEqualToZero ? "AND" : "OR");
	}
	/*
	private static final String PREDICATE_EXPENDITURE_INCLUDED_MOVEMENT_EQUAL_ZERO_FORMAT = 
			"((SELECT SUM(rae.%2$s) FROM %1$s ralav"
			+ " JOIN %3$s rae ON rae.%4$s = ralav.%5$s.identifier AND rae.%7$s = t.%7$s AND rae.%8$s = t.%8$s AND rae.%9$s = t.%9$s AND rae.%10$s = t.%10$s"
			+ " JOIN RegulatoryActImpl ra ON ra.identifier = rae.actIdentifier AND ra.year = exercise.year WHERE ralav.%11$s IS TRUE) %6$s 0)";
	*/

	public static void populatePredicateResource(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_IDENTIFIER,"t"
				,FieldHelper.join(ExpenditureImpl.FIELD_ACT_VERSION,LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_IDENTIFIER));
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"t"
				,FieldHelper.join(ExpenditureImpl.FIELD_ACT_VERSION,LegislativeActImpl.FIELD_IDENTIFIER));
		
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.SECTION_IDENTIFIER,"v"
				,ResourceView.FIELD_SECTION_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,"v"
				,ResourceView.FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ACTIVITY_IDENTIFIER,"v"
				,ResourceView.FIELD_ACTIVITY_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ECONOMIC_NATURE_IDENTIFIER,"v"
				,ResourceView.FIELD_ECONOMIC_NATURE_IDENTIFIER);
		
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.__AUDIT_IDENTIFIER__,"t",ExpenditureImpl.FIELD___AUDIT_IDENTIFIER__);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.__AUDIT_WHO__,"t",ExpenditureImpl.FIELD___AUDIT_WHO__);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.__AUDIT_WHEN__,"t",ExpenditureImpl.FIELD___AUDIT_WHEN__);
	}
	
	public static void populatePredicateLegislativeAct(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.EXERCISE_IDENTIFIER,"t",LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER);
		if(arguments.getFilterField(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER) != null) {
			predicate.add(String.format("EXISTS(SELECT bav.identifier FROM %s bav WHERE bav.act.identifier = :%s)",arguments.getFilterFieldValue(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER)));
		}
		if(arguments.getFilterField(Parameters.EXERCISE_YEAR) != null) {
			predicate.add(String.format("exercise.year = :%s",Parameters.EXERCISE_YEAR));
			filter.addField(Parameters.EXERCISE_YEAR, NumberHelper.get(Short.class, arguments.getFilterFieldValue(Parameters.EXERCISE_YEAR)));
		}
		Boolean inProgress = arguments.getFilterFieldValueAsBoolean(null,Parameters.LEGISLATIVE_ACT_IN_PROGRESS);
		if(inProgress != null) {
			predicate.add(String.format("(t.%s = :%s)",LegislativeActImpl.FIELD_IN_PROGRESS,Parameters.LEGISLATIVE_ACT_IN_PROGRESS));
			filter.addField(Parameters.LEGISLATIVE_ACT_IN_PROGRESS, inProgress);
		}
	}
	
	public static void populatePredicateLegislativeActVersion(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_IDENTIFIER,"t"
				,FieldHelper.join(LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_IDENTIFIER));
		/*Boolean defaultVersionInLatestLegislativeAct = arguments.getFilterFieldValueAsBoolean(null,Parameters.DEFAULT_LEGISLATIVE_ACT_VERSION_IN_LATEST_LEGISLATIVE_ACT);
		if(defaultVersionInLatestLegislativeAct != null) {
			predicate.add(String.format("EXISTS(SELECT p FROM %s p WHERE p.%s = t)",LegislativeActImpl.ENTITY_NAME,LegislativeActImpl.FIELD_DEFAULT_VERSION));
		}*/
	}
	
	public static void populatePredicateBudgetSpecializationUnit(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.SECTION_IDENTIFIER,"t"
				,BudgetSpecializationUnitImpl.FIELD_SECTION_IDENTIFIER);
	}
	
	public static void populatePredicateAdministrativeUnit(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.SECTION_IDENTIFIER,"t"
				,AdministrativeUnitImpl.FIELD_SECTION_IDENTIFIER);
	}
	
	public static void populatePredicateAction(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,"t"
				,ActionImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER);
	}
	
	private final String ACTIVITY_PREDICATE_SEARCH = parenthesis(or(
			LikeStringBuilder.getInstance().build("t",ActivityImpl.FIELD_CODE, Parameters.SEARCH)
			,LikeStringBuilder.getInstance().build("t", ActivityImpl.FIELD_NAME,Parameters.SEARCH)
	));
	public void populatePredicateActivity(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ACTION_IDENTIFIER,"t"
				,ActivityImpl.FIELD_ACTION_IDENTIFIER);
		
		if(arguments.getFilterField(Parameters.SEARCH) != null) {
			predicate.add(ACTIVITY_PREDICATE_SEARCH);
			String search = ValueHelper.defaultToIfBlank((String) arguments.getFilterFieldValue(Parameters.SEARCH),"");
			filter.addField(Parameters.SEARCH, LikeStringValueBuilder.getInstance().build(search, null, null));
		}
	}
	
	private final String RESOURCE_ACTIVITY_PREDICATE_SEARCH = parenthesis(or(
			LikeStringBuilder.getInstance().build("t",ResourceActivityImpl.FIELD_CODE, Parameters.SEARCH)
			,LikeStringBuilder.getInstance().build("t", ResourceActivityImpl.FIELD_NAME,Parameters.SEARCH)
	));
	public void populatePredicateResourceActivity(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,"t"
				,ResourceActivityImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER);
		
		if(arguments.getFilterField(Parameters.SEARCH) != null) {
			predicate.add(RESOURCE_ACTIVITY_PREDICATE_SEARCH);
			String search = ValueHelper.defaultToIfBlank((String) arguments.getFilterFieldValue(Parameters.SEARCH),"");
			filter.addField(Parameters.SEARCH, LikeStringValueBuilder.getInstance().build(search, null, null));
		}
	}
	
	public static void populatePredicateEconomicNature(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		if(arguments.getFilterField(Parameters.ACTIVITY_IDENTIFIER) != null) {
			predicate.add(String.format("EXISTS(SELECT e.%1$s FROM %2$s e WHERE e.%3$s = t.%4$s AND e.%5$s = :%5$s)",ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER
					,EconomicNatureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER));
			filter.addField(ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER, arguments.getFilterFieldValue(Parameters.ACTIVITY_IDENTIFIER));
		}
		if(arguments.getFilterField(Parameters.RESOURCE_ACTIVITY_IDENTIFIER) != null) {
			predicate.add(String.format("EXISTS(SELECT e.%1$s FROM %2$s e WHERE e.%3$s = t.%4$s AND e.%5$s = :%5$s)",ResourceImpl.FIELD_IDENTIFIER,ResourceImpl.ENTITY_NAME,ResourceImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER
					,EconomicNatureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER));
			filter.addField(ResourceImpl.FIELD_ACTIVITY_IDENTIFIER, arguments.getFilterFieldValue(Parameters.RESOURCE_ACTIVITY_IDENTIFIER));
		}
	}
	
	public static void populatePredicateFundingSource(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		if(arguments.getFilterField(Parameters.ACTIVITY_IDENTIFIER) != null) {
			predicate.add(String.format("EXISTS(SELECT e.%1$s FROM %2$s e WHERE e.%3$s = t.%4$s AND e.%5$s = :%5$s)",ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.ENTITY_NAME,ExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER
					,FundingSourceImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER));
			filter.addField(ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER, arguments.getFilterFieldValue(Parameters.ACTIVITY_IDENTIFIER));
		}
	}
	
	public static void populatePredicateLessor(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		if(arguments.getFilterField(Parameters.ACTIVITY_IDENTIFIER) != null) {
			predicate.add(String.format("EXISTS(SELECT e.%1$s FROM %2$s e WHERE e.%3$s = t.%4$s AND e.%5$s = :%5$s)",ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.ENTITY_NAME,ExpenditureImpl.FIELD_LESSOR_IDENTIFIER
					,LessorImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER));
			filter.addField(ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER, arguments.getFilterFieldValue(Parameters.ACTIVITY_IDENTIFIER));
		}
	}
	
	public static void populatePredicateRegulatoryAct(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		String legislativeActVersionIdentifier = (String) arguments.getFilterFieldValue(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER);
		if(StringHelper.isNotBlank(legislativeActVersionIdentifier)) {
			predicate.add(String.format("EXISTS(SELECT lav.identifier FROM LegislativeActVersionImpl lav JOIN LegislativeActImpl la ON la = lav.act JOIN ExerciseImpl e ON e.identifier = la.exerciseIdentifier AND lav.identifier = :%s WHERE t.year = e.year)",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER));
			filter.addField(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, legislativeActVersionIdentifier);
		}
		
		Boolean included = arguments.getFilterFieldValueAsBoolean(null,Parameters.REGULATORY_ACT_INCLUDED);
		if(included != null) {
			if(StringHelper.isNotBlank(legislativeActVersionIdentifier)) {
				predicate.add(String.format("%s EXISTS(SELECT ralav.identifier FROM RegulatoryActLegislativeActVersionImpl ralav WHERE ralav.regulatoryAct = t AND ralav.legislativeActVersion.identifier = :%s AND ralav.included IS TRUE)"
						,included ? "" : "NOT",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER));
				filter.addField(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, legislativeActVersionIdentifier);
			}
		}
		
		LocalDate greatestDate = arguments.getFilterFieldValueAsLocalDate(null,Parameters.REGULATORY_ACT_DATE_LOWER_THAN_OR_EQUAL);
		if(greatestDate != null) {
			predicate.add(String.format("t.%s <= :%s",RegulatoryActImpl.FIELD_DATE,Parameters.REGULATORY_ACT_DATE_LOWER_THAN_OR_EQUAL));
			filter.addField(Parameters.REGULATORY_ACT_DATE_LOWER_THAN_OR_EQUAL, greatestDate);
		}
		
		LocalDate lowestDate = arguments.getFilterFieldValueAsLocalDate(null,Parameters.REGULATORY_ACT_DATE_GREATER_THAN_OR_EQUAL);
		if(lowestDate != null) {
			predicate.add(String.format("t.%s >= :%s",RegulatoryActImpl.FIELD_DATE,Parameters.REGULATORY_ACT_DATE_GREATER_THAN_OR_EQUAL));
			filter.addField(Parameters.REGULATORY_ACT_DATE_GREATER_THAN_OR_EQUAL, lowestDate);
		}
	}
	
	public static void populatePredicateRegulatoryActExpenditure(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		@SuppressWarnings("unchecked")
		Collection<String> regulatoryActIdentifiers = (Collection<String>) arguments.getFilterFieldValue(Parameters.REGULATORY_ACT_IDENTIFIERS);
		if(CollectionHelper.isNotEmpty(regulatoryActIdentifiers)) {
			predicate.add(String.format("t.%s IN :%s",RegulatoryActExpenditureImpl.FIELD_ACT_IDENTIFIER,Parameters.REGULATORY_ACT_IDENTIFIERS));
			filter.addField(Parameters.REGULATORY_ACT_IDENTIFIERS, regulatoryActIdentifiers);
		}
	}
	
	public static void populatePredicateGeneratedAct(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"t"
				,FieldHelper.join(GeneratedActImpl.FIELD_LEGISLATIVE_ACT_VERSION,LegislativeActImpl.FIELD_IDENTIFIER));
	}
	
	public static void populatePredicateGeneratedActExpenditure(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.GENERATED_ACT_IDENTIFIER,"t"
				,FieldHelper.join(GeneratedActExpenditureImpl.FIELD_ACT,GeneratedActImpl.FIELD_IDENTIFIER));
		
		@SuppressWarnings("unchecked")
		Collection<String> generatedActIdentifiers = (Collection<String>) arguments.getFilterFieldValue(Parameters.GENERATED_ACT_IDENTIFIERS);
		if(CollectionHelper.isNotEmpty(generatedActIdentifiers)) {
			predicate.add(String.format("t.%s.identifier IN :%s",GeneratedActExpenditureImpl.FIELD_ACT,Parameters.GENERATED_ACT_IDENTIFIERS));
			filter.addField(Parameters.GENERATED_ACT_IDENTIFIERS, generatedActIdentifiers);
		}
	}
	
	/**/
	
	public static Boolean isExpenditureJoinedToView(QueryExecutorArguments arguments, Arguments builderArguments) {
		if(CollectionHelper.isNotEmpty(arguments.getProjections()) 
				&& CollectionUtils.containsAny(arguments.getProjections().stream().map(projection -> projection.getFieldName()).collect(Collectors.toList()),ExpenditureImpl.VIEW_FIELDS_NAMES))
			return Boolean.TRUE;
		
		if(CollectionHelper.isNotEmpty(arguments.getProcessableTransientFieldsNames()) 
				&& CollectionUtils.containsAny(arguments.getProcessableTransientFieldsNames(),ExpenditureImpl.VIEW_FIELDS_NAMES))
			return Boolean.TRUE;
				
		if(StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.SECTION_IDENTIFIER)) 
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.EXPENDITURE_NATURE_IDENTIFIER))
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER))
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.ACTION_IDENTIFIER))
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.ACTIVITY_IDENTIFIER))
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.ACTIVITY_CATEGORY_IDENTIFIER)))
			return Boolean.TRUE;
		
		return Boolean.FALSE;
	}
	
	public static Boolean isResourceJoinedToView(QueryExecutorArguments arguments, Arguments builderArguments) {
		if(CollectionHelper.isNotEmpty(arguments.getProjections()) 
				&& CollectionUtils.containsAny(arguments.getProjections().stream().map(projection -> projection.getFieldName()).collect(Collectors.toList()),ResourceImpl.VIEW_FIELDS_NAMES))
			return Boolean.TRUE;
		
		if(CollectionHelper.isNotEmpty(arguments.getProcessableTransientFieldsNames()) 
				&& CollectionUtils.containsAny(arguments.getProcessableTransientFieldsNames(),ResourceImpl.VIEW_FIELDS_NAMES))
			return Boolean.TRUE;
				
		if(StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.SECTION_IDENTIFIER)) 
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER))
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.ACTIVITY_IDENTIFIER)))
			return Boolean.TRUE;
		
		return Boolean.FALSE;
	}
}