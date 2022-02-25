package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class AbstractExpenditureImplAmountsReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		ExpenditureQueryStringBuilder.Projection.projectAmounts(arguments,hasView(),hasIncludedMovement(),hasAvailable());
		ExpenditureQueryStringBuilder.Tuple.joinAmounts(arguments,hasView(),hasIncludedMovement(),hasAvailable());
		return arguments;
	}
	
	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		ExpenditureQueryStringBuilder.Projection.setAmounts(expenditure, array,hasView(),hasIncludedMovement(),hasAvailable());
	}
	
	protected Boolean hasView() {
		return Boolean.TRUE;
	}
	
	protected Boolean hasIncludedMovement() {
		return null;
	}
	
	protected Boolean hasAvailable() {
		return null;
	}
}