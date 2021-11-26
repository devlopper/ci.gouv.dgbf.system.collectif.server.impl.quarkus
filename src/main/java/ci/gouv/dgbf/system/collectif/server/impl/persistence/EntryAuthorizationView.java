package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class EntryAuthorizationView extends AbstractAmountsView implements Serializable {

	private static final String COLUMN_SUFFIX = "_AE";
	
	public static final String COLUMN_ACTUAL = AbstractAmountsImpl.COLUMN_ACTUAL+COLUMN_SUFFIX;
	public static final String COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED = AbstractAmountsImpl.COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED+COLUMN_SUFFIX;
	public static final String COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT = AbstractAmountsImpl.COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT+COLUMN_SUFFIX;
	public static final String COLUMN_AVAILABLE = AbstractAmountsImpl.COLUMN_AVAILABLE+COLUMN_SUFFIX;
	public static final String COLUMN_INITIAL = AbstractAmountsImpl.COLUMN_INITIAL+COLUMN_SUFFIX;
	public static final String COLUMN_MOVEMENT = AbstractAmountsImpl.COLUMN_MOVEMENT+COLUMN_SUFFIX;
	public static final String COLUMN_MOVEMENT_INCLUDED = AbstractAmountsImpl.COLUMN_MOVEMENT_INCLUDED+COLUMN_SUFFIX;
}