package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ExpenditureAvailableView.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ExpenditureAvailableView.TABLE_NAME)
@org.hibernate.annotations.Immutable
public class ExpenditureAvailableView extends AbstractIdentifiableSystemScalarStringImpl implements Serializable{

	@Column(name = COLUMN_YEAR) Short year;
	@Column(name = COLUMN_ACTIVITY_IDENTIFIER) String activityIdentifier;
	@Column(name = COLUMN_ECONOMIC_NATURE_IDENTIFIER) String economicNatureIdentifier;
	@Column(name = COLUMN_FUNDING_SOURCE_IDENTIFIER) String fundingSourceIdentifier;
	@Column(name = COLUMN_LESSOR_IDENTIFIER) String lessorIdentifier;	
	@Column(name = COLUMN_ENTRY_AUTHORIZATION) Long entryAuthorization;
	@Column(name = COLUMN_PAYMENT_CREDIT) Long paymentCredit;

	public static final String FIELD_YEAR = "year";
	public static final String FIELD_ACTIVITY_IDENTIFIER = "activityIdentifier";
	public static final String FIELD_ECONOMIC_NATURE_IDENTIFIER = "economicNatureIdentifier";
	public static final String FIELD_FUNDING_SOURCE_IDENTIFIER = "fundingSourceIdentifier";
	public static final String FIELD_LESSOR_IDENTIFIER = "lessorIdentifier";
	public static final String FIELD_ENTRY_AUTHORIZATION = "entryAuthorization";
	public static final String FIELD_PAYMENT_CREDIT = "paymentCredit";
	
	public static final String ENTITY_NAME = "ExpenditureAvailableView";
	public static final String TABLE_NAME = "VA_DEPENSE_DISPONIBLE";
	
	public static final String COLUMN_YEAR = "exercice";
	public static final String COLUMN_ACTIVITY_IDENTIFIER = "activite";
	public static final String COLUMN_ECONOMIC_NATURE_IDENTIFIER = "nature_economique";
	public static final String COLUMN_FUNDING_SOURCE_IDENTIFIER = "source_financement";
	public static final String COLUMN_LESSOR_IDENTIFIER = "bailleur";
	public static final String COLUMN_ENTRY_AUTHORIZATION = "AE";
	public static final String COLUMN_PAYMENT_CREDIT = "CP";
}