package ci.gouv.dgbf.system.collectif.server.impl.service;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.service.GeneratedActDto;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;

@QuarkusTest
@TestProfile(Profiles.Service.GeneratedAct.Delete.class)
public class ServiceGeneratedActDeleteTest {

	@Inject Assertor assertor;
	
	@Test
	void delete_generated_notApplied() {
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", null);
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2", List.of("2","3","4","A_3","A_4"));
		
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON).queryParam(GeneratedActDto.JSON_LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2").queryParam(GeneratedActDto.JSON___AUDIT_WHO__, "meliane")
				//.log().all()
				.post("/api/actes_generes/suppression-par-version-collectif-budgetaire");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("Nombre d'actes supprimés : 5");
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", null);
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2", null);
	}
	
	@Test
	void deleteByLegislativeActVersionIdentifier_notGenerated() {
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON)
				//.log().all()
				.post("/api/actes_generes/suppression-par-version-collectif-budgetaire");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.BAD_REQUEST.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("L'identifiant de la version du collectif est requis\r\nLe nom d'utilisateur est requis");
	}
}