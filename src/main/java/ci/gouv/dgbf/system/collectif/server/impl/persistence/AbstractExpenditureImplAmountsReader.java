package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class AbstractExpenditureImplAmountsReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		new ExpenditureQueryStringBuilder.Projection.Amounts().setAdjustment(hasAdjustment()).setExpected(hasExpectedAdjustment()).setView(hasView()).setIncludedMovement(hasIncludedMovement()).setAvailable(hasAvailable()).build(arguments);
		new ExpenditureQueryStringBuilder.Tuple.Amounts().setView(hasView()).setIncludedMovement(hasIncludedMovement()).setAvailable(hasAvailable()).build(arguments);
		return arguments;
	}
	
	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		ExpenditureQueryStringBuilder.Projection.Amounts.set(expenditure, array,hasAdjustment(),hasExpectedAdjustment(),hasView(),hasIncludedMovement(),hasAvailable());
	}
	
	protected Boolean hasAdjustment() {
		return Boolean.TRUE;
	}
	
	protected Boolean hasExpectedAdjustment() {
		return Boolean.FALSE;
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