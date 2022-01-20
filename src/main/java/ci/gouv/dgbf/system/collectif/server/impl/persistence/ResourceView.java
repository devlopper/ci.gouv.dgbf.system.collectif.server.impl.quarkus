package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ResourceView.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ResourceView.TABLE_NAME)
@org.hibernate.annotations.Immutable
public class ResourceView extends AbstractIdentifiableSystemScalarStringImpl implements Serializable {

	@Column(name = COLUMN_SECTION_IDENTIFIER) String sectionIdentifier;
	@Column(name = COLUMN_SECTION_CODE) String sectionCode;
	@Column(name = COLUMN_SECTION_CODE_NAME) String sectionCodeName;
	
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER) String budgetSpecializationUnitIdentifier;
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE) String budgetSpecializationUnitCode;
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE_NAME) String budgetSpecializationUnitCodeName;
	
	@Column(name = COLUMN_ACTIVITY_IDENTIFIER) String activityIdentifier;
	@Column(name = COLUMN_ACTIVITY_CODE) String activityCode;
	@Column(name = COLUMN_ACTIVITY_CODE_NAME) String activityCodeName;
	
	@Column(name = COLUMN_ECONOMIC_NATURE_IDENTIFIER) String economicNatureIdentifier;
	@Column(name = COLUMN_ECONOMIC_NATURE_CODE) String economicNatureCode;
	@Column(name = COLUMN_ECONOMIC_NATURE_CODE_NAME) String economicNatureCodeName;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = RevenueView.FIELD_ACTUAL,column = @Column(name=RevenueView.COLUMN_ACTUAL))
		,@AttributeOverride(name = RevenueView.FIELD_INITIAL,column = @Column(name=RevenueView.COLUMN_INITIAL))
		})
	RevenueView revenue = new RevenueView();
	
	@Override
	public ExpenditureView setIdentifier(String identifier) {
		return (ExpenditureView) super.setIdentifier(identifier);
	}
	
	public static final String FIELD_SECTION_IDENTIFIER = "sectionIdentifier";
	public static final String FIELD_SECTION_CODE = "sectionCode";
	public static final String FIELD_SECTION_CODE_NAME = "sectionCodeName";
	
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER = "budgetSpecializationUnitIdentifier";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_CODE = "budgetSpecializationUnitCode";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_CODE_NAME = "budgetSpecializationUnitCodeName";
	
	public static final String FIELD_ACTIVITY_IDENTIFIER = "activityIdentifier";
	public static final String FIELD_ACTIVITY_CODE = "activityCode";
	public static final String FIELD_ACTIVITY_CODE_NAME = "activityCodeName";
	
	public static final String FIELD_ECONOMIC_NATURE_IDENTIFIER = "economicNatureIdentifier";
	public static final String FIELD_ECONOMIC_NATURE_CODE = "economicNatureCode";
	public static final String FIELD_ECONOMIC_NATURE_CODE_NAME = "economicNatureCodeName";
	
	public static final String FIELD_REVENUE = "revenue";
	
	/* Columns */
	
	public static final String COLUMN_IDENTIFIER = "identifiant";
	
	public static final String COLUMN_SECTION_IDENTIFIER = "section_identifiant";
	public static final String COLUMN_SECTION_CODE = "section_code";
	public static final String COLUMN_SECTION_CODE_NAME = "section_code_libelle";
	
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER = "usb_identifiant";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE = "usb_code";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE_NAME = "usb_code_libelle";
	
	public static final String COLUMN_ACTIVITY_IDENTIFIER = "activite_identifiant";
	public static final String COLUMN_ACTIVITY_CODE = "activite_code";
	public static final String COLUMN_ACTIVITY_CODE_NAME = "activite_code_libelle";
	
	public static final String COLUMN_ECONOMIC_NATURE_IDENTIFIER = "nature_economique_identifiant";
	public static final String COLUMN_ECONOMIC_NATURE_CODE = "nature_economique_code";
	public static final String COLUMN_ECONOMIC_NATURE_CODE_NAME = "nature_economique_code_libelle";
	
	public static final String ENTITY_NAME = "ResourceView";
	public static final String TABLE_NAME = "VMA_LIGNE_RECETTE";
}