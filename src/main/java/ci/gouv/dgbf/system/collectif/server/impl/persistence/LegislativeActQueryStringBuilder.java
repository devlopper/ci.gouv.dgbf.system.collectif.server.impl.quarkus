package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder.Arguments;

import lombok.Setter;
import lombok.experimental.Accessors;

public interface LegislativeActQueryStringBuilder {
	
	public static interface Projection {
		static void set(QueryExecutorArguments queryExecutorArguments, Arguments builderArguments) {
			
		}
		
		@Setter @Accessors(chain = true)
		public static class Amounts extends LegislativeActVersionQueryStringBuilder.Projection.Amounts implements Serializable {

			public Amounts() {
				expectedVariableName = "t";
			}
			
			public static void set(LegislativeActImpl legislativeAct,Object[] array) {
				if(legislativeAct == null)
					return;
				Integer index = ExpenditureQueryStringBuilder.Projection.Amounts.set(legislativeAct.getEntryAuthorization(Boolean.TRUE),array,1,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
				ExpenditureQueryStringBuilder.Projection.Amounts.set(legislativeAct.getPaymentCredit(Boolean.TRUE),array,index,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
			}
			
		}
	}
	
	public static interface Tuple {
		
		@Setter @Accessors(chain = true)
		public static class Amounts extends ExpenditureQueryStringBuilder.Tuple.Amounts implements Serializable {
			
			public Amounts() {
				joinAct = Boolean.FALSE;
				expenditureVariableName = "e";
				actVariableName = "t";
			}
			
			@Override
			protected void joinView(Arguments arguments) {
				arguments.getTuple().addJoins(String.format("JOIN %1$s %2$s ON %2$s.%3$s = %4$s",ExpenditureImpl.ENTITY_NAME,expenditureVariableName,ExpenditureImpl.FIELD_ACT_VERSION,actVersionVariableName));
				super.joinView(arguments);
			}
			
			@Override
			protected void joinActVersion(Arguments arguments) {
				arguments.getTuple().addJoins(String.format("JOIN %1$s %2$s ON %2$s.%3$s = %4$s AND %4$s.%5$s = %2$s",LegislativeActVersionImpl.ENTITY_NAME,actVersionVariableName,LegislativeActVersionImpl.FIELD_ACT,actVariableName
						,LegislativeActImpl.FIELD_DEFAULT_VERSION));
			}
		}
	}
	
	public static interface Predicate {
		
	}
	
	
}