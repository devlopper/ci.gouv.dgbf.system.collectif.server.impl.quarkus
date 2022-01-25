package ci.gouv.dgbf.system.collectif.server.impl.service;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.cyk.utility.rest.ResponseHelper;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.service.ExerciseDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ResourceDto;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Service.Exercise.Read.class)
public class ServiceExerciseReadTest {

	@Test
    public void get_many() {
		io.restassured.response.Response response = given().when()
				//.log().all()
				.get("/api/exercices");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.header(ResponseHelper.HEADER_X_TOTAL_COUNT, "3")
        	.body(ExerciseDto.JSON_IDENTIFIER, hasItems("2020","2021","2022"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test
    public void get_one() {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().queryParam("projections",ExerciseDto.JSON_IDENTIFIER).queryParam("projections",ExerciseDto.JSON_YEAR).get("/api/exercices/2021");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(ResourceDto.JSON_IDENTIFIER, equalTo("2021"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }	
}