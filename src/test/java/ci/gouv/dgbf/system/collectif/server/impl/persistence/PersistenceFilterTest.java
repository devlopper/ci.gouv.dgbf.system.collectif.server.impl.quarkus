package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.Filter.class)
public class PersistenceFilterTest {

	@Inject Assertor assertor;
	@Inject SectionPersistence sectionPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ExpenditureNaturePersistence expenditureNaturePersistence;
	@Inject BudgetaryActPersistence budgetaryActPersistence;
	@Inject BudgetaryActVersionPersistence budgetaryActVersionPersistence;
	
	@Test
	void budgetaryAct_byBudgetaryActVersionIdentifier_VAB01_01_is_AB01() {
		assertor.assertBudgetaryActByBudgetaryActVersion("VAB01_01", "AB01");
	}
	
	@Test
	void expenditure_byBudgetaryAct_0_isNull() {
		assertor.assertExpenditureByBudgetaryAct("0", null);
	}
	
	@Test
	void expenditure_byBudgetaryAct_AB01() {
		BudgetaryActVersion budgetaryActVersion = budgetaryActVersionPersistence.readOne(new QueryExecutorArguments().setQuery(new Query()
				.setIdentifier(budgetaryActVersionPersistence.getQueryIdentifierReadDynamicOne()))
				.addProjectionsFromStrings("identifier","code","name","budgetaryAct")
				.addFilterField("identifier", "VAB01_01"));
		assertThat(budgetaryActVersion).isNotNull();
		assertThat(budgetaryActVersion.getBudgetaryAct()).isNotNull();
		
		assertor.assertExpenditureByBudgetaryAct("AB01", List.of("1","10","11","12","13","14","15","16","17","18","19","2","20","21","22","23","24","25","26","27","28","29"
				,"3","30","31","32","4","5","6","7","8","9"));
	}
	
	@Test
	void expenditure_byBudgetaryAct_AB01_isNotEmpty() {
		assertor.assertExpenditureByBudgetaryAct("AB01", List.of("1","10","11","12","13","14","15","16","17","18","19","2","20","21","22","23","24","25","26","27","28","29"
				,"3","30","31","32","4","5","6","7","8","9"));
	}
	
	@Test
	void expenditure_byBudgetaryAct_AB02_isNotEmpty() {
		assertor.assertExpenditureByBudgetaryAct("AB02", List.of("33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48"));
	}
	
	@Test
	void budgetaryActVersion_byBudgetaryAct_AB01_isNotEmpty() {
		assertor.assertBudgetaryActVersionByBudgetaryAct("AB01", List.of("VAB01_01","VAB01_02"));
	}
	
	@Test
	void budgetaryActVersion_byBudgetaryAct_AB02_isNotEmpty() {
		assertor.assertBudgetaryActVersionByBudgetaryAct("AB02", List.of("VAB02_01"));
	}
	
	/*@Test
	void budgetSpecializationUnit_bySection_AB02_isNotEmpty() {
		assertor.assertBudgetSpecializationUnitBySection("AB02", List.of("VAB02_01"));
	}*/
	
	/**/
	
	@Test
	void expenditure_byBudgetaryAct_VAB01_01_isNotEmpty() {
		assertor.assertExpenditureByBudgetaryActVersion("VAB01_01", List.of("1","10","11","12","13","14","15","16","2","3","4","5","6","7","8","9"));
	}
	
	@Test
	void expenditure_byBudgetaryAct_VAB01_02_isNotEmpty() {
		assertor.assertExpenditureByBudgetaryActVersion("VAB01_02", List.of("17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32"));
	}
	
	@Test
	void expenditure_byBudgetaryAct_VAB02_01_isNotEmpty() {
		assertor.assertExpenditureByBudgetaryActVersion("VAB02_01", List.of("33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48"));
	}
	
	@Test
	void expenditure_byNature_P_isNotEmpty() {
		assertor.assertExpenditureByNature("P", List.of("1"));
	}
}