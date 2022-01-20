package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class ExpenditureImplAuditReader extends ExpenditureImplAuditsAsStringsReader implements Serializable {

	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		super.__set__(expenditure, array);
		expenditure.set__audit__(String.format(FORMAT, expenditure.get__auditFunctionality__(),expenditure.get__auditWho__(),expenditure.get__auditWhenAsString__()));
		expenditure.set__auditWho__(null);
		expenditure.set__auditFunctionality__(null);
		expenditure.set__auditWhat__(null);
		expenditure.set__auditWhen__(null);
	}
	
	public static final String FORMAT = "%s par %s le %s";
}