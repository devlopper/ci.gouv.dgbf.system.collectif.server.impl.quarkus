package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractRegulatoryActImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<RegulatoryActImpl> {

	@Override
	protected Class<RegulatoryActImpl> getEntityClass() {
		return RegulatoryActImpl.class;
	}
}