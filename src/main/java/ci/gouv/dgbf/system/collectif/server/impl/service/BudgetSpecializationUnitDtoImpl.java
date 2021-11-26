package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.__kernel__.object.AbstractObject;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.BudgetSpecializationUnitDto;
import ci.gouv.dgbf.system.collectif.server.api.service.SectionDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class BudgetSpecializationUnitDtoImpl extends AbstractObject implements BudgetSpecializationUnitDto,Serializable {

	@JsonbProperty(value = JSON_IDENTIFIER) String identifier;
	@JsonbProperty(value = JSON_CODE) String code;
	@JsonbProperty(value = JSON_NAME) String name;
	@JsonbProperty(value = JSON_SECTION) SectionDtoImpl section;
	@JsonbProperty(value = JSON_SECTION_IDENTIFIER) String sectionIdentifier;
	@JsonbProperty(value = JSON_SECTION_CODE) String sectionCode;
	@JsonbProperty(value = JSON_SECTION_CODE_NAME) String sectionCodeName;
	
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