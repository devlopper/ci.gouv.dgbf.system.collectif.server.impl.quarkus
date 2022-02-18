package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class LegislativeActDtoImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements LegislativeActDto,Serializable {

	@JsonbProperty(value = JSON_NUMBER) Byte number;
	@JsonbProperty(value = JSON_EXERCISE) ExerciseDtoImpl exercise;
	@JsonbProperty(value = JSON_EXERCISE_IDENTIFIER) String exerciseIdentifier;
	@JsonbProperty(value = JSON_EXERCISE_YEAR) Short exerciseYear;
	@JsonbProperty(value = JSON_EXERCISE_AS_STRING) String exerciseAsString;
	@JsonbProperty(value = JSON_DEFAULT_VERSION_AS_STRING) String defaultVersionAsString;
	@JsonbProperty(value = JSON_IN_PROGRESS) private Boolean inProgress;
	@JsonbProperty(value = JSON_IN_PROGRESS_AS_STRING) private String inProgressAsString;
	@JsonbProperty(value = JSON_ENTRY_AUTHORIZATION) EntryAuthorizationDtoImpl entryAuthorization;	
	@JsonbProperty(value = JSON_PAYMENT_CREDIT) PaymentCreditDtoImpl paymentCredit;		
	@JsonbProperty(value = JSON___AUDIT__) private String __audit__;

	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public LegislativeActDtoImpl setIdentifier(String identifier) {
		return (LegislativeActDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public LegislativeActDtoImpl setCode(String code) {
		return (LegislativeActDtoImpl) super.setCode(code);
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public String getCode() {
		return super.getCode();
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public LegislativeActDtoImpl setName(String name) {
		return (LegislativeActDtoImpl) super.setName(name);
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
    			,JSON_NUMBER,LegislativeActImpl.FIELD_NUMBER
    			,JSON_EXERCISE,LegislativeActImpl.FIELD_EXERCISE
    			,JSON_EXERCISE_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER
    			,JSON_EXERCISE_YEAR,LegislativeActImpl.FIELD_EXERCISE_YEAR
    			,JSON___AUDIT__,LegislativeActImpl.FIELD___AUDIT__
    			,JSONS_STRINGS,LegislativeActImpl.FIELDS_STRINGS
    			,JSONS_AMOUTNS,LegislativeActImpl.FIELDS_AMOUNTS
    			));
		AbstractServiceImpl.setProjections(LegislativeActDtoImpl.class, map);
	}
}