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
	//@Column(name = COLUMN_MOVEMENT_INCLUDED) Long movementIncluded = 0l;
	@Column(name = COLUMN_ACTUAL) Long actual = 0l;
	//@Column(name = COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED) Long actualMinusMovementIncluded = 0l;
	//@Column(name = COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT) Long actualMinusMovementIncludedPlusAdjustment = 0l;
	@Column(name = COLUMN_AVAILABLE) Long available = 0l;
	
	public static final String FIELD_INITIAL = "initial";
	public static final String FIELD_MOVEMENT = "movement";
	public static final String FIELD_MOVEMENT_INCLUDED = "movementIncluded";
	public static final String FIELD_ACTUAL = "actual";
	public static final String FIELD_ACTUAL_MINUS_MOVEMENT_INCLUDED = "actualMinusMovementIncluded";
	public static final String FIELD_AVAILABLE = "available";
	public static final String FIELD_INITIAL_PLUS_ADJUSTMENT = "initialPlusAdjustment";	
	public static final String FIELD_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT = "actualMinusMovementIncludedPlusAdjustment";
	
	public static final String COLUMN_INITIAL = "BUDGET_INITIAL";
	public static final String COLUMN_MOVEMENT = "MOUVEMENT";
	public static final String COLUMN_MOVEMENT_INCLUDED = "MOUVEMENT_INCLUS";
	public static final String COLUMN_ACTUAL = "BUDGET_ACTUEL";	
	public static final String COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED = "BUDGET_ACTUEL_CALCULE";
	public static final String COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT = "COLLECTIF";
	public static final String COLUMN_AVAILABLE = "DISPONIBLE";
}