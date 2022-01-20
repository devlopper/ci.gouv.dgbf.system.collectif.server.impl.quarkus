package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class RevenueView extends AbstractAmountsView implements Serializable {

	public static final String COLUMN_ACTUAL = AbstractAmountsImpl.COLUMN_ACTUAL;
	public static final String COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED = AbstractAmountsImpl.COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED;
	public static final String COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT = AbstractAmountsImpl.COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT;
	public static final String COLUMN_AVAILABLE = AbstractAmountsImpl.COLUMN_AVAILABLE;
	public static final String COLUMN_INITIAL = AbstractAmountsImpl.COLUMN_INITIAL;
	public static final String COLUMN_MOVEMENT = AbstractAmountsImpl.COLUMN_MOVEMENT;
	public static final String COLUMN_MOVEMENT_INCLUDED = AbstractAmountsImpl.COLUMN_MOVEMENT_INCLUDED;
}