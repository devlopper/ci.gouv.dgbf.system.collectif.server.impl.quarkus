package ci.gouv.dgbf.system.collectif.server.impl.persistence;

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

public interface ResourceQueryStringBuilder {
	
	String[] ENTRY_AUTHORIZATION_PAYMENT_CREDIT = {ResourceImpl.FIELD_REVENUE};
	String[] REGULATORY_ACT_LEGISLATIVE_ACT_VERSION_AND_AVAILABLE = {ResourceImpl.FIELDS_AMOUNTS};
	
	public static interface Projection {
		static void projectAmounts(Arguments arguments,Boolean view,Boolean includedMovement,Boolean available) {
			arguments.getProjection(Boolean.TRUE).addFromTuple("t",ResourceImpl.FIELD_IDENTIFIER);
			for(String fieldName : ENTRY_AUTHORIZATION_PAYMENT_CREDIT) {
				arguments.getProjection().add(projectAmount("t", FieldHelper.join(fieldName,AbstractAmountsImpl.FIELD_ADJUSTMENT)));
				if(Boolean.TRUE.equals(view)) {
					arguments.getProjection().add(projectAmount("ev", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_INITIAL)));
					arguments.getProjection().add(projectAmount("ev", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_MOVEMENT)));
					arguments.getProjection().add(projectAmount("ev", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_ACTUAL)));
				}
				/*
				if(Boolean.TRUE.equals(includedMovement))
					arguments.getProjection().add(projectAmount("im", fieldName));
				if(Boolean.TRUE.equals(available))
					arguments.getProjection().add(projectAmount("available", fieldName));
				*/
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
			/*
			if(Boolean.TRUE.equals(includedMovement) && index < array.length)
				amounts.setMovementIncluded(NumberHelper.getLong(array[index++],0l));
			if(Boolean.TRUE.equals(available) && index < array.length)
				amounts.setAvailable(NumberHelper.getLong(array[index++],0l));
			
			amounts.computeActualPlusAdjustment();
			if(Boolean.TRUE.equals(includedMovement))
				amounts.computeActualMinusMovementIncludedPlusAdjustment();
			if(Boolean.TRUE.equals(includedMovement) && Boolean.TRUE.equals(available))
				amounts.computeAvailableMinusMovementIncludedPlusAdjustment();
			*/
			return index;
		}
		
		static void setAmounts(ResourceImpl expenditure,Object[] array,Boolean view,Boolean includedMovement,Boolean available) {
			if(expenditure == null)
				return;
			/*Integer index = */setAmounts(expenditure.getRevenue(Boolean.TRUE),array,1,view,includedMovement,available);
			//setAmounts(expenditure.getPaymentCredit(Boolean.TRUE),array,index,view,includedMovement,available);
		}
		
		/**/
		
		static String projectResourceAmount(String tupleName,String fieldName) {
			return String.format("MAX(%s)",CaseStringBuilder.Case.instantiateWhenFieldIsNullThenZeroElseFieldAndBuild(FieldHelper.join(tupleName,fieldName),"0l"));
		}
		
		static String projectResourceAmountMovementIncluded(String fieldName) {
			return String.format("SUM(CASE WHEN ralav.included IS TRUE THEN rar.%sAmount ELSE 0l END)",fieldName);
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
		
		static void setAmounts(ResourceImpl expenditure,Object[] array) {
			if(expenditure == null)
				return;
			/*Integer index = */setAmounts(expenditure.getRevenue(Boolean.TRUE),array,1);
			//setAmounts(expenditure.getPaymentCredit(Boolean.TRUE),array,index);
		}
	}
	
	public static interface Tuple {
		
		public static String getView() {
			return String.format("JOIN %3$s ev ON ev.%4$s = lav.identifier AND ev.%5$s = t.%5$s AND ev.%6$s = t.%6$s", "t","exercise"
					,ResourceView.ENTITY_NAME,ResourceView.FIELD_LEGISLATIVE_ACT_VERSION_IDENTIFIER,ResourceView.FIELD_ACTIVITY_IDENTIFIER,ResourceView.FIELD_ECONOMIC_NATURE_IDENTIFIER);
		}
		
		public static String getIncludedMovement() {
			throw new RuntimeException("Not yet implemented");
			//return String.format("JOIN %1$s im ON im.%2$s = t.%2$s",ResourceIncludedMovementView.ENTITY_NAME,ResourceIncludedMovementView.FIELD_IDENTIFIER);
		}
		
		public static String getAvailable() {
			throw new RuntimeException("Not yet implemented");
			/*return String.format("JOIN %3$s available ON available.%4$s = exercise.%5$s AND available.%6$s = t.%6$s AND available.%7$s = t.%7$s AND available.%8$s = t.%8$s AND available.%9$s = t.%9$s", "t", "exercise"
					,ResourceAvailableView.ENTITY_NAME,ResourceAvailableView.FIELD_YEAR,ExerciseImpl.FIELD_YEAR,ResourceAvailableView.FIELD_ACTIVITY_IDENTIFIER,ResourceAvailableView.FIELD_ECONOMIC_NATURE_IDENTIFIER
					,ResourceAvailableView.FIELD_FUNDING_SOURCE_IDENTIFIER,ResourceAvailableView.FIELD_LESSOR_IDENTIFIER);
			*/
		}
		
		/**/
		
		static void joinAmounts(Arguments arguments,Boolean view,Boolean includedMovement,Boolean available) {
			if(Boolean.TRUE.equals(view)) {
				arguments.getTuple().addJoins(String.format("JOIN %s lav ON lav = t.%s",LegislativeActVersionImpl.ENTITY_NAME,ResourceImpl.FIELD_ACT_VERSION));
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
			arguments.getTuple().addJoins(String.format("JOIN %s lav ON lav = t.%s",LegislativeActVersionImpl.ENTITY_NAME,ResourceImpl.FIELD_ACT_VERSION));
			arguments.getTuple().addJoins("LEFT "+getView());
			arguments.getTuple().addJoins(String.format("JOIN %s la ON la = lav.%s",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT));
			arguments.getTuple().addJoins(String.format("LEFT JOIN %s exercise ON exercise.%s = la.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
		}
		
		static void joinRegulatoryActLegislativeActVersion(Arguments arguments) {
			joinLegislativeActVersionAndExercise(arguments);
			arguments.getTuple().addJoins("LEFT "+getJoinRegulatoryActResource());
			arguments.getTuple().addJoins(String.format("LEFT JOIN %s ra ON ra.%s = rae.%s",RegulatoryActImpl.ENTITY_NAME,RegulatoryActImpl.FIELD_IDENTIFIER,RegulatoryActResourceImpl.FIELD_ACT_IDENTIFIER));
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
		
		public static String getJoinRegulatoryActResource() {
			return String.format("JOIN %3$s rae ON rae.%4$s = exercise.%5$s AND rae.%6$s = t.%6$s AND rae.%7$s = t.%7$s", "t", "exercise"
					,RegulatoryActResourceImpl.ENTITY_NAME,RegulatoryActResourceImpl.FIELD_YEAR,ExerciseImpl.FIELD_YEAR,RegulatoryActResourceImpl.FIELD_ACTIVITY_IDENTIFIER,RegulatoryActResourceImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER);
		}
		
		public static void join(QueryExecutorArguments arguments, Arguments builderArguments) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",ResourceImpl.ENTITY_NAME));
			if(arguments.getFilterField(Parameters.INCLUDED_MOVEMENT_NOT_EQUAL_ZERO) != null || arguments.getFilterField(Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO) != null) {
				//builderArguments.getTuple().addJoins(String.format("JOIN %s lav ON lav = t.%s",LegislativeActVersionImpl.ENTITY_NAME,ResourceImpl.FIELD_ACT_VERSION));
				//builderArguments.getTuple().addJoins(String.format("JOIN %s la ON la = lav.%s",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT));
				//builderArguments.getTuple().addJoins(String.format("JOIN %s exercise ON exercise.%s = la.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));	
			}
			
			if(arguments.getFilterField(Parameters.AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_LESS_THAN_ZERO) != null) {
				//ResourceQueryStringBuilder.Join.joinRegulatoryActLegislativeActVersionAndAvailable(builderArguments);
				//builderArguments.getGroup(Boolean.TRUE).add("t.identifier");
			}
			
			/*String identifier = (String) arguments.getFilterFieldValue(Parameters.SECTION_IDENTIFIER);
			if(StringHelper.isNotBlank(identifier)) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s v ON v.identifier = t.identifier AND v.sectionIdentifier = '%s'",ResourceView.ENTITY_NAME
						,identifier));
				arguments.removeFilterFields(Parameters.SECTION_IDENTIFIER);
			}
			*/
			if(Boolean.TRUE.equals(isJoinedToView(arguments, builderArguments))) {
				//builderArguments.getTuple().addJoins(String.format("JOIN %s v ON v.identifier = t.identifier",ResourceView.ENTITY_NAME));
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
					&& CollectionUtils.containsAny(arguments.getProjections().stream().map(projection -> projection.getFieldName()).collect(Collectors.toList()),ResourceImpl.VIEW_FIELDS_NAMES))
				return Boolean.TRUE;
			
			if(CollectionHelper.isNotEmpty(arguments.getProcessableTransientFieldsNames()) 
					&& CollectionUtils.containsAny(arguments.getProcessableTransientFieldsNames(),ResourceImpl.VIEW_FIELDS_NAMES))
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
				return getMovementIncludedEqualZero(equal,ResourceImpl.FIELD_REVENUE);
			return getMovementIncludedEqualZero(equal,ResourceImpl.FIELD_REVENUE);
		}
		
		String ADJUSTMENT_NOT_EQUAL_ZERO_OR_MOVEMENT_INCLUDED_NOT_EQUAL_ZERO_FORMAT = "((t.%1$s.adjustment IS NOT NULL AND t.%1$s.adjustment <> 0l) OR (im.%1$s IS NOT NULL AND im.%1$s <> 0l))";
		String ADJUSTMENT_EQUAL_ZERO_AND_MOVEMENT_INCLUDED_EQUAL_ZERO_FORMAT = "t.%1$s.adjustment IS NULL OR t.%1$s.adjustment <> 0l AND im.%1$s IS NOT NULL AND im.%1$s <> 0l";
		static String getAdjustmentNotEqualZeroOrMovementIncludedNotEqualZero(Boolean notEqual,String amountFieldName) {
			return String.format(Boolean.TRUE.equals(notEqual) ? ADJUSTMENT_NOT_EQUAL_ZERO_OR_MOVEMENT_INCLUDED_NOT_EQUAL_ZERO_FORMAT : ADJUSTMENT_EQUAL_ZERO_AND_MOVEMENT_INCLUDED_EQUAL_ZERO_FORMAT ,amountFieldName);
		}
		
		static String getAdjustmentNotEqualZeroOrMovementIncludedNotEqualZero(Boolean notEqual) {
			if(Boolean.TRUE.equals(notEqual))
				return getAdjustmentNotEqualZeroOrMovementIncludedNotEqualZero(notEqual,ResourceImpl.FIELD_REVENUE);
			return getAdjustmentNotEqualZeroOrMovementIncludedNotEqualZero(notEqual,ResourceImpl.FIELD_REVENUE);
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
				return getAvailableMinusIncludedMovementPlusAdjustmentLessThanZero(lessThan,ResourceImpl.FIELD_REVENUE);
			return getAvailableMinusIncludedMovementPlusAdjustmentLessThanZero(lessThan,ResourceImpl.FIELD_REVENUE);
		}
	}	
}