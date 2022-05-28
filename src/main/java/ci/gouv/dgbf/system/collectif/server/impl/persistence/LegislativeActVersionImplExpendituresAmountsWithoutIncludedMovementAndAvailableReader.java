package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class LegislativeActVersionImplExpendituresAmountsWithoutIncludedMovementAndAvailableReader extends AbstractLegislativeActVersionImplExpendituresAmountsReader implements Serializable {

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
		return Boolean.TRUE;
	}
}