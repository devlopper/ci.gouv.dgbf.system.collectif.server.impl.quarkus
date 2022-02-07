package ci.gouv.dgbf.system.collectif.server.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.client.SpecificServiceGetter;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.service.ResourceDto;
import ci.gouv.dgbf.system.collectif.server.client.rest.Resource;
import ci.gouv.dgbf.system.collectif.server.impl.AbstractClientTest;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Client.Resource.Read.class)
public class ClientResourceReadTest extends AbstractClientTest{

	@Test
    public void get_many() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Resource.class).get(null,null, null, null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("4");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<Resource> resources = ResponseHelper.getEntityAsListFromJson(Resource.class,response);
		assertThat(resources).hasSize(4);
		assertThat(resources.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2","3","4");
    }
	
	@Test
    public void get_many_asStrings() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Resource.class).get(null,null, List.of("astrings"), null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("4");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<Resource> resources = ResponseHelper.getEntityAsListFromJson(Resource.class,response);
		assertThat(resources).hasSize(4);
		assertThat(resources.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2","3","4");
    }
	
	@Test
    public void get_one() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Resource.class).getByIdentifier("1", null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Resource resource = ResponseHelper.getEntity(Resource.class,response);
		assertThat(resource).isNotNull();
		assertThat(resource.getIdentifier()).isEqualTo("1");
		assertThat(resource.getRevenue()).isNull();
    }
	
	@Test
    public void get_one_revenue() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Resource.class).getByIdentifier("1", List.of(ResourceDto.JSON_IDENTIFIER,ResourceDto.JSON_REVENUE));
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Resource resource = ResponseHelper.getEntity(Resource.class,response);
		assertThat(resource).isNotNull();
		assertThat(resource.getIdentifier()).isEqualTo("1");
		assertThat(resource.getRevenue()).isNotNull();
		assertThat(resource.getRevenue().getAdjustment()).isEqualTo(3l);
    }
	
	@Test
    public void count() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Resource.class).count(null,null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Long count = ResponseHelper.getEntityAsLong(response);
		assertThat(count).isEqualTo(4l);
    }
	/*
	@Test
    public void adjustByEntryAuthorizations() {
		assertor.assertEntryAuthorization("1", 0l);
		assertor.assertPaymentCredit("1", 0l);
		Response response = Resource.getService().adjustByEntryAuthorizations(List.of(new ResourceDto.AdjustmentDto().setIdentifier("1").setRevenue(17l)),"test");
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertor.assertEntryAuthorization("1", 17l);
		assertor.assertPaymentCredit("1", 17l);
    }
    
	@Test
    public void adjust() {
		assertor.assertEntryAuthorization("2", 3l);
		assertor.assertPaymentCredit("2", 0l);
		Response response = Resource.getService().adjust(List.of(new ResourceDto.AdjustmentDto().setIdentifier("2").setEntryAuthorization(7l).setPaymentCredit(5l)),"test");
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertor.assertEntryAuthorization("2", 7l);
		assertor.assertPaymentCredit("2", 5l);
    }*/
}