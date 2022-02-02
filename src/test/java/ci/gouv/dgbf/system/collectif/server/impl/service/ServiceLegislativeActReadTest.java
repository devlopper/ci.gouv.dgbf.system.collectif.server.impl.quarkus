package ci.gouv.dgbf.system.collectif.server.impl.service;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;

import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.EntityReader;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureDto;
import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActDto;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Service.LegislativeAct.Read.class)
public class ServiceLegislativeActReadTest {

	@Test
    public void legislativeAct_get_many() {
		io.restassured.response.Response response = given()
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,LegislativeActDto.JSON_IDENTIFIER)
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,LegislativeActDto.JSONS_STRINGS,LegislativeActDto.JSONS_AMOUTNS,LegislativeActDto.JSON___AUDIT__)
				.when()
				//.log().all()
				.get("/api/collectifs_budgetaires");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.header(ResponseHelper.HEADER_X_TOTAL_COUNT, "3")
        	.body(ExpenditureDto.JSON_IDENTIFIER, hasItems("3","2","1"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
}