package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplAvailableMonitorableIsNotFalseReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(
				String.format("JOIN %s lav ON lav = t.%s", LegislativeActVersionImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION)
				,String.format("JOIN %1$s la ON la = lav.%2$s AND (la.%3$s IS NULL OR la.%3$s IS TRUE)", LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_AVAILABLE_MONITORABLE)
				);
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ExpenditureImpl.FIELD_IDENTIFIER).addFromTuple("la",LegislativeActImpl.FIELD_AVAILABLE_MONITORABLE);
		return arguments;
	}
}