package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.time.LocalDate;

import org.cyk.utility.__kernel__.time.TimeHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActImplFromDateAsTimestampReader extends AbstractLegislativeActImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(String.format("LEFT JOIN %1$s previous ON previous.%2$s = t.%2$s-1", LegislativeActImpl.ENTITY_NAME,LegislativeActImpl.FIELD_NUMBER));
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActImpl.FIELD_IDENTIFIER).addFromTuple("previous",LegislativeActImpl.FIELD_DATE);
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array) {
		Integer index = 0;
		legislativeAct.setIdentifier(getAsString(array, index++));
		legislativeAct.setFromDateAsTimestamp(TimeHelper.toMillisecond(getAsLocalDate(array, index++)));
	}
	
	public static LocalDate buildDate(LocalDate date,Short year) {
		if(date == null && year != null)
			date = LocalDate.of(year.intValue(), 1, 1);
		return date;
	}
}