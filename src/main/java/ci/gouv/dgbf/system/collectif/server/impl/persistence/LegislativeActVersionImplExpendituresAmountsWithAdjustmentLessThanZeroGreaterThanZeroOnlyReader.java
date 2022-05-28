package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class LegislativeActVersionImplExpendituresAmountsWithAdjustmentLessThanZeroGreaterThanZeroOnlyReader extends AbstractLegislativeActVersionImplExpendituresAmountsReader implements Serializable {

	@Override
	protected Boolean hasAdjustment() {
		return Boolean.FALSE;
	}
	
	@Override
	protected Boolean hasAdjustmentLessThanZero() {
		return Boolean.TRUE;
	}
	
	@Override
	protected Boolean hasAdjustmentGreaterThanZero() {
		return Boolean.TRUE;
	}
	
	@Override
	protected Boolean hasExpectedAdjustment() {
		return Boolean.FALSE;
	}
	
	@Override
	protected Boolean hasView() {
		return Boolean.TRUE;
	}
	
	@Override
	protected Boolean hasIncludedMovement() {
		return Boolean.FALSE;
	}
	
	@Override
	protected Boolean hasAvailable() {
		return Boolean.FALSE;
	}
	
	@Override
	protected Boolean hasActualAtLegislativeActDate() {
		return Boolean.FALSE;
	}
}