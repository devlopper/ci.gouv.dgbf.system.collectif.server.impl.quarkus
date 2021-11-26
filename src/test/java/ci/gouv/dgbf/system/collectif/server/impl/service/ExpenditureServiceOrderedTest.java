package ci.gouv.dgbf.system.collectif.server.impl.service;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.equalTo;

import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.EntityReader;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.service.EntryAuthorizationDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureDto;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Service.Integration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExpenditureServiceOrderedTest {

	@Test @Order(1)
    public void get_many() {
		io.restassured.response.Response response = given().when()
				//.log().all()
				.get("/api/depense");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.header(ResponseHelper.HEADER_X_TOTAL_COUNT, "2")
        	.body(ExpenditureDto.JSON_IDENTIFIER, hasItems("1"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void get_one_entryAuthorization() {
		io.restassured.response.Response response = given()
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,ExpenditureImpl.FIELD_IDENTIFIER)
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION)
				//.log().all()
				.when().get("/api/depense/2");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(ExpenditureDto.JSON_IDENTIFIER, equalTo("2"))
        	.body(FieldHelper.join(ExpenditureDto.JSON_ENTRY_AUTHORIZATION,EntryAuthorizationDto.JSON_ADJUSTMENT), equalTo(3))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void get_one() {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().get("/api/depense/1");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(ExpenditureDto.JSON_IDENTIFIER, equalTo("1"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void get_one_notfound() {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().get("/api/depense/0");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.NOT_FOUND.getStatusCode());
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void count() {
		io.restassured.response.Response response = given().when().get("/api/depense/nombre");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(equalTo("2"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
}