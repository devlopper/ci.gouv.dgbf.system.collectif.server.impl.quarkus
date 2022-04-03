package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureAmounts;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @MappedSuperclass
public abstract class AbstractExpenditureAmountsImpl extends AbstractAmountsImpl implements ExpenditureAmounts,Serializable {

	@Override
	public String toString() {
		return String.format(STRING_FORMAT, initial,movement,actual,movementIncluded,adjustment,actualMinusMovementIncludedPlusAdjustment,available,availableMinusMovementIncludedPlusAdjustment);
	}
	
	private static final String STRING_FORMAT = "INI=%s|MVT=%s|ACT=%s|MVI=%s|ADJ=%s|ACT-MVI+ADJ=%s|AVA=%s|AVA-MVI+ADJ=%s";
}