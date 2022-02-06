package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActBusiness;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Business.LegislativeAct.UpdateDefaultVersion.class)
public class BusinessLegislativeActUpdateDefaultVersionTest {

	@Inject Assertor assertor;
	@Inject LegislativeActBusiness business;
	
	@Test
	void updateDefaultVersion() {
		assertor.assertLegislativeActVersionIdentifier("1", "1_1");
		business.updateDefaultVersion("1_2", "meliane");
		assertor.assertLegislativeActVersionIdentifier("1", "1_2");
	}
	
	@Test
	void updateDefaultVersion_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.updateDefaultVersion(null,null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Version collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
	
	@Test
	void updateDefaultVersion_same() {
		assertor.assertLegislativeActVersionIdentifier("2", "2_1");
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.updateDefaultVersion("2_1", "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("1 est déja la version par défaut de Collectif Budgétaire 2021 du 01/07/2021");
	}
}