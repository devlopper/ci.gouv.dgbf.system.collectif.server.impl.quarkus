package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplAmountsReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		ExpenditureQueryStringBuilder.Projection.projectAmounts(arguments,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
		ExpenditureQueryStringBuilder.Tuple.joinAmounts(arguments,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
		return arguments;
	}
	
	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		ExpenditureQueryStringBuilder.Projection.setAmounts(expenditure, array,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
	}
}