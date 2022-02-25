package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class ExpenditureImplAmountsWithoutAvailableReader extends AbstractExpenditureImplAmountsReader implements Serializable {

	@Override
	protected Boolean hasIncludedMovement() {
		return Boolean.TRUE;
	}
}