package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import org.cyk.utility.persistence.server.query.ArraysReaderByIdentifiers;

public abstract class AbstractExpenditureImplReader extends ArraysReaderByIdentifiers.AbstractImpl.DefaultImpl<ExpenditureImpl> {

	protected static final String[] ENTRY_AUTHORIZATION_PAYMENT_CREDIT = new String[] {ExpenditureView.FIELD_ENTRY_AUTHORIZATION,ExpenditureView.FIELD_PAYMENT_CREDIT};
	
	@Override
	protected Class<ExpenditureImpl> getEntityClass() {
		return ExpenditureImpl.class;
	}
}