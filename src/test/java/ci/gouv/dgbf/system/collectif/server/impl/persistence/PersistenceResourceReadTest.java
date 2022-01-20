package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Resource;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceActivity;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourcePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.Resource.Read.class)
public class PersistenceResourceReadTest {

	@Inject Assertor assertor;
	@Inject SectionPersistence sectionPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ResourcePersistence resourcePersistence;
	@Inject ExpenditureNaturePersistence expenditureNaturePersistence;
	@Inject LegislativeActPersistence actPersistence;
	@Inject LegislativeActVersionPersistence actVersionPersistence;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	@Inject ResourceActivityPersistence resourceActivityPersistence;
	@Inject EconomicNaturePersistence economicNaturePersistence;
	
	@Test
	void readResourceActivityMany() {
		Collection<ResourceActivity> resourceActivities = resourceActivityPersistence.readMany(null, null, null);
		assertThat(resourceActivities).hasSize(3);
	}
	
	@Test
	void readResourceMany() {
		Collection<Resource> resources = resourcePersistence.readMany(null, null, null);
		assertThat(resources).hasSize(4);
	}
	
	@Test
	void readResourceOne() {
		Resource resource = resourcePersistence.readOne("1");
		assertThat(resource).isNotNull();
		assertThat(resource.getRevenue()).isNull();
	}
	
	@Test
	void readResourceOne_revenue() {
		Resource resource = resourcePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(resourcePersistence.getQueryIdentifierReadDynamicOne())).addFilterField("identifier", "1")
				.addProjectionsFromStrings(ResourceImpl.FIELD_REVENUE));
		assertThat(resource).isNotNull();
		assertThat(resource.getRevenue()).isNotNull();
	}
	
	@Test
	void readResourceOne_asStrings() {
		ResourceImpl resource = (ResourceImpl) resourcePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(resourcePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ResourceImpl.class))
				.addFilterField("identifier", "1").addProjectionsFromStrings(ResourceImpl.FIELDS_STRINGS)
				);
		assertThat(resource).isNotNull();
		assertThat(resource.getRevenue()).isNull();
		assertThat(resource.getActAsString()).isEqualTo("LFR1");
	}
	
	@Test
	void readResourceOne_amounts_initial_actual_movement_adjustment_actual_plus_adjustment() {
		ResourceImpl resource = (ResourceImpl) resourcePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(resourcePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ResourceImpl.class))
				.addFilterField("identifier", "1").addProjectionsFromStrings(ResourceImpl.FIELDS_AMOUNTS_INITIAL_ACTUAL_MOVEMENT_ADJUSTMENT_ACTUAL_PLUS_ADJUSTMENT)
				);
		assertThat(resource).isNotNull();
		assertThat(resource.getRevenue()).isNotNull();
		assertThat(resource.getRevenue().getInitial()).isEqualTo(1l);
		assertThat(resource.getRevenue().getActual()).isEqualTo(2l);
		assertThat(resource.getActAsString()).isNull();
	}
	
	@Test
	void countResource() {
		assertThat(resourcePersistence.count()).isEqualTo(4l);
	}
	
	@Test
	void readResourceAsStrings() {
		Collection<Object[]> objects = new ResourceImplAsStringsReader()
				.readByIdentifiers(List.of("1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[0]).isEqualTo("1");
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo("LFR1");
	}
	
	@Test
	void readRevenueAdjustment() {
		Collection<Object[]> objects = new ResourceImplEntryAuthorizationAdjustmentReader().readByIdentifiers(List.of("1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[0]).isEqualTo("1");
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo(3l);
	}
}