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
				new LegislativeActVersionQueryStringBuilder.Projection.Amounts().build(builderArguments);
		}
		
		@Setter @Accessors(chain = true)
		public static class Amounts extends ExpenditureQueryStringBuilder.Projection.Amounts implements Serializable {

			public Amounts() {
				variableName = "e";
				expectedVariableName = "la";
				sumable = Boolean.TRUE;
			}
			
			@Override
			protected Boolean isIdentifiable() {
				return Boolean.TRUE;
			}
			
			public static void set(LegislativeActVersionImpl legislativeActVersion,Object[] array) {
				if(legislativeActVersion == null)
					return;
				Integer index = ExpenditureQueryStringBuilder.Projection.Amounts.set(legislativeActVersion.getEntryAuthorization(Boolean.TRUE),array,1,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
				ExpenditureQueryStringBuilder.Projection.Amounts.set(legislativeActVersion.getPaymentCredit(Boolean.TRUE),array,index,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
			}
			
		}
	}
	
	public static interface Tuple {
		static void set(QueryExecutorArguments queryExecutorArguments, Arguments builderArguments) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",LegislativeActVersionImpl.ENTITY_NAME));
			/*
			Boolean amountSumable = queryExecutorArguments.getFilterFieldValueAsBoolean(null,Parameters.AMOUNT_SUMABLE);
			if(Boolean.TRUE.equals(amountSumable)) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s la ON la = t.%s AND la.%s = t",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_DEFAULT_VERSION));
				builderArguments.getTuple().addJoins(String.format("JOIN %s exercise ON exercise.%s = la.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
				builderArguments.getTuple().addJoins(String.format("JOIN %s e ON e.%s = t",ExpenditureImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION));
				builderArguments.getTuple().addJoins(String.format("JOIN %s ev ON ev.identifier = e.identifier",ExpenditureView.ENTITY_NAME));
				
				//if(Boolean.TRUE.equals(includedMovement))
					builderArguments.getTuple().addJoins("LEFT "+ExpenditureQueryStringBuilder.Tuple.getIncludedMovement("e"));
				//if(Boolean.TRUE.equals(available))
					builderArguments.getTuple().addJoins("LEFT "+ExpenditureQueryStringBuilder.Tuple.getAvailable("e"));
			}
			*/
			if((queryExecutorArguments.getFilterBackup() != null && queryExecutorArguments.getFilterBackup().getFieldValue(Parameters.DEFAULT_LEGISLATIVE_ACT_VERSION_IN_LATEST_LEGISLATIVE_ACT) != null)) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s la ON la = t.%s AND la.%s = t",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_DEFAULT_VERSION));
				builderArguments.getTuple().addJoins(String.format("LEFT JOIN %s exercise ON exercise.%s = la.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
				//builderArguments.getTuple().addJoins(String.format("JOIN %s dv ON dv = t AND dv.%s = la",LegislativeActVersionImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT));
			}
		}
		
		@Setter @Accessors(chain = true)
		public static class Amounts extends ExpenditureQueryStringBuilder.Tuple.Amounts implements Serializable {
			
			public Amounts() {
				joinActVersion = Boolean.FALSE;
				variableName = "e";
				actVersionVariableName = "t";
			}
			
			public void build(Arguments arguments) {
				if(Boolean.TRUE.equals(view)) {
					arguments.getTuple().addJoins(String.format("JOIN %1$s %2$s ON %2$s = %3$s.%4$s AND %2$s.%5$s = %3$s",LegislativeActImpl.ENTITY_NAME,"la","t",LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_DEFAULT_VERSION));
					arguments.getTuple().addJoins(String.format("LEFT JOIN %1$s %2$s ON %2$s.%3$s = %4$s.%5$s",ExerciseImpl.ENTITY_NAME,"exercise",ExerciseImpl.FIELD_IDENTIFIER,"la",LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
					arguments.getTuple().addJoins(String.format("JOIN %1$s %2$s ON %2$s.%3$s = %4$s",ExpenditureImpl.ENTITY_NAME,"e",ExpenditureImpl.FIELD_ACT_VERSION,"t"));
					arguments.getTuple().addJoins("LEFT "+ExpenditureQueryStringBuilder.Tuple.getView("e","t"));	
				}
				if(Boolean.TRUE.equals(includedMovement))
					arguments.getTuple().addJoins("LEFT "+ExpenditureQueryStringBuilder.Tuple.getIncludedMovement("e"));
				if(Boolean.TRUE.equals(available))
					arguments.getTuple().addJoins("LEFT "+ExpenditureQueryStringBuilder.Tuple.getAvailable("e"));
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