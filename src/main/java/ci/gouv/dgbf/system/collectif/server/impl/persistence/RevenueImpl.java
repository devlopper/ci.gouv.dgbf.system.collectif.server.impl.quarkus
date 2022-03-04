package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Embeddable;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Revenue;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class RevenueImpl extends AbstractResourceAmountsImpl implements Revenue,Serializable {

	@Override
	public RevenueImpl setAdjustment(Long adjustment) {
		return (RevenueImpl) super.setAdjustment(adjustment);
	}
	
	public static final String COLUMN_ADJUSTMENT = AbstractAmountsImpl.COLUMN_ADJUSTMENT;
	public static final String COLUMN_AVAILABLE = "MONTANT_"+AbstractAmountsImpl.COLUMN_AVAILABLE;
}