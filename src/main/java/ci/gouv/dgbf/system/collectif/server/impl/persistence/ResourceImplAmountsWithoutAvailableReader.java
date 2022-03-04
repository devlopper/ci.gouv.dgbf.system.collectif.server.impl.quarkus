package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class ResourceImplAmountsWithoutAvailableReader extends AbstractResourceImplAmountsReader implements Serializable {

	@Override
	protected Boolean hasIncludedMovement() {
		return Boolean.TRUE;
	}
}