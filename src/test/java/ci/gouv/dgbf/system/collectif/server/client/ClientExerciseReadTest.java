package ci.gouv.dgbf.system.collectif.server.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.client.SpecificServiceGetter;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.service.ExerciseDto;
import ci.gouv.dgbf.system.collectif.server.client.rest.Exercise;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Client.Exercise.Read.class)
public class ClientExerciseReadTest {

	@Inject Assertor assertor;
	
	@Test
    public void get_many() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Exercise.class).get(null,null, null, null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("3");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<Exercise> exercises = ResponseHelper.getEntityAsListFromJson(Exercise.class,response);
		assertThat(exercises).hasSize(3);
		assertThat(exercises.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("2020","2021","2022");
    }
	
	@Test
    public void get_one() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Exercise.class).getByIdentifier("2021", List.of(ExerciseDto.JSON_IDENTIFIER,ExerciseDto.JSON_YEAR));
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Exercise exercise = ResponseHelper.getEntity(Exercise.class,response);
		assertThat(exercise).isNotNull();
		assertThat(exercise.getIdentifier()).isEqualTo("2021");
		assertThat(exercise.getYear()).isEqualTo(Short.valueOf("2021"));
    }
}