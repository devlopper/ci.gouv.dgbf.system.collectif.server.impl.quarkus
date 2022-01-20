package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureNatureDto;
import io.quarkus.arc.Unremovable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @NoArgsConstructor @RequestScoped @Unremovable
public class ExpenditureNatureDtoImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements ExpenditureNatureDto,Serializable {

	@Override @JsonbProperty(value = ExpenditureNatureDto.JSON_IDENTIFIER)
	public ExpenditureNatureDtoImpl setIdentifier(String identifier) {
		return (ExpenditureNatureDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(value = ExpenditureNatureDto.JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override @JsonbProperty(value = ExpenditureNatureDto.JSON_CODE)
	public ExpenditureNatureDtoImpl setCode(String code) {
		return (ExpenditureNatureDtoImpl) super.setCode(code);
	}
	
	@Override @JsonbProperty(value = ExpenditureNatureDto.JSON_CODE)
	public String getCode() {
		return super.getCode();
	}
	
	@Override @JsonbProperty(value = ExpenditureNatureDto.JSON_NAME)
	public ExpenditureNatureDtoImpl setName(String name) {
		return (ExpenditureNatureDtoImpl) super.setName(name);
	}
	
	@Override @JsonbProperty(value = ExpenditureNatureDto.JSON_NAME)
	public String getName() {
		return super.getName();
	}

	static {
		Map<String,String> map = new HashMap<>();
		map.putAll(Map.of(
				JSON_IDENTIFIER,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_IDENTIFIER
    			,JSON_CODE,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_CODE
    			,JSON_NAME,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_NAME
    			));
		AbstractServiceImpl.setProjections(ExpenditureNatureDtoImpl.class, map);
	}
}