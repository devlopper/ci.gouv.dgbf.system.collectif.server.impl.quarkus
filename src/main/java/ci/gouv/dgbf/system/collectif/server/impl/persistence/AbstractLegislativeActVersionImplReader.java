package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractLegislativeActVersionImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<LegislativeActVersionImpl> {

	@Override
	protected Class<LegislativeActVersionImpl> getEntityClass() {
		return LegislativeActVersionImpl.class;
	}
}