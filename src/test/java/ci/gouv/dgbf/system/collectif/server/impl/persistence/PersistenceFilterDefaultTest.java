package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.Filter.Default.class)
public class PersistenceFilterDefaultTest {

	@Inject Assertor assertor;
	@Inject SectionPersistence sectionPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ExpenditureNaturePersistence expenditureNaturePersistence;
	@Inject LegislativeActPersistence actPersistence;
	@Inject LegislativeActVersionPersistence actVersionPersistence;
	
	@Test
	void legislativeAct_byLegislativeActVersionIdentifier_VAB01_01_is_AB01() {
		assertor.assertLegislativeActByLegislativeActVersion("VAB01_01", "AB01");
	}
	
	@Test
	void expenditure_byLegislativeAct_0_isNull() {
		assertor.assertExpenditureByLegislativeAct("0", null);
	}
	
	@Test
	void expenditure_byLegislativeAct_AB01() {
		LegislativeActVersion actVersion = actVersionPersistence.readOne(new QueryExecutorArguments().setQuery(new Query()
				.setIdentifier(actVersionPersistence.getQueryIdentifierReadDynamicOne()))
				.addProjectionsFromStrings("identifier","code","name","act")
				.addFilterField("identifier", "VAB01_01"));
		assertThat(actVersion).isNotNull();
		assertThat(actVersion.getAct()).isNotNull();
		
		assertor.assertExpenditureByLegislativeAct("AB01", List.of("1","10","11","12","13","14","15","16","17","18","19","2","20","21","22","23","24","25","26","27","28","29"
				,"3","30","31","32","4","5","6","7","8","9"));
	}
	
	@Test
	void expenditure_byLegislativeAct_AB01_isNotEmpty() {
		assertor.assertExpenditureByLegislativeAct("AB01", List.of("1","10","11","12","13","14","15","16","17","18","19","2","20","21","22","23","24","25","26","27","28","29"
				,"3","30","31","32","4","5","6","7","8","9"));
	}
	
	@Test
	void expenditure_byLegislativeAct_AB02_isNotEmpty() {
		assertor.assertExpenditureByLegislativeAct("AB02", List.of("33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48"));
	}
	
	@Test
	void legislativeActVersion_byLegislativeAct_AB01_isNotEmpty() {
		assertor.assertLegislativeActVersionByLegislativeAct("AB01", List.of("VAB01_02","VAB01_01"));
	}
	
	@Test
	void legislativeActVersion_byLegislativeAct_AB02_isNotEmpty() {
		assertor.assertLegislativeActVersionByLegislativeAct("AB02", List.of("VAB02_01"));
	}
	
	@Test
	void legislativeActVersion_latest() {
		LegislativeActVersion actVersion = actVersionPersistence.readOne(new QueryExecutorArguments().setQuery(new Query()
				.setIdentifier(actVersionPersistence.getQueryIdentifierReadDynamicOne()))
				.addProjectionsFromStrings("identifier","code","name","act")
				.addFilterField(Parameters.LATEST_LEGISLATIVE_ACT_VERSION, Boolean.TRUE));
		assertThat(actVersion).isNotNull();
		assertThat(actVersion.getAct()).isNotNull();
	}
	
	/*@Test
	void budgetSpecializationUnit_bySection_AB02_isNotEmpty() {
		assertor.assertBudgetSpecializationUnitBySection("AB02", List.of("VAB02_01"));
	}*/
	
	/**/
	
	@Test
	void expenditure_byLegislativeAct_VAB01_01_isNotEmpty() {
		assertor.assertExpenditureByLegislativeActVersion("VAB01_01", List.of("1","10","11","12","13","14","15","16","2","3","4","5","6","7","8","9"));
	}
	
	@Test
	void expenditure_byLegislativeAct_VAB01_02_isNotEmpty() {
		assertor.assertExpenditureByLegislativeActVersion("VAB01_02", List.of("17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32"));
	}
	
	@Test
	void expenditure_byLegislativeAct_VAB02_01_isNotEmpty() {
		assertor.assertExpenditureByLegislativeActVersion("VAB02_01", List.of("33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48"));
	}
	
	@Test
	void expenditure_byNature_P_isNotEmpty() {
		assertor.assertExpenditureByNature("P", List.of("1"));
	}
}