package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.BudgetSpecializationUnitDto;
import ci.gouv.dgbf.system.collectif.server.api.service.SectionDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import io.quarkus.arc.Unremovable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @NoArgsConstructor @RequestScoped @Unremovable
public class BudgetSpecializationUnitDtoImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements BudgetSpecializationUnitDto,Serializable {

	@JsonbProperty(value = JSON_SECTION) SectionDtoImpl section;
	@JsonbProperty(value = JSON_SECTION_IDENTIFIER) String sectionIdentifier;
	@JsonbProperty(value = JSON_SECTION_CODE) String sectionCode;
	@JsonbProperty(value = JSON_SECTION_CODE_NAME) String sectionCodeName;
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public BudgetSpecializationUnitDtoImpl setIdentifier(String identifier) {
		return (BudgetSpecializationUnitDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public BudgetSpecializationUnitDtoImpl setCode(String code) {
		return (BudgetSpecializationUnitDtoImpl) super.setCode(code);
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public String getCode() {
		return super.getCode();
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public BudgetSpecializationUnitDtoImpl setName(String name) {
		return (BudgetSpecializationUnitDtoImpl) super.setName(name);
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public String getName() {
		return super.getName();
	}
	
	@Override
	public BudgetSpecializationUnitDto setSection(SectionDto section) {
		this.section = (SectionDtoImpl) section;
		return this;
	}
	
	static {
		AbstractServiceImpl.setProjections(BudgetSpecializationUnitDtoImpl.class, Map.of(
    			JSON_IDENTIFIER,BudgetSpecializationUnitImpl.FIELD_IDENTIFIER
    			,JSON_CODE,BudgetSpecializationUnitImpl.FIELD_CODE
    			,JSON_NAME,BudgetSpecializationUnitImpl.FIELD_NAME
    			,JSON_SECTION,BudgetSpecializationUnitImpl.FIELD_SECTION
    			,JSON_SECTION_IDENTIFIER,BudgetSpecializationUnitImpl.FIELD_SECTION_IDENTIFIER
    			,JSON_SECTION_CODE,BudgetSpecializationUnitImpl.FIELD_SECTION_CODE
    			,JSON_SECTION_CODE_NAME,BudgetSpecializationUnitImpl.FIELD_SECTION_CODE_NAME
    			));
	}
}