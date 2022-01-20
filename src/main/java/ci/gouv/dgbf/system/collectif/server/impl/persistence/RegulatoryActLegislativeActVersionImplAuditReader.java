package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class RegulatoryActLegislativeActVersionImplAuditReader extends AbstractRegulatoryActLegislativeActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHO__,RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_FUNCTIONALITY__
				,RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHAT__,RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHEN__);
		return arguments;
	}
	
}