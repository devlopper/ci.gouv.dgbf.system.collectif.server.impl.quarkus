package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

import javax.persistence.Query;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;

public class RegulatoryActImplAuditsReader extends AbstractRegulatoryActImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",RegulatoryActImpl.FIELD_IDENTIFIER);
		arguments.getProjection(Boolean.TRUE).addFromTuple("p",RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHO__,RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_FUNCTIONALITY__
				,RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHAT__,RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHEN__);
		
		arguments.getTuple(Boolean.TRUE).addJoins(String.format("LEFT JOIN %s p ON p.%s = t AND p.legislativeActVersion.identifier = :%s",RegulatoryActLegislativeActVersionImpl.ENTITY_NAME
				,RegulatoryActLegislativeActVersionImpl.FIELD_REGULATORY_ACT,Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER));
		return arguments;
	}
	
	@Override
	protected void setQueryParameters(Query query, Collection<String> identifiers, Map<String, Object> parameters) {
		super.setQueryParameters(query, identifiers, parameters);
		String legislativeActVersionIdentifier = (String) MapHelper.readByKey(parameters, Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER);
		if(StringHelper.isBlank(legislativeActVersionIdentifier))
			throw new RuntimeException("La version du collectif est obligatoire");
		query.setParameter(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, legislativeActVersionIdentifier);
	}
	
	@Override
	protected void __set__(RegulatoryActImpl regulatoryAct, Object[] array) {
		Integer index = 1;
		regulatoryAct.set__auditWho__(getAsString(array, index++));
		regulatoryAct.set__auditFunctionality__(getAsString(array, index++));
		regulatoryAct.set__auditWhat__(getAsString(array, index++));
		regulatoryAct.set__auditWhen__((LocalDateTime) array[index++]);
	}
}