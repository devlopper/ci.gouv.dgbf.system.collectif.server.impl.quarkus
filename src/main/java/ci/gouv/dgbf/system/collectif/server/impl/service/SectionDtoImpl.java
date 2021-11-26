package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.SectionDto;
import io.quarkus.arc.Unremovable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @NoArgsConstructor @RequestScoped @Unremovable
public class SectionDtoImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements SectionDto,Serializable {

	@Override @JsonbProperty(value = SectionDto.JSON_IDENTIFIER)
	public SectionDtoImpl setIdentifier(String identifier) {
		return (SectionDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(value = SectionDto.JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override @JsonbProperty(value = SectionDto.JSON_CODE)
	public SectionDtoImpl setCode(String code) {
		return (SectionDtoImpl) super.setCode(code);
	}
	
	@Override @JsonbProperty(value = SectionDto.JSON_CODE)
	public String getCode() {
		return super.getCode();
	}
	
	@Override @JsonbProperty(value = SectionDto.JSON_NAME)
	public SectionDtoImpl setName(String name) {
		return (SectionDtoImpl) super.setName(name);
	}
	
	@Override @JsonbProperty(value = SectionDto.JSON_NAME)
	public String getName() {
		return super.getName();
	}
	
}