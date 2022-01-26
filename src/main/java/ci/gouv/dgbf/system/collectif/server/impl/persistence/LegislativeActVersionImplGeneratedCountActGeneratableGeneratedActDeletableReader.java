package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class LegislativeActVersionImplGeneratedCountActGeneratableGeneratedActDeletableReader extends LegislativeActVersionImplGeneratedCountReader implements Serializable {

	@Override
	protected void __set__(LegislativeActVersionImpl legislativeActVersion, Object[] array) {
		super.__set__(legislativeActVersion, array);
		legislativeActVersion.setActGeneratable(legislativeActVersion.getGeneratedActCount() == null || legislativeActVersion.getGeneratedActCount() == 0);
		legislativeActVersion.setGeneratedActDeletable(!Boolean.TRUE.equals(legislativeActVersion.getActGeneratable()));
	}
	
	@Override
	protected void processWhenHasNoEntityArray(LegislativeActVersionImpl legislativeActVersion) {
		super.processWhenHasNoEntityArray(legislativeActVersion);
		legislativeActVersion.setActGeneratable(Boolean.TRUE);
		legislativeActVersion.setGeneratedActDeletable(Boolean.FALSE);
	}
}