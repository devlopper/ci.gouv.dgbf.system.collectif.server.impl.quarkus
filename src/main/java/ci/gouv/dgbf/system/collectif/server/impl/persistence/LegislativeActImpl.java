package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureAmountsEntryAuthorizationPaymentCredit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = LegislativeActImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=LegislativeActImpl.TABLE_NAME)
@NamedQueries(value = {
		@NamedQuery(name = LegislativeActImpl.QUERY_READ_BY_IDENTIIFER,query = "SELECT t FROM LegislativeActImpl t WHERE t.identifier = :identifier")
})
@AttributeOverrides(value= {
		@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_WHO__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_WHO__,nullable = false))
		,@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_WHAT__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_WHAT__,nullable = false))
		,@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_WHEN__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_WHEN__,nullable = false))
		,@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_FUNCTIONALITY__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_FUNCTIONALITY__,nullable = false))
})
public class LegislativeActImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl implements LegislativeAct,ExpenditureAmountsEntryAuthorizationPaymentCredit,Serializable {

	@Column(name = COLUMN_EXERCISE) String exerciseIdentifier;
	@Transient ExerciseImpl exercise;
	@Transient Short exerciseYear;
	@Transient String exerciseAsString;
	
	@NotNull @Column(name = COLUMN_NUMBER,nullable = false) Byte number;
	
	@NotNull @Column(name = COLUMN_DATE,nullable = false) LocalDate date;
	
	@ManyToOne @JoinColumn(name = COLUMN_DEFAULT_VERSION) LegislativeActVersionImpl defaultVersion;
	@Transient String defaultVersionAsString;
	@Transient String defaultVersionIdentifier;
	
	@NotNull @Column(name = COLUMN_IN_PROGRESS,nullable = false) Boolean inProgress;
	@Transient String inProgressAsString;
	
	@Valid
	@Embedded
	@AttributeOverrides({@AttributeOverride(name = EntryAuthorizationImpl.FIELD_ADJUSTMENT,column = @Column(name=COLUMN_EXPECTED_ENTRY_AUTHORIZATION_ADJUSTMENT,nullable = true))})
	EntryAuthorizationImpl entryAuthorization;
	
	@Valid
	@Embedded
	@AttributeOverrides({@AttributeOverride(name = PaymentCreditImpl.FIELD_ADJUSTMENT,column = @Column(name=COLUMN_EXPECTED_PAYMENT_CREDIT_ADJUSTMENT,nullable = true))})
	PaymentCreditImpl paymentCredit;
	
	@Column(name = COLUMN_ACT_GENERATION_MODE) @Enumerated(EnumType.STRING) ActGenerationMode actGenerationMode;
	
	@Column(name = COLUMN_AVAILABLE_MONITORABLE) Boolean availableMonitorable;
	@Transient String availableMonitorableAsString;
	
	@Override
	public LegislativeActImpl setIdentifier(String identifier) {
		return (LegislativeActImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public LegislativeActImpl setCode(String code) {
		return (LegislativeActImpl) super.setCode(code);
	}
	
	@Override
	public LegislativeActImpl setName(String name) {
		return (LegislativeActImpl) super.setName(name);
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
	
	public static final String FIELD_DATE = "date";
	public static final String FIELD_NUMBER = "number";
	public static final String FIELD_ACT_GENERATION_MODE = "actGenerationMode";
	public static final String FIELD_EXERCISE_IDENTIFIER = "exerciseIdentifier";
	public static final String FIELD_EXERCISE_YEAR = "exerciseYear";
	public static final String FIELD_EXERCISE = "exercise";
	public static final String FIELD_DEFAULT_VERSION = "defaultVersion";
	public static final String FIELD_IN_PROGRESS = "inProgress";
	public static final String FIELD_IN_PROGRESS_AS_STRING = "inProgressAsString";
	public static final String FIELD_DEFAULT_VERSION_IDENTIFIER = "defaultVersionIdentifier";
	public static final String FIELD_ENTRY_AUTHORIZATION = "entryAuthorization";
	public static final String FIELD_PAYMENT_CREDIT = "paymentCredit";
	public static final String FIELD_AVAILABLE_MONITORABLE = "availableMonitorable";
	public static final String FIELDS_AMOUNTS_AVAILABLE = "amountsAvailable";
	public static final String FIELDS_STRINGS = "strings";
	public static final String FIELDS_AMOUNTS = "amounts";
	public static final String FIELDS_AMOUNTS_MOVEMENT_INCLUDED = "amountsMovementIncluded";
	
	public static final String FIELD_SIGNATORY = "signatory";
	public static final String FIELD_REFERENCE = "reference";
	public static final String FIELD_VERSIONS = "versions";
	
	public static final String ENTITY_NAME = "LegislativeActImpl";
	public static final String TABLE_NAME = "TA_COLLECTIF";
	
	public static final String COLUMN_DATE = "DATE";
	public static final String COLUMN_ACT_GENERATION_MODE = "MODE_GENERATION_ACTE";
	public static final String COLUMN_EXERCISE = "EXERCICE";
	public static final String COLUMN_DEFAULT_VERSION = "VERSION_PAR_DEFAUT";
	public static final String COLUMN_IN_PROGRESS = "EN_COURS";
	public static final String COLUMN_EXPECTED_ENTRY_AUTHORIZATION_ADJUSTMENT = "AJUSTEMENT_AE_ATTENDU";
	public static final String COLUMN_EXPECTED_PAYMENT_CREDIT_ADJUSTMENT = "AJUSTEMENT_CP_ATTENDU";
	public static final String COLUMN_SIGNATORY = "SIGNATAIRE";
	public static final String COLUMN_REFERENCE = "REFERENCE";
	public static final String COLUMN_AVAILABLE_MONITORABLE = "DISPONIBLE_SURVEILLABLE";
	public static final String COLUMN_NUMBER = "NUMERO";
	
	public static final String COLUMN___AUDIT_WHO__ = "AUDIT_ACTEUR";
	public static final String COLUMN___AUDIT_WHAT__ = "AUDIT_ACTION";
	public static final String COLUMN___AUDIT_FUNCTIONALITY__ = "AUDIT_FONCTIONNALITE";
	public static final String COLUMN___AUDIT_WHEN__ = "AUDIT_DATE";
	
	public static final String QUERY_READ_BY_IDENTIIFER = "LegislativeActImpl.readByIdentifier";
}