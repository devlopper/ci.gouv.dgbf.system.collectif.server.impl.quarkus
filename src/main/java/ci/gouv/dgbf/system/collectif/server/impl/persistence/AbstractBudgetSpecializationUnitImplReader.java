package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractBudgetSpecializationUnitImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<BudgetSpecializationUnitImpl> {

	@Override
	protected Class<BudgetSpecializationUnitImpl> getEntityClass() {
		return BudgetSpecializationUnitImpl.class;
	}
}