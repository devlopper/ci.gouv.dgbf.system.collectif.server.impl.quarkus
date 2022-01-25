package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.time.TimeHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActVersionImplCodeNameNumberCreationDateAsStringReader extends AbstractLegislativeActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_CODE,LegislativeActVersionImpl.FIELD_NAME,LegislativeActVersionImpl.FIELD_NUMBER
				,LegislativeActVersionImpl.FIELD_CREATION_DATE);		
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActVersionImpl legislativeActVersion, Object[] array) {
		Integer index = 0;
		legislativeActVersion.setIdentifier(getAsString(array, index++));
		legislativeActVersion.setCode(getAsString(array, index++));
		legislativeActVersion.setName(getAsString(array, index++));
		legislativeActVersion.setNumber(getAsByte(array, index++));
		legislativeActVersion.setCreationDateAsString(TimeHelper.formatLocalDateTime(getAsLocalDateTime(array, index++)));
		__set__(legislativeActVersion, array, index);
	}
	
	protected void __set__(LegislativeActVersionImpl legislativeActVersion, Object[] array,Integer index) {}
}