package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.Expenditure.Amount.class)
public class PersistenceExpenditureAmountTest {

	@Inject Assertor assertor;
	@Inject SectionPersistence sectionPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ExpenditureNaturePersistence expenditureNaturePersistence;
	@Inject LegislativeActPersistence actPersistence;
	@Inject LegislativeActVersionPersistence actVersionPersistence;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	@Inject ActivityPersistence activityPersistence;
	@Inject EconomicNaturePersistence economicNaturePersistence;
	@Inject UserTransaction userTransaction;
	
	@Test
	void readExpenditureOne_entryAuthorization() {
		Expenditure expenditure = expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne())).addFilterField("identifier", "1")
				.addProjectionsFromStrings(ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION));
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
	}
	
	@Test
	void readExpenditureOne_amounts_1() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ExpenditureImpl.class))
				.addFilterField("identifier", "1").addProjectionsFromStrings(ExpenditureImpl.FIELDS_AMOUNTS)
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
		assertThat(expenditure.getActAsString()).isNull();
	}
	
	@Test
	void readExpenditureOne_amounts_3() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ExpenditureImpl.class))
				.addFilterField("identifier", "3").addProjectionsFromStrings(ExpenditureImpl.FIELDS_AMOUNTS)
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
	
	@Test
	void readExpenditureOne_movementIncluded_1() {
		assertor.assertExpenditureMovementIncluded("1", 0l, 0l);
	}
	
	@Test
	void readExpenditureOne_movementIncluded_2() {
		assertor.assertExpenditureMovementIncluded("2", 0l, 0l);
	}
	
	@Test
	void readExpenditureOne_movementIncluded_3() {
		assertor.assertExpenditureMovementIncluded("3", -25l, -25l);
	}
	
	@Test
	void readEntryAuthorizationAdjustment() {
		Collection<Object[]> objects = new ExpenditureImplEntryAuthorizationAdjustmentReader().readByIdentifiers(List.of("1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[0]).isEqualTo("1");
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo(0l);
	}
	
	@Test
	void readEntryAuthorizationAdjustmentAvailable() {
		Collection<Object[]> objects = new ExpenditureImplEntryAuthorizationAdjustmentAvailableReader().readByIdentifiers(List.of("1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[0]).isEqualTo("1");
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo(0l);
	}
}