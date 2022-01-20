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

import ci.gouv.dgbf.system.collectif.server.api.persistence.Activity;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNature;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNature;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Section;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.Expenditure.Default.class)
public class PersistenceExpenditureDefaultTest {

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
	void readExpenditureNatureMany() {
		Collection<ExpenditureNature> expenditureNatures = expenditureNaturePersistence.readMany(null, null, null);
		assertThat(expenditureNatures).hasSize(4);
	}
	
	@Test
	void readExpenditureNatureOne() {
		ExpenditureNature expenditureNature = expenditureNaturePersistence.readOne("1");
		assertThat(expenditureNature).isNotNull();
	}
	
	@Test
	void readExpenditureMany() {
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(null, null, null);
		assertThat(expenditures).hasSize(4);
	}
	
	@Test
	void readExpenditureOne() {
		Expenditure expenditure = expenditurePersistence.readOne("1");
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getEntryAuthorization()).isNull();
	}
	
	@Test
	void readExpenditureOne_entryAuthorization() {
		Expenditure expenditure = expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne())).addFilterField("identifier", "1")
				.addProjectionsFromStrings(ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION));
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
	}
	
	@Test
	void readExpenditureOne_asStrings() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ExpenditureImpl.class))
				.addFilterField("identifier", "1").addProjectionsFromStrings(ExpenditureImpl.FIELDS_STRINGS)
				);
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getEntryAuthorization()).isNull();
		assertThat(expenditure.getPaymentCredit()).isNull();
		assertThat(expenditure.getActAsString()).isEqualTo("AB01");
	}
	
	@Test
	void readExpenditureOne_amounts() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ExpenditureImpl.class))
				.addFilterField("identifier", "1").addProjectionsFromStrings(ExpenditureImpl.FIELDS_AMOUNTS_INITIAL_ACTUAL_MOVEMENT_ADJUSTMENT_ACTUAL_PLUS_ADJUSTMENT)
				);
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
		assertThat(expenditure.getEntryAuthorization().getInitial()).isEqualTo(1l);
		assertThat(expenditure.getEntryAuthorization().getActual()).isEqualTo(2l);
		assertThat(expenditure.getPaymentCredit()).isNotNull();
		assertThat(expenditure.getActAsString()).isNull();
	}
	
	@Test
	void countExpenditure() {
		assertThat(expenditurePersistence.count()).isEqualTo(4l);
	}
	
	@Test
	void readExpenditureAsStrings() {
		Collection<Object[]> objects = new ExpenditureImplAsStringsReader()
				.readByIdentifiers(List.of("1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[0]).isEqualTo("1");
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo("AB01");
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
	
	@Test
	void readLegislativeActMany() {
		Collection<LegislativeAct> acts = actPersistence.readMany(null, null, null);
		assertThat(acts).hasSize(1);
	}
	
	@Test
	void readLegislativeActVersionMany() {
		Collection<LegislativeActVersion> actVersions = actVersionPersistence.readMany(null, null, null);
		assertThat(actVersions).hasSize(1);
	}
	
	@Test
	void readSectionMany() {
		Collection<Section> sections = sectionPersistence.readMany(null, null, null);
		assertThat(sections).hasSize(1);
	}
	
	@Test
	void readBudgetSpecializationUnitMany() {
		Collection<BudgetSpecializationUnit> budgetSpecializationUnits = budgetSpecializationUnitPersistence.readMany(null, null, null);
		assertThat(budgetSpecializationUnits).hasSize(1);
	}
	
	@Test
	void readBudgetSpecializationUnitManyBySection_XXX_isNull() {
		Collection<BudgetSpecializationUnit> budgetSpecializationUnits = budgetSpecializationUnitPersistence.readMany(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(budgetSpecializationUnitPersistence.getQueryIdentifierReadDynamic()))
				.addFilterField(Parameters.SECTION_IDENTIFIER, "XXX"));
		assertThat(budgetSpecializationUnits).isNull();
	}
	
	@Test
	void readBudgetSpecializationUnitManyBySection_S01_isNotEmpty() {
		Collection<BudgetSpecializationUnit> budgetSpecializationUnits = budgetSpecializationUnitPersistence.readMany(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(budgetSpecializationUnitPersistence.getQueryIdentifierReadDynamic()))
				.addFilterField(Parameters.SECTION_IDENTIFIER, "S01"));
		assertThat(budgetSpecializationUnits).hasSize(1);
	}
	
	@Test
	void readActivityOne() {
		Activity activity = activityPersistence.readOne("activity01");
		assertThat(activity).isNotNull();
		assertThat(activity.getExpenditureNature()).isNull();
	}
	
	@Test
	void readActivityOne_projections() {
		ActivityImpl activity = (ActivityImpl) activityPersistence.readOne("activity01",List.of(ActivityImpl.FIELDS_SECTION_ADMINISTRATIVE_UNIT_EXPENDITURE_NATURE_BUDGET_SPECIALIZATION_UNIT_ACTION
				,ActivityImpl.FIELD_ECONOMIC_NATURES));
		assertThat(activity).isNotNull();
		assertThat(activity.getExpenditureNature()).isNotNull();
		assertThat(activity.getSection()).isNotNull();
		assertThat(activity.getAdministrativeUnit()).isNotNull();
		assertThat(activity.getBudgetSpecializationUnit()).isNotNull();
		assertThat(activity.getAction()).isNotNull();
		assertThat(activity.getEconomicNatures()).hasSize(1);
	}
	
	@Test
	void readEconomicNatureMany_byActivity() {
		Collection<EconomicNature> economicNatures = economicNaturePersistence.readMany(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(economicNaturePersistence.getQueryIdentifierReadDynamic()).setTupleClass(EconomicNatureImpl.class))
				.addFilterField(Parameters.ACTIVITY_IDENTIFIER, "activity01"));
		assertThat(economicNatures).hasSize(1);
		
	}
}