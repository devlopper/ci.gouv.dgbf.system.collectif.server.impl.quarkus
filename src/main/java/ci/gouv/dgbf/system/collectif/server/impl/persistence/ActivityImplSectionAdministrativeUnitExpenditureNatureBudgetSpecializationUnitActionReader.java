package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ActivityImplSectionAdministrativeUnitExpenditureNatureBudgetSpecializationUnitActionReader extends AbstractActivityImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ActivityImpl.FIELD_IDENTIFIER
				,ActivityImpl.FIELD_SECTION_IDENTIFIER,ActivityImpl.FIELD_SECTION_CODE,ActivityImpl.FIELD_SECTION_NAME
				,ActivityImpl.FIELD_ADMINISTRATIVE_UNIT_IDENTIFIER,ActivityImpl.FIELD_ADMINISTRATIVE_UNIT_CODE,ActivityImpl.FIELD_ADMINISTRATIVE_UNIT_NAME
				,ActivityImpl.FIELD_EXPENDITURE_NATURE_IDENTIFIER,ActivityImpl.FIELD_EXPENDITURE_NATURE_CODE,ActivityImpl.FIELD_EXPENDITURE_NATURE_NAME								
				,ActivityImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,ActivityImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE,ActivityImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_NAME
				,ActivityImpl.FIELD_ACTION_IDENTIFIER,ActivityImpl.FIELD_ACTION_CODE,ActivityImpl.FIELD_ACTION_NAME
				);		
		return arguments;
	}
	
	@Override
	protected void __set__(ActivityImpl activity, Object[] array) {
		Integer index = 1;
		activity.setSection(new SectionImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
		activity.setAdministrativeUnit(new AdministrativeUnitImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
		activity.setExpenditureNature(new ExpenditureNatureImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));				
		activity.setBudgetSpecializationUnit(new BudgetSpecializationUnitImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
		activity.setAction(new ActionImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
	}
}