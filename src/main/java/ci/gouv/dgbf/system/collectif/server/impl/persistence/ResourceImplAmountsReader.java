package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class ResourceImplAmountsReader extends AbstractResourceImplAmountsReader implements Serializable {

	@Override
	protected Boolean hasIncludedMovement() {
		return Boolean.TRUE;
	}
	
	@Override
	protected Boolean hasAvailable() {
		return Boolean.TRUE;
	}
}