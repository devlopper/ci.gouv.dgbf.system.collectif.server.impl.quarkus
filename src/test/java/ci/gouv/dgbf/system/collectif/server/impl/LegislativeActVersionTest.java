package ci.gouv.dgbf.system.collectif.server.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.client.SpecificServiceGetter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActVersionDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.LegislativeActVersion.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class LegislativeActVersionTest {

	@Inject Assertor assertor;
	@Inject LegislativeActVersionPersistence persistence;
	@Inject LegislativeActVersionBusiness business;
	
	@Test @Order(1)
	void persistence_actFromDateAsTimestampDateAsTimestamp_2021_1_2() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne(new QueryExecutorArguments()
				.addFilterField(persistence.getParameterNameIdentifier(), "2021_1_2").addProjectionsFromStrings(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELDS_ACT_FROM_DATE_AS_TIMESTAMP_DATE_AS_TIMESTAMP));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).as("Identifiant").isEqualTo("2021_1_2");
		assertThat(legislativeActVersion.getActFromDateAsTimestamp()).as("Act from date as timestamp").isNull();
		assertThat(legislativeActVersion.getActDateAsTimestamp()).as("Act date as timestamp").isEqualTo(946684800000L);
	}
	
	@Test @Order(1)
	void persistence_actFromDateAsTimestampDateAsTimestamp_2022_1_1() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne(new QueryExecutorArguments()
				.addFilterField(persistence.getParameterNameIdentifier(), "2022_1_1").addProjectionsFromStrings(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELDS_ACT_FROM_DATE_AS_TIMESTAMP_DATE_AS_TIMESTAMP));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).as("Identifiant").isEqualTo("2022_1_1");
		assertThat(legislativeActVersion.getActFromDateAsTimestamp()).as("Act from date as timestamp").isNull();
		assertThat(legislativeActVersion.getActDateAsTimestamp()).as("Act date as timestamp").isEqualTo(1640995200000L);
	}
	
	@Test @Order(1)
	void persistence_actFromDateAsTimestampDateAsTimestamp_2022_2_1() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne(new QueryExecutorArguments()
				.addFilterField(persistence.getParameterNameIdentifier(), "2022_2_1").addProjectionsFromStrings(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELDS_ACT_FROM_DATE_AS_TIMESTAMP_DATE_AS_TIMESTAMP));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).as("Identifiant").isEqualTo("2022_2_1");
		assertThat(legislativeActVersion.getActFromDateAsTimestamp()).as("Act from date as timestamp").isEqualTo(946684800000L);
		assertThat(legislativeActVersion.getActDateAsTimestamp()).as("Act date as timestamp").isEqualTo(1640995200000L);
	}
	
	@Test @Order(1)
	void persistence_asStrings() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne(new QueryExecutorArguments()
				.addFilterField(persistence.getParameterNameIdentifier(), "2021_1_2").addProjectionsFromStrings(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELDS_STRINGS));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).as("Identifiant").isEqualTo("2021_1_2");
		//assertThat(legislativeActVersion.getActDateAsTimestamp()).as("Date as timestamp").isEqualTo(946684800000L);
	}
	
	@Test @Order(1)
	void persistence_actDateAsTimestamp() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()))
				.addFilterField(Parameters.DEFAULT_LEGISLATIVE_ACT_VERSION_IN_LATEST_LEGISLATIVE_ACT, Boolean.TRUE).addProjectionsFromStrings(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_ACT_DATE_AS_TIMESTAMP));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).as("Identifiant").isEqualTo("2022_1_2");
		assertThat(legislativeActVersion.getActDateAsTimestamp()).as("Date as timestamp").isEqualTo(1640995200000L);
	}
	
	@Test @Order(1)
	void persistence_defaultVersionInLatestAct() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()))
				.addFilterField(Parameters.DEFAULT_LEGISLATIVE_ACT_VERSION_IN_LATEST_LEGISLATIVE_ACT, Boolean.TRUE).addProjectionsFromStrings(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_IS_DEFAULT_VERSION));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("2022_1_2");
	}
	
	@Test @Order(1)
	void persistence_isDefaultVersion_2021_1_1() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()))
				.addFilterField("identifier", "2021_1_1").addProjectionsFromStrings(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_IS_DEFAULT_VERSION)
				);
		assertThat(legislativeActVersion).isNotNull();	
		assertThat(legislativeActVersion.getIsDefaultVersion()).isEqualTo(Boolean.TRUE);
	}
	
	@Test @Order(1)
	void persistence_isDefaultVersionAsString_2021_1_1() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()))
				.addFilterField("identifier", "2021_1_1").addProjectionsFromStrings(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELDS_STRINGS)
				);
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIsDefaultVersionAsString()).isEqualTo("Oui");
	}
	
	@Test @Order(1)
	void persistence_isDefaultVersion_2021_1_2() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()))
				.addFilterField("identifier", "2021_1_2").addProjectionsFromStrings(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_IS_DEFAULT_VERSION)
				);
		assertThat(legislativeActVersion).isNotNull();	
		assertThat(legislativeActVersion.getIsDefaultVersion()).isEqualTo(Boolean.FALSE);
	}
	
	@Test @Order(1)
	void persistence_isDefaultVersionAsString_2021_1_2() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()))
				.addFilterField("identifier", "2021_1_2").addProjectionsFromStrings(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELDS_STRINGS)
				);
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIsDefaultVersionAsString()).isEqualTo("Non");
	}
	
	@Test @Order(1)
	void persistence_readExpenditureOne_amounts_1() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne()))
				.addFilterField("identifier", "2021_1_1").addProjectionsFromStrings(LegislativeActVersionImpl.FIELDS_AMOUNTS,LegislativeActVersionImpl.FIELD___AUDIT__)
				);
		assertThat(legislativeActVersion).isNotNull();
		
		assertThat(legislativeActVersion.getEntryAuthorization()).isNotNull();
		assertThat(legislativeActVersion.getEntryAuthorization().getInitial()).isEqualTo(11l);
		assertThat(legislativeActVersion.getEntryAuthorization().getActual()).isEqualTo(19l);
		assertThat(legislativeActVersion.getEntryAuthorization().getMovement()).isEqualTo(8l);
		assertThat(legislativeActVersion.getEntryAuthorization().getAdjustment()).isEqualTo(0l);
		assertThat(legislativeActVersion.getEntryAuthorization().getActualPlusAdjustment()).isEqualTo(19l);
		assertThat(legislativeActVersion.getEntryAuthorization().getMovementIncluded()).isEqualTo(0l);
		assertThat(legislativeActVersion.getEntryAuthorization().getExpectedAdjustment()).isEqualTo(10l);
		assertThat(legislativeActVersion.getEntryAuthorization().getExpectedAdjustmentMinusAdjustment()).isEqualTo(10l);
		
		assertThat(legislativeActVersion.getPaymentCredit()).isNotNull();
		assertThat(legislativeActVersion.getPaymentCredit().getInitial()).isEqualTo(3l);
		assertThat(legislativeActVersion.getPaymentCredit().getActual()).isEqualTo(5l);
		assertThat(legislativeActVersion.getPaymentCredit().getMovement()).isEqualTo(2l);
		assertThat(legislativeActVersion.getPaymentCredit().getAdjustment()).isEqualTo(6l);
		assertThat(legislativeActVersion.getPaymentCredit().getActualPlusAdjustment()).isEqualTo(11l);
		assertThat(legislativeActVersion.getPaymentCredit().getMovementIncluded()).isEqualTo(0l);
		assertThat(legislativeActVersion.getPaymentCredit().getExpectedAdjustment()).isEqualTo(20l);
		assertThat(legislativeActVersion.getPaymentCredit().getExpectedAdjustmentMinusAdjustment()).isEqualTo(14l);
	}
	
	@Test @Order(1)
	void persistence_readMany() {
		Collection<LegislativeActVersion> legislativeActVersions = persistence.readMany(null, null, null);
		assertThat(legislativeActVersions).hasSize(9);
	}
	
	@Test @Order(1)
	void persistence_readOne() {
		LegislativeActVersion legislativeActVersion = persistence.readOne("2022_1_1");
		assertThat(legislativeActVersion).isNotNull();
	}
	
	@Test @Order(1)
	void persistence_readOne_projections_FIELDS_ACT_AS_STRING_CODE_NAME_NUMBER() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne("2020_1_1",List.of(LegislativeActVersionImpl.FIELDS_ACT_AS_STRING_CODE_NAME_NUMBER));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("2020_1_1");
		assertThat(legislativeActVersion.getCode()).isEqualTo("2020_1_1");
		assertThat(legislativeActVersion.getName()).isEqualTo("2020_1_1");
		assertThat(legislativeActVersion.getNumber()).isEqualTo(Byte.valueOf("1"));
		assertThat(legislativeActVersion.getActAsString()).isEqualTo("2020_1");
	}
	
	@Test @Order(1)
	void persistence_readOne_projections_FIELD_GENERATED_ACT_COUNT_null() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne("2020_1_1",List.of(LegislativeActVersionImpl.FIELD_GENERATED_ACT_COUNT));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("2020_1_1");
		assertThat(legislativeActVersion.getGeneratedActCount()).isEqualTo(Short.valueOf("0"));
	}
	
	@Test @Order(1)
	void persistence_readOne_projections_FIELD_GENERATED_ACT_COUNT_notNull() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne("2022_1_1",List.of(LegislativeActVersionImpl.FIELD_GENERATED_ACT_COUNT));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("2022_1_1");
		assertThat(legislativeActVersion.getGeneratedActCount()).isEqualTo(Short.valueOf("1"));
	}
	
	@Test @Order(1)
    public void service_get_many() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class).get(null,null, null, null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("9");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion> legislativeActVersions = ResponseHelper.getEntityAsListFromJson(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class,response);
		assertThat(legislativeActVersions).hasSize(9);
		assertThat(legislativeActVersions.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_2_1","2022_1_3","2022_1_2","2022_1_1","2021_1_4","2021_1_3","2021_1_2","2021_1_1","2020_1_1");
    }
	
	@Test @Order(1)
    public void service_get_many_identifier_generatedActCount() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class).get(null,null, List.of(LegislativeActVersionDto.JSON_IDENTIFIER
				,LegislativeActVersionDto.JSONS_GENERATED_ACT_COUNT_ACT_GENERATABLE_GENERATED_ACT_DELETABLE), null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("9");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion> legislativeActVersions = ResponseHelper.getEntityAsListFromJson(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class,response);
		assertThat(legislativeActVersions).hasSize(9);
		assertThat(legislativeActVersions.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_2_1","2022_1_3","2022_1_2","2022_1_1","2021_1_4","2021_1_3","2021_1_2","2021_1_1","2020_1_1");
		assertThat(legislativeActVersions.stream().map(e -> e.getGeneratedActCount()).collect(Collectors.toList())).containsExactly(Short.valueOf("0"),Short.valueOf("0"),Short.valueOf("0"),Short.valueOf("1"),Short.valueOf("0"),Short.valueOf("0"),Short.valueOf("0"),Short.valueOf("0"),Short.valueOf("0"));
		assertThat(legislativeActVersions.stream().map(e -> e.getActGeneratable()).collect(Collectors.toList())).containsExactly(Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
		assertThat(legislativeActVersions.stream().map(e -> e.getGeneratedActDeletable()).collect(Collectors.toList())).containsExactly(Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.TRUE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE);
    }
	
	@Test @Order(1)
    public void service_get_one() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class).getByIdentifier("2022_1_1", null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion legislativeActVersion = ResponseHelper.getEntity(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class,response);
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("2022_1_1");
		assertThat(legislativeActVersion.getGeneratedActCount()).isNull();
    }
	
	@Test @Order(1)
    public void service_get_one_generatedActCount() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class).getByIdentifier("2022_1_1", List.of(LegislativeActVersionDto.JSON_IDENTIFIER
				,LegislativeActVersionDto.JSONS_GENERATED_ACT_COUNT_ACT_GENERATABLE_GENERATED_ACT_DELETABLE));
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion legislativeActVersion = ResponseHelper.getEntity(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class,response);
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("2022_1_1");
		assertThat(legislativeActVersion.getGeneratedActCount()).isEqualTo(Short.valueOf("1"));
    }
	
	@Test @Order(1)
    public void service_count() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class).count(null,null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Long count = ResponseHelper.getEntityAsLong(response);
		assertThat(count).isEqualTo(9l);
    }
	
	@Test @Order(1)
    public void client_get_many() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class).get(null,null, null, null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("9");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion> legislativeActVersions = ResponseHelper.getEntityAsListFromJson(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class,response);
		assertThat(legislativeActVersions).hasSize(9);
		assertThat(legislativeActVersions.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_2_1","2022_1_3","2022_1_2","2022_1_1","2021_1_4","2021_1_3","2021_1_2","2021_1_1","2020_1_1");
    }
	
	@Test @Order(1)
    public void client_get_many_identifier_generatedActCount() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class).get(null,null, List.of(LegislativeActVersionDto.JSON_IDENTIFIER
				,LegislativeActVersionDto.JSONS_GENERATED_ACT_COUNT_ACT_GENERATABLE_GENERATED_ACT_DELETABLE), null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("9");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion> legislativeActVersions = ResponseHelper.getEntityAsListFromJson(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class,response);
		assertThat(legislativeActVersions).hasSize(9);
		assertThat(legislativeActVersions.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_2_1","2022_1_3","2022_1_2","2022_1_1","2021_1_4","2021_1_3","2021_1_2","2021_1_1","2020_1_1");
		assertThat(legislativeActVersions.stream().map(e -> e.getGeneratedActCount()).collect(Collectors.toList())).containsExactly(Short.valueOf("0"),Short.valueOf("0"),Short.valueOf("0"),Short.valueOf("1"),Short.valueOf("0"),Short.valueOf("0"),Short.valueOf("0"),Short.valueOf("0"),Short.valueOf("0"));
		assertThat(legislativeActVersions.stream().map(e -> e.getActGeneratable()).collect(Collectors.toList())).containsExactly(Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
		assertThat(legislativeActVersions.stream().map(e -> e.getGeneratedActDeletable()).collect(Collectors.toList())).containsExactly(Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.TRUE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE);
    }
	
	@Test @Order(1)
    public void client_get_one() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class).getByIdentifier("2022_1_1", null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion legislativeActVersion = ResponseHelper.getEntity(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class,response);
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("2022_1_1");
		assertThat(legislativeActVersion.getGeneratedActCount()).isNull();
    }
	
	@Test @Order(1)
    public void client_get_one_generatedActCount() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class).getByIdentifier("2022_1_1", List.of(LegislativeActVersionDto.JSON_IDENTIFIER
				,LegislativeActVersionDto.JSONS_GENERATED_ACT_COUNT_ACT_GENERATABLE_GENERATED_ACT_DELETABLE));
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion legislativeActVersion = ResponseHelper.getEntity(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class,response);
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("2022_1_1");
		assertThat(legislativeActVersion.getGeneratedActCount()).isEqualTo(Short.valueOf("1"));
    }
	
	@Test @Order(1)
    public void client_count() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion.class).count(null,null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Long count = ResponseHelper.getEntityAsLong(response);
		assertThat(count).isEqualTo(9l);
    }
	
	/* Create */
	
	@Test @Order(2)
	void business_create() {
		assertThat(persistence.readOne("2022_1_4")).isNull();
		business.create(null, null,null,"2022_1", "meliane");
		assertor.assertLegislativeActVersion("2022_1_4", "2022_1_4","Version 4 2022_1",Byte.valueOf("4"),"2022_1");
	}
	
	@Test @Order(2)
	void business_create_exerciseIdentifierNull() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.create((String)null,(String)null,(Byte)null,(String)null,(String)null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
	
	/* Copy */
	
	@Test @Order(3)
	void business_copy() {
		assertor.assertEntryAuthorization("2021_1_2_4", 3l);
		assertor.assertPaymentCredit("2021_1_2_4", 4l);
		assertor.assertEntryAuthorization("2021_1_3_4", 0l);
		assertor.assertPaymentCredit("2021_1_3_4", 0l);
		
		business.copy("2021_1_2","2021_1_3",null, "meliane");
		
		assertor.assertEntryAuthorization("2021_1_2_4", 3l);
		assertor.assertPaymentCredit("2021_1_2_4", 4l);
		assertor.assertEntryAuthorization("2021_1_3_4", 3l);
		assertor.assertPaymentCredit("2021_1_3_4", 4l);
	}
	
	@Test @Order(3)
	void business_copy_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.copy(null, null, null, null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Version collectif budgétaire est requis\r\nL'identifiant de Version collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
	
	/* Duplicate */
	
	//@Test @Order(3)
	void business_duplicate() {
		assertor.assertExpenditureByLegislativeActVersion("2021_1_4", List.of("2021_1_4_1","2021_1_4_2","2021_1_4_3","2021_1_4_4","2021_1_4_5"));
		assertor.assertExpenditureByLegislativeActVersion("2021_1_5",null);
		business.duplicate("2021_1_4", "meliane");
		assertor.assertExpenditureByLegislativeActVersion("2021_1_5", List.of("2021_1_5_1","2021_1_5_2","2021_1_5_3","2021_1_5_4","2021_1_5_5"));
	}
	
	@Test @Order(3)
	void business_duplicate_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.duplicate(null, null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Version collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
	
	public static String PA_ACTUALISER_VM(String tableName) {
		return null;
	}
}