package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder.Arguments;

public class ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableLegislativeActVersionAdjustableWithCodesReader extends ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableLegislativeActVersionAdjustableReader implements Serializable {

	@Override
	protected Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().addJoins(String.format("JOIN %1$s v ON v = t",ExpenditureView.ENTITY_NAME));
		return arguments;
	}
	
	@Override
	void addProjectionAdjustable(Arguments arguments) {
		super.addProjectionAdjustable(arguments);
		arguments.getProjection(Boolean.TRUE).add(String.format("CONCAT(v.%s,v.%s,v.%s,v.%s)",ExpenditureView.FIELD_ACTIVITY_CODE,ExpenditureView.FIELD_ECONOMIC_NATURE_CODE,ExpenditureView.FIELD_FUNDING_SOURCE_CODE,ExpenditureView.FIELD_LESSOR_CODE));
	}
	
	public static final Integer CODES_INDEX = 6;
}