package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureQueryStringBuilder.Projection.Amounts.SetArguments;

public class AbstractLegislativeActVersionImplExpendituresAmountsReader extends AbstractLegislativeActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		new LegislativeActVersionQueryStringBuilder.Projection.ExpendituresAmounts().setAdjustment(hasAdjustment()).setAdjustmentLessThanZero(hasAdjustmentLessThanZero()).setAdjustmentGreaterThanZero(hasAdjustmentGreaterThanZero())
		.setExpected(hasExpectedAdjustment()).setView(hasView()).setIncludedMovement(hasIncludedMovement()).setAvailable(hasAvailable()).setActualAtLegislativeActDate(hasActualAtLegislativeActDate()).build(arguments);
		new LegislativeActVersionQueryStringBuilder.Tuple.ExpendituresAmounts().setView(hasView()).setIncludedMovement(hasIncludedMovement()).setAvailable(hasAvailable()).setActualAtLegislativeActDate(hasActualAtLegislativeActDate()).build(arguments);
		arguments.getGroup(Boolean.TRUE).add("t.identifier");
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActVersionImpl legislativeActVersion, Object[] array) {
		LegislativeActVersionQueryStringBuilder.Projection.ExpendituresAmounts.set(legislativeActVersion,new SetArguments().setArray(array).setAdjustment(hasAdjustment()).setAdjustmentLessThanZero(hasAdjustmentLessThanZero())
				.setAdjustmentGreaterThanZero(hasAdjustmentGreaterThanZero()).setExpected(hasExpectedAdjustment()).setView(hasView()).setIncludedMovement(hasIncludedMovement()).setAvailable(hasAvailable())
				.setActualAtLegislativeActDate(hasActualAtLegislativeActDate()));
	}
	
	protected Boolean hasAdjustment() {
		return Boolean.TRUE;
	}
	
	protected Boolean hasAdjustmentLessThanZero() {
		return Boolean.TRUE;
	}
	
	protected Boolean hasAdjustmentGreaterThanZero() {
		return Boolean.TRUE;
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
	
	protected Boolean hasActualAtLegislativeActDate() {
		return null;
	}
}