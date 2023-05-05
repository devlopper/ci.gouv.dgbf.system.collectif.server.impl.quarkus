package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.persistence.Query;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.persistence.server.Helper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;

public class RegulatoryActImplYearNameEntryAuthAmntPntCditAmoIncAndIncAsStrReader extends AbstractRegulatoryActImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",RegulatoryActImpl.FIELD_IDENTIFIER,RegulatoryActImpl.FIELD_YEAR,RegulatoryActImpl.FIELD_NAME,RegulatoryActImpl.FIELD_ENTRY_AUTHORIZATION_AMOUNT,RegulatoryActImpl.FIELD_PAYMENT_CREDIT_AMOUNT);
		arguments.getProjection(Boolean.TRUE).addFromTuple("p",RegulatoryActLegislativeActVersionImpl.FIELD_INCLUDED);
		
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
		Integer index = 0;
		regulatoryAct.setIdentifier(getAsString(array, index++));
		regulatoryAct.setYear(getAsShort(array, index++));
		regulatoryAct.setName(getAsString(array, index++));
		regulatoryAct.setEntryAuthorizationAmount(getAsLong(array, index++));
		regulatoryAct.setPaymentCreditAmount(getAsLong(array, index++));
		regulatoryAct.setIncluded(getAsBoolean(array,index++));
		regulatoryAct.setIncludedAsString(Helper.ifTrueYesElseNo(regulatoryAct.getIncluded()));
	}
}