package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Map;

import javax.inject.Inject;

import org.cyk.utility.__kernel__.time.TimeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.business.ResourceBusiness;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Business.Resource.Adjust.class)
public class BusinessResourceAdjustTest {

	@Inject Assertor assertor;
	@Inject ResourceBusiness resourceBusiness;
	
	@Test
	void adjust() {
		assertor.assertResourceAudits("1", "christian", "AJUSTEMENT", "MODIFICATION", TimeHelper.toMillisecond(LocalDateTime.of(2000, 1, 2, 1, 1)));
		assertor.assertResourceAudit("1", "AJUSTEMENT par christian le");
		assertor.assertRevenue("1", 0l);
		resourceBusiness.adjust(Map.of("1",3l),"meliane");
		assertor.assertRevenue("1", 3l);
		assertor.assertResourceAudits("1", "meliane", "AJUSTEMENT", "MODIFICATION");
		assertor.assertResourceAudit("1", "AJUSTEMENT par meliane le");
	}
	
	@Test
	void adjust_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			resourceBusiness.adjust(null,"anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("Ajustements requis");
	}
}