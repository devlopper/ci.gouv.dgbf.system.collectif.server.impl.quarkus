package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.persistence.query.Language;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.query.string.CaseStringBuilder;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder.Arguments;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import lombok.Setter;
import lombok.experimental.Accessors;

public interface ResourceQueryStringBuilder {
	
	String[] REVENUE = {ResourceImpl.FIELD_REVENUE};
	String[] REGULATORY_ACT_LEGISLATIVE_ACT_VERSION_AND_AVAILABLE = {ResourceImpl.FIELDS_AMOUNTS};
	
	public static interface Projection {
		
		static void set(QueryExecutorArguments queryExecutorArguments, Arguments builderArguments) {
			Boolean amountSumable = queryExecutorArguments.getFilterFieldValueAsBoolean(null,Parameters.AMOUNT_SUMABLE);
			if(Boolean.TRUE.equals(amountSumable))
				new Amounts().setSumable(Boolean.TRUE).build(builderArguments);
		}
		
		@Setter @Accessors(chain = true)
		public static class Amounts {
			protected Boolean view=Boolean.TRUE,includedMovement=Boolean.FALSE,available=Boolean.FALSE;
			protected String variableName = "t",expectedVariableName;
			protected Boolean sumable = Boolean.FALSE;

			public void build(Arguments arguments) {
				if(isIdentifiable())
					arguments.getProjection(Boolean.TRUE).addFromTuple("t",ResourceImpl.FIELD_IDENTIFIER);
				for(String fieldName : REVENUE) {
					arguments.getProjection(Boolean.TRUE).add(get(variableName, FieldHelper.join(fieldName,AbstractAmountsImpl.FIELD_ADJUSTMENT)));
					if(StringHelper.isNotBlank(expectedVariableName))
						arguments.getProjection().add(get(expectedVariableName, FieldHelper.join(fieldName,AbstractAmountsImpl.FIELD_ADJUSTMENT)));
					if(Boolean.TRUE.equals(view)) {
						arguments.getProjection().add(get("v", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_INITIAL)));
						arguments.getProjection().add(get("v", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_MOVEMENT)));
						arguments.getProjection().add(get("v", FieldHelper.join(fieldName,AbstractAmountsView.FIELD_ACTUAL)));
					}
				}
			}
			
			protected Boolean isIdentifiable() {
				return !Boolean.TRUE.equals(sumable);
			}
			
			protected String get(String tupleName,String fieldName) {
				return get(tupleName, fieldName, sumable);
			}
			
			public static String get(String tupleName,String fieldName,Boolean sumable) {
				String string = CaseStringBuilder.Case.instantiateWhenFieldIsNullThenZeroElseFieldAndBuild(FieldHelper.join(tupleName,fieldName),"0l");
				if(Boolean.TRUE.equals(sumable))
					string = Language.formatSum(string);
				return string;
			}
			
			public static Integer set(AbstractAmountsImpl amounts,Object[] array,Integer index,Boolean expected,Boolean view,Boolean includedMovement,Boolean available) {
				if(amounts == null || index == null || index < 0)
					return index;
				if(index < array.length)
					amounts.setAdjustment(NumberHelper.getLong(array[index++],0l));
				if(Boolean.TRUE.equals(expected)) {
					amounts.setExpectedAdjustment(NumberHelper.getLong(array[index++],0l));
					amounts.computeExpectedAdjustmentMinusAdjustment();
				}
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
			
			static void set(ResourceImpl resource,Object[] array,Boolean view,Integer index) {
				if(resource == null)
					return;
				set(resource.getRevenue(Boolean.TRUE),array,index,null,view,null,null);
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
		
		public static String getView(String variableName,String actVersionVariableName) {
			return String.format("JOIN %3$s v ON v.%4$s = "+actVersionVariableName+".identifier AND v.%5$s = %1$s.%5$s AND v.%6$s = %1$s.%6$s", variableName,"exercise"
					,ResourceView.ENTITY_NAME,ResourceView.FIELD_LEGISLATIVE_ACT_VERSION_IDENTIFIER,ResourceView.FIELD_ACTIVITY_IDENTIFIER,ResourceView.FIELD_ECONOMIC_NATURE_IDENTIFIER);
		}
		
		public static String getView() {
			return getView("t","lav");
		}
		
		public static String getIncludedMovement(String variableName) {
			return null;
		}
		
		public static String getIncludedMovement() {
			return getIncludedMovement("t");
		}
		
		public static String getAvailable(String variableName) {
			return null;
		}
		
		public static String getAvailable() {
			return getAvailable("t");
		}
		
		/**/
		
		@Setter @Accessors(chain = true)
		public static class Amounts implements Serializable {
			protected Boolean view=Boolean.TRUE,includedMovement=Boolean.FALSE,available=Boolean.FALSE,joinActVersion=Boolean.TRUE,joinAct=Boolean.TRUE;
			protected String expenditureVariableName = "t",actVersionVariableName="lav",actVariableName="la",exerciseVariableName="exercise";
			
			public void build(Arguments arguments) {
				if(Boolean.TRUE.equals(view))
					view(arguments);
				if(Boolean.TRUE.equals(includedMovement))
					joinIncludedMovement(arguments);
				if(Boolean.TRUE.equals(available))
					joinAvailable(arguments);
			}
			
			protected void view(Arguments arguments) {
				if(Boolean.TRUE.equals(view)) {
					if(Boolean.TRUE.equals(joinActVersion))
						joinActVersion(arguments);
					joinView(arguments);
					if(Boolean.TRUE.equals(joinAct))
						joinAct(arguments);
					joinExercise(arguments);
				}
			}
			
			protected void joinActVersion(Arguments arguments) {
				arguments.getTuple().addJoins(String.format("JOIN %1$s %4$s ON %4$s = %2$s.%3$s",LegislativeActVersionImpl.ENTITY_NAME,expenditureVariableName,ExpenditureImpl.FIELD_ACT_VERSION,actVersionVariableName));
			}
			
			protected void joinAct(Arguments arguments) {
				arguments.getTuple().addJoins(String.format("JOIN %1$s %3$s ON %3$s = %4$s.%2$s",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT,actVariableName,actVersionVariableName));
			}
			
			protected void joinView(Arguments arguments) {
				arguments.getTuple().addJoins("LEFT "+getView(expenditureVariableName,actVersionVariableName));
			}
			
			protected void joinExercise(Arguments arguments) {
				arguments.getTuple().addJoins(String.format("LEFT JOIN %1$s %2$s ON %2$s.%3$s = %4$s.%5$s",ExerciseImpl.ENTITY_NAME,exerciseVariableName,ExerciseImpl.FIELD_IDENTIFIER,actVariableName,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
			}
			
			protected void joinIncludedMovement(Arguments arguments) {
				arguments.getTuple().addJoins("LEFT "+getJoinIncludedMovement());
			}
			
			protected void joinAvailable(Arguments arguments) {
				arguments.getTuple().addJoins("LEFT "+getJoinAvailable());
			}
			
			protected String getJoinIncludedMovement() {
				return Tuple.getIncludedMovement(expenditureVariableName);
			}
			
			protected String getJoinAvailable() {
				return Tuple.getAvailable(expenditureVariableName);
			}
		}
	}
	
	public static interface Predicate {
		
	}	
}