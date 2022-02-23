package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.cyk.utility.__kernel__.object.AbstractObject;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @MappedSuperclass
public abstract class AbstractAmountsView extends AbstractObject implements Serializable  {

	@Column(name = COLUMN_INITIAL) Long initial = 0l;
	@Column(name = COLUMN_MOVEMENT) Long movement = 0l;
	@Column(name = COLUMN_ACTUAL) Long actual = 0l;
	
	public static final String FIELD_INITIAL = "initial";
	public static final String FIELD_MOVEMENT = "movement";
	public static final String FIELD_ACTUAL = "actual";
	
	public static final String COLUMN_INITIAL = "BUDGET_INITIAL";
	public static final String COLUMN_MOVEMENT = "MOUVEMENT";
	public static final String COLUMN_ACTUAL = "BUDGET_ACTUEL";	
}