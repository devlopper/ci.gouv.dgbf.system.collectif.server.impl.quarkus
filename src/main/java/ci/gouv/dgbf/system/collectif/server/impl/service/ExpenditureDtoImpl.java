package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringAuditedImpl;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.EntryAuthorizationDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureDto;
import ci.gouv.dgbf.system.collectif.server.api.service.PaymentCreditDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import io.quarkus.arc.Unremovable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @NoArgsConstructor @RequestScoped @Unremovable
public class ExpenditureDtoImpl extends AbstractIdentifiableSystemScalarStringAuditedImpl implements ExpenditureDto,Serializable {

	@JsonbProperty(value = JSON_ENTRY_AUTHORIZATION) EntryAuthorizationDtoImpl entryAuthorization;	
	@JsonbProperty(value = JSON_PAYMENT_CREDIT) PaymentCreditDtoImpl paymentCredit;	
	@JsonbProperty(value = JSON_BUDGETARY_ACT_AS_STRING) String actAsString;
	@JsonbProperty(value = JSON_BUDGETARY_ACT_VERSION_AS_STRING) String actVersionAsString;
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
	
	@Override @JsonbProperty(value = ExpenditureDto.JSON___AUDIT__)
	public ExpenditureDtoImpl set__audit__(String __audit__) {
		return (ExpenditureDtoImpl) super.set__audit__(__audit__);
	}
	
	@Override @JsonbProperty(value = ExpenditureDto.JSON___AUDIT__)
	public String get__audit__() {
		return super.get__audit__();
	}
	
	@Override @JsonbProperty(value = ExpenditureDto.JSON___AUDIT_FUNCTIONALITY__)
	public String get__auditFunctionality__() {
		return super.get__auditFunctionality__();
	}
	
	@Override @JsonbProperty(value = ExpenditureDto.JSON___AUDIT_WHAT__)
	public String get__auditWhat__() {
		return super.get__auditWhat__();
	}
	
	@Override @JsonbProperty(value = ExpenditureDto.JSON___AUDIT_WHO__)
	public String get__auditWho__() {
		return super.get__auditWho__();
	}
	
	@Override @JsonbProperty(value = ExpenditureDto.JSON___AUDIT_WHEN__)
	public String get__auditWhenAsString__() {
		return super.get__auditWhenAsString__();
	}
	
	static {
		Map<String,String> map = new HashMap<>();
		map.putAll(Map.of(
				JSON_IDENTIFIER,ExpenditureImpl.FIELD_IDENTIFIER
    			,JSON_ENTRY_AUTHORIZATION,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION
    			,JSON_PAYMENT_CREDIT,ExpenditureImpl.FIELD_PAYMENT_CREDIT
    			,JSON_NATURE_AS_STRING,ExpenditureImpl.FIELD_NATURE_AS_STRING
    			,JSON_SECTION_AS_STRING,ExpenditureImpl.FIELD_SECTION_AS_STRING
    			,JSON_BUDGET_SPECIALIZATION_UNIT_AS_STRING,ExpenditureImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING
    			,JSONS_STRINGS,ExpenditureImpl.FIELDS_STRINGS
    			,JSONS_AMOUTNS,ExpenditureImpl.FIELDS_AMOUNTS
    			,JSON___AUDIT__,ExpenditureImpl.FIELD___AUDIT__
    			));
		AbstractServiceImpl.setProjections(ExpenditureDtoImpl.class, map);
	}
}