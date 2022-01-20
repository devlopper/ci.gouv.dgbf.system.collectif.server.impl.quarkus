package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ResourceImplAmountsMovementIncludedReader extends AbstractResourceImplReader implements Serializable {

	protected static final String[] AMOUNTS = new String[] {RegulatoryActResourceImpl.FIELD_REVENUE_AMOUNT};
	
	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().addJoins(String.format("JOIN %s lav ON lav.%s = t.%s",LegislativeActVersionImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_IDENTIFIER,ResourceImpl.FIELD_ACT_VERSION));
		arguments.getTuple().addJoins(String.format("JOIN %s la ON la.%s = lav.%s",LegislativeActImpl.ENTITY_NAME,LegislativeActImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_ACT));
		arguments.getTuple().addJoins(String.format("JOIN %1$s rae ON rae.%2$s = t.%2$s AND rae.%3$s = t.%3$s AND rae.%4$s = la.%4$s",RegulatoryActResourceImpl.ENTITY_NAME
				,RegulatoryActResourceImpl.FIELD_ACTIVITY_IDENTIFIER,RegulatoryActResourceImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER,RegulatoryActResourceImpl.FIELD_YEAR));
		arguments.getTuple().addJoins(String.format("JOIN %s ra ON ra.%s = rae.%s",RegulatoryActImpl.ENTITY_NAME,RegulatoryActImpl.FIELD_IDENTIFIER,RegulatoryActResourceImpl.FIELD_ACT_IDENTIFIER));
		arguments.getTuple().addJoins(String.format("JOIN %s ralav ON ralav.%s = ra.%s AND ralav.%s = lav.%s AND ralav.%s = TRUE",RegulatoryActLegislativeActVersionImpl.ENTITY_NAME,RegulatoryActLegislativeActVersionImpl.FIELD_REGULATORY_ACT
				,RegulatoryActImpl.FIELD_IDENTIFIER,RegulatoryActLegislativeActVersionImpl.FIELD_LEGISLATIVE_ACT_VERSION,LegislativeActVersionImpl.FIELD_IDENTIFIER,RegulatoryActLegislativeActVersionImpl.FIELD_INCLUDED));
		
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ResourceImpl.FIELD_IDENTIFIER);
		for(String field : AMOUNTS)
			arguments.getProjection(Boolean.TRUE).add(String.format("SUM(rae.%s)",field));
		arguments.getGroup(Boolean.TRUE).add("t.identifier");
		return arguments;
	}
	
	@Override
	protected void __set__(ResourceImpl resource, Object[] array) {
		Integer index = 1;
		resource.getRevenue(Boolean.TRUE).setMovementIncluded(ValueHelper.defaultToIfNull(getAsLong(array, index++),0l));
	}
	
	@Override
	protected void processWhenHasNoEntityArray(ResourceImpl resource) {
		super.processWhenHasNoEntityArray(resource);
		resource.getRevenue(Boolean.TRUE).setMovementIncluded(0l);
	}
}