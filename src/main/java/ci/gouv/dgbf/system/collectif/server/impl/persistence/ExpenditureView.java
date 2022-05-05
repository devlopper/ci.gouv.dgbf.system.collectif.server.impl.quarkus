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
@Entity(name = ExpenditureView.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ExpenditureView.TABLE_NAME)
@org.hibernate.annotations.Immutable
@NamedQueries(value = {
	@NamedQuery(name = ExpenditureView.QUERY_READ_IDENTIFIER_ACTIVITY_CODE_ECONOMIC_NATURE_CODE_FUNDING_SOURCE_CODE_LESSOR_CODE_BY_IDENTIFIERS
			,query = "SELECT t.identifier,t.activityCode,t.economicNatureCode,t.fundingSourceCode,t.lessorCode FROM ExpenditureView t WHERE t.identifier IN :identifiers")
})
public class ExpenditureView extends AbstractExpenditureView implements Serializable {

	public static final String ENTITY_NAME = "ExpenditureView";
	public static final String TABLE_NAME = "VMA_DEPENSE";
	
	public static final String QUERY_READ_IDENTIFIER_ACTIVITY_CODE_ECONOMIC_NATURE_CODE_FUNDING_SOURCE_CODE_LESSOR_CODE_BY_IDENTIFIERS = "ExpenditureView.readIdentifierActivityCodeEconomicNatureCodeFundingSourceCodeLessorCodeByIdentifiers";
}