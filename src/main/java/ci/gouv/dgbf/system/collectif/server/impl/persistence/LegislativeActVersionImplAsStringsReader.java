package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.Helper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActVersionImplAsStringsReader extends AbstractLegislativeActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(String.format("JOIN %s la ON la = t.%s", LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT)
				,String.format("LEFT JOIN %s dv ON dv = la.%s",LegislativeActVersionImpl.ENTITY_NAME,LegislativeActImpl.FIELD_DEFAULT_VERSION));
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_CODE,LegislativeActVersionImpl.FIELD_NAME,LegislativeActVersionImpl.FIELD_NUMBER
				,LegislativeActVersionImpl.FIELD_ADJUSTABLE);
		arguments.getProjection(Boolean.TRUE).addFromTuple("la",LegislativeActImpl.FIELD_NAME);
		arguments.getProjection(Boolean.TRUE).addFromTuple("dv",LegislativeActVersionImpl.FIELD_IDENTIFIER);
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActVersionImpl legislativeActVersion, Object[] array) {
		Integer index = 0;
		legislativeActVersion.setIdentifier(getAsString(array, index++));
		legislativeActVersion.setCode(getAsString(array, index++));
		legislativeActVersion.setName(getAsString(array, index++));
		legislativeActVersion.setNumber(getAsByte(array, index++));
		legislativeActVersion.setAdjustableAsString(Helper.ifTrueYesElseNo(getAsBoolean(array, index++)));
		legislativeActVersion.setActAsString(getAsString(array, index++));
		legislativeActVersion.setIsDefaultVersionAsString(Helper.ifTrueYesElseNo(legislativeActVersion.getIdentifier().equals(getAsString(array, index++))));
	}
}