package ci.gouv.dgbf.system.collectif.server.impl;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.business.GeneratedActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.service.GeneratedActDto;
import ci.gouv.dgbf.system.collectif.server.client.rest.GeneratedActController;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;

@QuarkusTest
@TestProfile(Profiles.GeneratedAct.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class GeneratedActTest {

	@Inject Assertor assertor;
	@Inject GeneratedActBusiness business;
	@Inject GeneratedActController controller;
	
	@Test @Order(2)
	void business_generate_alreadyGenerated() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.generateByLegislativeActVersionIdentifier("2021_1_3", "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("La génération des actes de Version 3 du Collectif budgétaire 2021 a déja été faite");
	}
	
	@Test @Order(2)
	void business_generate_notYetGenerated() {
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2021_1_1", null);
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2021_1_3", List.of("3"));
		
		business.generateByLegislativeActVersionIdentifier("2021_1_1", "meliane");
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2021_1_1", List.of("2021_1_1","A_2021_1_1_1"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("2021_1_1", List.of("2021_1_1_2021_1_1_1","2021_1_1_2021_1_1_2","2021_1_1_2021_1_1_3","2021_1_1_2021_1_1_4"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("A_2021_1_1_1", List.of("A_2021_1_1_1_1_1","A_2021_1_1_1_1_2"));
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2021_1_3", List.of("3"));
	}
	
	@Test @Order(3)
	void business_deleteByLegislativeActVersionIdentifier_notGenerated() {
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2021_1_2", null);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.deleteByLegislativeActVersionIdentifier("2021_1_2", "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("La version du collectif 2021_1_2 n'a aucun actes générés");
	}
	
	@Test @Order(3)
	void business_delete_generated_notApplied() {
		business.deleteByLegislativeActVersionIdentifier("2021_1_1", "meliane");
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2021_1_1", null);
	}
	
	@Test @Order(4)
	void service_generate_notYetGenerated() {
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON).queryParam(GeneratedActDto.JSON_LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2021_1_1").queryParam(GeneratedActDto.JSON___AUDIT_WHO__, "meliane")
				//.log().all()
				.post("/api/actes_generes/generation-par-version-collectif-budgetaire");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("Nombre d'actes générés : 2");
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2021_1_1", List.of("2021_1_1","A_2021_1_1_1"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("2021_1_1", List.of("2021_1_1_2021_1_1_1","2021_1_1_2021_1_1_2","2021_1_1_2021_1_1_3","2021_1_1_2021_1_1_4"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("A_2021_1_1_1", List.of("A_2021_1_1_1_1_1","A_2021_1_1_1_1_2"));
	}
	
	@Test @Order(4)
	void service_generate_alreadyGenerated() {
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON)
				//.log().all()
				.post("/api/actes_generes/generation-par-version-collectif-budgetaire");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.BAD_REQUEST.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("L'identifiant de Version collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
	
	@Test @Order(5)
	void service_delete_generated_notApplied() {
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON).queryParam(GeneratedActDto.JSON_LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2021_1_1").queryParam(GeneratedActDto.JSON___AUDIT_WHO__, "meliane")
				//.log().all()
				.post("/api/actes_generes/suppression-par-version-collectif-budgetaire");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("Nombre d'actes supprimés : 2");
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2021_1_1", null);
	}
	
	@Test @Order(5)
	void service_deleteByLegislativeActVersionIdentifier_notGenerated() {
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON)
				//.log().all()
				.post("/api/actes_generes/suppression-par-version-collectif-budgetaire");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.BAD_REQUEST.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("L'identifiant de la version du collectif est requis\r\nLe nom d'utilisateur est requis");
	}
	
	@Test @Order(6)
	void client_generate_alreadyGenerated() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			controller.generateByLegislativeActVersionIdentifier("2021_1_3", "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("La génération des actes de Version 3 du Collectif budgétaire 2021 a déja été faite");
	}
	
	@Test @Order(6)
	void client_generate_notYetGenerated() {
		controller.generateByLegislativeActVersionIdentifier("2021_1_1", "meliane");
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2021_1_1", List.of("2021_1_1","A_2021_1_1_1"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("2021_1_1", List.of("2021_1_1_2021_1_1_1","2021_1_1_2021_1_1_2","2021_1_1_2021_1_1_3","2021_1_1_2021_1_1_4"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("A_2021_1_1_1", List.of("A_2021_1_1_1_1_1","A_2021_1_1_1_1_2"));
	}
	
	@Test @Order(7)
	void client_deleteByLegislativeActVersionIdentifier_notGenerated() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			controller.deleteByLegislativeActVersionIdentifier("2021_1_2", "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("La version du collectif 2021_1_2 n'a aucun actes générés");
	}
	
	@Test @Order(7)
	void client_delete_generated_notApplied() {
		controller.deleteByLegislativeActVersionIdentifier("2021_1_1", "meliane");		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2021_1_1", null);
	}
}