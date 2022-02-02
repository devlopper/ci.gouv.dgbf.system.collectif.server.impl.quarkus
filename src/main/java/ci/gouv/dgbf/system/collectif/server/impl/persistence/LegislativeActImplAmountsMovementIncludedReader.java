package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActImplAmountsMovementIncludedReader extends AbstractLegislativeActImplReader implements Serializable {

	protected static final String[] AMOUNTS = new String[] {RegulatoryActExpenditureImpl.FIELD_ENTRY_AUTHORIZATION_AMOUNT,RegulatoryActExpenditureImpl.FIELD_PAYMENT_CREDIT_AMOUNT};
	
	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().addJoins(String.format("JOIN %s lav ON lav = t.%s",LegislativeActVersionImpl.ENTITY_NAME,LegislativeActImpl.FIELD_DEFAULT_VERSION));
		arguments.getTuple().addJoins(String.format("JOIN %s ex ON ex.%s = lav",ExpenditureImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION));
		arguments.getTuple().addJoins(String.format("JOIN %s e ON e.%s = t.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
		arguments.getTuple().addJoins(String.format("JOIN %1$s rae ON rae.%2$s = ex.%2$s AND rae.%3$s = ex.%3$s AND rae.%4$s = ex.%4$s AND rae.%5$s = ex.%5$s AND rae.%6$s = e.%6$s",RegulatoryActExpenditureImpl.ENTITY_NAME
				,RegulatoryActExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER
				,RegulatoryActExpenditureImpl.FIELD_LESSOR_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_YEAR));
		arguments.getTuple().addJoins(String.format("JOIN %s ra ON ra.%s = rae.%s",RegulatoryActImpl.ENTITY_NAME,RegulatoryActImpl.FIELD_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ACT_IDENTIFIER));
		arguments.getTuple().addJoins(String.format("JOIN %s ralav ON ralav.%s = ra.%s AND ralav.%s = lav.%s AND ralav.%s = TRUE",RegulatoryActLegislativeActVersionImpl.ENTITY_NAME,RegulatoryActLegislativeActVersionImpl.FIELD_REGULATORY_ACT
				,RegulatoryActImpl.FIELD_IDENTIFIER,RegulatoryActLegislativeActVersionImpl.FIELD_LEGISLATIVE_ACT_VERSION,LegislativeActVersionImpl.FIELD_IDENTIFIER,RegulatoryActLegislativeActVersionImpl.FIELD_INCLUDED));
		
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActImpl.FIELD_IDENTIFIER);
		for(String field : AMOUNTS)
			arguments.getProjection(Boolean.TRUE).add(String.format("SUM(rae.%s)",field));
		arguments.getGroup(Boolean.TRUE).add("t.identifier");
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array) {
		Integer index = 1;
		legislativeAct.getEntryAuthorization(Boolean.TRUE).setMovementIncluded(ValueHelper.defaultToIfNull(getAsLong(array, index++),0l));
		legislativeAct.getPaymentCredit(Boolean.TRUE).setMovementIncluded(ValueHelper.defaultToIfNull(getAsLong(array, index++),0l));
	}
	
	@Override
	protected void processWhenHasNoEntityArray(LegislativeActImpl legislativeAct) {
		super.processWhenHasNoEntityArray(legislativeAct);
		legislativeAct.getEntryAuthorization(Boolean.TRUE).setMovementIncluded(0l);
		legislativeAct.getPaymentCredit(Boolean.TRUE).setMovementIncluded(0l);
	}
}