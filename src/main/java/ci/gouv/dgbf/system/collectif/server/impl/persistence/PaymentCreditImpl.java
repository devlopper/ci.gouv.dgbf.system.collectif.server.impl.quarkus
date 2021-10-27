package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Embeddable;

import ci.gouv.dgbf.system.collectif.server.api.persistence.PaymentCredit;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class PaymentCreditImpl extends AbstractAmountsImpl implements PaymentCredit,Serializable {

	private static final String COLUMN_SUFFIX = "_CP";
	
	public static final String COLUMN_ADJUSTMENT = AbstractAmountsImpl.COLUMN_ADJUSTMENT+COLUMN_SUFFIX;
}