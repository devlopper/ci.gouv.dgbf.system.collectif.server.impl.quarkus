package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.time.TimeHelper;

public class ExpenditureImplAuditsAsStringsReader extends ExpenditureImplAuditsReader implements Serializable {

	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		super.__set__(expenditure, array);
		expenditure.set__auditWhenAsString__(TimeHelper.formatLocalDateTime(expenditure.get__auditWhen__()));
		expenditure.set__auditWhen__(null);
	}
	
}