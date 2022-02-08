package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.Expenditure.Read.class)
public class PersistenceExpenditureReadTest {

	@Inject Assertor assertor;
	@Inject SectionPersistence sectionPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ExpenditureNaturePersistence expenditureNaturePersistence;
	@Inject LegislativeActPersistence actPersistence;
	@Inject LegislativeActVersionPersistence actVersionPersistence;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	@Inject ActivityPersistence activityPersistence;
	@Inject EconomicNaturePersistence economicNaturePersistence;
	@Inject UserTransaction userTransaction;
	
	@Test
	void read_2021_1_1_ADJUSTMENTS_NOT_EQUAL_ZERO() {
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_1",Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO,Boolean.TRUE));
		assertThat(expenditures).isNotNull();
		assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_1_1_2","2022_1_1_4","2022_1_1_5");
	}
	
	@Test
	void read_2021_1_1_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO() {
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_1",Parameters.INCLUDED_MOVEMENT_NOT_EQUAL_ZERO,Boolean.TRUE));
		assertThat(expenditures).isNotNull();
		assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_1_1_6");
	}
	
	@Test
	void read_2021_1_1_ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO() {
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_1",Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO,Boolean.TRUE));
		assertThat(expenditures).isNotNull();
		assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_1_1_2","2022_1_1_4","2022_1_1_5","2022_1_1_6");
	}
}