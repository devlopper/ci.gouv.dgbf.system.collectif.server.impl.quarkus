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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringAuditedImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.EntryAuthorization;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureAmountsEntryAuthorizationPaymentCredit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.PaymentCredit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ExpenditureImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ExpenditureImpl.TABLE_NAME,uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				ExpenditureImpl.COLUMN_ACT_VERSION,ExpenditureImpl.COLUMN_ACTIVITY_IDENTIFIER,ExpenditureImpl.COLUMN_ECONOMIC_NATURE_IDENTIFIER,ExpenditureImpl.COLUMN_FUNDING_SOURCE_IDENTIFIER,ExpenditureImpl.COLUMN_LESSOR_IDENTIFIER
		})
})
@NamedQueries(value = {
		@NamedQuery(name = ExpenditureImpl.QUERY_READ_BY_IDENTIIFERS,query = "SELECT t FROM ExpenditureImpl t WHERE t.identifier IN :identifiers")
})
@AttributeOverrides(value= {
		@AttributeOverride(name = ExpenditureImpl.FIELD___AUDIT_WHO__,column = @Column(name=ExpenditureImpl.COLUMN___AUDIT_WHO__,nullable = false))
		,@AttributeOverride(name = ExpenditureImpl.FIELD___AUDIT_WHAT__,column = @Column(name=ExpenditureImpl.COLUMN___AUDIT_WHAT__,nullable = false))
		,@AttributeOverride(name = ExpenditureImpl.FIELD___AUDIT_WHEN__,column = @Column(name=ExpenditureImpl.COLUMN___AUDIT_WHEN__,nullable = false))
		,@AttributeOverride(name = ExpenditureImpl.FIELD___AUDIT_FUNCTIONALITY__,column = @Column(name=ExpenditureImpl.COLUMN___AUDIT_FUNCTIONALITY__,nullable = false))
})
@NamedStoredProcedureQueries(value = {
		@NamedStoredProcedureQuery(
			name = ExpenditureImpl.STORED_PROCEDURE_QUERY_PROCEDURE_NAME_IMPORT
			,procedureName = ExpenditureImpl.STORED_PROCEDURE_QUERY_PROCEDURE_NAME_IMPORT
			,parameters = {
				@StoredProcedureParameter(name = ExpenditureImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_LEGISLATIVE_ACT_VERSION_IDENTIFIER , mode = ParameterMode.IN,type = String.class)
				,@StoredProcedureParameter(name = ExpenditureImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHO , mode = ParameterMode.IN,type = String.class)
				,@StoredProcedureParameter(name = ExpenditureImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_FUNCTIONALITY , mode = ParameterMode.IN,type = String.class)
				,@StoredProcedureParameter(name = ExpenditureImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHAT , mode = ParameterMode.IN,type = String.class)
				,@StoredProcedureParameter(name = ExpenditureImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHEN , mode = ParameterMode.IN,type = java.sql.Date.class)
			}
		)
	})
public class ExpenditureImpl extends AbstractIdentifiableSystemScalarStringAuditedImpl implements Expenditure,ExpenditureAmountsEntryAuthorizationPaymentCredit,Serializable {

	@NotNull @Column(name = COLUMN_ACTIVITY_IDENTIFIER,nullable = false) String activityIdentifier;
	@NotNull @Column(name = COLUMN_ECONOMIC_NATURE_IDENTIFIER,nullable = false) String economicNatureIdentifier;
	@NotNull @Column(name = COLUMN_FUNDING_SOURCE_IDENTIFIER,nullable = false) String fundingSourceIdentifier;
	@NotNull @Column(name = COLUMN_LESSOR_IDENTIFIER,nullable = false) String lessorIdentifier;
	
	@Valid
	@Embedded
	@AttributeOverrides({@AttributeOverride(name = EntryAuthorizationImpl.FIELD_ADJUSTMENT,column = @Column(name=COLUMN_ENTRY_AUTHORIZATION_ADJUSTMENT,nullable = false))})
	EntryAuthorizationImpl entryAuthorization;
	
	@Valid
	@Embedded
	@AttributeOverrides({@AttributeOverride(name = PaymentCreditImpl.FIELD_ADJUSTMENT,column = @Column(name=COLUMN_PAYMENT_CREDIT_ADJUSTMENT,nullable = false))})
	PaymentCreditImpl paymentCredit;
	
