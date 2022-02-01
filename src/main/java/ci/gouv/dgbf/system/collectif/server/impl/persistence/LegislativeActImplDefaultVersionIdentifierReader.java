package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActImplDefaultVersionIdentifierReader extends AbstractLegislativeActImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActImpl.FIELD_IDENTIFIER);
		arguments.getProjection(Boolean.TRUE).addFromTuple("lav",LegislativeActVersionImpl.FIELD_IDENTIFIER);
		arguments.getTuple(Boolean.TRUE).addJoins("LEFT JOIN LegislativeActVersionImpl lav ON lav = t.defaultVersion");
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array) {
		Integer index = 0;
		legislativeAct.setIdentifier(getAsString(array, index++));
		legislativeAct.setDefaultVersionIdentifier(getAsString(array, index++));
		__set__(legislativeAct, array, index);
	}
	
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array,Integer index) {}
}