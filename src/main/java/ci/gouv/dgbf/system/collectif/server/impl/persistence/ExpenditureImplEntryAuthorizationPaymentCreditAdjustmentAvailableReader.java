package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ExpenditureImpl.FIELD_IDENTIFIER,FieldHelper.join(ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION,EntryAuthorizationImpl.FIELD_ADJUSTMENT)
				,FieldHelper.join(ExpenditureImpl.FIELD_PAYMENT_CREDIT,EntryAuthorizationImpl.FIELD_ADJUSTMENT));
		
		arguments.getTuple().addJoins(String.format("LEFT JOIN %1$s p ON p.%2$s = t.%2$s",ExpenditureAvailableView.ENTITY_NAME,ExpenditureAvailableView.FIELD_IDENTIFIER));
		arguments.getProjection(Boolean.TRUE).addFromTuple("p",ExpenditureAvailableView.FIELD_ENTRY_AUTHORIZATION,ExpenditureAvailableView.FIELD_PAYMENT_CREDIT);
		return arguments;
	}
	
	public static final Integer ENTRY_AUTHORIZATION_AVAILABLE_INDEX = 3;
	public static final Integer PAYMENT_CREDIT_AVAILABLE_INDEX = 4;
}