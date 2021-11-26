package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.EntryAuthorizationDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureDto;
import ci.gouv.dgbf.system.collectif.server.api.service.PaymentCreditDto;
import io.quarkus.arc.Unremovable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @NoArgsConstructor @RequestScoped @Unremovable
public class ExpenditureDtoImpl extends AbstractIdentifiableSystemScalarStringImpl implements ExpenditureDto,Serializable {

	@JsonbProperty(value = JSON_ENTRY_AUTHORIZATION) EntryAuthorizationDtoImpl entryAuthorization;	
	@JsonbProperty(value = JSON_PAYMENT_CREDIT) PaymentCreditDtoImpl paymentCredit;	
	@JsonbProperty(value = JSON_BUDGETARY_ACT_AS_STRING) String budgetaryActAsString;
	@JsonbProperty(value = JSON_BUDGETARY_ACT_VERSION_AS_STRING) String budgetaryActVersionAsString;
	@JsonbProperty(value = JSON_SECTION_AS_STRING) String sectionAsString;
	@JsonbProperty(value = JSON_NATURE_AS_STRING) String natureAsString;
	@JsonbProperty(value = JSON_BUDGET_SPECIALIZATION_UNIT_AS_STRING) String budgetSpecializationUnitAsString;
	@JsonbProperty(value = JSON_ACTION_AS_STRING) String actionAsString;
	@JsonbProperty(value = JSON_ACTIVITY_AS_STRING) String activityAsString;
	@JsonbProperty(value = JSON_ECONOMIC_NATURE_AS_STRING) String economicNatureAsString;
	@JsonbProperty(value = JSON_FUNDING_SOURCE_AS_STRING) String fundingSourceAsString;
	@JsonbProperty(value = JSON_LESSOR_AS_STRING) String lessorAsString;

	@Override @JsonbProperty(ExpenditureDto.JSON_IDENTIFIER)
	public ExpenditureDtoImpl setIdentifier(String identifier) {
		return (ExpenditureDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(ExpenditureDto.JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override
	public ExpenditureDto setEntryAuthorization(EntryAuthorizationDto entryAuthorization) {
		this.entryAuthorization = (EntryAuthorizationDtoImpl) entryAuthorization;
		return this;
	}
	
	@Override
	public ExpenditureDto setPaymentCredit(PaymentCreditDto paymentCredit) {
		this.paymentCredit = (PaymentCreditDtoImpl) paymentCredit;
		return this;
	}
	
}