package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.RegulatoryActDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class RegulatoryActDtoImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements RegulatoryActDto,Serializable {

	@JsonbProperty(value = JSON_YEAR) Short year;
	@JsonbProperty(value = JSON_ENTRY_AUTHORIZATION_AMOUNT) Long entryAuthorizationAmount;
	@JsonbProperty(value = JSON_PAYMENT_CREDIT_AMOUNT) Long paymentCreditAmount;
	@JsonbProperty(value = JSON_INCLUDED) String included;
	@JsonbProperty(value = JSON_INCLUDED_AS_STRING) String includedAsString;
	@JsonbProperty(value = JSON___AUDIT__) String __audit__;
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public RegulatoryActDtoImpl setIdentifier(String identifier) {
		return (RegulatoryActDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public RegulatoryActDtoImpl setCode(String code) {
		return (RegulatoryActDtoImpl) super.setCode(code);
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public String getCode() {
		return super.getCode();
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public RegulatoryActDtoImpl setName(String name) {
		return (RegulatoryActDtoImpl) super.setName(name);
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public String getName() {
		return super.getName();
	}

	static {
		Map<String,String> map = new HashMap<>();
		map.putAll(Map.of(
				JSON_IDENTIFIER,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_IDENTIFIER
    			,JSON_CODE,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_CODE
    			,JSON_NAME,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_NAME
    			,JSON_YEAR,RegulatoryActImpl.FIELD_YEAR
    			,JSON_ENTRY_AUTHORIZATION_AMOUNT,RegulatoryActImpl.FIELD_ENTRY_AUTHORIZATION_AMOUNT
    			,JSON_PAYMENT_CREDIT_AMOUNT,RegulatoryActImpl.FIELD_PAYMENT_CREDIT_AMOUNT
    			,JSON_INCLUDED,RegulatoryActImpl.FIELD_INCLUDED
    			,JSON_INCLUDED_AS_STRING,RegulatoryActImpl.FIELD_INCLUDED_AS_STRING
    			,JSONS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AS_STRING,RegulatoryActImpl.FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING
    			,JSON___AUDIT__,RegulatoryActImpl.FIELD___AUDIT__
    			));
		AbstractServiceImpl.setProjections(RegulatoryActDtoImpl.class, map);
	}
}