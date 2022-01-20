package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractRegulatoryActLegislativeActVersionImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<RegulatoryActLegislativeActVersionImpl> {

	@Override
	protected Class<RegulatoryActLegislativeActVersionImpl> getEntityClass() {
		return RegulatoryActLegislativeActVersionImpl.class;
	}
}