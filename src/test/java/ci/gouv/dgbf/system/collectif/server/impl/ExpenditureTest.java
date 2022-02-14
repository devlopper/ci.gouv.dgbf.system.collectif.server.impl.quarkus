package ci.gouv.dgbf.system.collectif.server.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.time.TimeHelper;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.business.ExpenditureBusinessImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationAdjustmentAvailableReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationAdjustmentReader;
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
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ExpenditureBusiness expenditureBusiness;
	
	@Test @Order(1)
	void readExpenditureView() {
		Collection<ExpenditureView> expenditures = entityManager.createQuery("SELECT t FROM ExpenditureView t",ExpenditureView.class).getResultList();
		assertThat(expenditures).isNotNull();
		assertThat(FieldHelper.readSystemIdentifiersAsStrings(expenditures)).containsExactlyInAnyOrder("2021_1_1_1","2021_1_1_2","2021_1_1_3","2021_1_1_4","2021_1_1_5"
				,"2021_1_2_1","2021_1_2_2","2021_1_2_3","2021_1_2_4","2021_1_2_5","2022_1_2_1","2022_1_2_2","2022_1_2_3","2022_1_2_4","2022_1_2_5");
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
	
	/* Import */
	
	@Test @Order(2)
	void import_2021_1_2() {
		ExpenditureBusinessImpl.IMPORT_BATCH_SIZE = 1;
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
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.import_("2021_1_running","christian");
	    });
		assertThat(exception.getMessage()).isEqualTo("Dépense de 2021_1_running en cours d'importation");
		TimeHelper.pause(1l * 2000);
		expenditureBusiness.import_("2021_1_running","christian");
	}
	
	/* Amounts */
	
	@Test @Order(3)
	void readExpenditureOne_entryAuthorization() {
		Expenditure expenditure = expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne())).addFilterField("identifier", "2022_1_2_1")
				.addProjectionsFromStrings(ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION));
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
	}
	
	@Test @Order(3)
	void readExpenditureOne_amounts_1() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ExpenditureImpl.class))
				.addFilterField("identifier", "2022_1_2_1").addProjectionsFromStrings(ExpenditureImpl.FIELDS_AMOUNTS)
				);
		assertThat(expenditure).isNotNull();
		
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
		assertThat(expenditure.getEntryAuthorization().getInitial()).isEqualTo(1l);
		assertThat(expenditure.getEntryAuthorization().getActual()).isEqualTo(2l);
		assertThat(expenditure.getEntryAuthorization().getMovement()).isEqualTo(1l);
		assertThat(expenditure.getEntryAuthorization().getAdjustment()).isEqualTo(0l);
		assertThat(expenditure.getEntryAuthorization().getActualPlusAdjustment()).isEqualTo(2l);
		assertThat(expenditure.getEntryAuthorization().getMovementIncluded()).isEqualTo(0l);
		
		assertThat(expenditure.getPaymentCredit()).isNotNull();
		assertThat(expenditure.getPaymentCredit().getInitial()).isEqualTo(3l);
		assertThat(expenditure.getPaymentCredit().getActual()).isEqualTo(5l);
		assertThat(expenditure.getPaymentCredit().getMovement()).isEqualTo(2l);
		assertThat(expenditure.getPaymentCredit().getAdjustment()).isEqualTo(7l);
		assertThat(expenditure.getPaymentCredit().getActualPlusAdjustment()).isEqualTo(12l);
		assertThat(expenditure.getPaymentCredit().getMovementIncluded()).isEqualTo(0l);
		
		assertThat(expenditure.getActAsString()).isNull();
	}
	
	@Test @Order(3)
	void readExpenditureOne_amounts_3() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ExpenditureImpl.class))
				.addFilterField("identifier", "2022_1_2_3").addProjectionsFromStrings(ExpenditureImpl.FIELDS_AMOUNTS)
				);
		assertThat(expenditure).isNotNull();
		
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
		assertThat(expenditure.getEntryAuthorization().getInitial()).isEqualTo(10l);
		assertThat(expenditure.getEntryAuthorization().getActual()).isEqualTo(17l);
		assertThat(expenditure.getEntryAuthorization().getMovement()).isEqualTo(7l);
		assertThat(expenditure.getEntryAuthorization().getAdjustment()).isEqualTo(0l);
		assertThat(expenditure.getEntryAuthorization().getActualPlusAdjustment()).isEqualTo(17l);
		assertThat(expenditure.getEntryAuthorization().getMovementIncluded()).isEqualTo(-25l);
		
		assertThat(expenditure.getPaymentCredit()).isNotNull();
		assertThat(expenditure.getActAsString()).isNull();
	}
	
	@Test @Order(3)
	void readExpenditureOne_movementIncluded_1() {
		assertor.assertExpenditureMovementIncluded("2022_1_2_1", 0l, 0l);
	}
	
	@Test @Order(3)
	void readExpenditureOne_movementIncluded_2() {
		assertor.assertExpenditureMovementIncluded("2022_1_2_2", 0l, 0l);
	}
	
	@Test @Order(3)
	void readExpenditureOne_movementIncluded_3() {
		assertor.assertExpenditureMovementIncluded("2022_1_2_3", -25l, -25l);
	}
	
	@Test @Order(3)
	void readEntryAuthorizationAdjustment() {
		Collection<Object[]> objects = new ExpenditureImplEntryAuthorizationAdjustmentReader().readByIdentifiers(List.of("2022_1_2_1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo(0l);
	}
	
	@Test @Order(3)
	void readEntryAuthorizationAdjustmentAvailable() {
		Collection<Object[]> objects = new ExpenditureImplEntryAuthorizationAdjustmentAvailableReader().readByIdentifiers(List.of("2022_1_2_1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo(0l);
	}
	
	@Test @Order(3)
	void read_2022_1_2_ADJUSTMENTS_NOT_EQUAL_ZERO() {
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2",Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO,Boolean.TRUE));
		assertThat(expenditures).isNotNull();
		assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_1_2_1");
	}
	
	@Test @Order(3)
	void read_2022_1_2_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO() {
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2",Parameters.INCLUDED_MOVEMENT_NOT_EQUAL_ZERO,Boolean.TRUE));
		assertThat(expenditures).isNotNull();
		assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_1_2_3","2022_1_2_4","2022_1_2_5");
	}
	
	@Test @Order(3)
	void read_2022_1_2_ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO() {
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2",Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO,Boolean.TRUE));
		assertThat(expenditures).isNotNull();
		assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_1_2_1","2022_1_2_3","2022_1_2_4","2022_1_2_5");
	}
	
	/**/
	
	public static String PA_ACTUALISER_VM(String tableName) {
		TimeHelper.pause(1l * 1000l);
		return null;
	}
}