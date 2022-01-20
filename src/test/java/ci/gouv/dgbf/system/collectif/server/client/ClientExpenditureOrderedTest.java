package ci.gouv.dgbf.system.collectif.server.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.client.SpecificServiceGetter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureDto;
import ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Client.Default.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class ClientExpenditureOrderedTest {

	@Inject Assertor assertor;
	
	@Test @Order(1)
    public void get_many() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Expenditure.class).get(null,null, null, null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("2");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<Expenditure> expenditures = ResponseHelper.getEntityAsListFromJson(Expenditure.class,response);
		assertThat(expenditures).hasSize(2);
		assertThat(expenditures.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2");
    }
	
	@Test @Order(1)
    public void get_many_asStrings() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Expenditure.class).get(null,null, List.of("astrings"), null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo("2");
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<Expenditure> expenditures = ResponseHelper.getEntityAsListFromJson(Expenditure.class,response);
		assertThat(expenditures).hasSize(2);
		assertThat(expenditures.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2");
    }
	
	@Test @Order(1)
    public void get_one() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Expenditure.class).getByIdentifier("1", null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Expenditure expenditure = ResponseHelper.getEntity(Expenditure.class,response);
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getIdentifier()).isEqualTo("1");
		assertThat(expenditure.getEntryAuthorization()).isNull();
    }
	
	@Test @Order(1)
    public void get_one_entryAuthorization() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Expenditure.class).getByIdentifier("2", List.of(ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION));
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Expenditure expenditure = ResponseHelper.getEntity(Expenditure.class,response);
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getIdentifier()).isEqualTo("2");
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
		assertThat(expenditure.getEntryAuthorization().getAdjustment()).isEqualTo(3l);
		assertThat(expenditure.getPaymentCredit()).isNull();
    }
	
	@Test @Order(1)
    public void count() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(Expenditure.class).count(null,null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Long count = ResponseHelper.getEntityAsLong(response);
		assertThat(count).isEqualTo(2l);
    }
	
	@Test @Order(2)
    public void adjustByEntryAuthorizations() {
		assertor.assertEntryAuthorization("1", 0l);
		assertor.assertPaymentCredit("1", 0l);
		Response response = Expenditure.getService().adjustByEntryAuthorizations(List.of(new ExpenditureDto.AdjustmentDto().setIdentifier("1").setEntryAuthorization(17l)),"test");
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertor.assertEntryAuthorization("1", 17l);
		assertor.assertPaymentCredit("1", 17l);
    }
	
	@Test @Order(2)
    public void adjust() {
		assertor.assertEntryAuthorization("2", 3l);
		assertor.assertPaymentCredit("2", 0l);
		Response response = Expenditure.getService().adjust(List.of(new ExpenditureDto.AdjustmentDto().setIdentifier("2").setEntryAuthorization(7l).setPaymentCredit(5l)),"test");
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertor.assertEntryAuthorization("2", 7l);
		assertor.assertPaymentCredit("2", 5l);
    }
}