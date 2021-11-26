package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EntryAuthorization;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.PaymentCredit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ExpenditureImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ExpenditureImpl.TABLE_NAME)
public class ExpenditureImpl extends AbstractIdentifiableSystemScalarStringImpl implements Expenditure,Serializable {

	@NotNull @Column(name = COLUMN_ACTIVITY_IDENTIFIER,nullable = false)
	String activityIdentifier;
	
	@NotNull @Column(name = COLUMN_ECONOMIC_NATURE_IDENTIFIER,nullable = false)
	String economicNatureIdentifier;
	
	@NotNull @Column(name = COLUMN_FUNDING_SOURCE_IDENTIFIER,nullable = false)
	String fundingSourceIdentifier;
	
	@NotNull @Column(name = COLUMN_LESSOR_IDENTIFIER,nullable = false)
	String lessorIdentifier;
	
	@Valid
	@Embedded
	@AttributeOverrides({@AttributeOverride(name = EntryAuthorizationImpl.FIELD_ADJUSTMENT,column = @Column(name=COLUMN_ENTRY_AUTHORIZATION_ADJUSTMENT,nullable = false))})
	EntryAuthorizationImpl entryAuthorization;
	
	@Valid
	@Embedded
	@AttributeOverrides({@AttributeOverride(name = PaymentCreditImpl.FIELD_ADJUSTMENT,column = @Column(name=COLUMN_PAYMENT_CREDIT_ADJUSTMENT,nullable = false))})
	PaymentCreditImpl paymentCredit;
	
	@NotNull @ManyToOne @JoinColumn(name = COLUMN_BUDGETARY_ACT_VERSION)
	BudgetaryActVersionImpl budgetaryActVersion;

	@Transient String budgetaryActAsString;
	@Transient String budgetaryActVersionAsString;
	@Transient String sectionAsString;
	@Transient String natureAsString;
	@Transient String budgetSpecializationUnitAsString;
	@Transient String actionAsString;
	@Transient String activityAsString;
	@Transient String economicNatureAsString;
	@Transient String fundingSourceAsString;
	@Transient String lessorAsString;
	
	@Override
	public ExpenditureImpl setIdentifier(String identifier) {
		return (ExpenditureImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public Expenditure setEntryAuthorization(EntryAuthorization entryAuthorization) {
		this.entryAuthorization = (EntryAuthorizationImpl) entryAuthorization;
		return this;
	}
	
	public EntryAuthorizationImpl getEntryAuthorization(Boolean instantiateIfNull) {
		if(entryAuthorization == null && Boolean.TRUE.equals(instantiateIfNull))
			entryAuthorization = new EntryAuthorizationImpl();
		return entryAuthorization;
	}
	
	@Override
	public Expenditure setPaymentCredit(PaymentCredit paymentCredit) {
		this.paymentCredit = (PaymentCreditImpl) paymentCredit;
		return this;
	}
	
	public PaymentCreditImpl getPaymentCredit(Boolean instantiateIfNull) {
		if(paymentCredit == null && Boolean.TRUE.equals(instantiateIfNull))
			paymentCredit = new PaymentCreditImpl();
		return paymentCredit;
	}
	
	@Override
	public Expenditure setBudgetaryActVersion(BudgetaryActVersion budgetaryActVersion) {
		this.budgetaryActVersion = (BudgetaryActVersionImpl) budgetaryActVersion;
		return this;
	}
	
	public static final String FIELD_BUDGETARY_ACT_VERSION = "budgetaryActVersion";
	public static final String FIELD_ENTRY_AUTHORIZATION = "entryAuthorization";
	public static final String FIELD_PAYMENT_CREDIT = "paymentCredit";
	
	public static final String COLUMN_IDENTIFIER = "identifiant";
	public static final String COLUMN_ACTIVITY_IDENTIFIER = "activite";
	public static final String COLUMN_ECONOMIC_NATURE_IDENTIFIER = "nature_economique";
	public static final String COLUMN_FUNDING_SOURCE_IDENTIFIER = "source_financement";
	public static final String COLUMN_LESSOR_IDENTIFIER = "bailleur";
	public static final String COLUMN_ENTRY_AUTHORIZATION_ADJUSTMENT = EntryAuthorizationImpl.COLUMN_ADJUSTMENT;
	public static final String COLUMN_PAYMENT_CREDIT_ADJUSTMENT = PaymentCreditImpl.COLUMN_ADJUSTMENT;
	public static final String COLUMN_BUDGETARY_ACT_VERSION = "version_acte";
	
	public static final String ENTITY_NAME = "ExpenditureImpl";
	public static final String TABLE_NAME = "TA_LIGNE_DEPENSE";
}