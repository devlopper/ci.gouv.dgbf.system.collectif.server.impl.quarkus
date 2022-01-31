package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class LegislativeActImplAuditsAsStringsReader extends LegislativeActImplAuditsReader implements Serializable {

	@Override
	protected void __set__(LegislativeActImpl expenditure, Object[] array) {
		super.__set__(expenditure, array);
		__setAuditsAsStrings__(expenditure, array);
	}
	
}