package ci.gouv.dgbf.system.collectif.server.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.time.TimeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImportableView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImportedView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureView;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Expenditure.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class ExpenditureTest {

	@Inject EntityManager entityManager;
	@Inject Assertor assertor;
	@Inject ExpenditureBusiness expenditureBusiness;
	
	@Test @Order(1)
	void readExpenditureView() {
		Collection<ExpenditureView> expenditures = entityManager.createQuery("SELECT t FROM ExpenditureView t",ExpenditureView.class).getResultList();
		assertThat(expenditures).isNotNull();
		assertThat(FieldHelper.readSystemIdentifiersAsStrings(expenditures)).containsExactlyInAnyOrder("2021_1_1_1","2021_1_1_2","2021_1_1_3","2021_1_1_4","2021_1_1_5"
				,"2021_1_2_1","2021_1_2_2","2021_1_2_3","2021_1_2_4","2021_1_2_5");
	}
	
	@Test @Order(1)
	void readExpenditureImportedView() {
		Collection<ExpenditureImportedView> expenditures = entityManager.createQuery("SELECT t FROM ExpenditureImportedView t",ExpenditureImportedView.class).getResultList();
		assertThat(expenditures).isNotNull();
		assertThat(FieldHelper.readSystemIdentifiersAsStrings(expenditures)).containsExactlyInAnyOrder("2021_1_1_1","2021_1_1_2","2021_1_1_3","2021_1_1_4","2021_1_1_5");
	}
	
	@Test @Order(1)
	void readExpenditureImportableView() {
		Collection<ExpenditureImportableView> expenditures = entityManager.createQuery("SELECT t FROM ExpenditureImportableView t",ExpenditureImportableView.class).getResultList();
		assertThat(expenditures).isNotNull();
		assertThat(FieldHelper.readSystemIdentifiersAsStrings(expenditures)).containsExactlyInAnyOrder("2021_1_2_1","2021_1_2_2","2021_1_2_3","2021_1_2_4","2021_1_2_5");
	}
	
	@Test @Order(2)
	void import_2021_1_2() {
		assertor.assertExpenditureByLegislativeActVersion("2021_1_2", null);
		expenditureBusiness.import_("2021_1_2", "meliane");
		assertor.assertExpenditureByLegislativeActVersion("2021_1_2", List.of("2021_1_2_1","2021_1_2_2","2021_1_2_3","2021_1_2_4","2021_1_2_5"));
	}
	
	@Test
	void import_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.import_(null,null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Version collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
	
	@Test @Order(2)
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
	
	public static String PA_ACTUALISER_VM(String tableName) {
		TimeHelper.pause(3l * 1000l);
		return null;
	}
}