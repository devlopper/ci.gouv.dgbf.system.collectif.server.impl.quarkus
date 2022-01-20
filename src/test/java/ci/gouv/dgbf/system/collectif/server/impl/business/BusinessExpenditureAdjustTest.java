package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Map;

import javax.inject.Inject;

import org.cyk.utility.__kernel__.time.TimeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Business.Expenditure.Adjust.class)
public class BusinessExpenditureAdjustTest {

	@Inject Assertor assertor;
	@Inject ExpenditureBusiness expenditureBusiness;
	
	@Test
	void adjust() {
		assertor.assertExpenditureAudits("1", "christian", "AJUSTEMENT", "MODIFICATION", TimeHelper.toMillisecond(LocalDateTime.of(2000, 1, 2, 1, 1)));
		assertor.assertExpenditureAudit("1", "AJUSTEMENT par christian le");
		assertor.assertEntryAuthorization("1", 0l);
		assertor.assertPaymentCredit("1", 0l);
		expenditureBusiness.adjust(Map.of("1",new Long[] {3l,1l}),"meliane");
		assertor.assertEntryAuthorization("1", 3l);
		assertor.assertPaymentCredit("1", 1l);
		assertor.assertExpenditureAudits("1", "meliane", "AJUSTEMENT", "MODIFICATION");
		assertor.assertExpenditureAudit("1", "AJUSTEMENT par meliane le");
	}
	
	@Test
	void adjustByEntryAuthorizations() {
		assertor.assertEntryAuthorization("2", 0l);
		assertor.assertPaymentCredit("2", 0l);
		expenditureBusiness.adjustByEntryAuthorizations(Map.of("2",3l),"sandrine");
		assertor.assertEntryAuthorization("2", 3l);
		assertor.assertPaymentCredit("2", 3l);
		assertor.assertExpenditureAudits("2", "sandrine", "AJUSTEMENT_PAR_AE", "MODIFICATION");
	}
	
	@Test
	void adjustByEntryAuthorizations_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjustByEntryAuthorizations(null,"anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("Ajustements requis");
	}
	
	@Test
	void adjustByEntryAuthorizations_identifierNotExist() {
		Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjustByEntryAuthorizations(Map.of("identifier_not_exist",0l),"anonymous");
	    });
	}
	
	@Test
	void adjustByEntryAuthorizations_availableNotEnough() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjustByEntryAuthorizations(Map.of("identifier_available_not_enough",-2l),"anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("La ligne identifier_available_not_enough à un disponible A.E. insuffisant(-2,0)\r\nLa ligne identifier_available_not_enough à un disponible C.P. insuffisant(-2,0)");
	}
	
	@Test
	void adjust_availableNotEnough() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjust(Map.of("identifier_available_not_enough",new Long[] {-2l,-2l}),"anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("La ligne identifier_available_not_enough à un disponible A.E. insuffisant(-2,0)\r\nLa ligne identifier_available_not_enough à un disponible C.P. insuffisant(-2,0)");
	}
	
	@Test
	void adjust_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjust(null,"anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("Ajustements requis");
	}
}