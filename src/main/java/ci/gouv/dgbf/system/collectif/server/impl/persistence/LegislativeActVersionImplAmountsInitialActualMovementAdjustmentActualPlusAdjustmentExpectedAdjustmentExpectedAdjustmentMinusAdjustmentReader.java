package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.persistence.query.Language;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActVersionImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentExpectedAdjustmentExpectedAdjustmentMinusAdjustmentReader extends AbstractLegislativeActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().addJoins(
				String.format("JOIN %s la ON la = t.%s",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT)
				,String.format("JOIN %s e ON e.%s = t",ExpenditureImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION)				
				,String.format("JOIN %1$s ev ON ev.%2$s = e.%2$s",ExpenditureView.ENTITY_NAME,ExpenditureView.FIELD_IDENTIFIER)
				//,String.format("LEFT JOIN %1$s available ON available.%2$s = e.%2$s",ExpenditureAvailableView.ENTITY_NAME,ExpenditureAvailableView.FIELD_IDENTIFIER)
			);
		
		arguments.getGroup(Boolean.TRUE).add(FieldHelper.join("t",LegislativeActVersionImpl.FIELD_IDENTIFIER));
		
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActVersionImpl.FIELD_IDENTIFIER);
		for(String fieldName : AbstractExpenditureImplReader.ENTRY_AUTHORIZATION_PAYMENT_CREDIT) {
			arguments.getProjection().add(Language.formatSum(FieldHelper.join("ev",fieldName,AbstractAmountsView.FIELD_INITIAL)),Language.formatSum(FieldHelper.join("ev",fieldName,AbstractAmountsView.FIELD_ACTUAL)));
			arguments.getProjection().add(Language.formatSum(FieldHelper.join("e",fieldName,AbstractAmountsImpl.FIELD_ADJUSTMENT)))/*.add(Language.formatSum(FieldHelper.join("available",fieldName)))*/;
			arguments.getProjection().addFromTuple("la", FieldHelper.join(fieldName,AbstractAmountsImpl.FIELD_ADJUSTMENT));
			arguments.getGroup(Boolean.TRUE).add(FieldHelper.join("la",fieldName,AbstractAmountsImpl.FIELD_ADJUSTMENT));
		}
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActVersionImpl legislativeActVersion, Object[] array) {
		legislativeActVersion.copy(array,1,Boolean.FALSE,Boolean.TRUE);
	}
	
	@Override
	protected void processWhenHasNoEntityArray(LegislativeActVersionImpl legislativeActVersion) {
		super.processWhenHasNoEntityArray(legislativeActVersion);
		legislativeActVersion.setZeroEntryAuthorizationPaymentCredit();
	}
}