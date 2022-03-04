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
@Entity(name = ResourceImportableView.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name = ResourceImportableView.TABLE_NAME)
@org.hibernate.annotations.Immutable
@NamedQueries(value = {
		@NamedQuery(name = ResourceImportableView.QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER,query = "SELECT t.identifier,t.activityIdentifier,t.economicNatureIdentifier,t.revenue.initial,t.revenue.actual "
				+ "FROM ResourceImportableView t "+ResourceImportableView.QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_WHERE+" ORDER BY t.identifier ASC")
		,@NamedQuery(name = ResourceImportableView.QUERY_COUNT_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER,query = "SELECT COUNT(t.identifier) FROM ResourceImportableView t "+ResourceImportableView.QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_WHERE)
})
public class ResourceImportableView extends AbstractResourceView implements Serializable {

	public static final String ENTITY_NAME = "ResourceImportableView";
	public static final String TABLE_NAME = "VA_RESSOURCE_IMPORTABLE";
	
	public static final String QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER = "ResourceImportableView.readByLegislativeActVersionIdentifier";
	public static final String QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_WHERE = "WHERE t.legislativeActVersionIdentifier = :legislativeActVersionIdentifier";
	public static final String QUERY_COUNT_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER = "ResourceImportableView.countByLegislativeActVersionIdentifier";
}