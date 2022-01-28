package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractLegislativeActImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<LegislativeActImpl> {

	@Override
	protected Class<LegislativeActImpl> getEntityClass() {
		return LegislativeActImpl.class;
	}
}