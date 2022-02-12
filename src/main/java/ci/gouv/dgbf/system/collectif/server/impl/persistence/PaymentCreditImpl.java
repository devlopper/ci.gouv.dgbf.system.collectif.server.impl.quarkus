package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Embeddable;

import ci.gouv.dgbf.system.collectif.server.api.persistence.PaymentCredit;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class PaymentCreditImpl extends AbstractExpenditureAmountsImpl implements PaymentCredit,Serializable {

	@Override
	public PaymentCreditImpl setInitial(Long initial) {
		return (PaymentCreditImpl) super.setInitial(initial);
	}
	
	@Override
	public PaymentCreditImpl setActual(Long actual) {
		return (PaymentCreditImpl) super.setActual(actual);
	}
	
	private static final String COLUMN_SUFFIX = "_CP";
	
	public static final String COLUMN_ADJUSTMENT = AbstractAmountsImpl.COLUMN_ADJUSTMENT+COLUMN_SUFFIX;
	public static final String COLUMN_AVAILABLE = "MONTANT_"+AbstractAmountsImpl.COLUMN_AVAILABLE+COLUMN_SUFFIX;
}