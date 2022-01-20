package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class ResourceActivityImplEconomicNaturesReader extends AbstractResourceActivityImplNamablesReader implements Serializable {

	@Override
	protected void __set__(ResourceActivityImpl activity, Object[] array) {
		Integer index = 1;
		activity.addEconomicNatures(new EconomicNatureImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
	}

	@Override
	protected String getNamableEntityName() {
		return EconomicNatureImpl.ENTITY_NAME;
	}

	@Override
	protected String getNamableIdentifierFieldNameFromExpenditure() {
		return ResourceImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER;
	}
}