package ci.gouv.dgbf.system.collectif.server.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.cyk.utility.service.client.Controller;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.client.rest.BudgetaryAct;
import ci.gouv.dgbf.system.collectif.server.client.rest.BudgetaryActVersion;
import ci.gouv.dgbf.system.collectif.server.client.rest.BudgetaryActVersionController;
import ci.gouv.dgbf.system.collectif.server.client.rest.ExpenditureNature;
import ci.gouv.dgbf.system.collectif.server.client.rest.Section;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Client.class)
public class ClientGetTest {

	@Inject Assertor assertor;
	@Inject BudgetaryActVersionController budgetaryActVersionController;
	
	@Test
    public void budgetaryAct() {
		assertor.assertClientNamable(BudgetaryAct.class, List.of("A001"), 1l);
    }
	
	@Test
    public void budgetaryActVersion() {
		assertor.assertClientNamable(BudgetaryActVersion.class, List.of("V001"), 1l);
    }
	
	@Test
    public void budgetaryActVersion_projections() {
		BudgetaryActVersion budgetaryActVersion = budgetaryActVersionController.getByIdentifier("V001", new Controller.GetArguments()
				.projections("identifiant"));
		assertThat(budgetaryActVersion.getIdentifier()).isEqualTo("V001");
		assertThat(budgetaryActVersion.getCode()).isNull();
		assertThat(budgetaryActVersion.getName()).isNull();
		assertThat(budgetaryActVersion.getBudgetaryActIdentifier()).isNull();
		
		budgetaryActVersion = budgetaryActVersionController.getByIdentifier("V001", new Controller.GetArguments()
				.projections("identifiant","code"));
		assertThat(budgetaryActVersion.getIdentifier()).isEqualTo("V001");
		assertThat(budgetaryActVersion.getCode()).isEqualTo("V001");
		assertThat(budgetaryActVersion.getName()).isNull();
		assertThat(budgetaryActVersion.getBudgetaryActIdentifier()).isNull();
		
		budgetaryActVersion = budgetaryActVersionController.getByIdentifier("V001", new Controller.GetArguments()
				.projections("identifiant","code","libelle"));
		assertThat(budgetaryActVersion.getIdentifier()).isEqualTo("V001");
		assertThat(budgetaryActVersion.getCode()).isEqualTo("V001");
		assertThat(budgetaryActVersion.getName()).isEqualTo("Version 001");
		assertThat(budgetaryActVersion.getBudgetaryActIdentifier()).isNull();
		
		budgetaryActVersion = budgetaryActVersionController.getByIdentifier("V001", new Controller.GetArguments()
				.projections("identifiant","code","libelle","identifiant_acte_budgetaire","acte_budgetaire"));
		assertThat(budgetaryActVersion.getIdentifier()).isEqualTo("V001");
		assertThat(budgetaryActVersion.getCode()).isEqualTo("V001");
		assertThat(budgetaryActVersion.getName()).isEqualTo("Version 001");
		assertThat(budgetaryActVersion.getBudgetaryActIdentifier()).isNotNull();
		assertThat(budgetaryActVersion.getBudgetaryAct()).isNotNull();
    }
	
	@Test
    public void budgetaryActVersion_byBudgetaryActIdentifier_A001() {
		assertor.assertClientBudgetaryActVersionsIdentifiersByBudgetaryActIdentifier("A001", List.of("V001"));
    }
	
	@Test
    public void budgetaryActVersion_byBudgetaryActIdentifier_A002() {
		assertor.assertClientBudgetaryActVersionsIdentifiersByBudgetaryActIdentifier("A002", null);
    }
	
	@Test
    public void section() {
		assertor.assertClientNamable(Section.class, List.of("101","323","327"), 3l);
    }
	
	@Test
    public void expenditureNature() {
		assertor.assertClientNamable(ExpenditureNature.class, List.of("1","2","3"), 4l);
    }
}