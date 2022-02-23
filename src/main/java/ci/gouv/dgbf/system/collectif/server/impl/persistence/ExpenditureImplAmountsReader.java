package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplAmountsReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		ExpenditureQueryStringBuilder.Projection.projectAmounts(arguments);
		ExpenditureQueryStringBuilder.Join.joinRegulatoryActLegislativeActVersionAndAvailable(arguments);
		return arguments;
	}
	
	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		ExpenditureQueryStringBuilder.Projection.setAmounts(expenditure, array);
	}
}