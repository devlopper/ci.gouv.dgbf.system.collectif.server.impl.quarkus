package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.__kernel__.object.AbstractObject;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActDto;
import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActVersionDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class LegislativeActVersionDtoImpl extends AbstractObject implements LegislativeActVersionDto,Serializable {

	@JsonbProperty(value = JSON_IDENTIFIER) String identifier;
	@JsonbProperty(value = JSON_CODE) String code;
	@JsonbProperty(value = JSON_NAME) String name;
	@JsonbProperty(value = JSON_IS_DEFAULT_VERSION) Boolean isDefaultVersion;
	@JsonbProperty(value = JSON_IS_DEFAULT_VERSION_AS_STRING) String isDefaultVersionAsString;
	@JsonbProperty(value = JSON_LEGISLATIVE_ACT) LegislativeActDtoImpl act;
	@JsonbProperty(value = JSON_LEGISLATIVE_ACT_IDENTIFIER) String actIdentifier;
	@JsonbProperty(value = JSON_LEGISLATIVE_ACT_DATE_AS_TIMESTAMP) Long actDateAsTimestamp;
	@JsonbProperty(value = JSON_LEGISLATIVE_ACT_FROM_DATE_AS_TIMESTAMP) Long actFromDateAsTimestamp;
	@JsonbProperty(value = JSON_LEGISLATIVE_ACT_AS_STRING) String actAsString;
	@JsonbProperty(value = JSON_GENERATED_ACT_COUNT) Short generatedActCount;
	@JsonbProperty(value = JSON_ACT_GENERATABLE) Boolean actGeneratable;
	@JsonbProperty(value = JSON_GENERATED_ACT_DELETABLE) Boolean generatedActDeletable;
	@JsonbProperty(value = JSON_NUMBER) Byte number;
	@JsonbProperty(value = JSON_REVENUE) RevenueDtoImpl revenue;	
	@JsonbProperty(value = JSON_ENTRY_AUTHORIZATION) EntryAuthorizationDtoImpl entryAuthorization;	
	@JsonbProperty(value = JSON_PAYMENT_CREDIT) PaymentCreditDtoImpl paymentCredit;			
	@JsonbProperty(value = JSON___AUDIT__) String __audit__;	
	
	@Override @JsonbProperty(value = JSON_LEGISLATIVE_ACT)
	public LegislativeActVersionDto setAct(LegislativeActDto act) {
		this.act = (LegislativeActDtoImpl) act;
		return this;
	}
	
	static {
		Map<String,String> map = new HashMap<>();
		map.putAll(Map.of(
    			LegislativeActVersionDto.JSON_IDENTIFIER,LegislativeActVersionImpl.FIELD_IDENTIFIER
    			,LegislativeActVersionDto.JSON_CODE,LegislativeActVersionImpl.FIELD_CODE
    			,LegislativeActVersionDto.JSON_NAME,LegislativeActVersionImpl.FIELD_NAME
    			,LegislativeActVersionDto.JSON_NUMBER,LegislativeActVersionImpl.FIELD_NUMBER
    			,LegislativeActVersionDto.JSON_IS_DEFAULT_VERSION,LegislativeActVersionImpl.FIELD_IS_DEFAULT_VERSION
    			,LegislativeActVersionDto.JSON_IS_DEFAULT_VERSION_AS_STRING,LegislativeActVersionImpl.FIELD_IS_DEFAULT_VERSION_AS_STRING
    			,LegislativeActVersionDto.JSON_LEGISLATIVE_ACT,LegislativeActVersionImpl.FIELD_ACT
    			,LegislativeActVersionDto.JSON_LEGISLATIVE_ACT_IDENTIFIER,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER
    			,LegislativeActVersionDto.JSON_LEGISLATIVE_ACT_DATE_AS_TIMESTAMP,LegislativeActVersionImpl.FIELD_ACT_DATE_AS_TIMESTAMP
    			,LegislativeActVersionDto.JSON_GENERATED_ACT_COUNT,LegislativeActVersionImpl.FIELD_GENERATED_ACT_COUNT
    			));
		map.putAll(Map.of(
    			LegislativeActVersionDto.JSONS_STRINGS,LegislativeActVersionImpl.FIELDS_STRINGS
    			,LegislativeActVersionDto.JSONS_AMOUTNS,LegislativeActVersionImpl.FIELDS_AMOUNTS
    			,LegislativeActVersionDto.JSONS_EXPENDITURES_AMOUTNS,LegislativeActVersionImpl.FIELDS_EXPENDITURES_AMOUNTS
    			,LegislativeActVersionDto.JSONS_EXPENDITURES_AMOUTNS_WITH_AVAILABLE_ONLY,LegislativeActVersionImpl.FIELDS_EXPENDITURES_AMOUNTS_WITH_AVAILABLE_ONLY
    			,LegislativeActVersionDto.JSONS_EXPENDITURES_AMOUTNS_WITHOUT_AVAILABLE,LegislativeActVersionImpl.FIELDS_EXPENDITURES_AMOUNTS_WITHOUT_AVAILABLE
    			,LegislativeActVersionDto.JSONS_EXPENDITURES_ENTRY_AUTHORIZATION_AMOUTNS,LegislativeActVersionImpl.FIELDS_EXPENDITURES_ENTRY_AUTHORIZATION_AMOUNTS
    			,LegislativeActVersionDto.JSONS_EXPENDITURES_PAYMENT_CREDIT_AMOUTNS,LegislativeActVersionImpl.FIELDS_EXPENDITURES_PAYMENT_CREDIT_AMOUNTS
    			,LegislativeActVersionDto.JSONS_RESOURCES_AMOUTNS,LegislativeActVersionImpl.FIELDS_RESOURCES_AMOUNTS
    			,LegislativeActVersionDto.JSONS_GENERATED_ACT_COUNT_ACT_GENERATABLE_GENERATED_ACT_DELETABLE,LegislativeActVersionImpl.FIELDS_GENERATED_ACT_COUNT_ACT_GENERATABLE_GENERATED_ACT_DELETABLE
    			,LegislativeActVersionDto.JSONS_LEGISLATIVE_ACT_FROM_DATE_AS_TIMESTAMP_DATE_AS_TIMESTAMP,LegislativeActVersionImpl.FIELDS_ACT_FROM_DATE_AS_TIMESTAMP_DATE_AS_TIMESTAMP
    			));
		map.putAll(Map.of(
    			LegislativeActVersionDto.JSONS_EXPENDITURES_AMOUTNS_WITHOUT_INCLUDED_MOVEMENT_AND_AVAILABLE,LegislativeActVersionImpl.FIELDS_EXPENDITURES_AMOUNTS_WITHOUT_INCLUDED_MOVEMENT_AND_AVAILABLE
    			,LegislativeActVersionDto.JSONS_EXPENDITURES_AMOUTNS_WITH_INCLUDED_MOVEMENT_ONLY,LegislativeActVersionImpl.FIELDS_EXPENDITURES_AMOUNTS_WITH_INCLUDED_MOVEMENT_ONLY
    			,LegislativeActVersionDto.JSONS_EXPENDITURES_AMOUTNS_WITH_ADJUSTMENT_LOWER_THAN_ZERO_GREATER_THAN_ZERO_ONLY,LegislativeActVersionImpl.FIELDS_EXPENDITURES_AMOUNTS_WITH_ADJUSTMENT_LOWER_THAN_ZERO_GREATER_THAN_ZERO_ONLY
    			));
		AbstractServiceImpl.setProjections(LegislativeActVersionDtoImpl.class, map);
	}
}