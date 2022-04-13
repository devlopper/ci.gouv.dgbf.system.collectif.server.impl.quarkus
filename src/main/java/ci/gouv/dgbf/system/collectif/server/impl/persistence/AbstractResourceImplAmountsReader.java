package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class AbstractResourceImplAmountsReader extends AbstractResourceImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		new ResourceQueryStringBuilder.Projection.Amounts().setView(hasView()).setIncludedMovement(hasIncludedMovement()).setAvailable(hasAvailable()).build(arguments);
		new ResourceQueryStringBuilder.Tuple.Amounts().setView(hasView()).setIncludedMovement(hasIncludedMovement()).setAvailable(hasAvailable()).build(arguments);
		return arguments;
	}
	
	@Override
	protected void __set__(ResourceImpl resource, Object[] array) {
		ResourceQueryStringBuilder.Projection.setAmounts(resource, array,hasView(),hasIncludedMovement(),hasAvailable());
	}
	
	protected Boolean hasView() {
		return Boolean.TRUE;
	}
	
	protected Boolean hasIncludedMovement() {
		return null;
	}
	
	protected Boolean hasAvailable() {
		return null;
	}
}