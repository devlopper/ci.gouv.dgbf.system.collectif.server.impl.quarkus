package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class AbstractLegislativeActImplAmountsReader extends AbstractLegislativeActImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		new LegislativeActQueryStringBuilder.Projection.ExpendituresAmounts().setView(hasView()).setIncludedMovement(hasIncludedMovement()).setAvailable(hasAvailable()).setActualAtLegislativeActDate(hasActualAtLegislativeActDate()).build(arguments);
		new LegislativeActQueryStringBuilder.Tuple.Amounts().setView(hasView()).setIncludedMovement(hasIncludedMovement()).setAvailable(hasAvailable()).setActualAtLegislativeActDate(hasActualAtLegislativeActDate()).build(arguments);
		arguments.getGroup(Boolean.TRUE).add("t.identifier");
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array) {
		LegislativeActQueryStringBuilder.Projection.ExpendituresAmounts.set(legislativeAct, array);
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
	
	protected Boolean hasActualAtLegislativeActDate() {
		return null;
	}
}