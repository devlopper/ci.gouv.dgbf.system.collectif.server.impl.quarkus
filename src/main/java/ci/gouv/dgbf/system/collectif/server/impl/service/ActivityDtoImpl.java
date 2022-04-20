package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.ActionDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ActivityDto;
import ci.gouv.dgbf.system.collectif.server.api.service.AdministrativeUnitDto;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetCategoryDto;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetSpecializationUnitDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureNatureDto;
import ci.gouv.dgbf.system.collectif.server.api.service.SectionDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActivityImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ActivityDtoImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements ActivityDto,Serializable {

	@JsonbProperty(value = JSON_EXPENDITURE_NATURE) ExpenditureNatureDtoImpl expenditureNature;
	@JsonbProperty(value = JSON_SECTION) SectionDtoImpl section;
	@JsonbProperty(value = JSON_ADMINISTRATIVE_UNIT) AdministrativeUnitDtoImpl administrativeUnit;
	@JsonbProperty(value = JSON_BUDGET_SPECIALIZATION_UNIT) BudgetSpecializationUnitDtoImpl budgetSpecializationUnit;
	@JsonbProperty(value = JSON_BUDGET_CATEGORY) BudgetCategoryDtoImpl budgetCategory;
	@JsonbProperty(value = JSON_ACTION) ActionDtoImpl action;
	@JsonbProperty(value = JSON_ECONOMIC_NATURES) ArrayList<EconomicNatureDtoImpl> economicNatures;
	@JsonbProperty(value = JSON_FUNDING_SOURCES) ArrayList<FundingSourceDtoImpl> fundingSources;
	@JsonbProperty(value = JSON_LESSORS) ArrayList<LessorDtoImpl> lessors;
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public ActivityDtoImpl setIdentifier(String identifier) {
		return (ActivityDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public ActivityDtoImpl setCode(String code) {
		return (ActivityDtoImpl) super.setCode(code);
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public String getCode() {
		return super.getCode();
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public ActivityDtoImpl setName(String name) {
		return (ActivityDtoImpl) super.setName(name);
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public String getName() {
		return super.getName();
	}
	
	@Override @JsonbProperty(value = JSON_EXPENDITURE_NATURE)
	public ActivityDto setExpenditureNature(ExpenditureNatureDto expenditureNature) {
		this.expenditureNature = (ExpenditureNatureDtoImpl) expenditureNature;
		return this;
	}
	
	@Override
	public ActivityDto setSection(SectionDto section) {
		this.section = (SectionDtoImpl) section;
		return this;
	}
	
	@Override
	public ActivityDto setAdministrativeUnit(AdministrativeUnitDto administrativeUnit) {
		this.administrativeUnit = (AdministrativeUnitDtoImpl) administrativeUnit;
		return this;
	}
	
	@Override
	public ActivityDto setBudgetSpecializationUnit(BudgetSpecializationUnitDto budgetSpecializationUnit) {
		this.budgetSpecializationUnit = (BudgetSpecializationUnitDtoImpl) budgetSpecializationUnit;
		return this;
	}
	
	@Override
	public ActivityDto setAction(ActionDto action) {
		this.action = (ActionDtoImpl) action;
		return this;
	}
	
	@Override
	public ActivityDto setBudgetCategory(BudgetCategoryDto budgetCategory) {
		this.budgetCategory = (BudgetCategoryDtoImpl) budgetCategory;
		return null;
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
    			,JSON_EXPENDITURE_NATURE,ActivityImpl.FIELD_EXPENDITURE_NATURE
    			,JSON_SECTION,ActivityImpl.FIELD_SECTION
    			,JSON_ADMINISTRATIVE_UNIT,ActivityImpl.FIELD_ADMINISTRATIVE_UNIT
    			,JSON_BUDGET_SPECIALIZATION_UNIT,ActivityImpl.FIELD_BUDGET_SPECIALIZATION_UNIT
    			,JSON_ACTION,ActivityImpl.FIELD_ACTION
    			));
		map.putAll(Map.of(
				JSON_ECONOMIC_NATURES,ActivityImpl.FIELD_ECONOMIC_NATURES
    			,JSON_FUNDING_SOURCES,ActivityImpl.FIELD_FUNDING_SOURCES
    			,JSON_LESSORS,ActivityImpl.FIELD_LESSORS
    			,JSONS_SECTION_ADMINISTRATIVE_UNIT_EXPENDITURE_NATURE_BUDGET_SPECIALIZATION_UNIT_ACTION,ActivityImpl.FIELDS_SECTION_ADMINISTRATIVE_UNIT_EXPENDITURE_NATURE_BUDGET_SPECIALIZATION_UNIT_ACTION
    			,JSONS_BUDGET_CATEGORY_SECTION_ADMINISTRATIVE_UNIT_EXPENDITURE_NATURE_BUDGET_SPECIALIZATION_UNIT_ACTION,ActivityImpl.FIELDS_BUDGET_CATEGORY_SECTION_ADMINISTRATIVE_UNIT_EXPENDITURE_NATURE_BUDGET_SPECIALIZATION_UNIT_ACTION
    			));
		AbstractServiceImpl.setProjections(ActivityDtoImpl.class, map);
	}
}