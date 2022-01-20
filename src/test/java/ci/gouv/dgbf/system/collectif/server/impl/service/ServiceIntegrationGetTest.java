package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.service.ActivityDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ActivityService;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetSpecializationUnitService;
import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActService;
import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActVersionService;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureService;
import ci.gouv.dgbf.system.collectif.server.api.service.SectionService;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Service.Integration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceIntegrationGetTest {

	@Inject Assertor assertor;
	
	@Test
    public void legislativeAct_get_one_projections() {
		assertor.assertServiceGetOne(LegislativeActService.PATH, "1", Map.of("identifiant","1","code","1","libelle","1"));
    }
	
	@Test
    public void legislativeActVersion_get_one_projections() {
		assertor.assertServiceGetOne(LegislativeActVersionService.PATH, "1",List.of("identifiant","code","libelle","acte_budgetaire")
				, Map.of("identifiant","1","code","1","libelle","1","acte_budgetaire.identifiant","1","acte_budgetaire.code","1","acte_budgetaire.libelle","1"));
    }
	
	@Test
    public void section_get_many() {
		assertor.assertServiceGetMany(SectionService.PATH, "3", List.of("101"));
    }
	
	@Test
    public void section_get_one() {
		assertor.assertServiceGetOne(SectionService.PATH, "323", Map.of("identifiant","323","code","323","libelle","Intérieur"));
    }
	
	@Test
    public void section_get_one_notfound() {
		assertor.assertServiceGetOneNotFound(SectionService.PATH, "0");
    }
	
	@Test
    public void section_count() {
		assertor.assertServiceGetNumber(SectionService.PATH, "3");
    }
	
	@Test
    public void expenditureNature_get_many() {
		assertor.assertServiceGetMany(ExpenditureService.PATH, "2", List.of("1"));
    }
	
	@Test
    public void expenditureNature_get_one() {
		assertor.assertServiceGetOne(ExpenditureService.PATH, "1");
    }
	
	@Test
    public void expenditureNature_get_one_notfound() {
		assertor.assertServiceGetOneNotFound(ExpenditureService.PATH, "0");
    }
	
	@Test
    public void expenditureNature_count() {
		assertor.assertServiceGetNumber(ExpenditureService.PATH, "2");
    }
	
	@Test
    public void budgetSpecializationUnit_get_many() {
		assertor.assertServiceGetMany(BudgetSpecializationUnitService.PATH, "1", List.of("22086"));
    }
	
	@Test
    public void budgetSpecializationUnit_get_one() {
		assertor.assertServiceGetOne(BudgetSpecializationUnitService.PATH, "22086");
    }
	
	@Test
    public void budgetSpecializationUnit_get_one_notfound() {
		assertor.assertServiceGetOneNotFound(BudgetSpecializationUnitService.PATH, "0");
    }
	
	@Test
    public void budgetSpecializationUnit_count() {
		assertor.assertServiceGetNumber(BudgetSpecializationUnitService.PATH, "1");
    }
	
	@Test
    public void activity_get_one_projections() {
		assertor.assertServiceGetOne(ActivityService.PATH, "activity01",List.of("identifiant","code","libelle"
				,ActivityDto.JSONS_SECTION_ADMINISTRATIVE_UNIT_EXPENDITURE_NATURE_BUDGET_SPECIALIZATION_UNIT_ACTION,ActivityDto.JSON_ECONOMIC_NATURES
				,ActivityDto.JSON_FUNDING_SOURCES,ActivityDto.JSON_LESSORS)
				, Map.of("identifiant","activity01","code","activity01","libelle","activity 01"));
    }
}