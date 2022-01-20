package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.RegulatoryAct.Read.class)
public class PersistenceRegulatoryActReadTest {

	@Inject Assertor assertor;
	@Inject RegulatoryActPersistence regulatoryActPersistence;
	@Inject UserTransaction userTransaction;
	
	@Test
	void readRegulatoryActMany() {
		Collection<RegulatoryAct> regulatoryActs = regulatoryActPersistence.readMany(null, null, null);
		assertThat(regulatoryActs).hasSize(4);
	}
	
	@Test
	void readRegulatoryActOne() {
		RegulatoryAct regulatoryAct = regulatoryActPersistence.readOne("1");
		assertThat(regulatoryAct).isNotNull();
	}
	
	@Test
	void readRegulatoryActOne_1_not_in_version_1_projections() {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(regulatoryActPersistence.getQueryIdentifierReadDynamicOne())).addFilterFieldsValues(regulatoryActPersistence.getParameterNameIdentifier(), "1",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "1");
		arguments.addProjectionsFromStrings(RegulatoryActImpl.FIELD_IDENTIFIER);
		arguments.addProcessableTransientFieldsNames(RegulatoryActImpl.FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING);
		RegulatoryActImpl regulatoryAct = (RegulatoryActImpl) regulatoryActPersistence.readOne(arguments);
		assertThat(regulatoryAct).isNotNull();
		assertThat(regulatoryAct.getYear()).isEqualTo(Short.parseShort("2021"));
		assertThat(regulatoryAct.getName()).isEqualTo("1");
		assertThat(regulatoryAct.getEntryAuthorizationAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getPaymentCreditAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getIncluded()).isEqualTo(null);
		assertThat(regulatoryAct.getIncludedAsString()).isEqualTo("Non");
	}
	
	@Test
	void readRegulatoryActOne_1_not_in_version_2_projections() {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(regulatoryActPersistence.getQueryIdentifierReadDynamicOne())).addFilterFieldsValues(regulatoryActPersistence.getParameterNameIdentifier(), "1",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2");
		arguments.addProjectionsFromStrings(RegulatoryActImpl.FIELD_IDENTIFIER);
		arguments.addProcessableTransientFieldsNames(RegulatoryActImpl.FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING);
		RegulatoryActImpl regulatoryAct = (RegulatoryActImpl) regulatoryActPersistence.readOne(arguments);
		assertThat(regulatoryAct).isNotNull();
		assertThat(regulatoryAct.getYear()).isEqualTo(Short.parseShort("2021"));
		assertThat(regulatoryAct.getName()).isEqualTo("1");
		assertThat(regulatoryAct.getEntryAuthorizationAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getPaymentCreditAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getIncluded()).isEqualTo(null);
		assertThat(regulatoryAct.getIncludedAsString()).isEqualTo("Non");
	}
	
	@Test
	void readRegulatoryActOne_2_in_version_1_projections() {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(regulatoryActPersistence.getQueryIdentifierReadDynamicOne())).addFilterFieldsValues(regulatoryActPersistence.getParameterNameIdentifier(), "2",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "1");
		arguments.addProjectionsFromStrings(RegulatoryActImpl.FIELD_IDENTIFIER);
		arguments.addProcessableTransientFieldsNames(RegulatoryActImpl.FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING);
		RegulatoryActImpl regulatoryAct = (RegulatoryActImpl) regulatoryActPersistence.readOne(arguments);
		assertThat(regulatoryAct).isNotNull();
		assertThat(regulatoryAct.getYear()).isEqualTo(Short.parseShort("2021"));
		assertThat(regulatoryAct.getName()).isEqualTo("1");
		assertThat(regulatoryAct.getEntryAuthorizationAmount()).isEqualTo(1);
		assertThat(regulatoryAct.getPaymentCreditAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getIncluded()).isEqualTo(Boolean.TRUE);
		assertThat(regulatoryAct.getIncludedAsString()).isEqualTo("Oui");
	}
	
	@Test
	void readRegulatoryActOne_2_not_in_version_2_projections() {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(regulatoryActPersistence.getQueryIdentifierReadDynamicOne())).addFilterFieldsValues(regulatoryActPersistence.getParameterNameIdentifier(), "2",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2");
		arguments.addProjectionsFromStrings(RegulatoryActImpl.FIELD_IDENTIFIER);
		arguments.addProcessableTransientFieldsNames(RegulatoryActImpl.FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING);
		RegulatoryActImpl regulatoryAct = (RegulatoryActImpl) regulatoryActPersistence.readOne(arguments);
		assertThat(regulatoryAct).isNotNull();
		assertThat(regulatoryAct.getYear()).isEqualTo(Short.parseShort("2021"));
		assertThat(regulatoryAct.getName()).isEqualTo("1");
		assertThat(regulatoryAct.getEntryAuthorizationAmount()).isEqualTo(1);
		assertThat(regulatoryAct.getPaymentCreditAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getIncluded()).isEqualTo(null);
		assertThat(regulatoryAct.getIncludedAsString()).isEqualTo("Non");
	}
}