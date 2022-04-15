package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class LegislativeActVersionImplExpendituresAmountsWithIncludedMovementOnlyReader extends AbstractLegislativeActVersionImplExpendituresAmountsReader implements Serializable {

	@Override
	protected Boolean hasAdjustment() {
		return Boolean.FALSE;
	}
	
	@Override
	protected Boolean hasExpectedAdjustment() {
		return Boolean.FALSE;
	}
	
	@Override
	protected Boolean hasView() {
		return Boolean.FALSE;
	}
	
	@Override
	protected Boolean hasIncludedMovement() {
		return Boolean.TRUE;
	}
	
	@Override
	protected Boolean hasAvailable() {
		return Boolean.FALSE;
	}
}