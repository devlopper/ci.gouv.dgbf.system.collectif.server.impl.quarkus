package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplAmountsInitialReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().addJoins(String.format("LEFT JOIN %1$s a ON a.%2$s = t.%2$s",ExpenditureView.ENTITY_NAME,ExpenditureView.FIELD_IDENTIFIER));
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ExpenditureImpl.FIELD_IDENTIFIER);
		for(String fieldName : ENTRY_AUTHORIZATION_PAYMENT_CREDIT)
			arguments.getProjection().addFromTuple("a",FieldHelper.join(fieldName,AbstractAmountsView.FIELD_INITIAL));
		return arguments;
	}
	
	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		Integer index = 1;
		for(AbstractExpenditureAmountsImpl amounts : new AbstractExpenditureAmountsImpl[] {expenditure.getEntryAuthorization(Boolean.TRUE),expenditure.getPaymentCredit(Boolean.TRUE)})
			amounts.setInitial(getAsLong(array, index++));
	}
}