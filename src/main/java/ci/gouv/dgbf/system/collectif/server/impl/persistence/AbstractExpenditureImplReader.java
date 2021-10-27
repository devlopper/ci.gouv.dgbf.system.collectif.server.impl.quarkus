package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractExpenditureImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<ExpenditureImpl> {

	@Override
	protected Class<ExpenditureImpl> getEntityClass() {
		return ExpenditureImpl.class;
	}
}