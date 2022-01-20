package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.BudgetSpecializationUnitDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ResourceActivityDto;
import ci.gouv.dgbf.system.collectif.server.api.service.SectionDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceActivityImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ResourceActivityDtoImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements ResourceActivityDto,Serializable {

	@JsonbProperty(value = JSON_SECTION) SectionDtoImpl section;
	@JsonbProperty(value = JSON_BUDGET_SPECIALIZATION_UNIT) BudgetSpecializationUnitDtoImpl budgetSpecializationUnit;
	@JsonbProperty(value = JSON_ECONOMIC_NATURES) ArrayList<EconomicNatureDtoImpl> economicNatures;
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public ResourceActivityDtoImpl setIdentifier(String identifier) {
		return (ResourceActivityDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public ResourceActivityDtoImpl setCode(String code) {
		return (ResourceActivityDtoImpl) super.setCode(code);
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public String getCode() {
		return super.getCode();
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public ResourceActivityDtoImpl setName(String name) {
		return (ResourceActivityDtoImpl) super.setName(name);
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public String getName() {
		return super.getName();
	}
	
	@Override
	public ResourceActivityDto setSection(SectionDto section) {
		this.section = (SectionDtoImpl) section;
		return this;
	}
	
	@Override
	public ResourceActivityDto setBudgetSpecializationUnit(BudgetSpecializationUnitDto budgetSpecializationUnit) {
		this.budgetSpecializationUnit = (BudgetSpecializationUnitDtoImpl) budgetSpecializationUnit;
		return this;
	}
	
	/*
	@Override
	public ActivityDto setEconomicNatures(ArrayList<? extends EconomicNatureDto> economicNatures) {
		this.economicNatures = (ArrayList<EconomicNatureDtoImpl>) CollectionHelper.cast(EconomicNatureDtoImpl.class, economicNatures);
		return this;
	}
	
	@Override
	public ActivityDto setFundingSources(ArrayList<? extends FundingSourceDto> fundingSources) {
		this.fundingSources = (ArrayList<FundingSourceDtoImpl>) CollectionHelper.cast(FundingSourceDtoImpl.class, fundingSources);
		return this;
	}
	
	@Override
	public ActivityDto setLessors(ArrayList<? extends LessorDto> lessors) {
		this.lessors = (ArrayList<LessorDtoImpl>) CollectionHelper.cast(LessorDtoImpl.class, lessors);
		return this;
	}
	*/
	static {
		Map<String,String> map = new HashMap<>();
		map.putAll(Map.of(
				JSON_IDENTIFIER,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_IDENTIFIER
    			,JSON_CODE,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_CODE
    			,JSON_NAME,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_NAME
    			,JSON_SECTION,ResourceActivityImpl.FIELD_SECTION
    			,JSON_BUDGET_SPECIALIZATION_UNIT,ResourceActivityImpl.FIELD_BUDGET_SPECIALIZATION_UNIT
    			));
		map.putAll(Map.of(
				JSON_ECONOMIC_NATURES,ResourceActivityImpl.FIELD_ECONOMIC_NATURES
    			,JSONS_SECTION_BUDGET_SPECIALIZATION_UNIT,ResourceActivityImpl.FIELDS_SECTION_BUDGET_SPECIALIZATION_UNIT
    			));
		AbstractServiceImpl.setProjections(ResourceActivityDtoImpl.class, map);
	}
}