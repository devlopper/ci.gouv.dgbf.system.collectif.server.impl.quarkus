package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractActivityImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<ActivityImpl> {

	@Override
	protected Class<ActivityImpl> getEntityClass() {
		return ActivityImpl.class;
	}
}