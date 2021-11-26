package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplAsStringsReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(
				String.format("JOIN %s bv ON bv = t.%s", BudgetaryActVersionImpl.ENTITY_NAME,ExpenditureImpl.FIELD_BUDGETARY_ACT_VERSION)
				,String.format("JOIN %s b ON b = bv.%s", BudgetaryActImpl.ENTITY_NAME,BudgetaryActVersionImpl.FIELD_BUDGETARY_ACT)
				,String.format("LEFT JOIN %s v ON v.%s = t.%s", ExpenditureView.ENTITY_NAME,ExpenditureView.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_IDENTIFIER)
				);
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ExpenditureImpl.FIELD_IDENTIFIER).addFromTuple("b",BudgetaryActImpl.FIELD_CODE)
		.addFromTuple("bv",BudgetaryActVersionImpl.FIELD_CODE);
		
		arguments.getProjection(Boolean.TRUE).addFromTuple("v",ExpenditureView.FIELD_NATURE_CODE,ExpenditureView.FIELD_SECTION_CODE
				,ExpenditureView.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE,ExpenditureView.FIELD_ACTION_CODE,ExpenditureView.FIELD_ACTIVITY_CODE
				,ExpenditureView.FIELD_ECONOMIC_NATURE_CODE,ExpenditureView.FIELD_FUNDING_SOURCE_CODE_NAME,ExpenditureView.FIELD_LESSOR_CODE_NAME);
		return arguments;
	}
	
	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		Integer index = 0;
		expenditure.setIdentifier(getAsString(array, index++));
		expenditure.setBudgetaryActAsString(getAsString(array, index++));
		expenditure.setBudgetaryActVersionAsString(getAsString(array, index++));
		expenditure.setNatureAsString(getAsString(array, index++));
		expenditure.setSectionAsString(getAsString(array, index++));
		expenditure.setBudgetSpecializationUnitAsString(getAsString(array, index++));
		expenditure.setActionAsString(getAsString(array, index++));
		expenditure.setActivityAsString(getAsString(array, index++));
		expenditure.setEconomicNatureAsString(getAsString(array, index++));
		expenditure.setFundingSourceAsString(getAsString(array, index++));
		expenditure.setLessorAsString(getAsString(array, index++));
	}
}