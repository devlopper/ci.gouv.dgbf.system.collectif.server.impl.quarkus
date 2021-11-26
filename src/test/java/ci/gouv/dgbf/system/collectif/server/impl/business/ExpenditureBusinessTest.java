package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
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
	
	@Test
	void adjust_identifierNotExist() {
		Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjust(Map.of("identifier_not_exist",0l));
	    });
	}
	
	@Test
	void adjust_availableNotEnough() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjust(Map.of("identifier_available_not_enough",-2l));
	    });
		assertThat(exception.getMessage()).isEqualTo("La ligne identifier_available_not_enough à un disponible insuffisant(-2,0)");
	}
}