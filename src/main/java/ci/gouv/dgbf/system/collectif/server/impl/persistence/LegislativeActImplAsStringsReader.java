package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.time.TimeHelper;
import org.cyk.utility.persistence.server.Helper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActImplAsStringsReader extends AbstractLegislativeActImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(
				String.format("JOIN %s e ON e.%s = t.%s", ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER)
				,String.format("LEFT JOIN %s dv ON dv = t.%s", LegislativeActVersionImpl.ENTITY_NAME,LegislativeActImpl.FIELD_DEFAULT_VERSION)
				,String.format("LEFT JOIN %1$s previous ON previous.%2$s = t.%2$s-1", LegislativeActImpl.ENTITY_NAME,LegislativeActImpl.FIELD_NUMBER)
				);
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_CODE,LegislativeActImpl.FIELD_NAME
				,LegislativeActImpl.FIELD_NUMBER,LegislativeActImpl.FIELD_IN_PROGRESS,LegislativeActImpl.FIELD_DATE);
		arguments.getProjection(Boolean.TRUE).addFromTuple("e",ExerciseImpl.FIELD_YEAR).addFromTuple("dv",LegislativeActVersionImpl.FIELD_NUMBER).addFromTuple("previous", LegislativeActImpl.FIELD_DATE);
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array) {
		Integer index = 0;
		legislativeAct.setIdentifier(getAsString(array, index++));
		legislativeAct.setCode(getAsString(array, index++));
		legislativeAct.setName(getAsString(array, index++));
		legislativeAct.setNumber(getAsByte(array, index++));
		legislativeAct.setInProgressAsString(Helper.ifTrueYesElseNo(getAsBoolean(array, index++)));
		legislativeAct.setDateAsString(TimeHelper.formatLocalDate(getAsLocalDate(array, index++)));
		Short year = getAsShort(array, index++);
		legislativeAct.setExerciseAsString(StringHelper.get(year));
		legislativeAct.setDefaultVersionAsString(StringHelper.get(getAsByte(array, index++)));
		legislativeAct.setFromDateAsString(TimeHelper.formatLocalDate(LegislativeActImplFromDateAsTimestampReader.buildDate(getAsLocalDate(array, index++), year)));
	}
	
	
}