package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class LegislativeActImplExerciseYearReader extends AbstractLegislativeActImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getTuple(Boolean.TRUE).addJoins(String.format("LEFT JOIN %s exercise ON exercise.%s = t.%s", ExerciseImpl.ENTITY_NAME,ExerciseImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",LegislativeActImpl.FIELD_IDENTIFIER).addFromTuple("exercise", ExerciseImpl.FIELD_YEAR);
		return arguments;
	}
	
	@Override
	public LegislativeActImplExerciseYearReader setEntityManager(EntityManager entityManager) {
		return (LegislativeActImplExerciseYearReader) super.setEntityManager(entityManager);
	}
	
	@Override
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array) {
		Integer index = 0;
		legislativeAct.setIdentifier(getAsString(array, index++));
		legislativeAct.setExerciseYear(getAsShort(array, index++));
		__set__(legislativeAct, array, index);
	}
	
	protected void __set__(LegislativeActImpl legislativeAct, Object[] array,Integer index) {}
}