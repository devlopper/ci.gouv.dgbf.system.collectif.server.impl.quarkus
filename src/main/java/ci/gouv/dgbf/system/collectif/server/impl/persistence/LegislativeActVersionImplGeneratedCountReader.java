package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActVersionImplGeneratedCountReader extends AbstractLegislativeActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(String.format("JOIN %s ga ON ga.%s = t", GeneratedActImpl.ENTITY_NAME,GeneratedActImpl.FIELD_LEGISLATIVE_ACT_VERSION));
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActVersionImpl.FIELD_IDENTIFIER).add("COUNT(ga.identifier)");		
		arguments.getGroup(Boolean.TRUE).add("t.identifier");
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActVersionImpl legislativeActVersion, Object[] array) {
		Integer index = 0;
		legislativeActVersion.setIdentifier(getAsString(array, index++));
		legislativeActVersion.setGeneratedActCount(NumberHelper.get(Short.class, getAsLong(array, index++)));
	}
}