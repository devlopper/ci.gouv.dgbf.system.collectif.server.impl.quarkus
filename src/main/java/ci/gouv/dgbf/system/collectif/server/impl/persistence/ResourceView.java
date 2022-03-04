package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ResourceView.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ResourceView.TABLE_NAME)
@org.hibernate.annotations.Immutable
public class ResourceView extends AbstractResourceView implements Serializable {
	
	public static final String ENTITY_NAME = "ResourceView";
	public static final String TABLE_NAME = "VMA_RESSOURCE";
}