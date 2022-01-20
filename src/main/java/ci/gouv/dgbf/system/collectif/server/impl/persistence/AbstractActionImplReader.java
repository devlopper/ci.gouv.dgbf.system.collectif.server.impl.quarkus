package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractActionImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<ActionImpl> {

	@Override
	protected Class<ActionImpl> getEntityClass() {
		return ActionImpl.class;
	}
}