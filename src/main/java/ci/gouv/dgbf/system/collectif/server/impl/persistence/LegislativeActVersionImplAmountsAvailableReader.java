package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.persistence.query.Language;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActVersionImplAmountsAvailableReader extends AbstractLegislativeActVersionImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple().addJoins(
				String.format("JOIN %s la ON la = t.%s",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT)
				,String.format("JOIN %s e ON e.%s = t",ExpenditureImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION)				
				,String.format("LEFT JOIN %1$s available ON available.%2$s = e.%2$s",ExpenditureAvailableView.ENTITY_NAME,ExpenditureAvailableView.FIELD_IDENTIFIER)
			);
		
		arguments.getGroup(Boolean.TRUE).add(FieldHelper.join("t",LegislativeActVersionImpl.FIELD_IDENTIFIER));
		
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActVersionImpl.FIELD_IDENTIFIER);
		for(String fieldName : AbstractExpenditureImplReader.ENTRY_AUTHORIZATION_PAYMENT_CREDIT)
			arguments.getProjection().add(Language.formatSum(FieldHelper.join("available",fieldName)));
		return arguments;
	}
	
	@Override
	protected void __set__(LegislativeActVersionImpl legislativeActVersion, Object[] array) {
		Integer index = 1;
		legislativeActVersion.getEntryAuthorization(Boolean.TRUE).setAvailable(getAsLong(array, index++));
		legislativeActVersion.getPaymentCredit(Boolean.TRUE).setAvailable(getAsLong(array, index++));
	}
	
	@Override
	protected void processWhenHasNoEntityArray(LegislativeActVersionImpl legislativeActVersion) {
		super.processWhenHasNoEntityArray(legislativeActVersion);
		legislativeActVersion.getEntryAuthorization(Boolean.TRUE).setAvailable(0l);
		legislativeActVersion.getPaymentCredit(Boolean.TRUE).setAvailable(0l);
	}
}