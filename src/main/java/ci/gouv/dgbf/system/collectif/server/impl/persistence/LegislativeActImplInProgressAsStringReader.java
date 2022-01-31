package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.Helper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActImplInProgressAsStringReader extends AbstractLegislativeActImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_IN_PROGRESS);
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array) {
		Integer index = 0;
		legislativeAct.setIdentifier(getAsString(array, index++));
		legislativeAct.setInProgressAsString(Helper.ifTrueYesElseNo(getAsBoolean(array, index++)));
		__set__(legislativeAct, array, index);
	}
	
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array,Integer index) {}
}