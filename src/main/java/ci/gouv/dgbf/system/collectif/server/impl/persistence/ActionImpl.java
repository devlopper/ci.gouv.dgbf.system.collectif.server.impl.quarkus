package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Action;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ActionImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ActionImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class ActionImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements Action,Serializable {

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
	
	@Column(name = COLUMN_BUDGET_CATEGORY_IDENTIFIER) String budgetCategoryIdentifier;
	@Column(name = COLUMN_BUDGET_CATEGORY_CODE) String budgetCategoryCode;
	@Column(name = COLUMN_BUDGET_CATEGORY_NAME) String budgetCategoryName;
	@Column(name = COLUMN_BUDGET_CATEGORY_CODE_NAME) String budgetCategoryCodeName;
	@Transient BudgetCategoryImpl budgetCategory;
	
	@Override
	public ActionImpl setIdentifier(String identifier) {
		return (ActionImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public ActionImpl setCode(String code) {
		return (ActionImpl) super.setCode(code);
	}
	
	@Override
	public ActionImpl setName(String name) {
		return (ActionImpl) super.setName(name);
	}
	
	@Override
	public Action setSection(Section section) {
		this.section = (SectionImpl) section;
		return this;
	}
	
	@Override
	public Action setBudgetSpecializationUnit(BudgetSpecializationUnit budgetSpecializationUnit) {
		this.budgetSpecializationUnit = (BudgetSpecializationUnitImpl) budgetSpecializationUnit;
		return this;
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
	
	public static final String FIELDS_SECTION_BUDGET_SPECIALIZATION_UNIT = "sectionBudgetSpecializationUnit";
	
	public static final String ENTITY_NAME = "ActionImpl";
	public static final String TABLE_NAME = "VMA_ACTION";
	
	public static final String COLUMN_SECTION_IDENTIFIER = "section_identifiant";
	public static final String COLUMN_SECTION_CODE = "section_code";
	public static final String COLUMN_SECTION_NAME = "section_libelle";
	public static final String COLUMN_SECTION_CODE_NAME = "section_code_libelle";
	
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER = "usb_identifiant";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE = "usb_code";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_NAME = "usb_libelle";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE_NAME = "usb_code_libelle";
	
	public static final String COLUMN_BUDGET_CATEGORY_IDENTIFIER = "categorie_budget_identifiant";
	public static final String COLUMN_BUDGET_CATEGORY_CODE = "categorie_budget_code";
	public static final String COLUMN_BUDGET_CATEGORY_NAME = "categorie_budget_libelle";
	public static final String COLUMN_BUDGET_CATEGORY_CODE_NAME = "categorie_budget_code_libelle";
}