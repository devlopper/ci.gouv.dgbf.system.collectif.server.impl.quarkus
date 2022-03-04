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

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceActivity;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ResourceActivityImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ResourceActivityImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class ResourceActivityImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements ResourceActivity,Serializable {

	@Column(name = COLUMN_SECTION_IDENTIFIER) String sectionIdentifier;
	@Column(name = COLUMN_SECTION_CODE) String sectionCode;
	@Column(name = COLUMN_SECTION_NAME) String sectionName;
	@Column(name = COLUMN_SECTION_CODE_NAME) String sectionCodeName;
	@Transient SectionImpl section;
	
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER) String budgetSpecializationUnitIdentifier;
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE) String budgetSpecializationUnitCode;
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_NAME) String budgetSpecializationUnitName;
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE_NAME) String budgetSpecializationUnitCodeName;
	@Transient BudgetSpecializationUnitImpl budgetSpecializationUnit;
	
	@Transient private Collection<EconomicNatureImpl> economicNatures;

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
	public ResourceActivity setSection(Section section) {
		this.section = (SectionImpl) section;
		return this;
	}
	
	@Override
	public ResourceActivity setBudgetSpecializationUnit(BudgetSpecializationUnit budgetSpecializationUnit) {
		this.budgetSpecializationUnit = (BudgetSpecializationUnitImpl) budgetSpecializationUnit;
		return this;
	}
	
	public Collection<EconomicNatureImpl> getEconomicNatures(Boolean instantiateIfNull) {
		if(economicNatures == null && Boolean.TRUE.equals(instantiateIfNull))
			economicNatures = new ArrayList<>();
		return economicNatures;
	}
	
	public ResourceActivityImpl addEconomicNatures(Collection<EconomicNatureImpl> economicNatures) {
		if(CollectionHelper.isEmpty(economicNatures))
			return this;
		getEconomicNatures(Boolean.TRUE).addAll(economicNatures);
		return this;
	}
	
	public ResourceActivityImpl addEconomicNatures(EconomicNatureImpl...economicNatures) {
		if(ArrayHelper.isEmpty(economicNatures))
			return this;
		return addEconomicNatures(CollectionHelper.listOf(economicNatures));
	}
	
	public static final String FIELD_SECTION_IDENTIFIER = "sectionIdentifier";
	public static final String FIELD_SECTION_CODE = "sectionCode";
	public static final String FIELD_SECTION_NAME = "sectionName";
	public static final String FIELD_SECTION_CODE_NAME = "sectionCodeName";
	public static final String FIELD_SECTION = "section";
	
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER = "budgetSpecializationUnitIdentifier";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_CODE = "budgetSpecializationUnitCode";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_NAME = "budgetSpecializationUnitName";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_CODE_NAME = "budgetSpecializationUnitCodeName";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT = "budgetSpecializationUnit";
	
	public static final String FIELD_ECONOMIC_NATURES = "economicNatures";
	
	public static final String FIELDS_SECTION_BUDGET_SPECIALIZATION_UNIT = "sectionBudgetSpecializationUnit";
	
	public static final String ENTITY_NAME = "ResourceActivityImpl";
	public static final String TABLE_NAME = "VMA_ACTIVITE_RESSOURCE";
	
	public static final String COLUMN_SECTION_IDENTIFIER = "section_identifiant";
	public static final String COLUMN_SECTION_CODE = "section_code";
	public static final String COLUMN_SECTION_NAME = "section_libelle";
	public static final String COLUMN_SECTION_CODE_NAME = "section_code_libelle";
	
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER = "usb_identifiant";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE = "usb_code";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_NAME = "usb_libelle";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE_NAME = "usb_code_libelle";
}