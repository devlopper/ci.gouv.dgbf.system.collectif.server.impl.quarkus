package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractResourceActivityImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<ResourceActivityImpl> {

	@Override
	protected Class<ResourceActivityImpl> getEntityClass() {
		return ResourceActivityImpl.class;
	}
}