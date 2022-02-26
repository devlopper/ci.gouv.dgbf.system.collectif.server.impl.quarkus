package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = RegulatoryActImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=RegulatoryActImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class RegulatoryActImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements RegulatoryAct,Serializable {

	@Column(name = COLUMN_YEAR) Short year;
	
	@Column(name = COLUMN_DATE) LocalDate date;
	@Transient String dateAsString;
	
	@Column(name = COLUMN_ENTRY_AUTHORIZATION_AMOUNT) Long entryAuthorizationAmount;
	@Column(name = COLUMN_PAYMENT_CREDIT_AMOUNT) Long paymentCreditAmount;
	@Transient Boolean included;
	@Transient String includedAsString;
	@Transient String legislativeActJoinIdentifier;
	@Transient Boolean applied;
	
	@Transient String __audit__;
	@Transient String __auditWho__;
	@Transient String __auditFunctionality__;
	@Transient String __auditWhat__;
	@Transient LocalDateTime __auditWhen__;
	@Transient String __auditWhenAsString__;
	
	@Override
	public RegulatoryActImpl setIdentifier(String identifier) {
		return (RegulatoryActImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public RegulatoryActImpl setCode(String code) {
		return (RegulatoryActImpl) super.setCode(code);
	}
	
	@Override
	public RegulatoryActImpl setName(String name) {
		return (RegulatoryActImpl) super.setName(name);
	}
	
	public static final String FIELD_YEAR = "year";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_DATE_AS_STRING = "dateAsString";
	public static final String FIELD_ENTRY_AUTHORIZATION_AMOUNT = "entryAuthorizationAmount";
	public static final String FIELD_PAYMENT_CREDIT_AMOUNT = "paymentCreditAmount";
	public static final String FIELD_INCLUDED = "included";
	public static final String FIELD_INCLUDED_AS_STRING = "includedAsString";
	public static final String FIELDS_CODE_NAME_YEAR_DATE_AS_STRING_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING = "codeNameYearDateAsStringEntryAuthorizationAmountPaymentCreditAmountIncludedAndIncludedAsString";
	public static final String FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING = "yearNameEntryAuthorizationAmountPaymentCreditAmountIncludedAndIncludedAsString";
	public static final String FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT = "yearNameEntryAuthorizationAmountPaymentCreditAmount";
	public static final String FIELDS_INCLUDED_AND_INCLUDED_AS_STRING = "includedAndIncludedAsString";
	public static final String FIELDS_AUDITS_AS_STRINGS = "auditsAsString";
	public static final String FIELD___AUDIT__ = "__audit__";
	
	public static final String ENTITY_NAME = "RegulatoryActImpl";
	public static final String TABLE_NAME = "VMA_ACTE_GESTION";
	
	public static final String COLUMN_YEAR = "exercice";
	public static final String COLUMN_DATE = "date_";
	public static final String COLUMN_ENTRY_AUTHORIZATION_AMOUNT = "montant_ae";
	public static final String COLUMN_PAYMENT_CREDIT_AMOUNT = "montant_cp";
}