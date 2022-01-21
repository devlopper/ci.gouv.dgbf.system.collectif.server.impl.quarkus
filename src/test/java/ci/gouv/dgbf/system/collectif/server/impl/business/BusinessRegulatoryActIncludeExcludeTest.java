package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.business.RegulatoryActBusiness;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Business.RegulatoryAct.IncludeExclude.class)
public class BusinessRegulatoryActIncludeExcludeTest {

	@Inject Assertor assertor;
	@Inject RegulatoryActBusiness regulatoryActBusiness;
	
	/* Include */
	
	@Test
	void include_true() {
		assertor.assertRegulatoryAct("include_included_true","1",Boolean.TRUE);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			regulatoryActBusiness.include("1",null,"meliane", "include_included_true");
	    });
		assertThat(exception.getMessage()).isEqualTo("Les Actes de gestion suivant sont déja inclus : 1 1");
	}
	
	@Test
	void include_true_existingIgnorableIsTrue() {
		assertor.assertRegulatoryAct("include_included_true","1",Boolean.TRUE);
		regulatoryActBusiness.include("1",Boolean.TRUE,"meliane", "include_included_true");
		assertor.assertRegulatoryAct("include_included_true","1",Boolean.TRUE);
	}
	
	@Test
	void include_false() {
		assertor.assertRegulatoryAct("include_included_false","1",Boolean.FALSE);
		regulatoryActBusiness.include("1",Boolean.TRUE,"meliane", "include_included_false");
		assertor.assertRegulatoryAct("include_included_false","1",Boolean.TRUE);
		assertor.assertRegulatoryActAudit("include_included_false","1", "meliane", "INCLUSION", "MODIFICATION");
	}
	
	@Test
	void include_null() {
		assertor.assertRegulatoryAct("include_included_null","1",Boolean.FALSE);
		regulatoryActBusiness.include("1",Boolean.TRUE,"meliane", "include_included_null");
		assertor.assertRegulatoryAct("include_included_null","1",Boolean.TRUE);
	}
	
	@Test
	void include_not_marked0() {
		assertor.assertRegulatoryAct("not_marked0","1",null);
		regulatoryActBusiness.include("1",Boolean.TRUE,"meliane", "not_marked0");
		assertor.assertRegulatoryAct("not_marked0","1",Boolean.TRUE);
	}
	
	/* Exclude */
	
	@Test
	void exclude_false() {
		assertor.assertRegulatoryAct("exclude_included_false","1",Boolean.FALSE);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			regulatoryActBusiness.exclude("1",null,"meliane", "exclude_included_false");
	    });
		assertThat(exception.getMessage()).isEqualTo("Les Actes de gestion suivant sont déja exclus : 5 5");
	}
	
	@Test
	void exclude_null() {
		assertor.assertRegulatoryAct("exclude_included_null","1",Boolean.FALSE);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			regulatoryActBusiness.exclude("1",null,"meliane", "exclude_included_null");
	    });
		assertThat(exception.getMessage()).isEqualTo("Les Actes de gestion suivant sont déja exclus : 6 6");
	}
	
	@Test
	void exclude_true() {
		assertor.assertRegulatoryAct("exclude_included_true","1",Boolean.TRUE);
		regulatoryActBusiness.exclude("1",null,"meliane", "exclude_included_true");
		assertor.assertRegulatoryAct("exclude_included_true","1",Boolean.FALSE);
	}
}