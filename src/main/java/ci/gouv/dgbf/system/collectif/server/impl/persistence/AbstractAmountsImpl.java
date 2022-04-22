package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.object.AbstractObject;
import org.hibernate.envers.Audited;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Amounts;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @MappedSuperclass @Audited(withModifiedFlag = true)
public abstract class AbstractAmountsImpl extends AbstractObject implements Amounts,Serializable  {

	@NotNull @Column(name = COLUMN_ADJUSTMENT,nullable = false) Long adjustment = 0l;
	
	@Transient Long expectedAdjustment = 0l;
	@Transient Long expectedAdjustmentMinusAdjustment = 0l;
	
	@Transient Long initial = 0l;
	@Transient Long movement = 0l;
	@Transient Long movementIncluded = 0l;
	@Transient Long actual = 0l;
	@Transient Long actualMinusMovementIncluded = 0l;
	@Transient Long available = 0l;	
	@Transient Long actualPlusAdjustment = 0l;
	@Transient Long actualMinusMovementIncludedPlusAdjustment = 0l;
	@Transient Long availableMinusMovementIncludedPlusAdjustment = 0l;
	
	public AbstractAmountsImpl copy(AbstractAmountsView amounts) {
		if(amounts == null)
			return this;
		initial = amounts.initial;
		movement = amounts.movement;
		actual = amounts.actual;
		return this;
	}
	
	void nullify() {
		adjustment = null;
		expectedAdjustment = null;
		expectedAdjustmentMinusAdjustment = null;
		
		initial = null;
		movement = null;
		movementIncluded = null;
		actual = null;
		actualMinusMovementIncluded = null;
		available = null;	
		actualPlusAdjustment = null;
		actualMinusMovementIncludedPlusAdjustment = null;
		availableMinusMovementIncludedPlusAdjustment = null;
	}
	
	@Override
	public String toString() {
		return String.format(STRING_FORMAT, initial,movement,actual,movementIncluded,adjustment,actualMinusMovementIncludedPlusAdjustment,available,availableMinusMovementIncludedPlusAdjustment);
	}
	
	private static final String STRING_FORMAT = "INI=%s|MVT=%s|ACT=%s|MVI=%s|ADJ=%s|ACT-MVI+ADJ=%s|AVA=%s|AVA-MVI+ADJ=%s";
	
	public static final String FIELD_INITIAL = "initial";
	public static final String FIELD_MOVEMENT = "movement";
	public static final String FIELD_MOVEMENT_INCLUDED = "movementIncluded";
	public static final String FIELD_ACTUAL = "actual";
	public static final String FIELD_ACTUAL_MINUS_MOVEMENT_INCLUDED = "actualMinusMovementIncluded";
	public static final String FIELD_AVAILABLE = "available";
	public static final String FIELD_ADJUSTMENT = "adjustment";
	public static final String FIELD_EXPECTED_ADJUSTMENT = "expectedAdjustment";
	public static final String FIELD_EXPECTED_ADJUSTMENT_MINUS_ADJUSTMENT = "expectedAdjustmentMinusAdjustment";
	public static final String FIELD_INITIAL_PLUS_ADJUSTMENT = "initialPlusAdjustment";	
	public static final String FIELD_ACTUAL_PLUS_ADJUSTMENT = "actualPlusAdjustment";
	public static final String FIELD_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT = "actualMinusMovementIncludedPlusAdjustment";
	public static final String FIELD_AVAILABLE_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT = "availableMinusMovementIncludedPlusAdjustment";
	
	public static final String FIELD_MOVEMENT_VIEW = "movementView";
	public static final String FIELD_MOVEMENT_SELECTED = "movementSelected";
	public static final String FIELD_INITIAL_PLUS_MOVEMENT_SELECTED_PLUS_ADJUSTMENT = "initialPlusMovementSelectedPlusAdjustment";

	public static final String COLUMN_INITIAL = "BUDGET_INITIAL";
	public static final String COLUMN_MOVEMENT = "MOUVEMENT";
	public static final String COLUMN_ACTUAL = "BUDGET_ACTUEL";
	public static final String COLUMN_MOVEMENT_INCLUDED = "MOUVEMENT_INCLUS";
	public static final String COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED = "BUDGET_ACTUEL_CALCULE";
	public static final String COLUMN_AVAILABLE = "DISPONIBLE";
	public static final String COLUMN_ADJUSTMENT = "AJUSTEMENT";
	public static final String COLUMN_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT = "COLLECTIF";
}