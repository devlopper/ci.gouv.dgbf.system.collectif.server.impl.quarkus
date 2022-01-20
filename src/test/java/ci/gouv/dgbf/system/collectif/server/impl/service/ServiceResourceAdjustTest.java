package ci.gouv.dgbf.system.collectif.server.impl.service;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.time.TimeHelper;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.service.ResourceDto;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;

@QuarkusTest
@TestProfile(Profiles.Service.Resource.Adjust.class)
public class ServiceResourceAdjustTest {

	@Inject Assertor assertor;
	
	@Test
	void adjust() {
		assertor.assertResourceAudits("1", "christian", "AJUSTEMENT", "MODIFICATION", TimeHelper.toMillisecond(LocalDateTime.of(2000, 1, 2, 1, 1)));
		assertor.assertResourceAudit("1", "AJUSTEMENT par christian le");
		assertor.assertRevenue("1", 0l);
		
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON).body(JsonbBuilder.create().toJson(List.of(new ResourceDto.AdjustmentDto().setIdentifier("1").setRevenue(3l))))
				.queryParam(ResourceDto.JSON___AUDIT_WHO__, "meliane")
				//.log().all()
				.post("/api/ressources/ajustements");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("Nombre de ligne de ressource mise à jour : 1");
		
		
		assertor.assertRevenue("1", 3l);
		assertor.assertResourceAudits("1", "meliane", "AJUSTEMENT", "MODIFICATION");
		assertor.assertResourceAudit("1", "AJUSTEMENT par meliane le");
	}
	
	@Test
	void adjust_null() {
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON)
				//.log().all()
				.post("/api/ressources/ajustements");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.BAD_REQUEST.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("Ajustements requis\r\nNom d'utilisateur requis");
	}
}