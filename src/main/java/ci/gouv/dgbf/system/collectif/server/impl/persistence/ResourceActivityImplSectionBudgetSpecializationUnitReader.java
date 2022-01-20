package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ResourceActivityImplSectionBudgetSpecializationUnitReader extends AbstractResourceActivityImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ResourceActivityImpl.FIELD_IDENTIFIER
				,ResourceActivityImpl.FIELD_SECTION_IDENTIFIER,ResourceActivityImpl.FIELD_SECTION_CODE,ResourceActivityImpl.FIELD_SECTION_NAME
				,ResourceActivityImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,ResourceActivityImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE,ResourceActivityImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_NAME
				);		
		return arguments;
	}
	
	@Override
	protected void __set__(ResourceActivityImpl activity, Object[] array) {
		Integer index = 1;
		activity.setSection(new SectionImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
		activity.setBudgetSpecializationUnit(new BudgetSpecializationUnitImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
	}
}