package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class ActivityImplFundingSourcesReader extends AbstractActivityImplNamablesReader implements Serializable {

	@Override
	protected void __set__(ActivityImpl activity, Object[] array) {
		Integer index = 1;
		activity.addFundingSources(new FundingSourceImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
	}

	@Override
	protected String getNamableEntityName() {
		return FundingSourceImpl.ENTITY_NAME;
	}

	@Override
	protected String getNamableIdentifierFieldNameFromExpenditure() {
		return ExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER;
	}
}