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
@Entity(name = ExpenditureView.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ExpenditureView.TABLE_NAME)
@org.hibernate.annotations.Immutable
public class ExpenditureView extends AbstractIdentifiableSystemScalarStringImpl implements Serializable {

	@Column(name = COLUMN_EXERCISE) Short exercise;
	
	@Column(name = COLUMN_NATURE_IDENTIFIER) String natureIdentifier;
	@Column(name = COLUMN_NATURE_CODE) String natureCode;
	@Column(name = COLUMN_NATURE_CODE_NAME) String natureCodeName;
	
	@Column(name = COLUMN_SECTION_IDENTIFIER) String sectionIdentifier;
	@Column(name = COLUMN_SECTION_CODE) String sectionCode;
	@Column(name = COLUMN_SECTION_CODE_NAME) String sectionCodeName;
	
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER) String budgetSpecializationUnitIdentifier;
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE) String budgetSpecializationUnitCode;
	@Column(name = COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE_NAME) String budgetSpecializationUnitCodeName;
	
	@Column(name = COLUMN_ACTION_IDENTIFIER) String actionIdentifier;
	@Column(name = COLUMN_ACTION_CODE) String actionCode;
	@Column(name = COLUMN_ACTION_CODE_NAME) String actionCodeName;
	
	@Column(name = COLUMN_ACTIVITY_IDENTIFIER) String activityIdentifier;
	@Column(name = COLUMN_ACTIVITY_CODE) String activityCode;
	@Column(name = COLUMN_ACTIVITY_CODE_NAME) String activityCodeName;
	
	@Column(name = COLUMN_ECONOMIC_NATURE_IDENTIFIER) String economicNatureIdentifier;
	@Column(name = COLUMN_ECONOMIC_NATURE_CODE) String economicNatureCode;
	@Column(name = COLUMN_ECONOMIC_NATURE_CODE_NAME) String economicNatureCodeName;
	
	@Column(name = COLUMN_FUNDING_SOURCE_IDENTIFIER) String fundingSourceIdentifier;
	@Column(name = COLUMN_FUNDING_SOURCE_CODE) String fundingSourceCode;
	@Column(name = COLUMN_FUNDING_SOURCE_CODE_NAME) String fundingSourceCodeName;
	
	@Column(name = COLUMN_LESSOR_IDENTIFIER) String lessorIdentifier;
	@Column(name = COLUMN_LESSOR_CODE) String lessorCode;
	@Column(name = COLUMN_LESSOR_CODE_NAME) String lessorCodeName;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = EntryAuthorizationView.FIELD_ACTUAL,column = @Column(name=EntryAuthorizationView.COLUMN_ACTUAL))
		//,@AttributeOverride(name = EntryAuthorizationView.FIELD_ACTUAL_MINUS_MOVEMENT_INCLUDED,column = @Column(name=EntryAuthorizationView.COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED))
		//,@AttributeOverride(name = EntryAuthorizationView.FIELD_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT,column = @Column(name=EntryAuthorizationView.COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT))
		//,@AttributeOverride(name = EntryAuthorizationView.FIELD_AVAILABLE,column = @Column(name=EntryAuthorizationView.COLUMN_AVAILABLE))
		,@AttributeOverride(name = EntryAuthorizationView.FIELD_INITIAL,column = @Column(name=EntryAuthorizationView.COLUMN_INITIAL))
		//,@AttributeOverride(name = EntryAuthorizationView.FIELD_MOVEMENT,column = @Column(name=EntryAuthorizationView.COLUMN_MOVEMENT))
		//,@AttributeOverride(name = EntryAuthorizationView.FIELD_MOVEMENT_INCLUDED,column = @Column(name=EntryAuthorizationView.COLUMN_MOVEMENT_INCLUDED))
		})
	EntryAuthorizationView entryAuthorization = new EntryAuthorizationView();
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = PaymentCreditView.FIELD_ACTUAL,column = @Column(name=PaymentCreditView.COLUMN_ACTUAL))
		//,@AttributeOverride(name = PaymentCreditView.FIELD_ACTUAL_MINUS_MOVEMENT_INCLUDED,column = @Column(name=PaymentCreditView.COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED))
		//,@AttributeOverride(name = PaymentCreditView.FIELD_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT,column = @Column(name=PaymentCreditView.COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT))
		//@AttributeOverride(name = PaymentCreditView.FIELD_AVAILABLE,column = @Column(name=PaymentCreditView.COLUMN_AVAILABLE))
		,@AttributeOverride(name = PaymentCreditView.FIELD_INITIAL,column = @Column(name=PaymentCreditView.COLUMN_INITIAL))
		//,@AttributeOverride(name = PaymentCreditView.FIELD_MOVEMENT,column = @Column(name=PaymentCreditView.COLUMN_MOVEMENT))
		//,@AttributeOverride(name = PaymentCreditView.FIELD_MOVEMENT_INCLUDED,column = @Column(name=PaymentCreditView.COLUMN_MOVEMENT_INCLUDED))
		})
	PaymentCreditView paymentCredit = new PaymentCreditView();
	
	@Override
	public ExpenditureView setIdentifier(String identifier) {
		return (ExpenditureView) super.setIdentifier(identifier);
	}
	
	public static final String FIELD_EXERCISE = "exercise";
	
	public static final String FIELD_NATURE_IDENTIFIER = "natureIdentifier";
	public static final String FIELD_NATURE_CODE = "natureCode";
	public static final String FIELD_NATURE_CODE_NAME = "natureCodeName";
	
	public static final String FIELD_SECTION_IDENTIFIER = "sectionIdentifier";
	public static final String FIELD_SECTION_CODE = "sectionCode";
	public static final String FIELD_SECTION_CODE_NAME = "sectionCodeName";
	
	public static final String FIELD_ADMINISTRATIVE_UNIT_IDENTIFIER = "administrativeUnitIdentifier";
	public static final String FIELD_ADMINISTRATIVE_UNIT_CODE = "administrativeUnitCode";
	public static final String FIELD_ADMINISTRATIVE_UNIT_CODE_NAME = "administrativeUnitCodeName";
	
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER = "budgetSpecializationUnitIdentifier";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_CODE = "budgetSpecializationUnitCode";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_CODE_NAME = "budgetSpecializationUnitCodeName";
	
	public static final String FIELD_ACTION_IDENTIFIER = "actionIdentifier";
	public static final String FIELD_ACTION_CODE = "actionCode";
	public static final String FIELD_ACTION_CODE_NAME = "actionCodeName";
	
	public static final String FIELD_ACTIVITY_IDENTIFIER = "activityIdentifier";
	public static final String FIELD_ACTIVITY_CODE = "activityCode";
	public static final String FIELD_ACTIVITY_CODE_NAME = "activityCodeName";
	
	public static final String FIELD_ECONOMIC_NATURE_IDENTIFIER = "economicNatureIdentifier";
	public static final String FIELD_ECONOMIC_NATURE_CODE = "economicNatureCode";
	public static final String FIELD_ECONOMIC_NATURE_CODE_NAME = "economicNatureCodeName";
	
	public static final String FIELD_FUNDING_SOURCE_IDENTIFIER = "fundingSourceIdentifier";
	public static final String FIELD_FUNDING_SOURCE_CODE = "fundingSourceCode";
	public static final String FIELD_FUNDING_SOURCE_CODE_NAME = "fundingSourceCodeName";
	
	public static final String FIELD_LESSOR_IDENTIFIER = "lessorIdentifier";
	public static final String FIELD_LESSOR_CODE = "lessorCode";
	public static final String FIELD_LESSOR_CODE_NAME = "lessorCodeName";
	
	public static final String FIELD_ENTRY_AUTHORIZATION = "entryAuthorization";
	public static final String FIELD_PAYMENT_CREDIT = "paymentCredit";
	
	/* Columns */
	
	public static final String COLUMN_IDENTIFIER = "identifiant";
	
	public static final String COLUMN_EXERCISE = "exercice";
	
	public static final String COLUMN_NATURE_IDENTIFIER = "nature_depense_identifiant";
	public static final String COLUMN_NATURE_CODE = "nature_depense_code";
	public static final String COLUMN_NATURE_CODE_NAME = "nature_depense_code_libelle";
	
	public static final String COLUMN_SECTION_IDENTIFIER = "section_identifiant";
	public static final String COLUMN_SECTION_CODE = "section_code";
	public static final String COLUMN_SECTION_CODE_NAME = "section_code_libelle";
	
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER = "usb_identifiant";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE = "usb_code";
	public static final String COLUMN_BUDGET_SPECIALIZATION_UNIT_CODE_NAME = "usb_code_libelle";
	
	public static final String COLUMN_ACTION_IDENTIFIER = "action_identifiant";
	public static final String COLUMN_ACTION_CODE = "action_code";
	public static final String COLUMN_ACTION_CODE_NAME = "action_code_libelle";
	
	public static final String COLUMN_ACTIVITY_IDENTIFIER = "activite_identifiant";
	public static final String COLUMN_ACTIVITY_CODE = "activite_code";
	public static final String COLUMN_ACTIVITY_CODE_NAME = "activite_code_libelle";
	
	public static final String COLUMN_ECONOMIC_NATURE_IDENTIFIER = "nature_economique_identifiant";
	public static final String COLUMN_ECONOMIC_NATURE_CODE = "nature_economique_code";
	public static final String COLUMN_ECONOMIC_NATURE_CODE_NAME = "nature_economique_code_libelle";
	
	public static final String COLUMN_FUNDING_SOURCE_IDENTIFIER = "source_financement_identifiant";
	public static final String COLUMN_FUNDING_SOURCE_CODE = "source_financement_code";
	public static final String COLUMN_FUNDING_SOURCE_CODE_NAME = "sf_code_libelle";
	
	public static final String COLUMN_LESSOR_IDENTIFIER = "bailleur_identifiant";
	public static final String COLUMN_LESSOR_CODE = "bailleur_code";
	public static final String COLUMN_LESSOR_CODE_NAME = "bailleur_code_libelle";
	
	public static final String ENTITY_NAME = "ExpenditureView";
	public static final String TABLE_NAME = "VMA_DEPENSE";
}