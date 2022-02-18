package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActVersionImplIsDefaultVersionReader extends AbstractLegislativeActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(String.format("JOIN %s la ON la = t.%s", LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT)
				,String.format("LEFT JOIN %s dv ON dv = la.%s",LegislativeActVersionImpl.ENTITY_NAME,LegislativeActImpl.FIELD_DEFAULT_VERSION)
				);
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActVersionImpl.FIELD_IDENTIFIER);		
		arguments.getProjection(Boolean.TRUE).addFromTuple("dv",LegislativeActVersionImpl.FIELD_IDENTIFIER);
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActVersionImpl legislativeActVersion, Object[] array) {
		Integer index = 0;
		legislativeActVersion.setIdentifier(getAsString(array, index++));
		legislativeActVersion.setIsDefaultVersion(legislativeActVersion.getIdentifier().equals(getAsString(array, index++)));
	}
}