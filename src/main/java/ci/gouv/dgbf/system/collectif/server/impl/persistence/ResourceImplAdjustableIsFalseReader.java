package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ResourceImplAdjustableIsFalseReader extends AbstractResourceImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(String.format("JOIN %s lav ON lav = t.%s AND lav.%s IS NOT TRUE", LegislativeActVersionImpl.ENTITY_NAME,ResourceImpl.FIELD_ACT_VERSION,LegislativeActVersionImpl.FIELD_ADJUSTABLE));
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ResourceImpl.FIELD_IDENTIFIER).addFromTuple("lav",LegislativeActVersionImpl.FIELD_ADJUSTABLE);
		return arguments;
	}
}