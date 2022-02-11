package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Business.LegislativeActVersion.Copy.class)
public class BusinessLegislativeActVersionCopyTest {

	@Inject Assertor assertor;
	@Inject LegislativeActVersionPersistence persistence;
	@Inject LegislativeActVersionBusiness business;
	
	@Test
	void copy() {
		assertor.assertEntryAuthorization("2022_1_1_1", 0l);
		assertor.assertPaymentCredit("2022_1_1_1", 6l);
		assertor.assertEntryAuthorization("2022_1_2_1", 2l);
		assertor.assertPaymentCredit("2022_1_2_1", 0l);
		
		business.copy("2022_1_1", "2022_1_2",null, "meliane");
		
		assertor.assertEntryAuthorization("2022_1_1_1", 0l);
		assertor.assertPaymentCredit("2022_1_1_1", 6l);
		assertor.assertEntryAuthorization("2022_1_2_1", 0l);
		assertor.assertPaymentCredit("2022_1_2_1", 6l);
	}
	
	@Test
	void copy_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.copy(null, null, null, null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Version collectif budgétaire est requis\r\nL'identifiant de Version collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
}