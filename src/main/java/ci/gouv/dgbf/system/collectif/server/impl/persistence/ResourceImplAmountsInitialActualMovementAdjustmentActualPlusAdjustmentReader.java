package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ResourceImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentReader extends AbstractResourceImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().addJoins(String.format("LEFT JOIN %1$s a1 ON a1.%2$s = t.%2$s",ResourceView.ENTITY_NAME,ResourceView.FIELD_IDENTIFIER));
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ResourceImpl.FIELD_IDENTIFIER);
		arguments.getProjection().addFromTuple("a1.revenue",AbstractAmountsView.FIELD_INITIAL,AbstractAmountsView.FIELD_ACTUAL);
		arguments.getProjection().addFromTuple("t.revenue",AbstractAmountsImpl.FIELD_ADJUSTMENT);
		return arguments;
	}
	
	@Override
	protected void __set__(ResourceImpl expenditure, Object[] array) {
		Integer index = 1;		
		expenditure.getRevenue(Boolean.TRUE).setInitial(ValueHelper.defaultToIfNull(getAsLong(array, index++),0l));
		expenditure.getRevenue(Boolean.TRUE).setActual(ValueHelper.defaultToIfNull(getAsLong(array, index++),0l));
		expenditure.getRevenue(Boolean.TRUE).computeMovement();
		expenditure.getRevenue(Boolean.TRUE).setAdjustment(ValueHelper.defaultToIfNull(getAsLong(array, index++),0l));
		expenditure.getRevenue(Boolean.TRUE).computeActualPlusAdjustment();
	}
	
	@Override
	protected void processWhenHasNoEntityArray(ResourceImpl expenditure) {
		super.processWhenHasNoEntityArray(expenditure);
		expenditure.getRevenue(Boolean.TRUE).setInitial(0l);
		expenditure.getRevenue(Boolean.TRUE).setActual(0l);
		expenditure.getRevenue(Boolean.TRUE).setMovement(0l);
		expenditure.getRevenue(Boolean.TRUE).setAdjustment(0l);
		expenditure.getRevenue(Boolean.TRUE).setActualPlusAdjustment(0l);
	}
}