package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNature;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Section;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.Default.class)
public class ExpenditurePersistenceTest {

	@Inject Assertor assertor;
	@Inject SectionPersistence sectionPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ExpenditureNaturePersistence expenditureNaturePersistence;
	@Inject BudgetaryActPersistence budgetaryActPersistence;
	@Inject BudgetaryActVersionPersistence budgetaryActVersionPersistence;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
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
				.addFilterField("identifier", "1").addProjectionsFromStrings(Expenditure.PROPERTY_AS_STRINGS)
				);
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getEntryAuthorization()).isNull();
		assertThat(expenditure.getPaymentCredit()).isNull();
		assertThat(expenditure.getBudgetaryActAsString()).isEqualTo("AB01");
	}
	
	@Test
	void readExpenditureOne_amounts() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ExpenditureImpl.class))
				.addFilterField("identifier", "1").addProjectionsFromStrings(Expenditure.PROPERTY_AMOUNTS)
				);
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
		assertThat(expenditure.getEntryAuthorization().getInitial()).isEqualTo(1l);
		assertThat(expenditure.getEntryAuthorization().getActual()).isEqualTo(2l);
		assertThat(expenditure.getPaymentCredit()).isNotNull();
		assertThat(expenditure.getBudgetaryActAsString()).isNull();
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
	void buildUpdateEntryAuthoriationsAdjustmentsQuery_one() {
		assertThat(ExpenditurePersistenceImpl.buildUpdateEntryAuthoriationsAdjustmentsQuery(Map.of("1",2l)))
		.isEqualTo("UPDATE ExpenditureImpl SET entryAuthorization.adjustment = CASE WHEN identifier='1' THEN 2 END WHERE identifier IN :identifiers");
	}
	
	@Test
	void buildUpdateEntryAuthoriationsAdjustmentsQuery_many() {
		Map<String,Long> map = new LinkedHashMap<>();
		map.put("1",2l);
		map.put("2",-3l);
		assertThat(ExpenditurePersistenceImpl.buildUpdateEntryAuthoriationsAdjustmentsQuery(map))
		.isEqualTo("UPDATE ExpenditureImpl SET entryAuthorization.adjustment = CASE WHEN identifier='1' THEN 2 WHEN identifier='2' THEN -3 END WHERE identifier IN :identifiers");
	}
	
	@Test
	void updateEntryAuthoriations_one() {
		assertor.assertEntryAuthorization("uea_one_01", 0l);
		try {
			userTransaction.begin();
			expenditurePersistence.updateEntryAuthoriations(Map.of("uea_one_01",3l));
			userTransaction.commit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}		
		assertor.assertEntryAuthorization("uea_one_01", 3l);
	}
	
	@Test
	void updateEntryAuthoriations_many() {
		assertor.assertEntryAuthorization("uea_many_01", 0l);
		assertor.assertEntryAuthorization("uea_many_02", -2l);
		try {
			userTransaction.begin();
			expenditurePersistence.updateEntryAuthoriations(Map.of("uea_many_01",3l,"uea_many_02",5l));
			userTransaction.commit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}		
		assertor.assertEntryAuthorization("uea_many_01", 3l);
		assertor.assertEntryAuthorization("uea_many_02", 5l);
	}
	
	@Test
	void readBudgetaryActMany() {
		Collection<BudgetaryAct> budgetaryActs = budgetaryActPersistence.readMany(null, null, null);
		assertThat(budgetaryActs).hasSize(1);
	}
	
	@Test
	void readBudgetaryActVersionMany() {
		Collection<BudgetaryActVersion> budgetaryActVersions = budgetaryActVersionPersistence.readMany(null, null, null);
		assertThat(budgetaryActVersions).hasSize(1);
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
}