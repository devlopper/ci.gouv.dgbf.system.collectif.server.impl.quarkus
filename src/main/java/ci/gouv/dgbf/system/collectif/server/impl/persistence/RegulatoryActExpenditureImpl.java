package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActExpenditure;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = RegulatoryActExpenditureImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=RegulatoryActExpenditureImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class RegulatoryActExpenditureImpl extends AbstractIdentifiableSystemScalarStringImpl implements RegulatoryActExpenditure,Serializable {

	@Column(name = COLUMN_ACT_IDENTIFIER) String actIdentifier;
	@Column(name = COLUMN_YEAR) Short year;
	@Column(name = COLUMN_ACTIVITY_IDENTIFIER) String activityIdentifier;
	@Column(name = COLUMN_ECONOMIC_NATURE_IDENTIFIER) String economicNatureIdentifier;
	@Column(name = COLUMN_FUNDING_SOURCE_IDENTIFIER) String fundingSourceIdentifier;
	@Column(name = COLUMN_LESSOR_IDENTIFIER) String lessorIdentifier;	
	@Column(name = COLUMN_ENTRY_AUTHORIZATION_AMOUNT) Long entryAuthorizationAmount;
	@Column(name = COLUMN_PAYMENT_CREDIT_AMOUNT) Long paymentCreditAmount;
	
	@Override
	public RegulatoryActExpenditureImpl setIdentifier(String identifier) {
		return (RegulatoryActExpenditureImpl) super.setIdentifier(identifier);
	}
	
	public static final String FIELD_ACT_IDENTIFIER = "actIdentifier";
	public static final String FIELD_YEAR = "year";
	public static final String FIELD_ACTIVITY_IDENTIFIER = "activityIdentifier";
	public static final String FIELD_ECONOMIC_NATURE_IDENTIFIER = "economicNatureIdentifier";
	public static final String FIELD_FUNDING_SOURCE_IDENTIFIER = "fundingSourceIdentifier";
	public static final String FIELD_LESSOR_IDENTIFIER = "lessorIdentifier";
	public static final String FIELD_ENTRY_AUTHORIZATION_AMOUNT = "entryAuthorizationAmount";
	public static final String FIELD_PAYMENT_CREDIT_AMOUNT = "paymentCreditAmount";
	
	public static final String ENTITY_NAME = "RegulatoryActExpenditureImpl";
	public static final String TABLE_NAME = "VMA_ACTE_GESTION_DEPENSE";
	
	public static final String COLUMN_ACT_IDENTIFIER = "acte";
	public static final String COLUMN_YEAR = "exercice";
	public static final String COLUMN_ACTIVITY_IDENTIFIER = "activite";
	public static final String COLUMN_ECONOMIC_NATURE_IDENTIFIER = "nature_economique";
	public static final String COLUMN_FUNDING_SOURCE_IDENTIFIER = "source_financement";
	public static final String COLUMN_LESSOR_IDENTIFIER = "bailleur";
	public static final String COLUMN_ENTRY_AUTHORIZATION_AMOUNT = "montant_ae";
	public static final String COLUMN_PAYMENT_CREDIT_AMOUNT = "montant_cp";
}