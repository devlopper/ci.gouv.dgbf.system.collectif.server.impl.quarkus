package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActVersionImplLegislativeActDateReader extends AbstractLegislativeActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(String.format("JOIN %s la ON la = t.%s", LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT));
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActVersionImpl.FIELD_IDENTIFIER).addFromTuple("la",LegislativeActImpl.FIELD_DATE);		
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActVersionImpl actVersion, Object[] array) {
		Integer index = 0;
		actVersion.setIdentifier(getAsString(array, index++));
		actVersion.setActDate(getAsLocalDate(array, index++));
	}
}