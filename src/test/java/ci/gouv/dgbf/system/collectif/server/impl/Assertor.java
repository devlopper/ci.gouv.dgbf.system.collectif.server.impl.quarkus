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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.time.TimeHelper;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.client.SpecificServiceGetter;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Resource;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourcePersistence;
import ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersionController;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.AbstractAmountsImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplAuditReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplAuditsReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationAdjustmentReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplPaymentCreditAdjustmentReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImplAuditReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImplDefaultVersionIdentifierReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImplAuditsReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImplIncludedReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImplAuditReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImplAuditsReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImplRevenueAdjustmentReader;
import io.restassured.specification.RequestSpecification;

@ApplicationScoped
public class Assertor {

	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ResourcePersistence resourcePersistence;
	@Inject LegislativeActPersistence actPersistence;
	@Inject LegislativeActVersionPersistence actVersionPersistence;
	@Inject GeneratedActPersistence generatedActPersistence;
	@Inject GeneratedActExpenditurePersistence generatedActExpenditurePersistence;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	@Inject SpecificServiceGetter specificServiceGetter;
	@Inject LegislativeActVersionController actVersionController;
	
	void assertExpenditureAmounts(AbstractAmountsImpl amounts,AbstractAmountsImpl expected) {
		String name = amounts.getClass().getSimpleName();
		assertThat(amounts.getInitial()).as("initial "+name).isEqualTo(expected.getInitial());
		assertThat(amounts.getMovement()).as("mouvement "+name).isEqualTo(expected.getMovement());
		assertThat(amounts.getActual()).as("actuel "+name).isEqualTo(expected.getActual());
		assertThat(amounts.getAdjustment()).as("ajustement "+name).isEqualTo(expected.getAdjustment());
		assertThat(amounts.getMovementIncluded()).as("mouvement inclus "+name).isEqualTo(expected.getMovementIncluded());
		assertThat(amounts.getAvailable()).as("disponible "+name).isEqualTo(expected.getAvailable());
		assertThat(amounts.getActualMinusMovementIncludedPlusAdjustment()).as("actuel - mouvement inclu + ajustement "+name).isEqualTo(expected.getActualMinusMovementIncludedPlusAdjustment());
		assertThat(amounts.getAvailableMinusMovementIncludedPlusAdjustment()).as("disponible - mouvement inclu + ajustement "+name).isEqualTo(expected.getAvailableMinusMovementIncludedPlusAdjustment());
	}
	
	public void assertLegislativeActAudit(String identifier,String audit) {
		LegislativeActImpl legislativeAct = DependencyInjection.inject(EntityManager.class).find(LegislativeActImpl.class, identifier);
		new LegislativeActImplAuditReader().readThenSet(List.of(legislativeAct), null);
		assertThat(legislativeAct.get__audit__()).as("audit").startsWith(audit);
	}
	
