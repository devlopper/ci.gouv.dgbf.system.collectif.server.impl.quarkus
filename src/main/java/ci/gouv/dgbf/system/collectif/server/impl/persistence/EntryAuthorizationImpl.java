package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Embeddable;

import ci.gouv.dgbf.system.collectif.server.api.persistence.EntryAuthorization;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class EntryAuthorizationImpl extends AbstractExpenditureAmountsImpl implements EntryAuthorization,Serializable {

	@Override
	public EntryAuthorizationImpl setInitial(Long initial) {
		return (EntryAuthorizationImpl) super.setInitial(initial);
	}
	
	@Override
	public EntryAuthorizationImpl setActual(Long actual) {
		return (EntryAuthorizationImpl) super.setActual(actual);
	}
	
	@Override
	public EntryAuthorizationImpl setAdjustment(Long adjustment) {
		return (EntryAuthorizationImpl) super.setAdjustment(adjustment);
	}
	
	private static final String COLUMN_SUFFIX = "_AE";
	
	public static final String COLUMN_ADJUSTMENT = AbstractAmountsImpl.COLUMN_ADJUSTMENT+COLUMN_SUFFIX;
	public static final String COLUMN_AVAILABLE = "MONTANT_"+AbstractAmountsImpl.COLUMN_AVAILABLE+COLUMN_SUFFIX;
}