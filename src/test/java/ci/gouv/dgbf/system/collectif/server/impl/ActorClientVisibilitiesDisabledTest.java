package ci.gouv.dgbf.system.collectif.server.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.client.rest.BudgetCategoryController;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.ActorWithVisibilitiesDisabled.class)
@QuarkusTestResource(QuarkusTestResourceLifecycleManager.class)
public class ActorClientVisibilitiesDisabledTest extends org.cyk.quarkus.extension.test.AbstractTest {
	
	@Inject BudgetCategoryController budgetCategoryController;
	
	@BeforeEach
	void listenBefore() {
		ci.gouv.dgbf.system.collectif.server.client.rest.ApplicationLifeCycleListener.initialize();
	}
	
	@Test
    void client_budgetCategory_get() {
		Collection<ci.gouv.dgbf.system.collectif.server.client.rest.BudgetCategory> budgetCategories = budgetCategoryController.get();
		assertThat(budgetCategories).isNotNull();
		assertThat(budgetCategories.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2","3","4","5","6","7","8","9");
    }
	
	@Test
    void client_budgetCategory_getByIdentifier() {
		ci.gouv.dgbf.system.collectif.server.client.rest.BudgetCategory budgetCategory = budgetCategoryController.getByIdentifier("3");
		assertThat(budgetCategory).isNotNull();
		assertThat(budgetCategory.getIdentifier()).isEqualTo("3");
    }
}