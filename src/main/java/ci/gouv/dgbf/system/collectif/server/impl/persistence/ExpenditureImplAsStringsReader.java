package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplAsStringsReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(
				String.format("JOIN %s bv ON bv = t.%s", LegislativeActVersionImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION)
				,String.format("JOIN %s b ON b = bv.%s", LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT)
				,String.format("LEFT JOIN %s v ON v.%s = t.%s", ExpenditureView.ENTITY_NAME,ExpenditureView.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_IDENTIFIER)
				);
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ExpenditureImpl.FIELD_IDENTIFIER).addFromTuple("b",LegislativeActImpl.FIELD_CODE)
		.addFromTuple("bv",LegislativeActVersionImpl.FIELD_CODE);
		
		arguments.getProjection(Boolean.TRUE).addFromTuple("v",ExpenditureView.FIELD_NATURE_CODE,ExpenditureView.FIELD_SECTION_CODE
				,ExpenditureView.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE,ExpenditureView.FIELD_ACTION_CODE,ExpenditureView.FIELD_ACTIVITY_CODE
				,ExpenditureView.FIELD_ECONOMIC_NATURE_CODE,ExpenditureView.FIELD_FUNDING_SOURCE_CODE_NAME,ExpenditureView.FIELD_LESSOR_CODE_NAME);
		return arguments;
	}
	
	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		Integer index = 0;
		expenditure.setIdentifier(getAsString(array, index++));
		expenditure.setActAsString(getAsString(array, index++));
		expenditure.setActVersionAsString(getAsString(array, index++));
		expenditure.setNatureAsString(getAsString(array, index++));
		expenditure.setSectionAsString(getAsString(array, index++));
		expenditure.setBudgetSpecializationUnitAsString(getAsString(array, index++));
		expenditure.setActionAsString(getAsString(array, index++));
		expenditure.setActivityAsString(getAsString(array, index++));
		expenditure.setEconomicNatureAsString(getAsString(array, index++));
		expenditure.setFundingSourceAsString(StringUtils.substringAfter(getAsString(array, index++)," "));
		expenditure.setLessorAsString(StringUtils.substringAfter(getAsString(array, index++)," "));
	}
}