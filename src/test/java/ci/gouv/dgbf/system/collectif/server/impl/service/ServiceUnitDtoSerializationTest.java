package ci.gouv.dgbf.system.collectif.server.impl.service;

import javax.json.bind.JsonbBuilder;

import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestProfile(Profiles.Service.Unit.class)
public class ServiceUnitDtoSerializationTest {

	@Test
    public void serialize_legislativeActVersion() {
		LegislativeActVersionDtoImpl legislativeActVersionDto = new LegislativeActVersionDtoImpl();
		//legislativeActVersionDto.setIdentifier("1");
		//legislativeActVersionDto.setCode("c");
		//legislativeActVersionDto.setName("n");
		legislativeActVersionDto.setAct(new LegislativeActDtoImpl());
		assertThat(JsonbBuilder.create().toJson(legislativeActVersionDto)).isNotBlank();
    }
	
	@Test
    public void serialize_expenditure() {
		ExpenditureDtoImpl expenditure = new ExpenditureDtoImpl();
		expenditure.setEntryAuthorization(new EntryAuthorizationDtoImpl());
		assertThat(JsonbBuilder.create().toJson(expenditure)).isNotBlank();
    }
}