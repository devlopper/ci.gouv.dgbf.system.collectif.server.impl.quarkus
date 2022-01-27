package ci.gouv.dgbf.system.collectif.server.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.client.SpecificServiceGetter;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActVersionDto;
import ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Client.LegislativeActVersion.Read.class)
public class ClientLegislativeActVersionReadTest {

	@Inject Assertor assertor;
	
	@Test
    public void get_many() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(LegislativeActVersion.class).get(null,null, null, null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("5");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<LegislativeActVersion> legislativeActVersions = ResponseHelper.getEntityAsListFromJson(LegislativeActVersion.class,response);
		assertThat(legislativeActVersions).hasSize(5);
		assertThat(legislativeActVersions.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("1_1","1_2","2_1","2_2","2_3");
    }
	
	@Test
    public void get_many_identifier_generatedActCount() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(LegislativeActVersion.class).get(null,null, List.of(LegislativeActVersionDto.JSON_IDENTIFIER
				,LegislativeActVersionDto.JSONS_GENERATED_ACT_COUNT_ACT_GENERATABLE_GENERATED_ACT_DELETABLE), null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("5");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<LegislativeActVersion> legislativeActVersions = ResponseHelper.getEntityAsListFromJson(LegislativeActVersion.class,response);
		assertThat(legislativeActVersions).hasSize(5);
		assertThat(legislativeActVersions.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("1_1","1_2","2_1","2_2","2_3");
		assertThat(legislativeActVersions.stream().map(e -> e.getGeneratedActCount()).collect(Collectors.toList())).containsExactly(Short.valueOf("0"),Short.valueOf("0"),Short.valueOf("1"),Short.valueOf("0"),Short.valueOf("0"));
		assertThat(legislativeActVersions.stream().map(e -> e.getActGeneratable()).collect(Collectors.toList())).containsExactly(Boolean.TRUE,Boolean.TRUE,Boolean.FALSE,Boolean.TRUE,Boolean.TRUE);
		assertThat(legislativeActVersions.stream().map(e -> e.getGeneratedActDeletable()).collect(Collectors.toList())).containsExactly(Boolean.FALSE,Boolean.FALSE,Boolean.TRUE,Boolean.FALSE,Boolean.FALSE);
    }
	
	@Test
    public void get_one() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(LegislativeActVersion.class).getByIdentifier("2_1", null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		LegislativeActVersion legislativeActVersion = ResponseHelper.getEntity(LegislativeActVersion.class,response);
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("2_1");
		assertThat(legislativeActVersion.getGeneratedActCount()).isNull();
    }
	
	@Test
    public void get_one_generatedActCount() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(LegislativeActVersion.class).getByIdentifier("2_1", List.of(LegislativeActVersionDto.JSON_IDENTIFIER
				,LegislativeActVersionDto.JSONS_GENERATED_ACT_COUNT_ACT_GENERATABLE_GENERATED_ACT_DELETABLE));
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		LegislativeActVersion legislativeActVersion = ResponseHelper.getEntity(LegislativeActVersion.class,response);
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("2_1");
		assertThat(legislativeActVersion.getGeneratedActCount()).isEqualTo(Short.valueOf("1"));
    }
	
	@Test
    public void count() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(LegislativeActVersion.class).count(null,null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Long count = ResponseHelper.getEntityAsLong(response);
		assertThat(count).isEqualTo(5l);
    }
}