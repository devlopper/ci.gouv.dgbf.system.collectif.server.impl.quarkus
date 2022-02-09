package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

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
@TestProfile(Profiles.Business.Expenditure.Import.class)
public class BusinessExpenditureImportTest {

	@Inject Assertor assertor;
	@Inject ExpenditureBusiness expenditureBusiness;
	
	@Test
	void import_2022_1_1() {
		expenditureBusiness.import_("2022_1_1", "meliane");
	}
	
	@Test
	void import_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.import_(null,null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Version collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
	
	@Test
	void import_running() {
		new Thread() {
			public void run() {
				expenditureBusiness.import_("2021_1_running","christian");
			}
		}.start();
		TimeHelper.pause(1l * 1000);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.import_("2021_1_running","christian");
	    });
		assertThat(exception.getMessage()).isEqualTo("Dépense de 2021_1_running en cours d'importation");
		TimeHelper.pause(1l * 3000);
		expenditureBusiness.import_("2021_1_running","christian");
	}
	
	/**/
	
	public static class Procedures {
		
		public static String PA_IMPORTER_DEPENSE(String legislativeActVersionIdentifier,String auditWho,String auditFunctionality,String auditWhat,java.sql.Date auditWhen) {
			if("2021_1_running".equals(legislativeActVersionIdentifier))
				TimeHelper.pause(1l * 3000);
			return null;
		}
	}
}