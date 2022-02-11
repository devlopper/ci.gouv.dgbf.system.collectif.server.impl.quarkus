package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
@TestProfile(Profiles.Business.LegislativeActVersion.Duplicate.class)
public class BusinessLegislativeActVersionDuplicateTest {

	@Inject Assertor assertor;
	@Inject LegislativeActVersionPersistence persistence;
	@Inject LegislativeActVersionBusiness business;
	
	@Test
	void duplicate() {
		assertor.assertExpenditureByLegislativeActVersion("2022_1_1", List.of("2022_1_1_1","2022_1_1_2","2022_1_1_3","2022_1_1_4"));
		assertor.assertExpenditureByLegislativeActVersion("2022_1_2",null);
		business.duplicate("2022_1_1", "meliane");
		assertor.assertExpenditureByLegislativeActVersion("2022_1_1", List.of("2022_1_1_1","2022_1_1_2","2022_1_1_3","2022_1_1_4"));
		//assertor.assertExpenditureByLegislativeActVersion("2022_1_2",List.of("2022_1_2_1","2022_1_2_2","2022_1_2_3","2022_1_2_4"));
	}
	
	@Test
	void duplicate_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.duplicate(null, null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Version collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
	
	/**/
	
	public static String PA_IMPORTER_DEPENSE(String legislativeActVersionIdentifier,String auditWho,String auditFunctionality,String auditWhat,java.sql.Date auditWhen) {
		return null;
	}
}