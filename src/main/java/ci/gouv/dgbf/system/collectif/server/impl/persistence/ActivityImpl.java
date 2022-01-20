package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Action;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Activity;
import ci.gouv.dgbf.system.collectif.server.api.persistence.AdministrativeUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNature;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ActivityImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ActivityImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class ActivityImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements Activity,Serializable {

	@Column(name = COLUMN_EXPENDITURE_NATURE_IDENTIFIER) String expenditureNatureIdentifier;
	@Column(name = COLUMN_EXPENDITURE_NATURE_CODE) String expenditureNatureCode;
	@Column(name = COLUMN_EXPENDITURE_NATURE_NAME) String expenditureNatureName;
	@Column(name = COLUMN_EXPENDITURE_NATURE_CODE_NAME) String expenditureNatureCodeName;
	@Transient ExpenditureNatureImpl expenditureNature;
	
	@Column(name = COLUMN_SECTION_IDENTIFIER) String sectionIdentifier;
	@Column(name = COLUMN_SECTION_CODE) String sectionCode;
	@Column(name = COLUMN_SECTION_NAME) String sectionName;
	@Column(name = COLUMN_SECTION_CODE_NAME) String sectionCodeName;
	@Transient SectionImpl section;
	
	@Column(name = COLUMN_ADMINISTRATIVE_UNIT_IDENTIFIER) String administrativeUnitIdentifier;
	@Column(name = COLUMN_ADMINISTRATIVE_UNIT_CODE) String administrativeUnitCode;
	@Column(name = COLUMN_ADMINISTRATIVE_UNIT_NAME) String administrativeUnitName;
	@Column(name = COLUMN_ADMINISTRATIVE_UNIT_CODE_NAME) String administrativeUnitCodeName;
	@Transient AdministrativeUnitImpl administrativeUnit;
	
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER) String budgetSpecializationUnitIdentifier;
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE) String budgetSpecializationUnitCode;
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_NAME) String budgetSpecializationUnitName;
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE_NAME) String budgetSpecializationUnitCodeName;
	@Transient BudgetSpecializationUnitImpl budgetSpecializationUnit;
	
	@Column(name = COLUMN_ACTION_IDENTIFIER) String actionIdentifier;
	@Column(name = COLUMN_ACTION_CODE) String actionCode;
	@Column(name = COLUMN_ACTION_NAME) String actionName;
	@Column(name = COLUMN_ACTION_CODE_NAME) String actionCodeName;
	@Transient ActionImpl action;
	
	@Transient private Collection<EconomicNatureImpl> economicNatures;
	@Transient private Collection<FundingSourceImpl> fundingSources;
	@Transient private Collection<LessorImpl> lessors;
	
	@Override
	public ActivityImpl setIdentifier(String identifier) {
		return (ActivityImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public ActivityImpl setCode(String code) {
		return (ActivityImpl) super.setCode(code);
	}
	
	@Override
	public ActivityImpl setName(String name) {
		return (ActivityImpl) super.setName(name);
	}
	
	@Override
	public Activity setExpenditureNature(ExpenditureNature expenditureNature) {
		this.expenditureNature = (ExpenditureNatureImpl) expenditureNature;
		return this;
	}
	
	@Override
	public Activity setSection(Section section) {
		this.section = (SectionImpl) section;
		return this;
	}
	
	@Override
	public Activity setAdministrativeUnit(AdministrativeUnit administrativeUnit) {
		this.administrativeUnit = (AdministrativeUnitImpl) administrativeUnit;
		return this;
	}
	
	@Override
	public Activity setBudgetSpecializationUnit(BudgetSpecializationUnit budgetSpecializationUnit) {
		this.budgetSpecializationUnit = (BudgetSpecializationUnitImpl) budgetSpecializationUnit;
		return this;
	}
	
	@Override
	public Activity setAction(Action action) {
		this.action = (ActionImpl) action;
		return this;
	}
	
	public Collection<EconomicNatureImpl> getEconomicNatures(Boolean instantiateIfNull) {
		if(economicNatures == null && Boolean.TRUE.equals(instantiateIfNull))
			economicNatures = new ArrayList<>();
		return economicNatures;
	}
	
	public ActivityImpl addEconomicNatures(Collection<EconomicNatureImpl> economicNatures) {
		if(CollectionHelper.isEmpty(economicNatures))
			return this;
		getEconomicNatures(Boolean.TRUE).addAll(economicNatures);
		return this;
	}
	
	public ActivityImpl addEconomicNatures(EconomicNatureImpl...economicNatures) {
		if(ArrayHelper.isEmpty(economicNatures))
			return this;
		return addEconomicNatures(CollectionHelper.listOf(economicNatures));
	}
	
	public Collection<FundingSourceImpl> getFundingSources(Boolean instantiateIfNull) {
		if(fundingSources == null && Boolean.TRUE.equals(instantiateIfNull))
			fundingSources = new ArrayList<>();
		return fundingSources;
	}
	
	public ActivityImpl addFundingSources(Collection<FundingSourceImpl> fundingSources) {
		if(CollectionHelper.isEmpty(fundingSources))
			return this;
		getFundingSources(Boolean.TRUE).addAll(fundingSources);
		return this;
	}
	
	public ActivityImpl addFundingSources(FundingSourceImpl...fundingSources) {
		if(ArrayHelper.isEmpty(fundingSources))
			return this;
		return addFundingSources(CollectionHelper.listOf(fundingSources));
	}
	
	public Collection<LessorImpl> getLessors(Boolean instantiateIfNull) {
		if(lessors == null && Boolean.TRUE.equals(instantiateIfNull))
			lessors = new ArrayList<>();
		return lessors;
	}
	
	public ActivityImpl addLessors(Collection<LessorImpl> lessors) {
		if(CollectionHelper.isEmpty(lessors))
			return this;
		getLessors(Boolean.TRUE).addAll(lessors);
		return this;
	}
	
	public ActivityImpl addLessors(LessorImpl...lessors) {
		if(ArrayHelper.isEmpty(lessors))
			return this;
		return addLessors(CollectionHelper.listOf(lessors));
	}
	
	public static final String FIELD_EXPENDITURE_NATURE_IDENTIFIER = "expenditureNatureIdentifier";
	public static final String FIELD_EXPENDITURE_NATURE_CODE = "expenditureNatureCode";
	public static final String FIELD_EXPENDITURE_NATURE_NAME = "expenditureNatureName";
	public static final String FIELD_EXPENDITURE_NATURE_CODE_NAME = "expenditureNatureCodeName";
	public static final String FIELD_EXPENDITURE_NATURE = "expenditureNature";
	
	public static final String FIELD_SECTION_IDENTIFIER = "sectionIdentifier";
	public static final String FIELD_SECTION_CODE = "sectionCode";
	public static final String FIELD_SECTION_NAME = "sectionName";
	public static final String FIELD_SECTION_CODE_NAME = "sectionCodeName";
	public static final String FIELD_SECTION = "section";
	
	public static final String FIELD_ADMINISTRATIVE_UNIT_IDENTIFIER = "administrativeUnitIdentifier";
	public static final String FIELD_ADMINISTRATIVE_UNIT_CODE = "administrativeUnitCode";
	public static final String FIELD_ADMINISTRATIVE_UNIT_NAME = "administrativeUnitName";
	public static final String FIELD_ADMINISTRATIVE_UNIT_CODE_NAME = "administrativeUnitCodeName";
	public static final String FIELD_ADMINISTRATIVE_UNIT = "administrativeUnit";
	
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER = "budgetSpecializationUnitIdentifier";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_CODE = "budgetSpecializationUnitCode";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_NAME = "budgetSpecializationUnitName";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_CODE_NAME = "budgetSpecializationUnitCodeName";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT = "budgetSpecializationUnit";
	
	public static final String FIELD_ACTION_IDENTIFIER = "actionIdentifier";
	public static final String FIELD_ACTION_CODE = "actionCode";
	public static final String FIELD_ACTION_NAME = "actionName";
	public static final String FIELD_ACTION_CODE_NAME = "actionCodeName";
	public static final String FIELD_ACTION = "action";
	
	public static final String FIELD_ECONOMIC_NATURES = "economicNatures";
	public static final String FIELD_FUNDING_SOURCES = "fundingSources";
	public static final String FIELD_LESSORS = "lessors";
	
	public static final String FIELDS_SECTION_ADMINISTRATIVE_UNIT_EXPENDITURE_NATURE_BUDGET_SPECIALIZATION_UNIT_ACTION = "expenditureNatureSectionAdministrativeUnitBudgetSpecializationUnitAction";
	
	public static final String ENTITY_NAME = "ActivityImpl";
	public static final String TABLE_NAME = "VMA_ACTIVITE";
	
	public static final String COLUMN_EXPENDITURE_NATURE_IDENTIFIER = "nature_depense_identifiant";
	public static final String COLUMN_EXPENDITURE_NATURE_CODE = "nature_depense_code";
	public static final String COLUMN_EXPENDITURE_NATURE_NAME = "nature_depense_libelle";
	public static final String COLUMN_EXPENDITURE_NATURE_CODE_NAME = "nature_depense_code_libelle";
	
	public static final String COLUMN_SECTION_IDENTIFIER = "section_identifiant";
	public static final String COLUMN_SECTION_CODE = "section_code";
	public static final String COLUMN_SECTION_NAME = "section_libelle";
	public static final String COLUMN_SECTION_CODE_NAME = "section_code_libelle";
	
	public static final String COLUMN_ADMINISTRATIVE_UNIT_IDENTIFIER = "ua_identifiant";
	public static final String COLUMN_ADMINISTRATIVE_UNIT_CODE = "ua_code";
	public static final String COLUMN_ADMINISTRATIVE_UNIT_NAME = "ua_libelle";
	public static final String COLUMN_ADMINISTRATIVE_UNIT_CODE_NAME = "ua_code_libelle";
	
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER = "usb_identifiant";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE = "usb_code";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_NAME = "usb_libelle";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE_NAME = "usb_code_libelle";
	
	public static final String COLUMN_ACTION_IDENTIFIER = "action_identifiant";
	public static final String COLUMN_ACTION_CODE = "action_code";
	public static final String COLUMN_ACTION_NAME = "action_libelle";
	public static final String COLUMN_ACTION_CODE_NAME = "action_code_libelle";
}