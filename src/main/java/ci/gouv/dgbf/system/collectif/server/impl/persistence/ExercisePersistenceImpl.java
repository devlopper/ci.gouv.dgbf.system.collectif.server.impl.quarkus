package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Exercise;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExercisePersistence;

@ApplicationScoped
public class ExercisePersistenceImpl extends AbstractSpecificPersistenceImpl<Exercise>  implements ExercisePersistence,Serializable {

	public ExercisePersistenceImpl() {
		entityClass = Exercise.class;
		entityImplClass = ExerciseImpl.class;
	}
}