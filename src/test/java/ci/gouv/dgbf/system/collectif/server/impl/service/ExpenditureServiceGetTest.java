package ci.gouv.dgbf.system.collectif.server.impl.service;

import javax.inject.Inject;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Service.Integration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExpenditureServiceGetTest {

	@Inject Assertor assertor;
	

}