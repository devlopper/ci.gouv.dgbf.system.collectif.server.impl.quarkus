package ci.gouv.dgbf.system.collectif.server.client;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Service.Unit.class)
public class ExpenditureServiceClientTest {

	@Inject Assertor assertor;
	
	@Test
    public void import_() {
		/*assertor.assertCountFile(10l);
		assertor.assertFile("1", List.of(FileImpl.FIELD_IDENTIFIER), List.of(FileImpl.FIELD_NAME_AND_EXTENSION), "1", null, null, "Chant à la croix.pdf", null, null, null,null);
		FileService fileService = FileService.getProxy();
		assertThat(fileService).isNotNull();
		fileService.import_(null, null);
		assertor.assertCountFile(13l);*/
	}
}