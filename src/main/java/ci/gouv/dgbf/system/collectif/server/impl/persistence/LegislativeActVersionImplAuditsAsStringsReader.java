package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class LegislativeActVersionImplAuditsAsStringsReader extends LegislativeActVersionImplAuditsReader implements Serializable {

	@Override
	protected void __set__(LegislativeActVersionImpl legislativeActVersion, Object[] array) {
		super.__set__(legislativeActVersion, array);
		__setAuditsAsStrings__(legislativeActVersion, array);
	}
	
}