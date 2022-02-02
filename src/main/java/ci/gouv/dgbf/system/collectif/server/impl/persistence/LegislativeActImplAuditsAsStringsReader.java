package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class LegislativeActImplAuditsAsStringsReader extends LegislativeActImplAuditsReader implements Serializable {

	@Override
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array) {
		super.__set__(legislativeAct, array);
		__setAuditsAsStrings__(legislativeAct, array);
	}
	
}