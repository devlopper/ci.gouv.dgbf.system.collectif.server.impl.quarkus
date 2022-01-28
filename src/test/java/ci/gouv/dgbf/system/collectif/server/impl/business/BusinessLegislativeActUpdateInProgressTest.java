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
@TestProfile(Profiles.Business.LegislativeAct.UpdateInProgress.class)
public class BusinessLegislativeActUpdateInProgressTest {

	@Inject Assertor assertor;
	@Inject LegislativeActBusiness business;
	
	@Test
	void updateInProgress() {
		assertor.assertLegislativeActInProgress("1", Boolean.FALSE);
		business.updateInProgress("1", Boolean.TRUE, "meliane");
		assertor.assertLegislativeActInProgress("1", Boolean.TRUE);
	}
	
	@Test
	void updateInProgress_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.updateInProgress(null,null,null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Collectif budgétaire est requis\r\nLa valeur <<en cours>> est requise\r\nLe nom d'utilisateur est requis");
	}
	
	@Test
	void updateInProgress_same() {
		assertor.assertLegislativeActInProgress("2", Boolean.TRUE);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.updateInProgress("2", Boolean.TRUE, "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("Collectif Budgétaire 2021 du 01/07/2021 est déja en cours");
	}
}