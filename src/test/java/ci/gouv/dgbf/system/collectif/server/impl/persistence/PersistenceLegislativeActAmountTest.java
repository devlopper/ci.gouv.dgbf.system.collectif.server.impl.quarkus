package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNaturePersistence;
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
@TestProfile(Profiles.Persistence.LegislativeAct.Amount.class)
public class PersistenceLegislativeActAmountTest {

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
	/*
	@Test
	void readExpenditureOne_entryAuthorization() {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) actPersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(actPersistence.getQueryIdentifierReadDynamicOne())).addFilterField("identifier", "2021_1")
				.addProjectionsFromStrings(LegislativeActImpl.FIELD_ENTRY_AUTHORIZATION));
		assertThat(legislativeAct).isNotNull();
		assertThat(legislativeAct.getEntryAuthorization()).isNotNull();
	}
	*/
	@Test
	void readExpenditureOne_amounts_1() {
		LegislativeActImpl legislativeAct = (LegislativeActImpl) actPersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(actPersistence.getQueryIdentifierReadDynamicOne()))
				.addFilterField("identifier", "2021_1").addProjectionsFromStrings(LegislativeActImpl.FIELDS_AMOUNTS,LegislativeActImpl.FIELD___AUDIT__)
				);
		assertThat(legislativeAct).isNotNull();
		
		assertThat(legislativeAct.getEntryAuthorization()).isNotNull();
		assertThat(legislativeAct.getEntryAuthorization().getInitial()).isEqualTo(11l);
		assertThat(legislativeAct.getEntryAuthorization().getActual()).isEqualTo(19l);
		assertThat(legislativeAct.getEntryAuthorization().getMovement()).isEqualTo(8l);
		assertThat(legislativeAct.getEntryAuthorization().getAdjustment()).isEqualTo(0l);
		assertThat(legislativeAct.getEntryAuthorization().getActualPlusAdjustment()).isEqualTo(19l);
		assertThat(legislativeAct.getEntryAuthorization().getMovementIncluded()).isEqualTo(0l);
		assertThat(legislativeAct.getEntryAuthorization().getExpectedAdjustment()).isEqualTo(10l);
		assertThat(legislativeAct.getEntryAuthorization().getExpectedAdjustmentMinusAdjustment()).isEqualTo(10l);
		
		assertThat(legislativeAct.getPaymentCredit()).isNotNull();
		assertThat(legislativeAct.getPaymentCredit().getInitial()).isEqualTo(3l);
		assertThat(legislativeAct.getPaymentCredit().getActual()).isEqualTo(5l);
		assertThat(legislativeAct.getPaymentCredit().getMovement()).isEqualTo(2l);
		assertThat(legislativeAct.getPaymentCredit().getAdjustment()).isEqualTo(7l);
		assertThat(legislativeAct.getPaymentCredit().getActualPlusAdjustment()).isEqualTo(12l);
		assertThat(legislativeAct.getPaymentCredit().getMovementIncluded()).isEqualTo(0l);
		assertThat(legislativeAct.getPaymentCredit().getExpectedAdjustment()).isEqualTo(20l);
		assertThat(legislativeAct.getPaymentCredit().getExpectedAdjustmentMinusAdjustment()).isEqualTo(13l);
	}
	/*
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
	*/
	@Test
	void readOne_movementIncluded_2020() {
		assertor.assertLegislativeActMovementIncluded("2020_1", 0l, 0l);
	}
	
	@Test
	void readOne_movementIncluded_2021() {
		assertor.assertLegislativeActMovementIncluded("2021_1", 0l, 0l);
	}
	
	@Test
	void readOne_movementIncluded_2022() {
		assertor.assertLegislativeActMovementIncluded("2022_1", 0l, 0l);
	}
	
	/*@Test
	void readEntryAuthorizationAdjustment() {
		Collection<Object[]> objects = new LegislativeActImplEntryAuthorizationAdjustmentReader().readByIdentifiers(List.of("1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[0]).isEqualTo("1");
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo(0l);
	}
	
	@Test
	void readEntryAuthorizationAdjustmentAvailable() {
		Collection<Object[]> objects = new LegislativeActImplEntryAuthorizationAdjustmentAvailableReader().readByIdentifiers(List.of("1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[0]).isEqualTo("1");
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo(0l);
	}*/
}