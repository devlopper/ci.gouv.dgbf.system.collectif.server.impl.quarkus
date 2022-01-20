package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ActionImplSectionBudgetSpecializationUnitReader extends AbstractActionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ActionImpl.FIELD_IDENTIFIER
				,ActionImpl.FIELD_SECTION_IDENTIFIER,ActionImpl.FIELD_SECTION_CODE,ActionImpl.FIELD_SECTION_NAME
				,ActionImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,ActionImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE,ActionImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_NAME
				);
		return arguments;
	}
	
	@Override
	protected void __set__(ActionImpl action, Object[] array) {
		Integer index = 1;
		action.setSection(new SectionImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
		action.setBudgetSpecializationUnit(new BudgetSpecializationUnitImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
	}
}