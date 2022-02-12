package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ExpenditureImportableView.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name = ExpenditureImportableView.TABLE_NAME)
@org.hibernate.annotations.Immutable
@NamedQueries(value = {
		@NamedQuery(name = ExpenditureImportableView.QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER,query = "SELECT t.identifier,t.activityIdentifier,t.economicNatureIdentifier,t.fundingSourceIdentifier,t.lessorIdentifier"
				+ ",t.entryAuthorization.initial,t.entryAuthorization.actual,t.paymentCredit.initial,t.paymentCredit.actual "
				+ "FROM ExpenditureImportableView t "+ExpenditureImportableView.QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_WHERE+" ORDER BY t.identifier ASC")
		,@NamedQuery(name = ExpenditureImportableView.QUERY_COUNT_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER,query = "SELECT COUNT(t.identifier) FROM ExpenditureImportableView t "+ExpenditureImportableView.QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_WHERE)
})
public class ExpenditureImportableView extends AbstractExpenditureView implements Serializable {

	public static final String ENTITY_NAME = "ExpenditureImportableView";
	public static final String TABLE_NAME = "VA_DEPENSE_IMPORTABLE";
	
	public static final String QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER = "ExpenditureImportableView.readByLegislativeActVersionIdentifier";
	public static final String QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_WHERE = "WHERE t.legislativeActVersionIdentifier = :legislativeActVersionIdentifier";
	public static final String QUERY_COUNT_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER = "ExpenditureImportableView.countByLegislativeActVersionIdentifier";
}