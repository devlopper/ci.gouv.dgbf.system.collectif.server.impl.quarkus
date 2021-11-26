package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class BudgetaryActVersionImplBudgetaryActIdentifierReader extends AbstractBudgetaryActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().getJoin(Boolean.TRUE).add(
				String.format("JOIN %s b ON b = t.%s", BudgetaryActImpl.ENTITY_NAME,BudgetaryActVersionImpl.FIELD_BUDGETARY_ACT)
				);
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",BudgetaryActVersionImpl.FIELD_IDENTIFIER).addFromTuple("b",BudgetaryActImpl.FIELD_IDENTIFIER);		
		return arguments;
	}
	
	@Override
	protected void __set__(BudgetaryActVersionImpl budgetaryActVersion, Object[] array) {
		Integer index = 0;
		budgetaryActVersion.setIdentifier(getAsString(array, index++));
		budgetaryActVersion.setBudgetaryActIdentifier(getAsString(array, index++));
	}
}