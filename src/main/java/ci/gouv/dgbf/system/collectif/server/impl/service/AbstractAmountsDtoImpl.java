package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.__kernel__.object.AbstractObject;

import ci.gouv.dgbf.system.collectif.server.api.service.AmountsDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public abstract class AbstractAmountsDtoImpl extends AbstractObject implements AmountsDto,Serializable  {

	@JsonbProperty(value = JSON_ADJUSTMENT) Long adjustment;
	@JsonbProperty(value = JSON_ADJUSTMENT_LOWER_THAN_ZERO) Long adjustmentLowerThanZero;
	@JsonbProperty(value = JSON_ADJUSTMENT_GREATER_THAN_ZERO) Long adjustmentGreaterThanZero;
	@JsonbProperty(value = JSON_EXPECTED_ADJUSTMENT) Long expectedAdjustment;
	@JsonbProperty(value = JSON_EXPECTED_ADJUSTMENT_MINUS_ADJUSTMENT) Long expectedAdjustmentMinusAdjustment;
	@JsonbProperty(value = JSON_INITIAL) Long initial;
	@JsonbProperty(value = JSON_MOVEMENT) Long movement;
	@JsonbProperty(value = JSON_MOVEMENT_INCLUDED) Long movementIncluded;
	@JsonbProperty(value = JSON_ACTUAL) Long actual;
	@JsonbProperty(value = JSON_ACTUAL_MINUS_MOVEMENT_INCLUDED) Long actualMinusMovementIncluded;
	@JsonbProperty(value = JSON_ACTUAL_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT) Long actualMinusMovementIncludedPlusAdjustment;
	@JsonbProperty(value = JSON_AVAILABLE_MINUS_MOVEMENT_INCLUDED_PLUS_ADJUSTMENT) Long availableMinusMovementIncludedPlusAdjustment;
	@JsonbProperty(value = JSON_ACTUAL_PLUS_ADJUSTMENT) Long actualPlusAdjustment;
	@JsonbProperty(value = JSON_AVAILABLE) Long available;
}