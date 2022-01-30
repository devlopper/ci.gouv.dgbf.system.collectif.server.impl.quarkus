package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Business.LegislativeAct.Create.class)
public class BusinessLegislativeActCreateTest {

	@Inject Assertor assertor;
	@Inject LegislativeActPersistence persistence;
	@Inject LegislativeActBusiness business;
	
	@Test
	void create() {
		assertThat(persistence.readOne("2021"));
		business.create(null, null,"2021", "meliane");
		assertor.assertLegislativeAct("2021", "2021","Collectif budgétaire 2021","2021");
	}
	
	@Test
	void create_exerciseIdentifierNull() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.create((String)null,(String)null,(String)null,(String)null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Exercice est requis\r\nLe nom d'utilisateur est requis");
	}
}