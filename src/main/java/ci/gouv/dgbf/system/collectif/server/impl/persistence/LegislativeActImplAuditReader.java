package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class LegislativeActImplAuditReader extends LegislativeActImplAuditsAsStringsReader implements Serializable {

	@Override
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array) {
		super.__set__(legislativeAct, array);
		__setAudit__(legislativeAct, array);
	}

}