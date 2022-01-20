package ci.gouv.dgbf.system.collectif.server.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.cyk.utility.__kernel__.time.TimeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.client.rest.Resource;
import ci.gouv.dgbf.system.collectif.server.client.rest.ResourceController;
import ci.gouv.dgbf.system.collectif.server.client.rest.Revenue;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Client.Resource.Adjust.class)
public class ClientResourceAdjustTest {

	@Inject Assertor assertor;
	@Inject ResourceController controller;
	
	@Test
	void adjust() {
		assertor.assertResourceAudits("1", "christian", "AJUSTEMENT", "MODIFICATION", TimeHelper.toMillisecond(LocalDateTime.of(2000, 1, 2, 1, 1)));
		assertor.assertResourceAudit("1", "AJUSTEMENT par christian le");
		assertor.assertRevenue("1", 0l);
		controller.adjust("meliane",new Resource().setIdentifier("1").setRevenue(new Revenue().setAdjustment(3l)));
		assertor.assertRevenue("1", 3l);
		assertor.assertResourceAudits("1", "meliane", "AJUSTEMENT", "MODIFICATION");
		assertor.assertResourceAudit("1", "AJUSTEMENT par meliane le");
	}
	
	@Test
	void adjust_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			controller.adjust("anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("Ajustements requis");
	}
}