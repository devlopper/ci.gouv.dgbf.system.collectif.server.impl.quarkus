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

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = BudgetSpecializationUnitImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=BudgetSpecializationUnitImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class BudgetSpecializationUnitImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements BudgetSpecializationUnit,Serializable {

	@Column(name = COLUMN_SECTION_IDENTIFIER) String sectionIdentifier;
	@Column(name = COLUMN_SECTION_CODE) String sectionCode;
	@Column(name = COLUMN_SECTION_NAME) String sectionName;
	@Column(name = COLUMN_SECTION_CODE_NAME) String sectionCodeName;
	@Transient SectionImpl section;
	
	@Column(name = COLUMN_BUDGET_CATEGORY_IDENTIFIER) String budgetCategoryIdentifier;
	@Column(name = COLUMN_BUDGET_CATEGORY_CODE) String budgetCategoryCode;
	@Column(name = COLUMN_BUDGET_CATEGORY_NAME) String budgetCategoryName;
	@Column(name = COLUMN_BUDGET_CATEGORY_CODE_NAME) String budgetCategoryCodeName;
	@Transient BudgetCategoryImpl budgetCategory;
	
	@Override
	public BudgetSpecializationUnitImpl setIdentifier(String identifier) {
		return (BudgetSpecializationUnitImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public BudgetSpecializationUnitImpl setCode(String code) {
		return (BudgetSpecializationUnitImpl) super.setCode(code);
	}
	
	@Override
	public BudgetSpecializationUnitImpl setName(String name) {
		return (BudgetSpecializationUnitImpl) super.setName(name);
	}
	
	@Override
	public BudgetSpecializationUnit setSection(Section section) {
		this.section = (SectionImpl) section;
		return this;
	}
	
	public static final String FIELD_SECTION_IDENTIFIER = "sectionIdentifier";
	public static final String FIELD_SECTION_CODE = "sectionCode";
	public static final String FIELD_SECTION_NAME = "sectionName";
	public static final String FIELD_SECTION_CODE_NAME = "sectionCodeName";
	public static final String FIELD_SECTION = "section";
	
	public static final String ENTITY_NAME = "BudgetSpecializationUnitImpl";
	public static final String TABLE_NAME = "VMA_USB";
	
	public static final String COLUMN_SECTION_IDENTIFIER = "section_identifiant";
	public static final String COLUMN_SECTION_CODE = "section_code";
	public static final String COLUMN_SECTION_NAME = "section_libelle";
	public static final String COLUMN_SECTION_CODE_NAME = "section_code_libelle";
	
	public static final String COLUMN_BUDGET_CATEGORY_IDENTIFIER = "categorie_budget_identifiant";
	public static final String COLUMN_BUDGET_CATEGORY_CODE = "categorie_budget_code";
	public static final String COLUMN_BUDGET_CATEGORY_NAME = "categorie_budget_libelle";
	public static final String COLUMN_BUDGET_CATEGORY_CODE_NAME = "categorie_budget_code_libelle";
}