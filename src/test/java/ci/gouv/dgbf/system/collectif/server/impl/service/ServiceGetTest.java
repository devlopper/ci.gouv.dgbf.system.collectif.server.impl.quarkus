package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.service.BudgetaryActService;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetaryActVersionService;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureService;
import ci.gouv.dgbf.system.collectif.server.api.service.SectionService;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Service.Integration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceGetTest {

	@Inject Assertor assertor;
	
	@Test
    public void budgetaryAct_get_one_projections() {
		assertor.assertServiceGetOne(BudgetaryActService.PATH, "1", Map.of("identifiant","1","code","1","libelle","1"));
    }
	
	@Test
    public void budgetaryActVersion_get_one_projections() {
		assertor.assertServiceGetOne(BudgetaryActVersionService.PATH, "1",List.of("identifiant","code","libelle","acte_budgetaire")
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
}