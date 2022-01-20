package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplEntryAuthorizationAdjustmentAvailableReader extends ExpenditureImplEntryAuthorizationAdjustmentReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().addJoins(String.format("LEFT JOIN %1$s p ON p.%2$s = t.%2$s",ExpenditureAvailableView.ENTITY_NAME,ExpenditureAvailableView.FIELD_IDENTIFIER));
		arguments.getProjection(Boolean.TRUE).addFromTuple("p",ExpenditureAvailableView.FIELD_ENTRY_AUTHORIZATION);
		return arguments;
	}
	
	public static final Integer ENTRY_AUTHORIZATION_AVAILABLE_INDEX = 2;
}