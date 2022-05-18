package ci.gouv.dgbf.system.collectif.server.impl;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.EntityReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureDto;
import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.LegislativeAct.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class LegislativeActTest {
	
	@Inject Assertor assertor;
	@Inject LegislativeActPersistence persistence;
	@Inject LegislativeActBusiness business;
	
	@Test @Order(1)
	void persistence_read_default() {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) persistence.readDefault();
		assertThat(legislativeAct).isNotNull();
		assertThat(legislativeAct.getIdentifier()).isEqualTo("2022_1");
	}
	
	@Test @Order(1)
	void persistence_fromDateAsTimestamp_2020_1() {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) persistence.readOne(new QueryExecutorArguments().addFilterField(persistence.getParameterNameIdentifier(), "2020_1").addProjectionsFromStrings(LegislativeActImpl.FIELD_FROM_DATE_AS_TIMESTAMP));
		assertThat(legislativeAct).isNotNull();
		assertThat(legislativeAct.getFromDateAsTimestamp()).isNull();
	}
	
	@Test @Order(1)
	void persistence_fromDateAsTimestamp_2020_2() {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) persistence.readOne(new QueryExecutorArguments().addFilterField(persistence.getParameterNameIdentifier(), "2020_2").addProjectionsFromStrings(LegislativeActImpl.FIELD_FROM_DATE_AS_TIMESTAMP));
		assertThat(legislativeAct).isNotNull();
		assertThat(legislativeAct.getFromDateAsTimestamp()).isEqualTo(1585699200000L);
	}
	
	@Test @Order(1)
	void persistence_asStrings_2020_1() {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) persistence.readOne(new QueryExecutorArguments().addFilterField(persistence.getParameterNameIdentifier(), "2020_1").addProjectionsFromStrings(LegislativeActImpl.FIELDS_STRINGS));
		assertThat(legislativeAct).isNotNull();	
		assertThat(legislativeAct.getIdentifier()).isEqualTo("2020_1");
		assertThat(legislativeAct.getFromDateAsString()).isEqualTo("01/01/2020");
		assertThat(legislativeAct.getDateAsString()).isEqualTo("01/04/2020");
	}
	
	@Test @Order(1)
	void persistence_asStrings_2020_2() {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) persistence.readOne(new QueryExecutorArguments().addFilterField(persistence.getParameterNameIdentifier(), "2020_2").addProjectionsFromStrings(LegislativeActImpl.FIELDS_STRINGS));
		assertThat(legislativeAct).isNotNull();	
		assertThat(legislativeAct.getIdentifier()).isEqualTo("2020_2");
		assertThat(legislativeAct.getFromDateAsString()).isEqualTo("01/04/2020");
		assertThat(legislativeAct.getDateAsString()).isEqualTo("01/07/2020");
	}
	
	@Test @Order(1)
	void persistence_readInProgressLatest() {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()))
				.addFilterField(Parameters.LATEST_LEGISLATIVE_ACT, Boolean.TRUE).addProjectionsFromStrings(LegislativeActImpl.FIELDS_AMOUNTS,LegislativeActImpl.FIELD___AUDIT__)
				);
		assertThat(legislativeAct).isNotNull();	
		assertThat(legislativeAct.getIdentifier()).isEqualTo("2022_1");
	}
	
	@Test @Order(1)
	void persistence_readExpenditureOne_amounts_1() {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()))
				.addFilterField("identifier", "2021_1").addProjectionsFromStrings(LegislativeActImpl.FIELDS_AMOUNTS,LegislativeActImpl.FIELD___AUDIT__)
				);
		assertThat(legislativeAct).isNotNull();
		
		assertThat(legislativeAct.getEntryAuthorization()).isNotNull();
		assertThat(legislativeAct.getEntryAuthorization().getInitial()).isEqualTo(0l);
		assertThat(legislativeAct.getEntryAuthorization().getActual()).isEqualTo(0l);
		assertThat(legislativeAct.getEntryAuthorization().getMovement()).isEqualTo(0l);
		assertThat(legislativeAct.getEntryAuthorization().getAdjustment()).isEqualTo(0l);
		assertThat(legislativeAct.getEntryAuthorization().getActualPlusAdjustment()).isEqualTo(0l);
		assertThat(legislativeAct.getEntryAuthorization().getMovementIncluded()).isEqualTo(0l);
		assertThat(legislativeAct.getEntryAuthorization().getExpectedAdjustment()).isEqualTo(50l);
		assertThat(legislativeAct.getEntryAuthorization().getExpectedAdjustmentMinusAdjustment()).isEqualTo(50l);
		
		assertThat(legislativeAct.getPaymentCredit()).isNotNull();
		assertThat(legislativeAct.getPaymentCredit().getInitial()).isEqualTo(0l);
		assertThat(legislativeAct.getPaymentCredit().getActual()).isEqualTo(0l);
		assertThat(legislativeAct.getPaymentCredit().getMovement()).isEqualTo(0l);
		assertThat(legislativeAct.getPaymentCredit().getAdjustment()).isEqualTo(7l);
		assertThat(legislativeAct.getPaymentCredit().getActualPlusAdjustment()).isEqualTo(7l);
		assertThat(legislativeAct.getPaymentCredit().getMovementIncluded()).isEqualTo(0l);
		assertThat(legislativeAct.getPaymentCredit().getExpectedAdjustment()).isEqualTo(100l);
		assertThat(legislativeAct.getPaymentCredit().getExpectedAdjustmentMinusAdjustment()).isEqualTo(93l);
	}
	
	@Test @Order(1)
	void persistence_readOne_movementIncluded_2020() {
		assertor.assertLegislativeActMovementIncluded("2020_1", 0l, 0l);
	}
	
	@Test @Order(1)
	void persistence_readOne_movementIncluded_2021() {
		assertor.assertLegislativeActMovementIncluded("2021_1", 0l, 0l);
	}
	
	@Test @Order(1)
	void persistence_readOne_movementIncluded_2022() {
		assertor.assertLegislativeActMovementIncluded("2022_1", 0l, 0l);
	}
	
	@Test @Order(1)
	void persistence_readMany() {
		Collection<LegislativeAct> legislativeActs = persistence.readMany(null, null, null);
		assertThat(legislativeActs).hasSize(4);
	}
	
	@Test @Order(1)
	void persistence_readOne() {
		LegislativeAct legislativeAct = persistence.readOne("2021_1");
		assertThat(legislativeAct).isNotNull();
	}
	
	@Test @Order(1)
	void persistence_readMany_2019() {
		Collection<LegislativeAct> legislativeActs = persistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.EXERCISE_YEAR, 2019));
		assertThat(legislativeActs).isNull();
	}
	
	@Test @Order(1)
	void persistence_readMany_2021() {
		Collection<LegislativeAct> legislativeActs = persistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.EXERCISE_YEAR, 2021));
		assertThat(legislativeActs).isNotNull();
		assertThat(legislativeActs.stream().map(x ->x.getIdentifier()).collect(Collectors.toList())).containsExactly("2021_1");
	}
	
	@Test @Order(1)
	void persistence_readMany_2022() {
		Collection<LegislativeAct> legislativeActs = persistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.EXERCISE_YEAR, 2022));
		assertThat(legislativeActs).isNotNull();
		assertThat(legislativeActs.stream().map(x ->x.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_1");
	}
	
	@Test @Order(1)
    void service_legislativeAct_get_many() {
		io.restassured.response.Response response = given()
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,LegislativeActDto.JSON_IDENTIFIER)
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,LegislativeActDto.JSONS_STRINGS,LegislativeActDto.JSONS_AMOUTNS,LegislativeActDto.JSON___AUDIT__)
				.when()
				//.log().all()
				.get("/api/collectifs-budgetaires");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.header(ResponseHelper.HEADER_X_TOTAL_COUNT, "4")
        	.body(ExpenditureDto.JSON_IDENTIFIER, hasItems("2022_1","2021_1","2020_2","2020_1"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	/* Create */
	
	@Test @Order(2)
	void business_create() {
		assertThat(persistence.readOne("2023_1")).isNull();
		business.create(null, null,"2023",LocalDate.of(2023, 2, 4), "meliane");
		assertThat(persistence.readOne("2023_1")).isNotNull();
		assertor.assertLegislativeAct("2023_1", "2023_1","Collectif budgétaire 2023 du 04/02/2023","2023");
		assertor.assertLegislativeActAudit("2023_1", LegislativeActBusiness.CREATE_AUDIT_IDENTIFIER);
		
		assertor.assertExpenditureByLegislativeActVersion("2023_1_1", List.of("2023_1_1_1"));
	}
	
	@Test @Order(2)
	void business_create_sameYear() {
		assertThat(persistence.readOne("2020_2")).isNotNull();
		assertThat(persistence.readOne("2020_3")).isNull();
		business.create(null, null,"2020",LocalDate.of(2020, 8, 4), "meliane");
		assertor.assertLegislativeAct("2020_3", "2020_3","Collectif budgétaire 2020 du 04/08/2020","2020");
	}
	
	@Test @Order(2)
	void business_create_exerciseIdentifierNull() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.create((String)null,(String)null,(String)null,(LocalDate)null,(String)null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Exercice est requis\r\nLa date est requise\r\nLe nom d'utilisateur est requis");
	}
	
	/* Update Version */
	
	@Test @Order(3)
	void business_updateDefaultVersion() {
		assertor.assertLegislativeActVersionIdentifier("2021_1", "2021_1_1");
		business.updateDefaultVersion("2021_1_2", "meliane");
		assertor.assertLegislativeActVersionIdentifier("2021_1", "2021_1_2");
	}
	
	@Test @Order(3)
	void business_updateDefaultVersion_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.updateDefaultVersion(null,null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Version collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
	
	@Test @Order(3)
	void business_updateDefaultVersion_same() {
		assertor.assertLegislativeActVersionIdentifier("2022_1", "2022_1_1");
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.updateDefaultVersion("2022_1_1", "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("2022_1_1 est déja la version par défaut de 2022_1");
	}
	
	/* Update In Progress */
	
	@Test @Order(4)
	void business_updateInProgress() {
		assertor.assertLegislativeActInProgress("2020_1", Boolean.FALSE);
		business.updateInProgress("2020_1", Boolean.TRUE, "meliane");
		assertor.assertLegislativeActInProgress("2020_1", Boolean.TRUE);
	}
	
	@Test @Order(4)
	void business_updateInProgress_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.updateInProgress(null,null,null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Collectif budgétaire est requis\r\nLa valeur <<en cours>> est requise\r\nLe nom d'utilisateur est requis");
	}
	
	@Test @Order(4)
	void business_updateInProgress_same() {
		assertor.assertLegislativeActInProgress("2022_1", Boolean.TRUE);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.updateInProgress("2022_1", Boolean.TRUE, "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("2022_1 est déja en cours");
	}
	
	/**/

	public static String PA_ACTUALISER_VM(String tableName) {
		return null;
	}
}