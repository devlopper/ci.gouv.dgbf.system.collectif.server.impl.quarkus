package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringAuditedImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActExpenditure;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = GeneratedActExpenditureImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=GeneratedActExpenditureImpl.TABLE_NAME,uniqueConstraints = {
		@UniqueConstraint(columnNames = {GeneratedActExpenditureImpl.COLUMN_ACT,GeneratedActExpenditureImpl.COLUMN_ACTIVITY_IDENTIFIER,GeneratedActExpenditureImpl.COLUMN_ECONOMIC_NATURE_IDENTIFIER
				,GeneratedActExpenditureImpl.COLUMN_FUNDING_SOURCE_IDENTIFIER,GeneratedActExpenditureImpl.COLUMN_LESSOR_IDENTIFIER})
})
@NamedQueries(value = {
		@NamedQuery(name = GeneratedActExpenditureImpl.QUERY_READ_BY_ACT_IDENTIIFERS,query = "SELECT t FROM GeneratedActExpenditureImpl t WHERE t.act.identifier IN :actIdentifiers")
})
@AttributeOverrides(value= {
		@AttributeOverride(name = GeneratedActExpenditureImpl.FIELD___AUDIT_WHO__,column = @Column(name=GeneratedActExpenditureImpl.COLUMN___AUDIT_WHO__,nullable = false))
		,@AttributeOverride(name = GeneratedActExpenditureImpl.FIELD___AUDIT_WHAT__,column = @Column(name=GeneratedActExpenditureImpl.COLUMN___AUDIT_WHAT__,nullable = false))
		,@AttributeOverride(name = GeneratedActExpenditureImpl.FIELD___AUDIT_WHEN__,column = @Column(name=GeneratedActExpenditureImpl.COLUMN___AUDIT_WHEN__,nullable = false))
		,@AttributeOverride(name = GeneratedActExpenditureImpl.FIELD___AUDIT_FUNCTIONALITY__,column = @Column(name=GeneratedActExpenditureImpl.COLUMN___AUDIT_FUNCTIONALITY__,nullable = false))
})
public class GeneratedActExpenditureImpl extends AbstractIdentifiableSystemScalarStringAuditedImpl implements GeneratedActExpenditure,Serializable {

	@NotNull @ManyToOne @JoinColumn(name = COLUMN_ACT,nullable = false,updatable = false) GeneratedActImpl act;
	@NotNull @Column(name = COLUMN_ACTIVITY_IDENTIFIER,nullable = false,updatable = false) String activityIdentifier;
	@NotNull @Column(name = COLUMN_ECONOMIC_NATURE_IDENTIFIER,nullable = false,updatable = false) String economicNatureIdentifier;
	@NotNull @Column(name = COLUMN_FUNDING_SOURCE_IDENTIFIER,nullable = false,updatable = false) String fundingSourceIdentifier;
	@NotNull @Column(name = COLUMN_LESSOR_IDENTIFIER,nullable = false,updatable = false) String lessorIdentifier;	
	@NotNull @Column(name = COLUMN_ENTRY_AUTHORIZATION_AMOUNT,nullable = false,updatable = false) Long entryAuthorizationAmount;
	@NotNull @Column(name = COLUMN_PAYMENT_CREDIT_AMOUNT,nullable = false,updatable = false) Long paymentCreditAmount;
	
	@Override
	public GeneratedActExpenditureImpl setIdentifier(String identifier) {
		return (GeneratedActExpenditureImpl) super.setIdentifier(identifier);
	}
	
	public static final String FIELD_ACT = "act";
	public static final String FIELD_ACTIVITY_IDENTIFIER = "activityIdentifier";
	public static final String FIELD_ECONOMIC_NATURE_IDENTIFIER = "economicNatureIdentifier";
	public static final String FIELD_FUNDING_SOURCE_IDENTIFIER = "fundingSourceIdentifier";
	public static final String FIELD_LESSOR_IDENTIFIER = "lessorIdentifier";
	public static final String FIELD_ENTRY_AUTHORIZATION_AMOUNT = "entryAuthorizationAmount";
	public static final String FIELD_PAYMENT_CREDIT_AMOUNT = "paymentCreditAmount";
	
	public static final String ENTITY_NAME = "GeneratedActExpenditureImpl";
	public static final String TABLE_NAME = "TA_ACTE_GENERE_DEPENSE";
	
	public static final String COLUMN_ACT = "ACTE_GENERE";
	public static final String COLUMN_ACTIVITY_IDENTIFIER = "activite";
	public static final String COLUMN_ECONOMIC_NATURE_IDENTIFIER = "nature_economique";
	public static final String COLUMN_FUNDING_SOURCE_IDENTIFIER = "source_financement";
	public static final String COLUMN_LESSOR_IDENTIFIER = "bailleur";
	public static final String COLUMN_ENTRY_AUTHORIZATION_AMOUNT = "montant_ae";
	public static final String COLUMN_PAYMENT_CREDIT_AMOUNT = "montant_cp";
	
	public static final String COLUMN___AUDIT_WHO__ = "AUDIT_ACTEUR";
	public static final String COLUMN___AUDIT_WHAT__ = "AUDIT_ACTION";
	public static final String COLUMN___AUDIT_FUNCTIONALITY__ = "AUDIT_FONCTIONNALITE";
	public static final String COLUMN___AUDIT_WHEN__ = "AUDIT_DATE";
	
	public static final String QUERY_READ_BY_ACT_IDENTIIFERS = "GeneratedActImpl.readByActIdentifiers";
}