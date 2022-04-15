package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class LegislativeActVersionImplExpendituresAmountsWithAvailableOnlyReader extends AbstractLegislativeActVersionImplExpendituresAmountsReader implements Serializable {

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
		return Boolean.FALSE;
	}
	
	@Override
	protected Boolean hasAvailable() {
		return Boolean.TRUE;
	}
}