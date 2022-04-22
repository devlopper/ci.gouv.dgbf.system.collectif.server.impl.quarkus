package ci.gouv.dgbf.system.collectif.server.impl;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.time.TimeHelper;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.EntityReader;
import org.cyk.utility.service.client.SpecificServiceGetter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.business.ResourceBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Resource;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourcePersistence;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ResourceDto;
import ci.gouv.dgbf.system.collectif.server.api.service.RevenueDto;
import ci.gouv.dgbf.system.collectif.server.client.rest.ResourceController;
import ci.gouv.dgbf.system.collectif.server.client.rest.Revenue;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImplAsStringsReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImplEntryAuthorizationAdjustmentReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RevenueImpl;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;

@QuarkusTest
@TestProfile(Profiles.Resource.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class ResourceTest {

	@Inject EntityManager entityManager;
	@Inject Assertor assertor;
	@Inject ResourcePersistence persistence;
	@Inject ResourceBusiness business;
	@Inject ResourceController controller;
	
	@Test @Order(1)
	void persistence_sumsAmounts() {
		/*String q = "SELECT SUM(CASE WHEN t.revenue.adjustment IS NULL THEN 0l ELSE t.revenue.adjustment END),SUM(CASE WHEN v.revenue.initial IS NULL THEN 0l ELSE v.revenue.initial END),SUM(CASE WHEN v.revenue.movement IS NULL THEN 0l ELSE v.revenue.movement END),SUM(CASE WHEN v.revenue.actual IS NULL THEN 0l ELSE v.revenue.actual END) FROM ResourceImpl t JOIN LegislativeActVersionImpl lav ON lav = t.actVersion LEFT JOIN ResourceView v ON v.legislativeActVersionIdentifier = lav.identifier AND v.activityIdentifier = t.activityIdentifier AND v.economicNatureIdentifier = t.economicNatureIdentifier JOIN LegislativeActImpl la ON la = lav.act LEFT JOIN ExerciseImpl exercise ON exercise.identifier = la.exerciseIdentifier WHERE t.actVersion.identifier = :vcol_id";
		entityManager.createQuery(q).setParameter("vcol_id", "2021_1_1").getResultList().forEach(array -> {
			System.out.println("ResourceTest.persistence_sumsAmounts() ARRAY : "+Arrays.deepToString((Object[])array));
		});
		*/
		ResourceImpl resource = (ResourceImpl) persistence.readOne(new QueryExecutorArguments().addProjectionsFromStrings(ResourceImpl.FIELDS_AMOUNTS).addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_1",Parameters.AMOUNT_SUMABLE,Boolean.TRUE));
		assertThat(resource).isNotNull();
		assertor.assertExpenditureAmounts(resource.getRevenue(),new RevenueImpl().setInitial(0l).setMovement(0l).setActual(0l).setAdjustment(0l).setAvailable(0l).setMovementIncluded(0l)
				.setActualMinusMovementIncludedPlusAdjustment(0l).setAvailableMinusMovementIncludedPlusAdjustment(0l));
	}
	
	@Test @Order(1)
	void persistence_readResourceOne_amounts_1() {
		ResourceImpl resource = (ResourceImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ResourceImpl.class))
				.addFilterField("identifier", "2021_1_1_1").addProjectionsFromStrings(ResourceImpl.FIELDS_AMOUNTS)
				);
		assertThat(resource).isNotNull();
		
		assertThat(resource.getRevenue()).isNotNull();
		assertThat(resource.getRevenue().getInitial()).isEqualTo(1l);
		assertThat(resource.getRevenue().getActual()).isEqualTo(3l);
		assertThat(resource.getRevenue().getMovement()).isEqualTo(2l);
		assertThat(resource.getRevenue().getAdjustment()).isEqualTo(3l);
		assertThat(resource.getRevenue().getActualPlusAdjustment()).isEqualTo(6l);
		assertThat(resource.getRevenue().getMovementIncluded()).isEqualTo(0l);
		
		assertThat(resource.getActAsString()).isNull();
	}
	
	@Test @Order(1)
	void persistence_readResourceMany() {
		Collection<Resource> resources = persistence.readMany(null, null, null);
		assertThat(resources).hasSize(4);
	}
	
	@Test @Order(1)
	void persistence_readResourceOne() {
		Resource resource = persistence.readOne("2021_1_1_1");
		assertThat(resource).isNotNull();
		assertThat(resource.getRevenue()).isNull();
	}
	
	@Test @Order(1)
	void persistence_readResourceOne_revenue() {
		Resource resource = persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne())).addFilterField("identifier", "2021_1_1_1")
				.addProjectionsFromStrings(ResourceImpl.FIELD_REVENUE));
		assertThat(resource).isNotNull();
		assertThat(resource.getRevenue()).isNotNull();
	}
	
	@Test @Order(1)
	void persistence_readResourceOne_asStrings() {
		ResourceImpl resource = (ResourceImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ResourceImpl.class))
				.addFilterField("identifier", "2021_1_1_1").addProjectionsFromStrings(ResourceImpl.FIELDS_STRINGS)
				);
		assertThat(resource).isNotNull();
		assertThat(resource.getRevenue()).isNull();
		assertThat(resource.getActAsString()).isEqualTo("2021_1");
	}
	
	@Test @Order(1)
	void persistence_readResourceOne_amounts_initial_actual_movement_adjustment_actual_plus_adjustment() {
		ResourceImpl resource = (ResourceImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ResourceImpl.class))
				.addFilterField("identifier", "2021_1_1_1").addProjectionsFromStrings(ResourceImpl.FIELDS_AMOUNTS_INITIAL_ACTUAL_MOVEMENT_ADJUSTMENT_ACTUAL_PLUS_ADJUSTMENT)
				);
		assertThat(resource).isNotNull();
		assertThat(resource.getRevenue()).isNotNull();
		assertThat(resource.getRevenue().getInitial()).isEqualTo(1l);
		assertThat(resource.getRevenue().getActual()).isEqualTo(3l);
		assertThat(resource.getActAsString()).isNull();
	}
	
	@Test @Order(1)
	void persistence_countResource() {
		assertThat(persistence.count()).isEqualTo(4l);
	}
	
	@Test @Order(1)
	void persistence_readResourceAsStrings() {
		Collection<Object[]> objects = new ResourceImplAsStringsReader()
				.readByIdentifiers(List.of("2021_1_1_1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[0]).isEqualTo("2021_1_1_1");
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo("2021_1");
	}
	
	@Test @Order(1)
	void persistence_readRevenueAdjustment() {
		Collection<Object[]> objects = new ResourceImplEntryAuthorizationAdjustmentReader().readByIdentifiers(List.of("2021_1_1_1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[0]).isEqualTo("2021_1_1_1");
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo(3l);
	}
	
	@Test @Order(1)
    public void service_get_many() {
		io.restassured.response.Response response = given().when()
				//.log().all()
				.get("/api/ressources");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.header(ResponseHelper.HEADER_X_TOTAL_COUNT, "4")
        	.body(ExpenditureDto.JSON_IDENTIFIER, hasItems("2021_1_1_1","2021_1_1_2","2021_1_1_3","2021_1_1_4"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_get_one_revenue() {
		io.restassured.response.Response response = given()
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,ResourceDto.JSON_IDENTIFIER)
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,ResourceDto.JSON_REVENUE)
				//.log().all()
				.when().get("/api/ressources/2021_1_1_1");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(ResourceDto.JSON_IDENTIFIER, equalTo("2021_1_1_1"))
        	.body(FieldHelper.join(ResourceDto.JSON_REVENUE,RevenueDto.JSON_ADJUSTMENT), equalTo(3))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_get_one() {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().get("/api/ressources/2021_1_1_1");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(ResourceDto.JSON_IDENTIFIER, equalTo("2021_1_1_1"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_get_one_audit() {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().param(EntityReader.PARAMETER_NAME_PROJECTIONS, ResourceDto.JSON___AUDIT__).get("/api/ressources/2021_1_1_1");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(ResourceDto.JSON_IDENTIFIER, equalTo("2021_1_1_1"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_get_one_notfound() {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().get("/api/ressources/0");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.NOT_FOUND.getStatusCode());
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_count() {
		io.restassured.response.Response response = given().when().get("/api/ressources/nombre");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(equalTo("4"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void client_get_many() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Resource.class).get(null,null, null, null, null, null, null,null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("4");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<ci.gouv.dgbf.system.collectif.server.client.rest.Resource> resources = ResponseHelper.getEntityAsListFromJson(ci.gouv.dgbf.system.collectif.server.client.rest.Resource.class,response);
		assertThat(resources).hasSize(4);
		assertThat(resources.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("2021_1_1_1","2021_1_1_2","2021_1_1_3","2021_1_1_4");
    }
	
	@Test @Order(1)
    public void client_get_many_asStrings() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Resource.class).get(null,null,null, List.of("astrings"), null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("4");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<ci.gouv.dgbf.system.collectif.server.client.rest.Resource> resources = ResponseHelper.getEntityAsListFromJson(ci.gouv.dgbf.system.collectif.server.client.rest.Resource.class,response);
		assertThat(resources).hasSize(4);
		assertThat(resources.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("2021_1_1_1","2021_1_1_2","2021_1_1_3","2021_1_1_4");
    }
	
	@Test @Order(1)
    public void client_get_one() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Resource.class).getByIdentifier("2021_1_1_1", null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		ci.gouv.dgbf.system.collectif.server.client.rest.Resource resource = ResponseHelper.getEntity(ci.gouv.dgbf.system.collectif.server.client.rest.Resource.class,response);
		assertThat(resource).isNotNull();
		assertThat(resource.getIdentifier()).isEqualTo("2021_1_1_1");
		assertThat(resource.getRevenue()).isNull();
    }
	
	@Test @Order(1)
    public void client_get_one_revenue() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Resource.class).getByIdentifier("2021_1_1_1", List.of(ResourceDto.JSON_IDENTIFIER,ResourceDto.JSON_REVENUE));
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		ci.gouv.dgbf.system.collectif.server.client.rest.Resource resource = ResponseHelper.getEntity(ci.gouv.dgbf.system.collectif.server.client.rest.Resource.class,response);
		assertThat(resource).isNotNull();
		assertThat(resource.getIdentifier()).isEqualTo("2021_1_1_1");
		assertThat(resource.getRevenue()).isNotNull();
		assertThat(resource.getRevenue().getAdjustment()).isEqualTo(3l);
    }
	
	@Test @Order(1)
    public void client_count() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Resource.class).count(null,null,null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Long count = ResponseHelper.getEntityAsLong(response);
		assertThat(count).isEqualTo(4l);
    }
	
	/* Import */
	
	@Test @Order(2)
	void business_import_2021_1_2() {
		assertor.assertResourceByLegislativeActVersion("2021_1_2", null);
		business.import_("2021_1_2", "meliane");
		assertor.assertResourceByLegislativeActVersion("2021_1_2", List.of("2021_1_2_1","2021_1_2_2","2021_1_2_3","2021_1_2_4","2021_1_2_5"));
	}
	
	@Test
	void business_import_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.import_(null,null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Version collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
	
	@Test @Order(2)
	void business_import_running() {
		new Thread() {
			public void run() {
				business.import_("2021_1_running","christian");
			}
		}.start();
		TimeHelper.pause(1l * 500);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.import_("2021_1_running","christian");
	    });
		assertThat(exception.getMessage()).isEqualTo("Ressource de 2021_1_running en cours d'importation");
		TimeHelper.pause(1l * 2000);
		business.import_("2021_1_running","christian");
	}
	
	/* Adjust */
	
	@Test @Order(2)
	void business_adjust() {
		assertor.assertResourceAudits("2021_1_1_1", "christian", "AJUSTEMENT", "MODIFICATION", TimeHelper.toMillisecond(LocalDateTime.of(2000, 1, 2, 1, 1)));
		assertor.assertResourceAudit("2021_1_1_1", "AJUSTEMENT par christian le");
		assertor.assertRevenue("2021_1_1_1", 3l);
		business.adjust(Map.of("2021_1_1_1",7l),"meliane");
		assertor.assertRevenue("2021_1_1_1", 7l);
		assertor.assertResourceAudits("2021_1_1_1", "meliane", ResourceBusiness.ADJUST_AUDIT_IDENTIFIER, "MODIFICATION");
		assertor.assertResourceAudit("2021_1_1_1", ResourceBusiness.ADJUST_AUDIT_IDENTIFIER+" par meliane le");
	}
	
	@Test @Order(2)
	void business_adjust_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.adjust(null,"anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("Ajustements requis");
	}
	
	@Test @Order(3)
	void service_adjust() {
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON).body(JsonbBuilder.create().toJson(List.of(new ResourceDto.AdjustmentDto().setIdentifier("2021_1_1_1").setRevenue(5l))))
				.queryParam(ResourceDto.JSON___AUDIT_WHO__, "meliane")
				//.log().all()
				.post("/api/ressources/ajustements");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("Nombre de Ressource mise à jour : 1");
		
		assertor.assertRevenue("2021_1_1_1", 5l);
		assertor.assertResourceAudits("2021_1_1_1", "meliane", ResourceBusiness.ADJUST_AUDIT_IDENTIFIER, "MODIFICATION");
		assertor.assertResourceAudit("2021_1_1_1", ResourceBusiness.ADJUST_AUDIT_IDENTIFIER+" par meliane le");
	}
	
	@Test @Order(3)
	void service_adjust_null() {
		io.restassured.response.Response response = given().when().contentType(ContentType.JSON)
				//.log().all()
				.post("/api/ressources/ajustements");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.BAD_REQUEST.getStatusCode())
        	;
		assertThat(response.then().extract().asString()).isEqualTo("Ajustements requis\r\nLe nom d'utilisateur est requis");
	}
	
	@Test @Order(4)
	void controller_adjust() {
		controller.adjust("meliane",new ci.gouv.dgbf.system.collectif.server.client.rest.Resource().setIdentifier("2021_1_1_1").setRevenue(new Revenue().setAdjustment(17l)));
		assertor.assertRevenue("2021_1_1_1", 17l);
		assertor.assertResourceAudits("2021_1_1_1", "meliane", ResourceBusiness.ADJUST_AUDIT_IDENTIFIER, "MODIFICATION");
		assertor.assertResourceAudit("2021_1_1_1", ResourceBusiness.ADJUST_AUDIT_IDENTIFIER+" par meliane le");
	}
	
	@Test @Order(4)
	void controller_adjust_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			controller.adjust("anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("Ajustements requis");
	}

	/**/
	
	public static String PA_ACTUALISER_VM(String tableName) {
		TimeHelper.pause(1l * 1000l);
		return null;
	}
}