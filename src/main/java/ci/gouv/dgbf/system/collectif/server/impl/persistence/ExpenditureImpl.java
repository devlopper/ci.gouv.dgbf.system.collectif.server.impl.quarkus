package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;

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
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder.Arguments;
import org.cyk.utility.persistence.server.view.MaterializedViewManager;

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
		,@NamedQuery(name = ExpenditureImpl.QUERY_READ_BY_ACT_VERSION_IDENTIFIER,query = "SELECT t FROM ExpenditureImpl t WHERE t.actVersion.identifier = :actVersionIdentifier ORDER BY t.identifier ASC")
		,@NamedQuery(name = ExpenditureImpl.QUERY_READ_FOR_COPY_BY_ACT_VERSION_IDENTIFIER_BY_SOURCE_ACT_VERSION_IDENTIFIER
		,query = "SELECT d.identifier,s.entryAuthorization.adjustment,s.paymentCredit.adjustment FROM ExpenditureImpl d JOIN ExpenditureImpl s ON s.activityIdentifier = d.activityIdentifier AND s.economicNatureIdentifier = d.economicNatureIdentifier"
				+ " AND s.fundingSourceIdentifier = d.fundingSourceIdentifier AND s.lessorIdentifier = d.lessorIdentifier"
				+ " WHERE d.actVersion.identifier = :legislativeActVersionIdentifier AND s.actVersion.identifier = :legislativeActVersionSourceIdentifier"
				+ " AND (s.entryAuthorization.adjustment <> d.entryAuthorization.adjustment OR s.paymentCredit.adjustment <> d.paymentCredit.adjustment)"
				+ " ORDER BY d.identifier ASC")
})
@AttributeOverrides(value= {
		@AttributeOverride(name = ExpenditureImpl.FIELD___AUDIT_IDENTIFIER__,column = @Column(name=ExpenditureImpl.COLUMN___AUDIT_IDENTIFIER__,nullable = false))
		,@AttributeOverride(name = ExpenditureImpl.FIELD___AUDIT_WHO__,column = @Column(name=ExpenditureImpl.COLUMN___AUDIT_WHO__,nullable = false))
		,@AttributeOverride(name = ExpenditureImpl.FIELD___AUDIT_WHAT__,column = @Column(name=ExpenditureImpl.COLUMN___AUDIT_WHAT__,nullable = false))
		,@AttributeOverride(name = ExpenditureImpl.FIELD___AUDIT_WHEN__,column = @Column(name=ExpenditureImpl.COLUMN___AUDIT_WHEN__,nullable = false))
		,@AttributeOverride(name = ExpenditureImpl.FIELD___AUDIT_FUNCTIONALITY__,column = @Column(name=ExpenditureImpl.COLUMN___AUDIT_FUNCTIONALITY__,nullable = false))
})
@NamedStoredProcedureQueries(value = {
		@NamedStoredProcedureQuery(
				name = MaterializedViewManager.AbstractImpl.STORED_PROCEDURE_QUERY_PROCEDURE_NAME
				,procedureName = MaterializedViewManager.AbstractImpl.STORED_PROCEDURE_QUERY_PROCEDURE_NAME
				,parameters = {
					@StoredProcedureParameter(name = MaterializedViewManager.AbstractImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_TABLE , mode = ParameterMode.IN,type = String.class)
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
	public ExpenditureImpl setEntryAuthorization(EntryAuthorization entryAuthorization) {
		this.entryAuthorization = (EntryAuthorizationImpl) entryAuthorization;
		return this;
	}
	
	public EntryAuthorizationImpl getEntryAuthorization(Boolean instantiateIfNull) {
		if(entryAuthorization == null && Boolean.TRUE.equals(instantiateIfNull))
			entryAuthorization = new EntryAuthorizationImpl();
		return entryAuthorization;
	}
	
	@Override
	public ExpenditureImpl setPaymentCredit(PaymentCredit paymentCredit) {
		this.paymentCredit = (PaymentCreditImpl) paymentCredit;
		return this;
	}
	
	public PaymentCreditImpl getPaymentCredit(Boolean instantiateIfNull) {
		if(paymentCredit == null && Boolean.TRUE.equals(instantiateIfNull))
			paymentCredit = new PaymentCreditImpl();
		return paymentCredit;
	}
	
	@Override
	public ExpenditureImpl setActVersion(LegislativeActVersion actVersion) {
		this.actVersion = (LegislativeActVersionImpl) actVersion;
		return this;
	}
	
	@Override
	public ExpenditureImpl set__auditWho__(String __auditWho__) {
		return (ExpenditureImpl) super.set__auditWho__(__auditWho__);
	}
	
	@Override
	public ExpenditureImpl set__auditFunctionality__(String __auditFunctionality__) {
		return (ExpenditureImpl) super.set__auditFunctionality__(__auditFunctionality__);
	}
	
	@Override
	public ExpenditureImpl set__auditWhen__(LocalDateTime __auditWhen__) {
		return (ExpenditureImpl) super.set__auditWhen__(__auditWhen__);
	}
	
	public ExpenditureImpl copyAmounts(ExpenditureImportableView expenditure) {
		if(expenditure == null || (expenditure.entryAuthorization == null && expenditure.paymentCredit == null))
			return this;
		if(expenditure.entryAuthorization != null)
			getEntryAuthorization(Boolean.TRUE).copy(expenditure.entryAuthorization);
		if(expenditure.paymentCredit != null)
			getPaymentCredit(Boolean.TRUE).copy(expenditure.paymentCredit);
		return this;
	}
	
	public static Boolean areEqualByActivityEconomicNatureFundingSourceLessor(ExpenditureImpl expenditure1,ExpenditureImpl expenditure2) {
		if(expenditure1 == null || expenditure2 == null)
			return null;
		return expenditure1.getActivityIdentifier().equals(expenditure2.getActivityIdentifier()) && expenditure1.getEconomicNatureIdentifier().equals(expenditure2.getEconomicNatureIdentifier())
				&& expenditure1.getFundingSourceIdentifier().equals(expenditure2.getFundingSourceIdentifier()) && expenditure1.getLessorIdentifier().equals(expenditure2.getLessorIdentifier());
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
	public static final String FIELDS_AMOUNTS_WITHOUT_AVAILABLE = "amounts_without_available";
	public static final String FIELDS_AMOUNTS_INITIAL_ACTUAL_MOVEMENT_ADJUSTMENT_ACTUAL_PLUS_ADJUSTMENT = "amountsInitialActualMovementAdjustmentActualPlusAdjustment";
	public static final String FIELDS_AMOUNTS_INITIAL = "amountsInitial";
	public static final String FIELDS_AMOUNTS_ACTUAL = "amountsActual";
	public static final String FIELDS_AMOUNTS_AVAILABLE = "amountsAvailable";
	public static final String FIELDS_AMOUNTS_MOVEMENT_INCLUDED = "amountsMovementIncluded";
	public static final String FIELDS_AUDITS_AS_STRINGS = "auditsAsStrings";
		
	public static final String ENTITY_NAME = "ExpenditureImpl";
	public static final String TABLE_NAME = "TA_DEPENSE";
	public static final String VIEW_NAME = "VMA_DEPENSE";
	
	public static final String COLUMN_IDENTIFIER = "identifiant";
	public static final String COLUMN_ACTIVITY_IDENTIFIER = "activite";
	public static final String COLUMN_ECONOMIC_NATURE_IDENTIFIER = "nature_economique";
	public static final String COLUMN_FUNDING_SOURCE_IDENTIFIER = "source_financement";
	public static final String COLUMN_LESSOR_IDENTIFIER = "bailleur";
	public static final String COLUMN_ENTRY_AUTHORIZATION_ADJUSTMENT = EntryAuthorizationImpl.COLUMN_ADJUSTMENT;
	public static final String COLUMN_PAYMENT_CREDIT_ADJUSTMENT = PaymentCreditImpl.COLUMN_ADJUSTMENT;
	public static final String COLUMN_ACT_VERSION = "VERSION_COLLECTIF";
	
	public static final String COLUMN___AUDIT_IDENTIFIER__ = "AUDIT_IDENTIFIANT";
	public static final String COLUMN___AUDIT_WHO__ = "AUDIT_ACTEUR";
	public static final String COLUMN___AUDIT_WHAT__ = "AUDIT_ACTION";
	public static final String COLUMN___AUDIT_FUNCTIONALITY__ = "AUDIT_FONCTIONNALITE";
	public static final String COLUMN___AUDIT_WHEN__ = "AUDIT_DATE";
	
	public static final String QUERY_READ_BY_IDENTIIFERS = "ExpenditureImpl.readByIdentifiers";
	public static final String QUERY_READ_BY_ACT_VERSION_IDENTIFIER = "ExpenditureImpl.readByActVersionIdentifier";
	public static final String QUERY_READ_FOR_COPY_BY_ACT_VERSION_IDENTIFIER_BY_SOURCE_ACT_VERSION_IDENTIFIER = "ExpenditureImpl.readForCopyByActVersionIdentifierBySourceActVersionIdentifier";
	
	public static final String[] VIEW_FIELDS_NAMES = {FIELDS_STRINGS,FIELDS_AMOUNTS_INITIAL_ACTUAL_MOVEMENT_ADJUSTMENT_ACTUAL_PLUS_ADJUSTMENT};
	
	/**/
	
	public static interface RuntimeQueryStringBuilder {
		public static interface Join {
		
			static void joinRegulatoryActLegislativeActVersion(QueryExecutorArguments queryExecutorArguments,  Arguments builderArguments) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s lav ON lav = t.%s",LegislativeActVersionImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACT_VERSION));
				builderArguments.getTuple().addJoins(String.format("JOIN %s la ON la = lav.%s",LegislativeActImpl.ENTITY_NAME,LegislativeActVersionImpl.FIELD_ACT));
				builderArguments.getTuple().addJoins(String.format("LEFT JOIN %s exercise ON exercise.%s = la.%s",ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
				builderArguments.getTuple().addJoins("LEFT "+getJoinRegulatoryActExpenditure());
				builderArguments.getTuple().addJoins(String.format("LEFT JOIN %s ra ON ra.%s = rae.%s",RegulatoryActImpl.ENTITY_NAME,RegulatoryActImpl.FIELD_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ACT_IDENTIFIER));
				builderArguments.getTuple().addJoins(String.format("LEFT JOIN %s ralav ON ralav.%s = ra AND ralav.%s = lav",RegulatoryActLegislativeActVersionImpl.ENTITY_NAME,RegulatoryActLegislativeActVersionImpl.FIELD_REGULATORY_ACT
						,RegulatoryActLegislativeActVersionImpl.FIELD_LEGISLATIVE_ACT_VERSION));
			}
		}
	}
	
	public static String getJoinRegulatoryActExpenditure(String tupleVariableName,String exerciseTupleVariableName) {
		return String.format("JOIN %3$s rae ON rae.%4$s = exercise.%5$s AND rae.%6$s = t.%6$s AND rae.%7$s = t.%7$s AND rae.%8$s = t.%8$s AND rae.%9$s = t.%9$s", tupleVariableName,exerciseTupleVariableName
				,RegulatoryActExpenditureImpl.ENTITY_NAME,RegulatoryActExpenditureImpl.FIELD_YEAR,ExerciseImpl.FIELD_YEAR,RegulatoryActExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER
				,RegulatoryActExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER,RegulatoryActExpenditureImpl.FIELD_LESSOR_IDENTIFIER);
	}
	
	public static String getJoinRegulatoryActExpenditure() {
		return getJoinRegulatoryActExpenditure("t", "exercise");
	}
}