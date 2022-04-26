package ci.gouv.dgbf.system.collectif.server.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.service.client.Controller;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ActionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetCategory;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetCategoryPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Section;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;
import ci.gouv.dgbf.system.collectif.server.client.rest.BudgetCategoryController;
import ci.gouv.dgbf.system.collectif.server.impl.client.ActorClient;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Actor.class)
@QuarkusTestResource(QuarkusTestResourceLifecycleManager.class)
public class ActorClientTest extends org.cyk.quarkus.extension.test.AbstractTest {
	
	@Inject @RestClient ActorClient client;
	@Inject SectionPersistence sectionPersistence;
	@Inject BudgetCategoryPersistence budgetCategoryPersistence;
	@Inject BudgetCategoryController budgetCategoryController;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	@Inject ActionPersistence actionPersistence;
	
	@BeforeEach
	void listenBefore() {
		ci.gouv.dgbf.system.collectif.server.client.rest.ApplicationLifeCycleListener.initialize();
		Controller.GetArguments.PARAMETER_VALUE_USER_NAME = "christian";
	}
	
	@Test
	void get_visibilities_sections_christian() {
		Collection<ActorClient.VisibilityDto> visibilities = client.getVisibilities(ActorClient.CODE_TYPE_DOMAINE_SECTION, "christian");
		assertThat(visibilities).isNotNull();
		assertThat(visibilities.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2");
	}
	
	@Test
	void get_sections() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			sectionPersistence.readMany(new QueryExecutorArguments());
	    });
		assertThat(exception.getMessage()).isEqualTo("Query parameter <<nom_utilisateur>> is required to get <<section.query.identifier.read.dynamic>>");
	}
	
	@Test
	void get_sections_christian() {
		Collection<Section> sections = sectionPersistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.USER_NAME, "christian"));
		assertThat(sections).isNotNull();
		assertThat(sections.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2");
	}
	
	@Test
	void get_sections_unknown() {
		Collection<Section> sections = sectionPersistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.USER_NAME, "unknown"));
		assertThat(sections).isNull();
	}
	
	@Test
	void get_budgetCategories() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			budgetCategoryPersistence.readMany(new QueryExecutorArguments());
	    });
		assertThat(exception.getMessage()).isEqualTo("Query parameter <<nom_utilisateur>> is required to get <<budgetcategory.query.identifier.read.dynamic>>");
	}
	
	@Test
	void persistence_budgetCategories_christian() {
		Collection<BudgetCategory> budgetCategories = budgetCategoryPersistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.USER_NAME, "christian"));
		assertThat(budgetCategories).isNotNull();
		assertThat(budgetCategories.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2");
	}
	
	@Test
	void persistence_budgetCategories_unknown() {
		Collection<BudgetCategory> budgetCategories = budgetCategoryPersistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.USER_NAME, "unknown"));
		assertThat(budgetCategories).isNull();
	}
	
	@Test
    void client_budgetCategories() {
		Collection<ci.gouv.dgbf.system.collectif.server.client.rest.BudgetCategory> budgetCategories = budgetCategoryController.get();
		assertThat(budgetCategories).isNotNull();
		assertThat(budgetCategories.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2");
    }
	
	@Test
	void get_budgetSpecializationUnits() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			budgetSpecializationUnitPersistence.readMany(new QueryExecutorArguments());
	    });
		assertThat(exception.getMessage()).isEqualTo("Query parameter <<nom_utilisateur>> is required to get <<budgetspecializationunit.query.identifier.read.dynamic>>");
	}
	
	@Test
	void persistence_budgetSpecializationUnits_christian() {
		Collection<BudgetSpecializationUnit> budgetSpecializationUnits = budgetSpecializationUnitPersistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.USER_NAME, "christian"));
		assertThat(budgetSpecializationUnits).isNotNull();
		assertThat(budgetSpecializationUnits.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2");
	}
	
	@Test
	void persistence_budgetSpecializationUnits_unknown() {
		Collection<BudgetSpecializationUnit> budgetSpecializationUnits = budgetSpecializationUnitPersistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.USER_NAME, "unknown"));
		assertThat(budgetSpecializationUnits).isNull();
	}
	
	/*@Test
	void get_actions() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			actionPersistence.readMany(new QueryExecutorArguments());
	    });
		assertThat(exception.getMessage()).isEqualTo("Query parameter <<nom_utilisateur>> is required to get <<action.query.identifier.read.dynamic>>");
	}
	
	@Test
	void get_actions_christian() {
		Collection<Action> actions = actionPersistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.USER_NAME, "christian"));
		assertThat(actions).isNotNull();
		assertThat(actions.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2");
	}
	
	@Test
	void get_actions_unknown() {
		Collection<Action> actions = actionPersistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.USER_NAME, "unknown"));
		assertThat(actions).isNull();
	}*/
}