	public void assertLegislativeAct(String identifier,String expectedCode,String expectedName,String expectedExerciseIdentifier) {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) actPersistence.readOne(identifier, List.of(LegislativeActImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_CODE,LegislativeActImpl.FIELD_NAME
				,LegislativeActImpl.FIELD_EXERCISE_IDENTIFIER));
		assertThat(legislativeAct).isNotNull();
		assertThat(legislativeAct.getIdentifier()).isEqualTo(identifier);
		assertThat(legislativeAct.getCode()).isEqualTo(expectedCode);
		assertThat(legislativeAct.getName()).isEqualTo(expectedName);
		assertThat(legislativeAct.getExerciseIdentifier()).isEqualTo(expectedExerciseIdentifier);		
	}
	
	public void assertLegislativeActMovementIncluded(String identifier,Long entryAuthorization,Long paymentCredit) {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) actPersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(actPersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(LegislativeActImpl.class))
				.addFilterField("identifier", identifier).addProjectionsFromStrings(LegislativeActImpl.FIELDS_AMOUNTS_MOVEMENT_INCLUDED)
				);

		assertThat(legislativeAct).as("collectif").isNotNull();
		
		assertThat(legislativeAct.getEntryAuthorization()).as("AE").isNotNull();
		assertThat(legislativeAct.getEntryAuthorization().getMovementIncluded()).as("AE mouvement inclus").isEqualTo(entryAuthorization);
		
		assertThat(legislativeAct.getPaymentCredit()).as("CP").isNotNull();
		assertThat(legislativeAct.getPaymentCredit().getMovementIncluded()).as("CP mouvement inclus").isEqualTo(paymentCredit);
	}
	
	public void assertLegislativeActVersion(String identifier,String expectedCode,String expectedName,Byte expectedNumber,String expectedActIdentifier) {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) actVersionPersistence.readOne(identifier, List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_CODE,LegislativeActVersionImpl.FIELD_NAME
				,LegislativeActVersionImpl.FIELD_NUMBER,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo(identifier);
		assertThat(legislativeActVersion.getCode()).isEqualTo(expectedCode);
		assertThat(legislativeActVersion.getName()).isEqualTo(expectedName);
		assertThat(legislativeActVersion.getNumber()).isEqualTo(expectedNumber);
		assertThat(legislativeActVersion.getActIdentifier()).isEqualTo(expectedActIdentifier);
	}
	
	public void assertLegislativeActVersionIdentifier(String identifier,String expectedVersionIdentifier) {
		Collection<Object[]> arrays = new LegislativeActImplDefaultVersionIdentifierReader().readByIdentifiers(List.of(identifier), null);
		assertThat(arrays).hasSize(1);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[0]).isEqualTo(identifier);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[1]).isEqualTo(expectedVersionIdentifier);
	}
	
	public void assertLegislativeActInProgress(String identifier,Boolean expectedInProgress) {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) actPersistence.readOne(identifier, List.of(LegislativeActImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_IN_PROGRESS));
		assertThat(legislativeAct).isNotNull();
		assertThat(legislativeAct.getIdentifier()).isEqualTo(identifier);
		assertThat(legislativeAct.getInProgress()).isEqualTo(expectedInProgress);
	}
	
	public void assertGeneratedActIdentifiersByLegislativeActVersionIdentifier(String legislativeActVersionIdentifier,Collection<String> expectedIdentifiers) {
		assertIdentifiers(generatedActPersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersionIdentifier)), expectedIdentifiers);
	}
	
	public void assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier(String generatedActIdentifier,Collection<String> expectedIdentifiers) {
		assertIdentifiers(generatedActExpenditurePersistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.GENERATED_ACT_IDENTIFIER,generatedActIdentifier)), expectedIdentifiers);
	}
	
	public void assertExpenditureMovementIncluded(String identifier,Long entryAuthorization,Long paymentCredit) {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ExpenditureImpl.class))
				.addFilterField("identifier", identifier).addProjectionsFromStrings(ExpenditureImpl.FIELDS_AMOUNTS_MOVEMENT_INCLUDED)
				);

		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
		assertThat(expenditure.getEntryAuthorization().getMovementIncluded()).isEqualTo(entryAuthorization);
		
		assertThat(expenditure.getPaymentCredit()).isNotNull();
		assertThat(expenditure.getPaymentCredit().getMovementIncluded()).isEqualTo(paymentCredit);
	}
	
	public void assertExpenditureAudit(String identifier,String audit) {
		ExpenditureImpl expenditure = DependencyInjection.inject(EntityManager.class).find(ExpenditureImpl.class, identifier);
		new ExpenditureImplAuditReader().readThenSet(List.of(expenditure), null);
		assertThat(expenditure.get__audit__()).as("audit").startsWith(audit);
	}
	
	public void assertExpenditureAudits(String identifier,String who,String functionality,String what,Long when) {
		Collection<Object[]> arrays =  new ExpenditureImplAuditsReader().readByIdentifiers(List.of(identifier), null);
		Object[] array = CollectionHelper.getFirst(arrays);
		assertAudits(new Object[] {array[1],array[2],array[3],array[4]}, who, functionality, what, when);
	}
	
	public void assertExpenditureAudits(String identifier,String who,String functionality,String what) {
		assertExpenditureAudits(identifier, who, functionality, what, System.currentTimeMillis());
	}
	
	public void assertResourceAudit(String identifier,String audit) {
		ResourceImpl resource = DependencyInjection.inject(EntityManager.class).find(ResourceImpl.class, identifier);
		new ResourceImplAuditReader().readThenSet(List.of(resource), null);
		assertThat(resource.get__audit__()).as("audit").startsWith(audit);
	}
	
	public void assertResourceAudits(String identifier,String who,String functionality,String what,Long when) {
		Collection<Object[]> arrays =  new ResourceImplAuditsReader().readByIdentifiers(List.of(identifier), null);
		Object[] array = CollectionHelper.getFirst(arrays);
		assertAudits(new Object[] {array[1],array[2],array[3],array[4]}, who, functionality, what, when);
	}
	
	public void assertResourceAudits(String identifier,String who,String functionality,String what) {
		assertResourceAudits(identifier, who, functionality, what, System.currentTimeMillis());
	}
	
	public void assertAudits(Object[] array,String who,String functionality,String what,Long when) {
		assertThat(array[0]).as("audit who").isEqualTo(who);
		assertThat(array[1]).as("audit functionality").isEqualTo(functionality);
		assertThat(array[2]).as("audit what").isEqualTo(what);
		assertThat(TimeHelper.toMillisecond((LocalDateTime)array[3])).as("audit when").isLessThanOrEqualTo(when);
	}
	
	public void assertRegulatoryAct(String identifier,String legislativeActVersionIdentifier,Boolean included) {
		Collection<Object[]> arrays = new RegulatoryActImplIncludedReader().readByIdentifiers(List.of(identifier), Map.of(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersionIdentifier));
		assertThat(arrays).hasSize(1);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[0]).isEqualTo(identifier);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[1]).isEqualTo(included);
	}
	
	public void assertRegulatoryActAudit(String identifier,String legislativeActVersionIdentifier,String who,String functionality,String what) {
		Collection<Object[]> arrays = new RegulatoryActImplAuditsReader().readByIdentifiers(List.of(identifier), Map.of(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersionIdentifier));
		Object[] array = CollectionHelper.getFirst(arrays);
		assertAudits(new Object[] {array[1],array[2],array[3],array[4]}, who, functionality, what, System.currentTimeMillis());
	}
	
	public void assertEntryAuthorization(String identifier,Long value) {
		Collection<Object[]> arrays = new ExpenditureImplEntryAuthorizationAdjustmentReader().readByIdentifiers(List.of(identifier), null);
		assertThat(arrays).hasSize(1);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[0]).isEqualTo(identifier);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[1]).isEqualTo(value);
	}
	
	public void assertPaymentCredit(String identifier,Long value) {
		Collection<Object[]> arrays = new ExpenditureImplPaymentCreditAdjustmentReader().readByIdentifiers(List.of(identifier), null);
		assertThat(arrays).hasSize(1);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[0]).isEqualTo(identifier);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[1]).isEqualTo(value);
	}
	
	public void assertRevenue(String identifier,Long value) {
		Collection<Object[]> arrays = new ResourceImplRevenueAdjustmentReader().readByIdentifiers(List.of(identifier), null);
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
	
	public void assertClientLegislativeActVersionsIdentifiersByLegislativeActIdentifier(String identifier,Collection<String> expectedIdentifiers) {
		Collection<ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion> actVersions = actVersionController.getByActIdentifier(identifier);
		assertIdentifiers(actVersions, expectedIdentifiers);
    }
	
    public <T> List<T> assertClientGetManyNamable(Class<T> klass,Collection<String> expectedCodes,String expectedTotalCount) {
		Response response = specificServiceGetter.get(klass).get(null,null,null, null, Boolean.TRUE, Boolean.TRUE, null, 3);
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
		Response response = specificServiceGetter.get(klass).count(null,null,null);
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
    
    public void assertLegislativeActByLegislativeActVersion(String identifier,String expectedIdentifier) {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(actPersistence.getQueryIdentifierReadDynamicOne()));
		arguments.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,identifier);
		LegislativeAct act = actPersistence.readOne(arguments);
		if(StringHelper.isBlank(expectedIdentifier)) {
			assertThat(act).as("Aucun acte budgétaire ayant une version identifiée par "+identifier).isNull();
		}else {
			assertThat(act).as("Un acte budgétaire trouvé ayant une version identifiée par "+identifier).isNotNull();
			assertThat(act.getIdentifier()).as("Identifiant de l'acte budgétaire est "+act.getIdentifier()).isEqualTo(expectedIdentifier);
		}
	}
    
    public void assertExpenditureByLegislativeAct(String identifier,Collection<String> expectedIdentifiers) {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamic()));
		arguments.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_IDENTIFIER,identifier);
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(arguments);
		if(CollectionHelper.isEmpty(expectedIdentifiers)) {
			assertThat(expenditures).as("Aucune dépenses trouvées dans l'acte budgétaire identifié par "+identifier).isNull();
		}else {
			assertThat(expenditures).as("Des dépenses trouvées dans l'acte budgétaire identifié par "+identifier).isNotEmpty();
			assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly(expectedIdentifiers.toArray(new String[] {}));
		}
	}
    
    public void assertExpenditureByLegislativeActVersion(String identifier,Collection<String> expectedIdentifiers) {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamic()));
		arguments.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,identifier);
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(arguments);
		if(CollectionHelper.isEmpty(expectedIdentifiers)) {
			assertThat(expenditures).as("Aucune dépenses trouvées dans la version de l'acte budgétaire identifié par "+identifier).isNull();
		}else {
			assertThat(expenditures).as("Des dépenses trouvées dans la version de l'acte budgétaire identifié par "+identifier).isNotEmpty();
			assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly(expectedIdentifiers.toArray(new String[] {}));
		}
	}
    
    public void assertResourceByLegislativeActVersion(String identifier,Collection<String> expectedIdentifiers) {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(resourcePersistence.getQueryIdentifierReadDynamic()));
		arguments.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,identifier);
		Collection<Resource> resources = resourcePersistence.readMany(arguments);
		if(CollectionHelper.isEmpty(expectedIdentifiers)) {
			assertThat(resources).as("Aucune ressources trouvées dans la version de l'acte budgétaire identifié par "+identifier).isNull();
		}else {
			assertThat(resources).as("Des ressources trouvées dans la version de l'acte budgétaire identifié par "+identifier).isNotEmpty();
			assertThat(resources.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly(expectedIdentifiers.toArray(new String[] {}));
		}
	}
    
    public void assertLegislativeActVersionByLegislativeAct(String identifier,Collection<String> expectedIdentifiers) {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(actVersionPersistence.getQueryIdentifierReadDynamic()));
		arguments.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_IDENTIFIER,identifier);
		Collection<LegislativeActVersion> actVersions = actVersionPersistence.readMany(arguments);
		if(CollectionHelper.isEmpty(expectedIdentifiers)) {
			assertThat(actVersions).as("Aucune versions de d'actes budgétaires trouvées dans l'acte budgétaire identifié par "+identifier).isNull();
		}else {
			assertThat(actVersions).as("Des versions d'actes budgétaires trouvées dans l'acte budgétaire identifié par "+identifier).isNotEmpty();
			assertThat(actVersions.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly(expectedIdentifiers.toArray(new String[] {}));
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
			assertThat(expenditures).as("Aucune dépenses trouvées avec la nature de dépense identifiée par "+identifier).isNull();
		}else {
			assertThat(expenditures).as("Des dépenses trouvées avec la nature de dépense identifiée par "+identifier).isNotEmpty();
			assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly(expectedIdentifiers.toArray(new String[] {}));
		}
	}
}