package ci.gouv.dgbf.system.collectif.server.impl;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.client.SpecificServiceGetter;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.client.rest.BudgetaryActVersionController;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationAdjustmentReader;
import io.restassured.specification.RequestSpecification;

@ApplicationScoped
public class Assertor {

	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject BudgetaryActPersistence budgetaryActPersistence;
	@Inject BudgetaryActVersionPersistence budgetaryActVersionPersistence;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	@Inject SpecificServiceGetter specificServiceGetter;
	@Inject BudgetaryActVersionController budgetaryActVersionController;
	
	public void assertEntryAuthorization(String identifier,Long value) {
		Collection<Object[]> arrays = new ExpenditureImplEntryAuthorizationAdjustmentReader().readByIdentifiers(List.of(identifier), null);
		assertThat(arrays).hasSize(1);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[0]).isEqualTo(identifier);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[1]).isEqualTo(value);
	}
	
	public void assertIdentifiers(Collection<?> objects,Collection<String> expectedIdentifiers) {
		if(CollectionHelper.isEmpty(objects)) {
			assertThat(expectedIdentifiers).as("Aucun identifiants trouvés").isNull();
		}else {
			assertThat(expectedIdentifiers).as("Identifiants trouvés").isNotNull();
			assertThat(objects.stream().map(x -> FieldHelper.readSystemIdentifier(x)).collect(Collectors.toList())).containsExactly(expectedIdentifiers.toArray(new String[] {}));
		}
	}
	
	public void assertClientBudgetaryActVersionsIdentifiersByBudgetaryActIdentifier(String identifier,Collection<String> expectedIdentifiers) {
		Collection<ci.gouv.dgbf.system.collectif.server.client.rest.BudgetaryActVersion> budgetaryActVersions =
				budgetaryActVersionController.getByBudgetaryActIdentifier(identifier);
		assertIdentifiers(budgetaryActVersions, expectedIdentifiers);
    }
	
    public <T> List<T> assertClientGetManyNamable(Class<T> klass,Collection<String> expectedCodes,String expectedTotalCount) {
		Response response = specificServiceGetter.get(klass).get(null,null, null, Boolean.TRUE, Boolean.TRUE, null, 3);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).as(String.format("GET %s has status OK", klass.getSimpleName())).isEqualTo(Response.Status.OK.getStatusCode());
		//assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).as(String.format("GET %s has xTotalCount %s", klass.getSimpleName(),expectedTotalCount)).isEqualTo(expectedTotalCount);
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<T> entities = ResponseHelper.getEntityAsListFromJson(klass,response);
		if(CollectionHelper.isEmpty(entities))
			assertThat(expectedCodes).as(String.format("GET %s is empty", klass.getSimpleName())).isNull();
		else{
			assertThat(expectedCodes).as(String.format("GET %s is not empty", klass.getSimpleName())).isNotNull();
			assertThat(entities).as(String.format("GET %s has %s element(s)", klass.getSimpleName(),expectedCodes.size())).hasSize(expectedCodes.size());
			assertThat(entities.stream().map(e -> FieldHelper.readSystemIdentifier(e)).collect(Collectors.toList()))
				.as(String.format("GET %s has codes matching %s", klass.getSimpleName(),expectedCodes))
				.containsExactly(expectedCodes.toArray(new String[] {}));
		}
		return entities;
    }
	
    public <T> void assertClientGetOneNamable(Class<T> klass,String identifier,String expectedCode,String expectedName) {
		Response response = specificServiceGetter.get(klass).getByIdentifier(identifier, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		T entity = ResponseHelper.getEntity(klass,response);
		assertThat(entity).as(String.format("%s.%s found", klass.getSimpleName(),identifier)).isNotNull();
		assertThat(FieldHelper.readBusinessIdentifier(entity)).as(String.format("%s.%s has code %s", klass.getSimpleName(),identifier,expectedCode)).isEqualTo(expectedCode);
		assertThat(FieldHelper.readName(entity)).as(String.format("%s.%s has name %s", klass.getSimpleName(),identifier,expectedName)).isEqualTo(expectedName);
    }
	
    public void assertClientCountNamable(Class<?> klass,Long expectedCount) {
		Response response = specificServiceGetter.get(klass).count(null,null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Long count = ResponseHelper.getEntityAsLong(response);
		assertThat(count).as(String.format("%s à %s élément(s)", klass.getSimpleName(),expectedCount)).isEqualTo(expectedCount);
    }
    
    @Test
    public <T> void assertClientNamable(Class<T> klass,Collection<String> expectedCodes,Long expectedTotalCount) {
    	List<T> entities = assertClientGetManyNamable(klass, expectedCodes, expectedTotalCount.toString());
    	if(CollectionHelper.isEmpty(entities)) {
    		
    	}else {
    		assertClientGetOneNamable(klass,(String)FieldHelper.readSystemIdentifier(entities.get(0)), (String)FieldHelper.readBusinessIdentifier(entities.get(0))
    				, (String)FieldHelper.readName(entities.get(0))); 		
    	}
    	assertClientCountNamable(klass, expectedTotalCount);
    }

    /**/
    
    public void assertServiceGetMany(String path,String expectedXTotalCount,Collection<String> expectedIdentifiers) {
		io.restassured.response.Response response = given().when()
				//.log().all()
				.get("/api/"+path);
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.header(ResponseHelper.HEADER_X_TOTAL_COUNT, expectedXTotalCount)
        	.body("identifiant", hasItems(expectedIdentifiers.toArray(new String[] {})))
        	;
		
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
    
    public void assertServiceGetOne(String path,String identifier,Collection<String> projections,Map<String,String> expectedFieldsValues) {
    	RequestSpecification requestSpecification = given().when();
    	if(CollectionHelper.isNotEmpty(projections))
    		requestSpecification.param("projections", projections);
		io.restassured.response.Response response = requestSpecification.get(String.format("/api/%s/%s",path,identifier));
		
		if(MapHelper.isNotEmpty(expectedFieldsValues))
			for(Map.Entry<String, String> entry : expectedFieldsValues.entrySet())
				response.then().body(entry.getKey(), equalTo(entry.getValue()));
		
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body("identifiant", equalTo(identifier))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
    
    public void assertServiceGetOne(String path,String identifier,Map<String,String> expectedFieldsValues) {
    	assertServiceGetOne(path, identifier, null,expectedFieldsValues);
    }
    
    public void assertServiceGetOne(String path,String identifier) {
    	assertServiceGetOne(path, identifier, null,Map.of("identifiant",identifier));
    }
    
    public void assertServiceGetOneNotFound(String path,String identifier) {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().get(String.format("/api/%s/%s",path,identifier));
		response.then()
		//.log().all()
        	.statusCode(Response.Status.NOT_FOUND.getStatusCode())
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
    
    public void assertServiceGetNumber(String path,String expectedNumber) {
		io.restassured.response.Response response = given().when().get(String.format("/api/%s/nombre",path));
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(equalTo(expectedNumber))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }

    /**/
    
    public void assertBudgetaryActByBudgetaryActVersion(String identifier,String expectedIdentifier) {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(budgetaryActPersistence.getQueryIdentifierReadDynamicOne()));
		arguments.addFilterFieldsValues(Parameters.BUDGETARY_ACT_VERSION_IDENTIFIER,identifier);
		BudgetaryAct budgetaryAct = budgetaryActPersistence.readOne(arguments);
		if(StringHelper.isBlank(expectedIdentifier)) {
			assertThat(budgetaryAct).as("Aucun acte budgétaire ayant une version identifiée par "+identifier).isNull();
		}else {
			assertThat(budgetaryAct).as("Un acte budgétaire trouvé ayant une version identifiée par "+identifier).isNotNull();
			assertThat(budgetaryAct.getIdentifier()).as("Identifiant de l'acte budgétaire est "+budgetaryAct.getIdentifier()).isEqualTo(expectedIdentifier);
		}
	}
    
    public void assertExpenditureByBudgetaryAct(String identifier,Collection<String> expectedIdentifiers) {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamic()));
		arguments.addFilterFieldsValues(Parameters.BUDGETARY_ACT_IDENTIFIER,identifier);
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(arguments);
		if(CollectionHelper.isEmpty(expectedIdentifiers)) {
			assertThat(expenditures).as("Aucune lignes de dépenses trouvées dans l'acte budgétaire identifié par "+identifier).isNull();
		}else {
			assertThat(expenditures).as("Des lignes de dépenses trouvées dans l'acte budgétaire identifié par "+identifier).isNotEmpty();
			assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly(expectedIdentifiers.toArray(new String[] {}));
		}
	}
    
    public void assertExpenditureByBudgetaryActVersion(String identifier,Collection<String> expectedIdentifiers) {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamic()));
		arguments.addFilterFieldsValues(Parameters.BUDGETARY_ACT_VERSION_IDENTIFIER,identifier);
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(arguments);
		if(CollectionHelper.isEmpty(expectedIdentifiers)) {
			assertThat(expenditures).as("Aucune lignes de dépenses trouvées dans la version de l'acte budgétaire identifié par "+identifier).isNull();
		}else {
			assertThat(expenditures).as("Des lignes de dépenses trouvées dans la version de l'acte budgétaire identifié par "+identifier).isNotEmpty();
			assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly(expectedIdentifiers.toArray(new String[] {}));
		}
	}
    
    public void assertBudgetaryActVersionByBudgetaryAct(String identifier,Collection<String> expectedIdentifiers) {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(budgetaryActVersionPersistence.getQueryIdentifierReadDynamic()));
		arguments.addFilterFieldsValues(Parameters.BUDGETARY_ACT_IDENTIFIER,identifier);
		Collection<BudgetaryActVersion> budgetaryActVersions = budgetaryActVersionPersistence.readMany(arguments);
		if(CollectionHelper.isEmpty(expectedIdentifiers)) {
			assertThat(budgetaryActVersions).as("Aucune versions de d'actes budgétaires trouvées dans l'acte budgétaire identifié par "+identifier).isNull();
		}else {
			assertThat(budgetaryActVersions).as("Des versions d'actes budgétaires trouvées dans l'acte budgétaire identifié par "+identifier).isNotEmpty();
			assertThat(budgetaryActVersions.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly(expectedIdentifiers.toArray(new String[] {}));
		}
	}
    
    public void assertBudgetSpecializationUnitBySection(String identifier,Collection<String> expectedIdentifiers) {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(budgetSpecializationUnitPersistence.getQueryIdentifierReadDynamic()));
		arguments.addFilterFieldsValues(Parameters.SECTION_IDENTIFIER,identifier);
		Collection<BudgetSpecializationUnit> budgetSpecializationUnits = budgetSpecializationUnitPersistence.readMany(arguments);
		if(CollectionHelper.isEmpty(expectedIdentifiers)) {
			assertThat(budgetSpecializationUnits).as("Aucune USBs trouvées dans la section identifiée par "+identifier).isNull();
		}else {
			assertThat(budgetSpecializationUnits).as("Des USBs trouvées dans la section identifiée par "+identifier).isNotEmpty();
			assertThat(budgetSpecializationUnits.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly(expectedIdentifiers.toArray(new String[] {}));
		}
	}
    
    public void assertExpenditureByNature(String identifier,Collection<String> expectedIdentifiers) {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamic()));
		arguments.addFilterFieldsValues(Parameters.EXPENDITURE_NATURE_IDENTIFIER,identifier);
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(arguments);
		if(CollectionHelper.isEmpty(expectedIdentifiers)) {
			assertThat(expenditures).as("Aucune lignes de dépenses trouvées avec la nature de dépense identifiée par "+identifier).isNull();
		}else {
			assertThat(expenditures).as("Des lignes de dépenses trouvées avec la nature de dépense identifiée par "+identifier).isNotEmpty();
			assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly(expectedIdentifiers.toArray(new String[] {}));
		}
	}
}