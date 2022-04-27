package ci.gouv.dgbf.system.collectif.server.impl;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.SpecificService;
import org.cyk.utility.service.client.Controller;
import org.cyk.utility.service.client.SpecificServiceGetter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetCategory;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetCategoryPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Exercise;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExercisePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetCategoryDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExerciseDto;
import ci.gouv.dgbf.system.collectif.server.client.rest.BudgetCategoryController;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.ReadOnlyEntity.class)
public class ReadOnlyEntityTest {

	@Inject Assertor assertor;
	@Inject Configuration configuration;
	@Inject ExercisePersistence exercisePersistence;
	@Inject BudgetCategoryPersistence budgetCategoryPersistence;
	@Inject BudgetCategoryController budgetCategoryController;
	
	@BeforeAll
	static void listenBeforeAll() {
		Controller.GetArguments.PROCESSABLE_BY_USER.clear();
	}
	
	/* Persistence */
	
	@Test
	void persistence_budgetCategory_readMany() {
		Collection<BudgetCategory> budgetCategories = budgetCategoryPersistence.readMany(null, null, null);
		assertThat(budgetCategories).hasSize(5);
	}
	
	@Test
	void persistence_budgetCategory_readMany_2022_1_1() {
		Collection<BudgetCategory> budgetCategories = budgetCategoryPersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_1"));
		assertThat(budgetCategories).isNull();
	}
	
	@Test
	void persistence_budgetCategory_readMany_2022_1_2() {
		Collection<BudgetCategory> budgetCategories = budgetCategoryPersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2"));
		assertThat(budgetCategories.stream().map(bc -> bc.getCode()).collect(Collectors.toList())).containsExactly("1","2");
	}
	
	@Test
	void persistence_budgetCategory_readMany_2022_1_3() {
		Collection<BudgetCategory> budgetCategories = budgetCategoryPersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_3"));
		assertThat(budgetCategories.stream().map(bc -> bc.getCode()).collect(Collectors.toList())).containsExactly("3","4","5");
	}
	
	@Test
	void persistence_budgetCategory_readMany_2022_1_4() {
		Collection<BudgetCategory> budgetCategories = budgetCategoryPersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_4"));
		assertThat(budgetCategories.stream().map(bc -> bc.getCode()).collect(Collectors.toList())).containsExactly("1","2","3","4","5");
	}
	
	@Test
	void persistence_budgetCategory_readMany_2022_1_5() {
		Collection<BudgetCategory> budgetCategories = budgetCategoryPersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_5"));
		assertThat(budgetCategories.stream().map(bc -> bc.getCode()).collect(Collectors.toList())).containsExactly("1","2","4");
	}
	
	@Test
	void persistence_budgetCategory_readOne() {
		BudgetCategory budgetCategory = budgetCategoryPersistence.readOne("5");
		assertThat(budgetCategory).isNotNull();
		assertThat(budgetCategory.getCode()).isEqualTo("5");
	}
	
	@Test
	void persistence_budgetCategory_default() {
		BudgetCategory budgetCategory = budgetCategoryPersistence.readDefault();
		assertThat(budgetCategory).isNotNull();
		assertThat(budgetCategory.getIdentifier()).isEqualTo(configuration.budgetCategory().defaultIdentifier());
	}
	
	@Test
	void persistence_exercise_readMany() {
		Collection<Exercise> exercises = exercisePersistence.readMany(null, null, null);
		assertThat(exercises).hasSize(3);
	}
	
	@Test
	void persistence_exercise_readOne() {
		Exercise exercise = exercisePersistence.readOne("2021");
		assertThat(exercise).isNotNull();
	}
	
	/* Service */
	
	@Test
    public void service_exercise_get_many() {
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
    public void service_exercise_get_one() {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().queryParam("projections",ExerciseDto.JSON_IDENTIFIER).queryParam("projections",ExerciseDto.JSON_YEAR).get("/api/exercices/2021");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(ExerciseDto.JSON_IDENTIFIER, equalTo("2021"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test
    public void service_budgetCategory_get_default() {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().param(SpecificService.PARAMETER_NAME_DEFAULTABLE, Boolean.TRUE).get("/api/categories-budgets/");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(BudgetCategoryDto.JSON_IDENTIFIER, hasItems(configuration.budgetCategory().defaultIdentifier()))
        	;
    }
	
	/* Client */
	
	@Test
    public void client_exercise_get_many() {		
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Exercise.class).get(null,null,null, null, null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("3");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<ci.gouv.dgbf.system.collectif.server.client.rest.Exercise> exercises = ResponseHelper.getEntityAsListFromJson(ci.gouv.dgbf.system.collectif.server.client.rest.Exercise.class,response);
		assertThat(exercises).hasSize(3);
		assertThat(exercises.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("2022","2021","2020");
    }
	
	@Test
    public void client_exercise_get_one() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Exercise.class).getByIdentifier("2021",null,null, List.of(ExerciseDto.JSON_IDENTIFIER,ExerciseDto.JSON_YEAR));
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		ci.gouv.dgbf.system.collectif.server.client.rest.Exercise exercise = ResponseHelper.getEntity(ci.gouv.dgbf.system.collectif.server.client.rest.Exercise.class,response);
		assertThat(exercise).isNotNull();
		assertThat(exercise.getIdentifier()).isEqualTo("2021");
		assertThat(exercise.getYear()).isEqualTo(Short.valueOf("2021"));
    }
	
	@Test
    public void client_budgetCategory_get_default() {
		ci.gouv.dgbf.system.collectif.server.client.rest.BudgetCategory budgetCategory = budgetCategoryController.getDefault();
		assertThat(budgetCategory).isNotNull();
		assertThat(budgetCategory.getIdentifier()).isEqualTo(configuration.budgetCategory().defaultIdentifier());
    }
	
	@Test
    public void client_budgetCategory_get_defaults() {
		Collection<ci.gouv.dgbf.system.collectif.server.client.rest.BudgetCategory> budgetCategories = budgetCategoryController.getDefaults();
		assertThat(budgetCategories).isNotNull();
		assertThat(budgetCategories.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly(configuration.budgetCategory().defaultIdentifier());
    }
}