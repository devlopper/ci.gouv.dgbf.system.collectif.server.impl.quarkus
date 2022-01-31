package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class ExpenditureImplAuditReader extends ExpenditureImplAuditsAsStringsReader implements Serializable {

	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		super.__set__(expenditure, array);
		__setAudit__(expenditure, array);
	}

}