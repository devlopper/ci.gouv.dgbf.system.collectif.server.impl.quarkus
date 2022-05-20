package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureAmountsEntryAuthorizationPaymentCredit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = LegislativeActVersionImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name = LegislativeActVersionImpl.TABLE_NAME,uniqueConstraints = {
		@UniqueConstraint(name = "UK_COLLECTIF_NUMERO",columnNames = {LegislativeActVersionImpl.COLUMN_ACT,LegislativeActVersionImpl.COLUMN_NUMBER})
})
@NamedQueries(value = {
		@NamedQuery(name = LegislativeActVersionImpl.QUERY_READ_BY_IDENTIIFER,query = "SELECT t FROM LegislativeActVersionImpl t WHERE t.identifier = :identifier")
})
@AttributeOverrides(value= {
		@AttributeOverride(name = LegislativeActVersionImpl.FIELD___AUDIT_IDENTIFIER__,column = @Column(name=LegislativeActVersionImpl.COLUMN___AUDIT_IDENTIFIER__,nullable = false))
		,@AttributeOverride(name = LegislativeActVersionImpl.FIELD___AUDIT_WHO__,column = @Column(name=LegislativeActVersionImpl.COLUMN___AUDIT_WHO__,nullable = false))
		,@AttributeOverride(name = LegislativeActVersionImpl.FIELD___AUDIT_WHAT__,column = @Column(name=LegislativeActVersionImpl.COLUMN___AUDIT_WHAT__,nullable = false))
		,@AttributeOverride(name = LegislativeActVersionImpl.FIELD___AUDIT_WHEN__,column = @Column(name=LegislativeActVersionImpl.COLUMN___AUDIT_WHEN__,nullable = false))
		,@AttributeOverride(name = LegislativeActVersionImpl.FIELD___AUDIT_FUNCTIONALITY__,column = @Column(name=LegislativeActVersionImpl.COLUMN___AUDIT_FUNCTIONALITY__,nullable = false))
})
@AuditOverrides({
	@AuditOverride(forClass = AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl.class)
})
public class LegislativeActVersionImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl implements LegislativeActVersion,ExpenditureAmountsEntryAuthorizationPaymentCredit,Serializable {

	@NotNull @ManyToOne @JoinColumn(name = COLUMN_ACT,nullable = false) @Audited LegislativeActImpl act;
	@Transient String actIdentifier;
	@Transient LocalDate actDate;
	@Transient Long actDateAsTimestamp;
	@Transient Short actDateYear;
	@Transient String actAsString;
	@Transient Long actFromDateAsTimestamp;
	
	@NotNull @Column(name = COLUMN_NUMBER,nullable = false) @Audited Byte number;
	@NotNull @Column(name = COLUMN_ADJUSTABLE,nullable = false) @Audited(withModifiedFlag = true,modifiedColumnName = COLUMN_ADJUSTABLE+"_MOD") Boolean adjustable;
	@Transient String adjustableAsString;
	
	@Transient Boolean isDefaultVersion;
	@Transient String isDefaultVersionAsString;
	
	/*
	 * This is used to handle non blocking processing
	 */
	//@Column(name = COLUMN_RUNNING_PROCESSING) Processing runningProcessing;
	
	@Transient LocalDateTime creationDate;
	@Transient String creationDateAsString;
	@Transient Short generatedActCount;
	@Transient Boolean actGeneratable;
	@Transient String actGeneratableAsString;
	@Transient Boolean generatedActDeletable;
	@Transient String generatedActDeletableAsString;
	@Transient RevenueImpl revenue;
	@Transient EntryAuthorizationImpl entryAuthorization;
	@Transient PaymentCreditImpl paymentCredit;
	
