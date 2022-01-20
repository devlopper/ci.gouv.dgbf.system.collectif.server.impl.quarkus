package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractResourceImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<ResourceImpl> {

	@Override
	protected Class<ResourceImpl> getEntityClass() {
		return ResourceImpl.class;
	}
}