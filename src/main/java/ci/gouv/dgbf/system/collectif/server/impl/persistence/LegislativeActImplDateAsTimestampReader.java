package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.time.TimeHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActImplDateAsTimestampReader extends AbstractLegislativeActImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_DATE);		
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array) {
		Integer index = 0;
		legislativeAct.setIdentifier(getAsString(array, index++));
		legislativeAct.setDateAsTimestamp(TimeHelper.toMillisecond(getAsLocalDate(array, index++)));
	}
}