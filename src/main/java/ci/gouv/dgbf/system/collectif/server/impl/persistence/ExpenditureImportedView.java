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
@Entity(name = ExpenditureImportedView.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name = ExpenditureImportedView.TABLE_NAME)
@org.hibernate.annotations.Immutable
public class ExpenditureImportedView extends AbstractExpenditureView implements Serializable {

	public static final String ENTITY_NAME = "ExpenditureImportedView";
	public static final String TABLE_NAME = "VA_DEPENSE_IMPORTEE";
}