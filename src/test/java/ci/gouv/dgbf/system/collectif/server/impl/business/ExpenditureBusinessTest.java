package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Business.class)
public class ExpenditureBusinessTest {

	@Inject Assertor assertor;
	@Inject ExpenditureBusiness expenditureBusiness;
	
	@Test
	void adjust() {
		assertor.assertEntryAuthorization("1", 0l);
		expenditureBusiness.adjust(Map.of("1",3l));
		assertor.assertEntryAuthorization("1", 3l);
	}
}
