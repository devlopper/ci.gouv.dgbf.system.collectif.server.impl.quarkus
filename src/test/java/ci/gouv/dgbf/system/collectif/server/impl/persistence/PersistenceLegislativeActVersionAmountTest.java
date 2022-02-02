package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.LegislativeActVersion.Amount.class)
public class PersistenceLegislativeActVersionAmountTest {

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
	void readExpenditureOne_amounts_1() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) actVersionPersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(actVersionPersistence.getQueryIdentifierReadDynamicOne()))
				.addFilterField("identifier", "2021_1_1").addProjectionsFromStrings(LegislativeActVersionImpl.FIELDS_AMOUNTS,LegislativeActVersionImpl.FIELD___AUDIT__)
				);
		assertThat(legislativeActVersion).isNotNull();
		
		assertThat(legislativeActVersion.getEntryAuthorization()).isNotNull();
		assertThat(legislativeActVersion.getEntryAuthorization().getInitial()).isEqualTo(11l);
		assertThat(legislativeActVersion.getEntryAuthorization().getActual()).isEqualTo(19l);
		assertThat(legislativeActVersion.getEntryAuthorization().getMovement()).isEqualTo(8l);
		assertThat(legislativeActVersion.getEntryAuthorization().getAdjustment()).isEqualTo(0l);
		assertThat(legislativeActVersion.getEntryAuthorization().getActualPlusAdjustment()).isEqualTo(19l);
		assertThat(legislativeActVersion.getEntryAuthorization().getMovementIncluded()).isEqualTo(0l);
		assertThat(legislativeActVersion.getEntryAuthorization().getExpectedAdjustment()).isEqualTo(10l);
		assertThat(legislativeActVersion.getEntryAuthorization().getExpectedAdjustmentMinusAdjustment()).isEqualTo(10l);
		
		assertThat(legislativeActVersion.getPaymentCredit()).isNotNull();
		assertThat(legislativeActVersion.getPaymentCredit().getInitial()).isEqualTo(3l);
		assertThat(legislativeActVersion.getPaymentCredit().getActual()).isEqualTo(5l);
		assertThat(legislativeActVersion.getPaymentCredit().getMovement()).isEqualTo(2l);
		assertThat(legislativeActVersion.getPaymentCredit().getAdjustment()).isEqualTo(6l);
		assertThat(legislativeActVersion.getPaymentCredit().getActualPlusAdjustment()).isEqualTo(11l);
		assertThat(legislativeActVersion.getPaymentCredit().getMovementIncluded()).isEqualTo(0l);
		assertThat(legislativeActVersion.getPaymentCredit().getExpectedAdjustment()).isEqualTo(20l);
		assertThat(legislativeActVersion.getPaymentCredit().getExpectedAdjustmentMinusAdjustment()).isEqualTo(14l);
	}
}