package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplAECPAdjustmentAvailableLegislativeActVersionAdjustableReader extends ExpenditureImplAECPAdjustmentAvailableReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		addProjectionAdjustable(arguments);
		return arguments;
	}
	
	void addProjectionAdjustable(QueryStringBuilder.Arguments arguments) {
		arguments.getProjection(Boolean.TRUE).addFromTuple("lav",LegislativeActVersionImpl.FIELD_ADJUSTABLE);
	}
	
	public static final Integer ADJUSTABLE_INDEX = 6;
}