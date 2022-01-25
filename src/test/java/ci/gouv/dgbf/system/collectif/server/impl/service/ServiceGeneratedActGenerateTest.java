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
@TestProfile(Profiles.Service.GeneratedAct.Generate.class)
public class ServiceGeneratedActGenerateTest {

	@Inject Assertor assertor;
	
	@Test
	void generate_notYetGenerated() {
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", null);
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("3", List.of("3"));
		
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON).queryParam(GeneratedActDto.JSON_LEGISLATIVE_ACT_VERSION_IDENTIFIER, "1").queryParam(GeneratedActDto.JSON___AUDIT_WHO__, "meliane")
				//.log().all()
				.post("/api/actes_generes/generation-par-version-collectif-budgetaire");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("Nombre d'actes générés : 3");
		
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", List.of("1","1_1","A_1_1"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("1", List.of("1_2","1_3","1_4"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("1_1", List.of("1_1_1_1","1_1_1_2"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("A_1_1", List.of("A_1_1_1_1","A_1_1_1_2"));
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("3", List.of("3"));
	}
	
	@Test
	void generate_alreadyGenerated() {
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON)
				//.log().all()
				.post("/api/actes_generes/generation-par-version-collectif-budgetaire");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.BAD_REQUEST.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("L'identifiant de la version du collectif est requis\r\nLe nom d'utilisateur est requis");
	}
}