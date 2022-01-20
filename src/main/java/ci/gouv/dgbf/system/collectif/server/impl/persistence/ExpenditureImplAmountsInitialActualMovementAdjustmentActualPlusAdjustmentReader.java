package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().addJoins(String.format("LEFT JOIN %1$s a1 ON a1.%2$s = t.%2$s",ExpenditureView.ENTITY_NAME,ExpenditureView.FIELD_IDENTIFIER)
				,String.format("LEFT JOIN %1$s a2 ON a2.%2$s = t.%2$s",ExpenditureAvailableView.ENTITY_NAME,ExpenditureAvailableView.FIELD_IDENTIFIER));
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ExpenditureImpl.FIELD_IDENTIFIER);
		for(String fieldName : ENTRY_AUTHORIZATION_PAYMENT_CREDIT) {
			arguments.getProjection().addFromTuple("a1",FieldHelper.join(fieldName,AbstractAmountsView.FIELD_INITIAL),FieldHelper.join(fieldName,AbstractAmountsView.FIELD_ACTUAL));
			arguments.getProjection().addFromTuple("t",FieldHelper.join(fieldName,AbstractAmountsImpl.FIELD_ADJUSTMENT));			
		}
		arguments.getProjection().addFromTuple("a2",ExpenditureAvailableView.FIELD_ENTRY_AUTHORIZATION,ExpenditureAvailableView.FIELD_PAYMENT_CREDIT);
		return arguments;
	}
	
	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		Integer index = 1;
		for(AbstractExpenditureAmountsImpl amounts : new AbstractExpenditureAmountsImpl[] {expenditure.getEntryAuthorization(Boolean.TRUE),expenditure.getPaymentCredit(Boolean.TRUE)}) {
			amounts.setInitial(ValueHelper.defaultToIfNull(getAsLong(array, index++),0l));
			amounts.setActual(ValueHelper.defaultToIfNull(getAsLong(array, index++),0l));
			amounts.computeMovement();
			amounts.setAdjustment(ValueHelper.defaultToIfNull(getAsLong(array, index++),0l));
			amounts.computeActualPlusAdjustment();
		}
		expenditure.getEntryAuthorization(Boolean.TRUE).setAvailable(getAsLong(array, index++));
		expenditure.getPaymentCredit(Boolean.TRUE).setAvailable(getAsLong(array, index++));
	}
	
	@Override
	protected void processWhenHasNoEntityArray(ExpenditureImpl expenditure) {
		super.processWhenHasNoEntityArray(expenditure);
		for(AbstractExpenditureAmountsImpl amounts : new AbstractExpenditureAmountsImpl[] {expenditure.getEntryAuthorization(Boolean.TRUE),expenditure.getPaymentCredit(Boolean.TRUE)}) {
			amounts.setInitial(0l);
			amounts.setActual(0l);
			amounts.setMovement(0l);
			amounts.setAdjustment(0l);
			amounts.setActualPlusAdjustment(0l);
			amounts.setAvailable(0l);
		}
	}
}