	@Override
	public LegislativeActVersionImpl setIdentifier(String identifier) {
		return (LegislativeActVersionImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public LegislativeActVersionImpl setCode(String code) {
		return (LegislativeActVersionImpl) super.setCode(code);
	}
	
	@Override
	public LegislativeActVersionImpl setName(String name) {
		return (LegislativeActVersionImpl) super.setName(name);
	}
	
	@Override
	public LegislativeActVersionImpl setAct(LegislativeAct act) {
		this.act = (LegislativeActImpl) act;
		return this;
	}
	
	@Override
	public LegislativeActVersionImpl setActDate(LocalDate actDate) {
		this.actDate = actDate;
		return this;
	}
	
	public RevenueImpl getRevenue(Boolean instantiateIfNull) {
		if(revenue == null && Boolean.TRUE.equals(instantiateIfNull))
			revenue = new RevenueImpl();
		return revenue;
	}
	
	public EntryAuthorizationImpl getEntryAuthorization(Boolean instantiateIfNull) {
		if(entryAuthorization == null && Boolean.TRUE.equals(instantiateIfNull))
			entryAuthorization = new EntryAuthorizationImpl();
		return entryAuthorization;
	}
	
	public PaymentCreditImpl getPaymentCredit(Boolean instantiateIfNull) {
		if(paymentCredit == null && Boolean.TRUE.equals(instantiateIfNull))
			paymentCredit = new PaymentCreditImpl();
		return paymentCredit;
	}
	
	//public static final String FIELD_ACT_GENERATION_MODE = "actGenerationMode";
	public static final String FIELD_ACT = "act";
	public static final String FIELD_ACT_IDENTIFIER = "actIdentifier";
	public static final String FIELD_ACT_DATE_AS_TIMESTAMP = "actDateAsTimestamp";
	public static final String FIELD_ACT_DATE = "actDate";
	public static final String FIELD_ACT_AS_STRING = "actAsString";
	public static final String FIELDS_ACT_FROM_DATE_AS_TIMESTAMP_DATE_AS_TIMESTAMP = "actFromDateAsTimestampDateAsTimestamp";
	public static final String FIELD_NUMBER = "number";
	public static final String FIELD_ADJUSTABLE = "adjustable";
	public static final String FIELD_ADJUSTABLE_AS_STRING = "adjustableAsString";
	public static final String FIELD_IS_DEFAULT_VERSION = "isDefaultVersion";
	public static final String FIELD_IS_DEFAULT_VERSION_AS_STRING = "isDefaultVersionAsString";
	public static final String FIELD_CREATION_DATE = "creationDate";
	public static final String FIELD_EXPENDITURE_NATURES = "expenditureNatures";
	public static final String FIELD_EXPENDITURE_NATURES_SUMS_AND_TOTAL = "expenditureNaturesSumsAndTotal";
	public static final String FIELD_GENERATED_ACT_COUNT = "generatedActCount";
	public static final String FIELD_ACT_GENERATABLE = "actGeneratable";
	public static final String FIELD_GENERATED_ACT_DELETABLE = "generatedActDeletable";
	public static final String FIELDS_GENERATED_ACT_COUNT_ACT_GENERATABLE_GENERATED_ACT_DELETABLE = "generatedActCountActGeneratableGeneratedActDeletable";
	public static final String FIELDS_ACT_AS_STRING_CODE_NAME_NUMBER = "actAsStringCodeNameNumberCreationDateAsString";
	public static final String FIELDS_CODE_NAME_NUMBER = "codeNameNumberCreationDateAsString";
	public static final String FIELDS_STRINGS = "strings";
	public static final String FIELDS_AMOUNTS = "amounts";
	public static final String FIELDS_EXPENDITURES_AMOUNTS = "expendituresAmounts";
	public static final String FIELDS_EXPENDITURES_AMOUNTS_WITHOUT_INCLUDED_MOVEMENT_AND_AVAILABLE = "expendituresAmountsWithoutIncludedMovementAndAvailable";
	public static final String FIELDS_EXPENDITURES_AMOUNTS_WITHOUT_AVAILABLE = "expendituresAmountsWithoutAvailable";
	public static final String FIELDS_EXPENDITURES_AMOUNTS_WITH_AVAILABLE_ONLY = "expendituresAmountsWithAvailableOnly";
	public static final String FIELDS_EXPENDITURES_AMOUNTS_WITH_INCLUDED_MOVEMENT_ONLY = "expendituresAmountsWithIncludedMovementOnly";
	public static final String FIELDS_EXPENDITURES_AMOUNTS_WITH_ADJUSTMENT_LESS_THAN_ZERO_GREATER_THAN_ZERO_ONLY = "expendituresAmountsWithAdjustmentLowerThanZeroGreaterThanZeroOnly";
	public static final String FIELDS_EXPENDITURES_ENTRY_AUTHORIZATION_AMOUNTS = "expendituresEntryAuthorizationAmounts";
	public static final String FIELDS_EXPENDITURES_PAYMENT_CREDIT_AMOUNTS = "expendituresPaymentCreditAmounts";
	public static final String FIELDS_RESOURCES_AMOUNTS = "resourcesAmounts";
	//public static final String FIELDS_AMOUNTS_SUMS = "amountsSums";
	public static final String FIELDS_AMOUNTS_AVAILABLE = "amountsAvailable";
	
	public static final String ENTITY_NAME = "LegislativeActVersionImpl";
	public static final String TABLE_NAME = "TA_VERSION_COLLECTIF";
	
	public static final String COLUMN_RUNNING_PROCESSING = "TRAITEMENT_EN_COURS_EXECUTION";
	public static final String COLUMN_ACT = "COLLECTIF";
	public static final String COLUMN_NUMBER = "NUMERO";
	public static final String COLUMN_ADJUSTABLE = "AJUSTABLE";
	public static final String COLUMN___AUDIT_IDENTIFIER__ = "AUDIT_IDENTIFIANT";
	public static final String COLUMN___AUDIT_WHO__ = "AUDIT_ACTEUR";
	public static final String COLUMN___AUDIT_WHAT__ = "AUDIT_ACTION";
	public static final String COLUMN___AUDIT_FUNCTIONALITY__ = "AUDIT_FONCTIONNALITE";
	public static final String COLUMN___AUDIT_WHEN__ = "AUDIT_DATE";
	
	public static final String QUERY_READ_BY_IDENTIIFER = "LegislativeActVersionImpl.readByIdentifier";
}