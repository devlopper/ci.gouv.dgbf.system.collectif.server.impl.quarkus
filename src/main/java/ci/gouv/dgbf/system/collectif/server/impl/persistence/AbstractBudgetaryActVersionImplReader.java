package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractBudgetaryActVersionImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<BudgetaryActVersionImpl> {

	@Override
	protected Class<BudgetaryActVersionImpl> getEntityClass() {
		return BudgetaryActVersionImpl.class;
	}
}