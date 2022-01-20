package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ResourceImplAsStringsReader extends AbstractResourceImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(
				String.format("JOIN %s lav ON lav = t.%s", LegislativeActVersionImpl.ENTITY_NAME,ResourceImpl.FIELD_ACT_VERSION)
				,String.format("JOIN %s la ON la = lav.%s", LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT)
				,String.format("LEFT JOIN %s v ON v.%s = t.%s", ResourceView.ENTITY_NAME,ResourceView.FIELD_IDENTIFIER,ResourceImpl.FIELD_IDENTIFIER)
				);
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ResourceImpl.FIELD_IDENTIFIER).addFromTuple("la",LegislativeActImpl.FIELD_CODE).addFromTuple("lav",LegislativeActVersionImpl.FIELD_CODE);		
		arguments.getProjection(Boolean.TRUE).addFromTuple("v",ResourceView.FIELD_SECTION_CODE,ResourceView.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE,ResourceView.FIELD_ACTIVITY_CODE,ResourceView.FIELD_ECONOMIC_NATURE_CODE);
		return arguments;
	}
	
	@Override
	protected void __set__(ResourceImpl resource, Object[] array) {
		Integer index = 0;
		resource.setIdentifier(getAsString(array, index++));
		resource.setActAsString(getAsString(array, index++));
		resource.setActVersionAsString(getAsString(array, index++));
		resource.setSectionAsString(getAsString(array, index++));
		resource.setBudgetSpecializationUnitAsString(getAsString(array, index++));
		resource.setActivityAsString(getAsString(array, index++));
		resource.setEconomicNatureAsString(getAsString(array, index++));
	}
}