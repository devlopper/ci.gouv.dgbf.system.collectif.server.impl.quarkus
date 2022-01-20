package ci.gouv.dgbf.system.collectif.server.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.cyk.utility.service.client.Controller;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.service.ActivityDto;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetSpecializationUnitDto;
import ci.gouv.dgbf.system.collectif.server.client.rest.Activity;
import ci.gouv.dgbf.system.collectif.server.client.rest.ActivityController;
import ci.gouv.dgbf.system.collectif.server.client.rest.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.client.rest.BudgetSpecializationUnitController;
import ci.gouv.dgbf.system.collectif.server.client.rest.ExpenditureNature;
import ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.client.rest.LegislativeActVersionController;
import ci.gouv.dgbf.system.collectif.server.client.rest.RegulatoryAct;
import ci.gouv.dgbf.system.collectif.server.client.rest.RegulatoryActController;
import ci.gouv.dgbf.system.collectif.server.client.rest.Section;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Client.Default.class)
public class ClientGetTest {

	@Inject Assertor assertor;
	@Inject LegislativeActVersionController actVersionController;
	@Inject BudgetSpecializationUnitController budgetSpecializationUnitController;
	@Inject ActivityController activityController;
	@Inject RegulatoryActController regulatoryActController;
	
	@Test
    public void legislativeAct() {
		assertor.assertClientNamable(LegislativeAct.class, List.of("A001"), 1l);
    }
	
	@Test
    public void legislativeActVersion() {
		assertor.assertClientNamable(LegislativeActVersion.class, List.of("V001"), 1l);
    }
	
	@Test
    public void legislativeActVersion_projections() {
		LegislativeActVersion actVersion = actVersionController.getByIdentifier("V001", new Controller.GetArguments()
				.projections("identifiant"));
		assertThat(actVersion.getIdentifier()).isEqualTo("V001");
		assertThat(actVersion.getCode()).isNull();
		assertThat(actVersion.getName()).isNull();
		assertThat(actVersion.getActIdentifier()).isNull();
		
		actVersion = actVersionController.getByIdentifier("V001", new Controller.GetArguments()
				.projections("identifiant","code"));
		assertThat(actVersion.getIdentifier()).isEqualTo("V001");
		assertThat(actVersion.getCode()).isEqualTo("V001");
		assertThat(actVersion.getName()).isNull();
		assertThat(actVersion.getActIdentifier()).isNull();
		
		actVersion = actVersionController.getByIdentifier("V001", new Controller.GetArguments()
				.projections("identifiant","code","libelle"));
		assertThat(actVersion.getIdentifier()).isEqualTo("V001");
		assertThat(actVersion.getCode()).isEqualTo("V001");
		assertThat(actVersion.getName()).isEqualTo("Version 001");
		assertThat(actVersion.getActIdentifier()).isNull();
		
		actVersion = actVersionController.getByIdentifier("V001", new Controller.GetArguments()
				.projections("identifiant","code","libelle","identifiant_acte_budgetaire","acte_budgetaire"));
		assertThat(actVersion.getIdentifier()).isEqualTo("V001");
		assertThat(actVersion.getCode()).isEqualTo("V001");
		assertThat(actVersion.getName()).isEqualTo("Version 001");
		assertThat(actVersion.getActIdentifier()).isNotNull();
		assertThat(actVersion.getAct()).isNotNull();
    }
	
	@Test
    public void legislativeActVersion_byActIdentifier_A001() {
		assertor.assertClientLegislativeActVersionsIdentifiersByLegislativeActIdentifier("A001", List.of("V001"));
    }
	
	@Test
    public void legislativeActVersion_byActIdentifier_A002() {
		assertor.assertClientLegislativeActVersionsIdentifiersByLegislativeActIdentifier("A002", null);
    }
	
	@Test
    public void section() {
		assertor.assertClientNamable(Section.class, List.of("101","323","327"), 3l);
    }
	
	@Test
    public void expenditureNature() {
		assertor.assertClientNamable(ExpenditureNature.class, List.of("1","2","3"), 4l);
    }
	
	@Test
    public void budgetSpecializationUnit() {
		assertor.assertClientNamable(BudgetSpecializationUnit.class, List.of("22086"), 1l);
		BudgetSpecializationUnit budgetSpecializationUnit = budgetSpecializationUnitController.getByIdentifier("22086", new Controller.GetArguments()
				.projections(BudgetSpecializationUnitDto.JSON_IDENTIFIER,BudgetSpecializationUnitDto.JSON_CODE,BudgetSpecializationUnitDto.JSON_NAME));
		assertThat(budgetSpecializationUnit.getIdentifier()).isEqualTo("22086");
		assertThat(budgetSpecializationUnit.getCode()).isEqualTo("22086");
		assertThat(budgetSpecializationUnit.getName()).isEqualTo("Budget");
    }
	
	@Test
    public void activity_byAction() {
		Collection<Activity> activities = activityController.getByParentIdentifier(Parameters.ACTION_IDENTIFIER, "action01");
		assertThat(activities).hasSize(1);
		Activity activity = activities.iterator().next();
		assertThat(activity.getIdentifier()).isEqualTo("activite01");
    }
	
	@Test
    public void activity_byIdentifier_projections() {
		Activity activity = activityController.getByIdentifier("activite02",new Controller.GetArguments().projections(ActivityDto.JSONS_SECTION_ADMINISTRATIVE_UNIT_EXPENDITURE_NATURE_BUDGET_SPECIALIZATION_UNIT_ACTION
				,ActivityDto.JSON_ECONOMIC_NATURES,ActivityDto.JSON_FUNDING_SOURCES,ActivityDto.JSON_LESSORS));
		assertThat(activity).isNotNull();
		assertThat(activity.getIdentifier()).isEqualTo("activite02");
		assertThat(activity.getExpenditureNature()).isNotNull();
		assertThat(activity.getSection()).isNotNull();
		assertThat(activity.getAdministrativeUnit()).isNotNull();
		assertThat(activity.getBudgetSpecializationUnit()).isNotNull();
		assertThat(activity.getAction()).isNotNull();
		assertThat(activity.getEconomicNatures()).hasSize(1);
    }
	
	@Test
    public void regulatoryAct() {
		assertor.assertClientNamable(RegulatoryAct.class, List.of("1","2","3"), 4l);
    }
}