package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.time.TimeHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActVersionImplLegislativeActFromDateAsTimestampReader extends AbstractLegislativeActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(String.format("JOIN %s la ON la = t.%s", LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT)
				,String.format("LEFT JOIN %1$s previous ON previous.%2$s = la.%2$s-1", LegislativeActImpl.ENTITY_NAME,LegislativeActImpl.FIELD_NUMBER));
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActVersionImpl.FIELD_IDENTIFIER).addFromTuple("previous",LegislativeActImpl.FIELD_DATE);		
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActVersionImpl actVersion, Object[] array) {
		Integer index = 0;
		actVersion.setIdentifier(getAsString(array, index++));
		actVersion.setActFromDateAsTimestamp(TimeHelper.toMillisecond(getAsLocalDate(array, index++)));
	}
}