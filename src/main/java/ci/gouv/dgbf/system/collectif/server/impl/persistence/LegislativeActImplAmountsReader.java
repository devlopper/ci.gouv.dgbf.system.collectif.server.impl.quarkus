package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class LegislativeActImplAmountsReader extends AbstractLegislativeActImplAmountsReader implements Serializable {

	@Override
	protected Boolean hasIncludedMovement() {
		return Boolean.TRUE;
	}
	
	@Override
	protected Boolean hasAvailable() {
		return Boolean.TRUE;
	}
	
	@Override
	protected Boolean hasActualAtLegislativeActDate() {
		return Boolean.TRUE;
	}
}