package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Exercise;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExercisePersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.Exercise.Read.class)
public class PersistenceExerciseReadTest {

	@Inject Assertor assertor;
	@Inject ExercisePersistence persistence;
	
	@Test
	void readMany() {
		Collection<Exercise> exercises = persistence.readMany(null, null, null);
		assertThat(exercises).hasSize(3);
	}
	
	@Test
	void readOne() {
		Exercise exercise = persistence.readOne("2021");
		assertThat(exercise).isNotNull();
	}
}