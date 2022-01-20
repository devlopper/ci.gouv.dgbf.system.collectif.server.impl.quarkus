package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class ActivityImplLessorsReader extends AbstractActivityImplNamablesReader implements Serializable {

	@Override
	protected void __set__(ActivityImpl activity, Object[] array) {
		Integer index = 1;
		activity.addLessors(new LessorImpl().setIdentifier(getAsString(array, index++)).setCode(getAsString(array, index++)).setName(getAsString(array, index++)));
	}

	@Override
	protected String getNamableEntityName() {
		return LessorImpl.ENTITY_NAME;
	}

	@Override
	protected String getNamableIdentifierFieldNameFromExpenditure() {
		return ExpenditureImpl.FIELD_LESSOR_IDENTIFIER;
	}
}