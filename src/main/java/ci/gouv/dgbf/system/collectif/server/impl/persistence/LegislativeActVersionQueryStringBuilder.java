package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder.Arguments;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import lombok.Setter;
import lombok.experimental.Accessors;

public interface LegislativeActVersionQueryStringBuilder {
	
	public static interface Projection {
		static void set(QueryExecutorArguments queryExecutorArguments, Arguments builderArguments) {
			Boolean amountSumable = queryExecutorArguments.getFilterFieldValueAsBoolean(null,Parameters.AMOUNT_SUMABLE);
			if(Boolean.TRUE.equals(amountSumable))
				new LegislativeActVersionQueryStringBuilder.Projection.ExpendituresAmounts().build(builderArguments);
		}
		
		@Setter @Accessors(chain = true)
		public static class ResourcesAmounts extends ResourceQueryStringBuilder.Projection.Amounts implements Serializable {

			public ResourcesAmounts() {
				variableName = "r";
				expectedVariableName = "la";
				sumable = Boolean.TRUE;
			}
			
			@Override
			protected Boolean isIdentifiable() {
				return Boolean.TRUE;
			}
			
			public static void set(LegislativeActVersionImpl legislativeActVersion,Object[] array,Boolean expected,Boolean view,Boolean includedMovement,Boolean available) {
				if(legislativeActVersion == null)
					return;
				ResourceQueryStringBuilder.Projection.Amounts.set(legislativeActVersion.getRevenue(Boolean.TRUE),array,1,expected,view,includedMovement,available);
			}
		}
		
		@Setter @Accessors(chain = true)
		public static class ExpendituresAmounts extends ExpenditureQueryStringBuilder.Projection.Amounts implements Serializable {

			public ExpendituresAmounts() {
				variableName = "e";
				expected = Boolean.TRUE;
				expectedVariableName = "la";
				sumable = Boolean.TRUE;
			}
			
			@Override
			protected Boolean isIdentifiable() {
				return Boolean.TRUE;
			}
			
			public static void set(LegislativeActVersionImpl legislativeActVersion,Object[] array,Boolean adjustment,Boolean expected,Boolean view,Boolean includedMovement,Boolean available) {
				if(legislativeActVersion == null)
					return;
				Integer index = ExpenditureQueryStringBuilder.Projection.Amounts.set(legislativeActVersion.getEntryAuthorization(Boolean.TRUE),array,1,adjustment,expected,view,includedMovement,available);
				ExpenditureQueryStringBuilder.Projection.Amounts.set(legislativeActVersion.getPaymentCredit(Boolean.TRUE),array,index,adjustment,expected,view,includedMovement,available);
			}
		}
	}
	
	public static interface Tuple {
		static void set(QueryExecutorArguments queryExecutorArguments, Arguments builderArguments) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",LegislativeActVersionImpl.ENTITY_NAME));
			if((queryExecutorArguments.getFilterBackup() != null && queryExecutorArguments.getFilterBackup().getFieldValue(Parameters.DEFAULT_LEGISLATIVE_ACT_VERSION_IN_LATEST_LEGISLATIVE_ACT) != null)) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s la ON la = t.%s AND la.%s = t",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_DEFAULT_VERSION));
				builderArguments.getTuple().addJoins(String.format("LEFT JOIN %s exercise ON exercise.%s = la.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
			}
		}
		
		@Setter @Accessors(chain = true)
		public static class ResourcesAmounts extends ResourceQueryStringBuilder.Tuple.Amounts implements Serializable {
			
			public ResourcesAmounts() {
				resourceVariableName = "r";
				actVersionVariableName = "t";
			}
			
			@Override
			protected void joinActVersion(Arguments arguments) {
				arguments.getTuple().addJoins(String.format("JOIN %1$s %2$s ON %2$s.%3$s = %4$s",ResourceImpl.ENTITY_NAME,resourceVariableName,ResourceImpl.FIELD_ACT_VERSION,actVersionVariableName));
			}
		}
		
		@Setter @Accessors(chain = true)
		public static class ExpendituresAmounts extends ExpenditureQueryStringBuilder.Tuple.Amounts implements Serializable {
			
			public ExpendituresAmounts() {
				expenditureVariableName = "e";
				actVersionVariableName = "t";
			}
			
			@Override
			protected void joinActVersion(Arguments arguments) {
				arguments.getTuple().addJoins(String.format("JOIN %1$s %2$s ON %2$s.%3$s = %4$s",ExpenditureImpl.ENTITY_NAME,expenditureVariableName,ExpenditureImpl.FIELD_ACT_VERSION,actVersionVariableName));
			}
		}
	}
	
	public static interface Predicate {
		static void set(QueryExecutorArguments queryExecutorArguments, Arguments builderArguments, org.cyk.utility.persistence.server.query.string.WhereStringBuilder.Predicate predicate,Filter filter) {
			RuntimeQueryStringBuilderImpl.addEqualsIfFilterHasFieldWithPath(queryExecutorArguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_IDENTIFIER,"t"
					,FieldHelper.join(LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_IDENTIFIER));
		}		
	}	
}