	@NotNull @ManyToOne @JoinColumn(name = COLUMN_ACT_VERSION)
	LegislativeActVersionImpl actVersion;
	
	@Transient String actAsString;
	@Transient String actVersionAsString;
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
	public Expenditure setActVersion(LegislativeActVersion actVersion) {
		this.actVersion = (LegislativeActVersionImpl) actVersion;
		return this;
	}
	
	public static final String FIELD_ACTIVITY_IDENTIFIER = "activityIdentifier";
	public static final String FIELD_ECONOMIC_NATURE_IDENTIFIER = "economicNatureIdentifier";
	public static final String FIELD_FUNDING_SOURCE_IDENTIFIER = "fundingSourceIdentifier";
	public static final String FIELD_LESSOR_IDENTIFIER = "lessorIdentifier";
	
	public static final String FIELD_ACT_VERSION = "actVersion";
	public static final String FIELD_ENTRY_AUTHORIZATION = "entryAuthorization";
	public static final String FIELD_PAYMENT_CREDIT = "paymentCredit";
	public static final String FIELD_NATURE_AS_STRING = "natureAsString";
	public static final String FIELD_SECTION_AS_STRING = "sectionAsString";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING = "budgetSpecializationUnitAsString";
	public static final String FIELDS_STRINGS = "strings";
	public static final String FIELDS_IDENTIFIERS = "identifiers";
	public static final String FIELDS_AMOUNTS = "amounts";
	public static final String FIELDS_AMOUNTS_INITIAL_ACTUAL_MOVEMENT_ADJUSTMENT_ACTUAL_PLUS_ADJUSTMENT = "amountsInitialActualMovementAdjustmentActualPlusAdjustment";
	public static final String FIELDS_AMOUNTS_INITIAL = "amountsInitial";
	public static final String FIELDS_AMOUNTS_ACTUAL = "amountsActual";
	public static final String FIELDS_AMOUNTS_AVAILABLE = "amountsAvailable";
	public static final String FIELDS_AMOUNTS_MOVEMENT_INCLUDED = "amountsMovementIncluded";
	public static final String FIELDS_AUDITS_AS_STRINGS = "auditsAsStrings";
		
	public static final String ENTITY_NAME = "ExpenditureImpl";
	public static final String TABLE_NAME = "TA_DEPENSE";
	
	public static final String COLUMN_IDENTIFIER = "identifiant";
	public static final String COLUMN_ACTIVITY_IDENTIFIER = "activite";
	public static final String COLUMN_ECONOMIC_NATURE_IDENTIFIER = "nature_economique";
	public static final String COLUMN_FUNDING_SOURCE_IDENTIFIER = "source_financement";
	public static final String COLUMN_LESSOR_IDENTIFIER = "bailleur";
	public static final String COLUMN_ENTRY_AUTHORIZATION_ADJUSTMENT = EntryAuthorizationImpl.COLUMN_ADJUSTMENT;
	public static final String COLUMN_PAYMENT_CREDIT_ADJUSTMENT = PaymentCreditImpl.COLUMN_ADJUSTMENT;
	public static final String COLUMN_ACT_VERSION = "VERSION_COLLECTIF";
	
	public static final String COLUMN___AUDIT_WHO__ = "AUDIT_ACTEUR";
	public static final String COLUMN___AUDIT_WHAT__ = "AUDIT_ACTION";
	public static final String COLUMN___AUDIT_FUNCTIONALITY__ = "AUDIT_FONCTIONNALITE";
	public static final String COLUMN___AUDIT_WHEN__ = "AUDIT_DATE";
	
	public static final String QUERY_READ_BY_IDENTIIFERS = "ExpenditureImpl.readByIdentifiers";
	
	public static final String[] VIEW_FIELDS_NAMES = {FIELDS_STRINGS,FIELDS_AMOUNTS_INITIAL_ACTUAL_MOVEMENT_ADJUSTMENT_ACTUAL_PLUS_ADJUSTMENT};
	
	public static final String STORED_PROCEDURE_QUERY_PROCEDURE_NAME_IMPORT = "PA_IMPORTER_DEPENSE";
	public static final String STORED_PROCEDURE_QUERY_PARAMETER_NAME_LEGISLATIVE_ACT_VERSION_IDENTIFIER = "p_version_collectif";
	public static final String STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHO = "audit_acteur";
	public static final String STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_FUNCTIONALITY = "audit_fonctionnalite";
	public static final String STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHAT = "audit_action";
	public static final String STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHEN = "audit_date";
}