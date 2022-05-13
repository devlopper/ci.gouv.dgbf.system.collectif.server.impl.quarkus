package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class AbstractLegislativeActVersionImplExpendituresAmountsReader extends AbstractLegislativeActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		new LegislativeActVersionQueryStringBuilder.Projection.ExpendituresAmounts().setAdjustment(hasAdjustment()).setExpected(hasExpectedAdjustment()).setView(hasView()).setIncludedMovement(hasIncludedMovement()).setAvailable(hasAvailable()).build(arguments);
		new LegislativeActVersionQueryStringBuilder.Tuple.ExpendituresAmounts().setView(hasView()).setIncludedMovement(hasIncludedMovement()).setAvailable(hasAvailable()).build(arguments);
		arguments.getGroup(Boolean.TRUE).add("t.identifier");
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActVersionImpl legislativeActVersion, Object[] array) {
		LegislativeActVersionQueryStringBuilder.Projection.ExpendituresAmounts.set(legislativeActVersion, array,hasAdjustment(),hasExpectedAdjustment(),hasView(),hasIncludedMovement(),hasAvailable());
	}
	
	protected Boolean hasAdjustment() {
		return Boolean.TRUE;
	}
	
	protected Boolean hasAdjustmentLowerThanZero() {
		return null;
	}
	
	protected Boolean hasAdjustmentGreaterThanZero() {
		return null;
	}
	
	protected Boolean hasExpectedAdjustment() {
		return Boolean.TRUE;
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