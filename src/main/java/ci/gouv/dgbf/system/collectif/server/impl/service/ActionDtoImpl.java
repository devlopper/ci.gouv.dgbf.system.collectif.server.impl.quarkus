package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.ActionDto;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetSpecializationUnitDto;
import ci.gouv.dgbf.system.collectif.server.api.service.SectionDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActionImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ActionDtoImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements ActionDto,Serializable {

	@JsonbProperty(value = JSON_SECTION) SectionDtoImpl section;
	@JsonbProperty(value = JSON_BUDGET_SPECIALIZATION_UNIT) BudgetSpecializationUnitDtoImpl budgetSpecializationUnit;
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public ActionDtoImpl setIdentifier(String identifier) {
		return (ActionDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public ActionDtoImpl setCode(String code) {
		return (ActionDtoImpl) super.setCode(code);
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public String getCode() {
		return super.getCode();
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public ActionDtoImpl setName(String name) {
		return (ActionDtoImpl) super.setName(name);
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public String getName() {
		return super.getName();
	}
	
	@Override
	public ActionDto setSection(SectionDto section) {
		this.section = (SectionDtoImpl) section;
		return this;
	}
	
	@Override
	public ActionDto setBudgetSpecializationUnit(BudgetSpecializationUnitDto budgetSpecializationUnit) {
		this.budgetSpecializationUnit = (BudgetSpecializationUnitDtoImpl) budgetSpecializationUnit;
		return this;
	}

	static {
		Map<String,String> map = new HashMap<>();
		map.putAll(Map.of(
				JSON_IDENTIFIER,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_IDENTIFIER
    			,JSON_CODE,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_CODE
    			,JSON_NAME,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_NAME
    			,JSON_SECTION,ActionImpl.FIELD_SECTION
    			,JSON_BUDGET_SPECIALIZATION_UNIT,ActionImpl.FIELD_BUDGET_SPECIALIZATION_UNIT
    			,JSONS_SECTION_BUDGET_SPECIALIZATION_UNIT,ActionImpl.FIELDS_SECTION_BUDGET_SPECIALIZATION_UNIT
    			));
		AbstractServiceImpl.setProjections(ActionDtoImpl.class, map);
	}
}