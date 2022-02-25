package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.cyk.utility.persistence.query.Language.parenthesis;
import static org.cyk.utility.persistence.query.Language.Where.and;
import static org.cyk.utility.persistence.query.Language.Where.or;

import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.persistence.query.Language;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.query.string.CaseStringBuilder;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder.Arguments;
import org.cyk.utility.persistence.server.query.string.WhereStringBuilder;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;

public interface ExpenditureQueryStringBuilder {
	
	String[] ENTRY_AUTHORIZATION_PAYMENT_CREDIT = {ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION,ExpenditureImpl.FIELD_PAYMENT_CREDIT};
	String[] REGULATORY_ACT_LEGISLATIVE_ACT_VERSION_AND_AVAILABLE = {ExpenditureImpl.FIELDS_AMOUNTS};
	
	public static interface Projection {
		static void projectAmounts(Arguments arguments,Boolean view,Boolean includedMovement,Boolean available) {
			arguments.getProjection(Boolean.TRUE).addFromTuple("t",ExpenditureImpl.FIELD_IDENTIFIER);
			for(String fieldName : ENTRY_AUTHORIZATION_PAYMENT_CREDIT) {
				arguments.getProjection().add(projectAmount("t", FieldHelper.join(fieldName,AbstractAmountsImpl.FIELD_ADJUSTMENT)));
				if(Boolean.TRUE.equals(view)) {
					arguments.getProjection().add(projectAmount("ev", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_INITIAL)));
					arguments.getProjection().add(projectAmount("ev", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_MOVEMENT)));
					arguments.getProjection().add(projectAmount("ev", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_ACTUAL)));
				}
				if(Boolean.TRUE.equals(includedMovement))
					arguments.getProjection().add(projectAmount("im", fieldName));
				if(Boolean.TRUE.equals(available))
					arguments.getProjection().add(projectAmount("available", fieldName));
			}
		}
		
		static String projectAmount(String tupleName,String fieldName) {
			return CaseStringBuilder.Case.instantiateWhenFieldIsNullThenZeroElseFieldAndBuild(FieldHelper.join(tupleName,fieldName),"0l");
		}
		
		static Integer setAmounts(AbstractAmountsImpl amounts,Object[] array,Integer index,Boolean view,Boolean includedMovement,Boolean available) {
			if(amounts == null || index == null || index < 0)
				return index;
			if(index < array.length)
				amounts.setAdjustment(NumberHelper.getLong(array[index++],0l));
			if(Boolean.TRUE.equals(view)) {
				if(index < array.length)
					amounts.setInitial(NumberHelper.getLong(array[index++],0l));
				if(index < array.length)
					amounts.setMovement(NumberHelper.getLong(array[index++],0l));
				if(index < array.length)
					amounts.setActual(NumberHelper.getLong(array[index++],0l));
			}
			if(Boolean.TRUE.equals(includedMovement) && index < array.length)
				amounts.setMovementIncluded(NumberHelper.getLong(array[index++],0l));
			if(Boolean.TRUE.equals(available) && index < array.length)
				amounts.setAvailable(NumberHelper.getLong(array[index++],0l));
			
			amounts.computeActualPlusAdjustment();
			if(Boolean.TRUE.equals(includedMovement))
				amounts.computeActualMinusMovementIncludedPlusAdjustment();
			if(Boolean.TRUE.equals(includedMovement) && Boolean.TRUE.equals(available))
				amounts.computeAvailableMinusMovementIncludedPlusAdjustment();
			return index;
		}
		
		static void setAmounts(ExpenditureImpl expenditure,Object[] array,Boolean view,Boolean includedMovement,Boolean available) {
			if(expenditure == null)
				return;
			Integer index = setAmounts(expenditure.getEntryAuthorization(Boolean.TRUE),array,1,view,includedMovement,available);
			setAmounts(expenditure.getPaymentCredit(Boolean.TRUE),array,index,view,includedMovement,available);
		}
		
		/**/
		
		static void projectAmounts(Arguments arguments) {
			arguments.getProjection(Boolean.TRUE).addFromTuple("t",ExpenditureImpl.FIELD_IDENTIFIER);
			for(String fieldName : ENTRY_AUTHORIZATION_PAYMENT_CREDIT) {
				arguments.getProjection(Boolean.TRUE).add(projectExpenditureAmount("ev", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_INITIAL)));
				arguments.getProjection(Boolean.TRUE).add(projectExpenditureAmount("ev", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_MOVEMENT)));
				arguments.getProjection(Boolean.TRUE).add(projectExpenditureAmount("ev", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_ACTUAL)));
				
				arguments.getProjection(Boolean.TRUE).add(projectExpenditureAmount("t", FieldHelper.join(fieldName,AbstractAmountsImpl.FIELD_ADJUSTMENT)));
				
				arguments.getProjection(Boolean.TRUE).add(projectExpenditureAmountMovementIncluded(fieldName));
				
				arguments.getProjection(Boolean.TRUE).add(projectExpenditureAmount("available", fieldName));
			}
		}
		
		static String projectExpenditureAmount(String tupleName,String fieldName) {
			return String.format("MAX(%s)",CaseStringBuilder.Case.instantiateWhenFieldIsNullThenZeroElseFieldAndBuild(FieldHelper.join(tupleName,fieldName),"0l"));
		}
		
		static String projectExpenditureAmountMovementIncluded(String fieldName) {
			return String.format("SUM(CASE WHEN ralav.included IS TRUE THEN rae.%sAmount ELSE 0l END)",fieldName);
		}
		
		static Integer setAmounts(AbstractAmountsImpl amounts,Object[] array,Integer index) {
			if(amounts == null || index == null || index < 0)
				return index;
			if(index < array.length)
				amounts.setInitial(NumberHelper.getLong(array[index++],0l));
			if(index < array.length)
				amounts.setMovement(NumberHelper.getLong(array[index++],0l));
			if(index < array.length)
				amounts.setActual(NumberHelper.getLong(array[index++],0l));
			if(index < array.length)
				amounts.setAdjustment(NumberHelper.getLong(array[index++],0l));
			if(index < array.length)
				amounts.setMovementIncluded(NumberHelper.getLong(array[index++],0l));
			if(index < array.length)
				amounts.setAvailable(NumberHelper.getLong(array[index++],0l));
			
			amounts.computeActualPlusAdjustment();
			amounts.computeActualMinusMovementIncludedPlusAdjustment();
			amounts.computeAvailableMinusMovementIncludedPlusAdjustment();
			
			return index;
		}
		
		static void setAmounts(ExpenditureImpl expenditure,Object[] array) {
			if(expenditure == null)
				return;
			Integer index = setAmounts(expenditure.getEntryAuthorization(Boolean.TRUE),array,1);
			setAmounts(expenditure.getPaymentCredit(Boolean.TRUE),array,index);
		}
	}
	
	public static interface Tuple {
		
		public static String getView() {
			return String.format("JOIN %3$s ev ON ev.%4$s = lav.identifier AND ev.%5$s = t.%5$s AND ev.%6$s = t.%6$s AND ev.%7$s = t.%7$s AND ev.%8$s = t.%8$s", "t","exercise"
					,ExpenditureView.ENTITY_NAME,ExpenditureView.FIELD_LEGISLATIVE_ACT_VERSION_IDENTIFIER,ExpenditureView.FIELD_ACTIVITY_IDENTIFIER,ExpenditureView.FIELD_ECONOMIC_NATURE_IDENTIFIER
					,ExpenditureView.FIELD_FUNDING_SOURCE_IDENTIFIER,ExpenditureView.FIELD_LESSOR_IDENTIFIER);
		}
		
		public static String getIncludedMovement() {
			return String.format("JOIN %1$s im ON im.%2$s = t.%2$s",ExpenditureIncludedMovementView.ENTITY_NAME,ExpenditureIncludedMovementView.FIELD_IDENTIFIER);
		}
		
		public static String getAvailable() {
			return String.format("JOIN %3$s available ON available.%4$s = exercise.%5$s AND available.%6$s = t.%6$s AND available.%7$s = t.%7$s AND available.%8$s = t.%8$s AND available.%9$s = t.%9$s", "t", "exercise"
					,ExpenditureAvailableView.ENTITY_NAME,ExpenditureAvailableView.FIELD_YEAR,ExerciseImpl.FIELD_YEAR,ExpenditureAvailableView.FIELD_ACTIVITY_IDENTIFIER,ExpenditureAvailableView.FIELD_ECONOMIC_NATURE_IDENTIFIER
					,ExpenditureAvailableView.FIELD_FUNDING_SOURCE_IDENTIFIER,ExpenditureAvailableView.FIELD_LESSOR_IDENTIFIER);
		}
		
		/**/
		
		static void joinAmounts(Arguments arguments,Boolean view,Boolean includedMovement,Boolean available) {
			if(Boolean.TRUE.equals(view)) {
				arguments.getTuple().addJoins(String.format("JOIN %s lav ON lav = t.%s",LegislativeActVersionImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION));
				arguments.getTuple().addJoins("LEFT "+getView());
				arguments.getTuple().addJoins(String.format("JOIN %s la ON la = lav.%s",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT));
				arguments.getTuple().addJoins(String.format("LEFT JOIN %s exercise ON exercise.%s = la.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
			}
			if(Boolean.TRUE.equals(includedMovement))
				arguments.getTuple().addJoins("LEFT "+getIncludedMovement());
			if(Boolean.TRUE.equals(available))
				arguments.getTuple().addJoins("LEFT "+getAvailable());
		}
		
		static void joinLegislativeActVersionAndExercise(Arguments arguments) {
			arguments.getTuple().addJoins(String.format("JOIN %s lav ON lav = t.%s",LegislativeActVersionImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION));
			arguments.getTuple().addJoins("LEFT "+getView());
			arguments.getTuple().addJoins(String.format("JOIN %s la ON la = lav.%s",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT));
			arguments.getTuple().addJoins(String.format("LEFT JOIN %s exercise ON exercise.%s = la.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
		}
		
		static void joinRegulatoryActLegislativeActVersion(Arguments arguments) {
			joinLegislativeActVersionAndExercise(arguments);
			arguments.getTuple().addJoins("LEFT "+getJoinRegulatoryActExpenditure());
			arguments.getTuple().addJoins(String.format("LEFT JOIN %s ra ON ra.%s = rae.%s",RegulatoryActImpl.ENTITY_NAME,RegulatoryActImpl.FIELD_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ACT_IDENTIFIER));
			arguments.getTuple().addJoins(String.format("LEFT JOIN %s ralav ON ralav.%s = ra AND ralav.%s = lav",RegulatoryActLegislativeActVersionImpl.ENTITY_NAME,RegulatoryActLegislativeActVersionImpl.FIELD_REGULATORY_ACT
					,RegulatoryActLegislativeActVersionImpl.FIELD_LEGISLATIVE_ACT_VERSION));
		}
		
		static void joinRegulatoryActLegislativeActVersionAndAvailable(Arguments arguments) {
			joinRegulatoryActLegislativeActVersion(arguments);
			arguments.getTuple().addJoins("LEFT "+getAvailable());
		}
		
		static void joinAmounts(Arguments arguments,Boolean isRead) {
			joinRegulatoryActLegislativeActVersionAndAvailable(arguments);
			if(Boolean.TRUE.equals(isRead))
				arguments.getGroup(Boolean.TRUE).add("t.identifier");
		}
		
		static void joinAmounts(Arguments arguments) {
			joinAmounts(arguments, Boolean.TRUE);
		}
		
		public static String getJoinRegulatoryActExpenditure() {
			return String.format("JOIN %3$s rae ON rae.%4$s = exercise.%5$s AND rae.%6$s = t.%6$s AND rae.%7$s = t.%7$s AND rae.%8$s = t.%8$s AND rae.%9$s = t.%9$s", "t", "exercise"
					,RegulatoryActExpenditureImpl.ENTITY_NAME,RegulatoryActExpenditureImpl.FIELD_YEAR,ExerciseImpl.FIELD_YEAR,RegulatoryActExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER
					,RegulatoryActExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_LESSOR_IDENTIFIER);
		}
		
		public static void join(QueryExecutorArguments arguments, Arguments builderArguments) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",ExpenditureImpl.ENTITY_NAME));
			if(arguments.getFilterField(Parameters.INCLUDED_MOVEMENT_NOT_EQUAL_ZERO) != null || arguments.getFilterField(Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO) != null) {
				//builderArguments.getTuple().addJoins(String.format("JOIN %s lav ON lav = t.%s",LegislativeActVersionImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION));
				//builderArguments.getTuple().addJoins(String.format("JOIN %s la ON la = lav.%s",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT));
				//builderArguments.getTuple().addJoins(String.format("JOIN %s exercise ON exercise.%s = la.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));	
			}
			
			if(arguments.getFilterField(Parameters.AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_LESS_THAN_ZERO) != null) {
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
			if(Boolean.TRUE.equals(isJoinedToView(arguments, builderArguments))) {
				//builderArguments.getTuple().addJoins(String.format("JOIN %s v ON v.identifier = t.identifier",ExpenditureView.ENTITY_NAME));
			}
		}
		
		public static Boolean isJoinedToRegulatoryActLegislativeActVersionAndAvailable(QueryExecutorArguments arguments, Arguments builderArguments) {
			if(CollectionHelper.isNotEmpty(arguments.getProjections()) 
					&& CollectionUtils.containsAny(arguments.getProjections().stream().map(projection -> projection.getFieldName()).collect(Collectors.toList()),REGULATORY_ACT_LEGISLATIVE_ACT_VERSION_AND_AVAILABLE))
				return Boolean.TRUE;
			
			if(CollectionHelper.isNotEmpty(arguments.getProcessableTransientFieldsNames()) 
					&& CollectionUtils.containsAny(arguments.getProcessableTransientFieldsNames(),REGULATORY_ACT_LEGISLATIVE_ACT_VERSION_AND_AVAILABLE))
				return Boolean.TRUE;
					
			if(arguments.getFilterFieldValue(Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO) != null
					|| arguments.getFilterFieldValue(Parameters.AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_LESS_THAN_ZERO) != null
					)
				return Boolean.TRUE;
			
			return Boolean.FALSE;
		}
		
		public static Boolean isJoinedToView(QueryExecutorArguments arguments, Arguments builderArguments) {
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
	}
	
	public static interface Predicate {
		public static void populate(QueryExecutorArguments queryExecutorArguments, Arguments arguments, WhereStringBuilder.Predicate predicate,Filter filter) {
			Boolean availableMinusIncludedMovementPlusAdjustmentLessThanZero = queryExecutorArguments.getFilterFieldValueAsBoolean(null,Parameters.AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_LESS_THAN_ZERO);
			if(availableMinusIncludedMovementPlusAdjustmentLessThanZero != null)
				predicate.add(Language.Where.notIfTrue(getAvailableMinusIncludedMovementPlusAdjustmentLessThanZero(null),availableMinusIncludedMovementPlusAdjustmentLessThanZero));
		}
		
		String MOVEMENT_INCLUDED_EQUAL_ZERO_FORMAT = "(im.%1$s IS NULL OR im.%1$s = 0l)";
		String MOVEMENT_INCLUDED_NOT_EQUAL_ZERO_FORMAT = "im.%1$s IS NOT NULL AND im.%1$s <> 0l";
		static String getMovementIncludedEqualZero(Boolean equal,String amountFieldName) {
			return String.format(Boolean.TRUE.equals(equal) ? MOVEMENT_INCLUDED_EQUAL_ZERO_FORMAT : MOVEMENT_INCLUDED_NOT_EQUAL_ZERO_FORMAT ,amountFieldName);
		}
		
		static String getMovementIncludedEqualZero(Boolean equal) {
			if(Boolean.TRUE.equals(equal))
				return parenthesis(or(getMovementIncludedEqualZero(equal,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION),getMovementIncludedEqualZero(equal,ExpenditureImpl.FIELD_PAYMENT_CREDIT)));
			return and(getMovementIncludedEqualZero(equal,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION),getMovementIncludedEqualZero(equal,ExpenditureImpl.FIELD_PAYMENT_CREDIT));
		}
		
		String ADJUSTMENT_NOT_EQUAL_ZERO_OR_MOVEMENT_INCLUDED_NOT_EQUAL_ZERO_FORMAT = "((t.%1$s.adjustment IS NOT NULL AND t.%1$s.adjustment <> 0l) OR (im.%1$s IS NOT NULL AND im.%1$s <> 0l))";
		String ADJUSTMENT_EQUAL_ZERO_AND_MOVEMENT_INCLUDED_EQUAL_ZERO_FORMAT = "t.%1$s.adjustment IS NULL OR t.%1$s.adjustment <> 0l AND im.%1$s IS NOT NULL AND im.%1$s <> 0l";
		static String getAdjustmentNotEqualZeroOrMovementIncludedNotEqualZero(Boolean notEqual,String amountFieldName) {
			return String.format(Boolean.TRUE.equals(notEqual) ? ADJUSTMENT_NOT_EQUAL_ZERO_OR_MOVEMENT_INCLUDED_NOT_EQUAL_ZERO_FORMAT : ADJUSTMENT_EQUAL_ZERO_AND_MOVEMENT_INCLUDED_EQUAL_ZERO_FORMAT ,amountFieldName);
		}
		
		static String getAdjustmentNotEqualZeroOrMovementIncludedNotEqualZero(Boolean notEqual) {
			if(Boolean.TRUE.equals(notEqual))
				return parenthesis(or(getAdjustmentNotEqualZeroOrMovementIncludedNotEqualZero(notEqual,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION),getAdjustmentNotEqualZeroOrMovementIncludedNotEqualZero(notEqual,ExpenditureImpl.FIELD_PAYMENT_CREDIT)));
			return and(getAdjustmentNotEqualZeroOrMovementIncludedNotEqualZero(notEqual,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION),getAdjustmentNotEqualZeroOrMovementIncludedNotEqualZero(notEqual,ExpenditureImpl.FIELD_PAYMENT_CREDIT));
		}
		
		String AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_LESS_THAN_ZERO_FORMAT = "("+CaseStringBuilder.Case.instantiateWhenFieldIsNullThenZeroElseFieldAndBuild("available.%1$s", "0l")
			+" - "+CaseStringBuilder.Case.instantiateWhenFieldIsNullThenZeroElseFieldAndBuild("im.%1$s", "0l")
			+" + "+CaseStringBuilder.Case.instantiateWhenFieldIsNullThenZeroElseFieldAndBuild("t.%1$s.adjustment", "0l")+" < 0l)";
		String AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_NOT_LESS_THAN_ZERO_FORMAT = "("+CaseStringBuilder.Case.instantiateWhenFieldIsNullThenZeroElseFieldAndBuild("available.%1$s", "0l")
		+" - "+CaseStringBuilder.Case.instantiateWhenFieldIsNullThenZeroElseFieldAndBuild("im.%1$s", "0l")
		+" + "+CaseStringBuilder.Case.instantiateWhenFieldIsNullThenZeroElseFieldAndBuild("t.%1$s.adjustment", "0l")+" >= 0l)";
		static String getAvailableMinusIncludedMovementPlusAdjustmentLessThanZero(Boolean lessThan,String amountFieldName) {
			return String.format(Boolean.TRUE.equals(lessThan) ? AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_LESS_THAN_ZERO_FORMAT : AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_NOT_LESS_THAN_ZERO_FORMAT ,amountFieldName);
		}
		
		static String getAvailableMinusIncludedMovementPlusAdjustmentLessThanZero(Boolean lessThan) {
			if(Boolean.TRUE.equals(lessThan))
				return parenthesis(or(getAvailableMinusIncludedMovementPlusAdjustmentLessThanZero(lessThan,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION),getAvailableMinusIncludedMovementPlusAdjustmentLessThanZero(lessThan,ExpenditureImpl.FIELD_PAYMENT_CREDIT)));
			return and(getAvailableMinusIncludedMovementPlusAdjustmentLessThanZero(lessThan,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION),getAvailableMinusIncludedMovementPlusAdjustmentLessThanZero(lessThan,ExpenditureImpl.FIELD_PAYMENT_CREDIT));
		}
		/*
		private static final String PREDICATE_EXPENDITURE_INCLUDED_MOVEMENT_EQUAL_ZERO_FORMAT = parenthesis(jpql(parenthesis("SELECT SUM(rae.%4$s) FROM %1$s ralav,%2$s rae,%3$s ra"
				+ " WHERE rae.%5$s = ralav.%6$s.identifier AND rae.%7$s = t.%7$s AND rae.%8$s = t.%8$s AND rae.%9$s = t.%9$s AND rae.%10$s = t.%10$s AND ra.identifier = rae.%5$s AND ra.%11$s = exercise.%11$s AND ralav.%12$s IS TRUE"),"%13$s","0"));
		private static String buildPredicateExpenditureMovementIncludedEqualZeroPredicate(Boolean isEqualToZero) {
			Collection<String> strings = new ArrayList<>();
			for(String field : RegulatoryActExpenditureImpl.ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT) {
				strings.add(String.format(PREDICATE_EXPENDITURE_INCLUDED_MOVEMENT_EQUAL_ZERO_FORMAT,RegulatoryActLegislativeActVersionImpl.ENTITY_NAME,RegulatoryActExpenditureImpl.ENTITY_NAME,RegulatoryActImpl.ENTITY_NAME,field
						,RegulatoryActExpenditureImpl.FIELD_ACT_IDENTIFIER,RegulatoryActLegislativeActVersionImpl.FIELD_REGULATORY_ACT,RegulatoryActExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER
						,RegulatoryActExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_LESSOR_IDENTIFIER,RegulatoryActImpl.FIELD_YEAR,RegulatoryActLegislativeActVersionImpl.FIELD_INCLUDED,Language.formatOperatorEqual(isEqualToZero)));
			}
			return StringHelper.concatenate(strings, " AND ");
		}
		
		private static final String PREDICATE_EXPENDITURE_AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_LESS_THAN_ZERO_FORMAT = parenthesis(jpql(parenthesis("SELECT SUM(available.%4$s-rae.%5$s+t.%6$s.adjustment) FROM %1$s ralav,%2$s rae,%3$s ra"
				+ " WHERE rae.%7$s = ralav.%8$s.identifier AND rae.%9$s = t.%9$s AND rae.%10$s = t.%10$s AND rae.%11$s = t.%11$s AND rae.%12$s = t.%12$s AND ra.identifier = rae.%7$s AND ra.%13$s = exercise.%13$s"),"%14$s","0"));
		private static String buildPredicateExpenditureAvailableMinusIncludedMovementPlusAdjustmentLessThanZeroPredicate(Boolean isLessThanZero) {
			Collection<String> strings = new ArrayList<>();
			for(String field : RegulatoryActExpenditureImpl.ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT) {
				strings.add(String.format(PREDICATE_EXPENDITURE_AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_LESS_THAN_ZERO_FORMAT
						,RegulatoryActLegislativeActVersionImpl.ENTITY_NAME,RegulatoryActExpenditureImpl.ENTITY_NAME,RegulatoryActImpl.ENTITY_NAME
						,StringUtils.substringBefore(field, "Amount"),field,StringUtils.substringBefore(field, "Amount")
						,RegulatoryActExpenditureImpl.FIELD_ACT_IDENTIFIER,RegulatoryActLegislativeActVersionImpl.FIELD_REGULATORY_ACT,RegulatoryActExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER
						,RegulatoryActExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_LESSOR_IDENTIFIER,RegulatoryActImpl.FIELD_YEAR
						,Language.formatOperatorLessThan(isLessThanZero)));
			}
			return StringHelper.concatenate(strings, " AND ");
		}*/
	}
	
	
}