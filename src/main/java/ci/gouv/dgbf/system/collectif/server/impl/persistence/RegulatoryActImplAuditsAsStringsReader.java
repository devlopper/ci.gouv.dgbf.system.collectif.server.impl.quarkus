package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.time.TimeHelper;

public class RegulatoryActImplAuditsAsStringsReader extends RegulatoryActImplAuditsReader implements Serializable {

	@Override
	protected void __set__(RegulatoryActImpl regulatoryAct, Object[] array) {
		super.__set__(regulatoryAct, array);
		if(regulatoryAct.get__auditWhen__() != null)
			regulatoryAct.set__auditWhenAsString__(TimeHelper.formatLocalDateTime(regulatoryAct.get__auditWhen__()));
		regulatoryAct.set__auditWhen__(null);
	}
}