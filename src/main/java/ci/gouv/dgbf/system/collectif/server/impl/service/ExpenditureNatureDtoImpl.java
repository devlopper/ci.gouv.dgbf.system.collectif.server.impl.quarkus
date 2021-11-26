package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

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
	
}