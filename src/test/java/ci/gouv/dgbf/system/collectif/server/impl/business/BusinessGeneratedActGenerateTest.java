package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.business.GeneratedActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.business.RegulatoryActBusiness;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Business.GeneratedAct.Generate.class)
public class BusinessGeneratedActGenerateTest {

	@Inject Assertor assertor;
	@Inject GeneratedActBusiness generatedActBusiness;
	
	/* Include */
	
	/*@Test
	void generate_alreadyGenerated() {
		assertor.assertRegulatoryAct("include_included_true","1",Boolean.TRUE);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			regulatoryActBusiness.include("1",null,"meliane", "include_included_true");
	    });
		assertThat(exception.getMessage()).isEqualTo("Les Actes de gestion suivant sont déja inclus : 1 1");
	}*/
	
	@Test
	void generate_notYetGenerated() {
		//assertor.assertRegulatoryAct("include_included_true","1",Boolean.TRUE);
		generatedActBusiness.generateByLegislativeActVersionIdentifier("1", "meliane");
		assertor.assertGeneratedActIdentifiers("1", List.of("1"));
		//assertor.assertRegulatoryAct("include_included_true","1",Boolean.TRUE);
	}
}