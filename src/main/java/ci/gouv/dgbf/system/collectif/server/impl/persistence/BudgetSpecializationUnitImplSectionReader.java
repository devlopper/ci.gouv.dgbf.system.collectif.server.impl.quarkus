package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class BudgetSpecializationUnitImplSectionReader extends AbstractBudgetSpecializationUnitImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",BudgetSpecializationUnitImpl.FIELD_IDENTIFIER
				,BudgetSpecializationUnitImpl.FIELD_SECTION_IDENTIFIER,BudgetSpecializationUnitImpl.FIELD_SECTION_CODE,BudgetSpecializationUnitImpl.FIELD_SECTION_NAME
				);
		return arguments;
	}
	
	@Override
	protected void __set__(BudgetSpecializationUnitImpl budgetSpecializationUnit, Object[] array) {
		Integer index = 1;
		budgetSpecializationUnit.setSection(new SectionImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
	}
}