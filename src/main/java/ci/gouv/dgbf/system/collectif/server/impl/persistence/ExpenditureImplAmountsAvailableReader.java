package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplAmountsAvailableReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().addJoins(String.format("LEFT JOIN %1$s a ON a.%2$s = t.%2$s",ExpenditureAvailableView.ENTITY_NAME,ExpenditureAvailableView.FIELD_IDENTIFIER));
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ExpenditureImpl.FIELD_IDENTIFIER);
		arguments.getProjection().addFromTuple("a",ExpenditureAvailableView.FIELD_ENTRY_AUTHORIZATION,ExpenditureAvailableView.FIELD_PAYMENT_CREDIT);
		return arguments;
	}
	
	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		Integer index = 1;
		for(AbstractExpenditureAmountsImpl amounts : new AbstractExpenditureAmountsImpl[] {expenditure.getEntryAuthorization(Boolean.TRUE),expenditure.getPaymentCredit(Boolean.TRUE)})
			amounts.setAvailable(getAsLong(array, index++));
	}
}