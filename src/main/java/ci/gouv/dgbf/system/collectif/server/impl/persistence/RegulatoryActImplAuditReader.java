package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.string.StringHelper;

public class RegulatoryActImplAuditReader extends RegulatoryActImplAuditsAsStringsReader implements Serializable {

	@Override
	protected void __set__(RegulatoryActImpl regulatoryAct, Object[] array) {
		super.__set__(regulatoryAct, array);
		if(StringHelper.isNotBlank(regulatoryAct.get__auditFunctionality__()) && StringHelper.isNotBlank(regulatoryAct.get__auditWho__())
				&& StringHelper.isNotBlank(regulatoryAct.get__auditWhenAsString__()))
			regulatoryAct.set__audit__(String.format(FORMAT, regulatoryAct.get__auditFunctionality__(),regulatoryAct.get__auditWho__(),regulatoryAct.get__auditWhenAsString__()));
		regulatoryAct.set__auditWho__(null);
		regulatoryAct.set__auditFunctionality__(null);
		regulatoryAct.set__auditWhat__(null);
		regulatoryAct.set__auditWhen__(null);
	}
	
	public static final String FORMAT = "%s par %s le %s";
}