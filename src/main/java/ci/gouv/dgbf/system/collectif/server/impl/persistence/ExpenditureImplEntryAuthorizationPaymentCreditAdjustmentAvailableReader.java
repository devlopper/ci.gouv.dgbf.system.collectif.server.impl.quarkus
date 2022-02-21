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
		
		arguments.getTuple().addJoins(
				String.format("JOIN %1$s lav ON lav = t.%2$s",LegislativeActVersionImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION)
				,String.format("JOIN %1$s la ON la = lav.%2$s",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT)
				,String.format("LEFT JOIN %1$s exercise ON exercise.identifier = la.%2$s",ExerciseImpl.ENTITY_NAME,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER)
				
				,String.format("LEFT JOIN %1$s a ON a.%2$s = exercise.%2$s AND a.%3$s = t.%3$s AND a.%4$s = t.%4$s AND a.%5$s = t.%5$s AND a.%6$s = t.%6$s"
				,ExpenditureAvailableView.ENTITY_NAME,ExpenditureAvailableView.FIELD_YEAR
				,ExpenditureAvailableView.FIELD_ACTIVITY_IDENTIFIER,ExpenditureAvailableView.FIELD_ECONOMIC_NATURE_IDENTIFIER,ExpenditureAvailableView.FIELD_FUNDING_SOURCE_IDENTIFIER,ExpenditureAvailableView.FIELD_LESSOR_IDENTIFIER)
				
				);
		arguments.getProjection(Boolean.TRUE).addFromTuple("a",ExpenditureAvailableView.FIELD_ENTRY_AUTHORIZATION,ExpenditureAvailableView.FIELD_PAYMENT_CREDIT);
		return arguments;
	}
	
	public static final Integer ENTRY_AUTHORIZATION_AVAILABLE_INDEX = 3;
	public static final Integer PAYMENT_CREDIT_AVAILABLE_INDEX = 4;
}