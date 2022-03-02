package ci.gouv.dgbf.system.collectif.server.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.business.RegulatoryActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActLegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImpl;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.RegulatoryAct.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class RegulatoryActTest {
	
	@Inject Assertor assertor;
	@Inject RegulatoryActPersistence persistence;
	@Inject RegulatoryActLegislativeActVersionPersistence regulatoryActLegislativeActVersionPersistence;
	@Inject RegulatoryActBusiness business;
	
	@Test @Order(1)
	void persistence_readRegulatoryActMany_REGULATORY_ACT_DATE_GREATER_THAN_OR_EQUAL() {
		Collection<RegulatoryAct> regulatoryActs = persistence.readMany(new QueryExecutorArguments().addFilterFieldsValues(Parameters.REGULATORY_ACT_DATE_GREATER_THAN_OR_EQUAL,LocalDate.of(2019, 1, 1)));
		assertThat(regulatoryActs).hasSize(8);
	}
	
	@Test @Order(1)
	void persistence_readRegulatoryActMany() {
		Collection<RegulatoryAct> regulatoryActs = persistence.readMany(null, null, null);
		assertThat(regulatoryActs).hasSize(19);
	}
	
	@Test @Order(1)
	void persistence_readRegulatoryActOne() {
		RegulatoryAct regulatoryAct = persistence.readOne("1");
		assertThat(regulatoryAct).isNotNull();
	}
	
	@Test @Order(1)
	void persistence_readRegulatoryActOne_1_not_in_version_1_projections() {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne())).addFilterFieldsValues(persistence.getParameterNameIdentifier(), "1",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2021_1_1");
		arguments.addProjectionsFromStrings(RegulatoryActImpl.FIELD_IDENTIFIER);
		arguments.addProcessableTransientFieldsNames(RegulatoryActImpl.FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING);
		RegulatoryActImpl regulatoryAct = (RegulatoryActImpl) persistence.readOne(arguments);
		assertThat(regulatoryAct).isNotNull();
		assertThat(regulatoryAct.getYear()).isEqualTo(Short.parseShort("2021"));
		assertThat(regulatoryAct.getName()).isEqualTo("1");
		assertThat(regulatoryAct.getEntryAuthorizationAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getPaymentCreditAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getIncluded()).isEqualTo(Boolean.FALSE);
		assertThat(regulatoryAct.getIncludedAsString()).isEqualTo("Non");
	}
	
	@Test @Order(1)
	void persistence_readRegulatoryActOne_1_not_in_version_2_projections() {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne())).addFilterFieldsValues(persistence.getParameterNameIdentifier(), "1",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2021_1_2");
		arguments.addProjectionsFromStrings(RegulatoryActImpl.FIELD_IDENTIFIER);
		arguments.addProcessableTransientFieldsNames(RegulatoryActImpl.FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING);
		RegulatoryActImpl regulatoryAct = (RegulatoryActImpl) persistence.readOne(arguments);
		assertThat(regulatoryAct).isNotNull();
		assertThat(regulatoryAct.getYear()).isEqualTo(Short.parseShort("2021"));
		assertThat(regulatoryAct.getName()).isEqualTo("1");
		assertThat(regulatoryAct.getEntryAuthorizationAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getPaymentCreditAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getIncluded()).isEqualTo(null);
		assertThat(regulatoryAct.getIncludedAsString()).isEqualTo("Non");
	}
	
	@Test @Order(1)
	void persistence_readRegulatoryActOne_2_in_version_1_projections() {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne())).addFilterFieldsValues(persistence.getParameterNameIdentifier(), "2",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2021_1_1");
		arguments.addProjectionsFromStrings(RegulatoryActImpl.FIELD_IDENTIFIER);
		arguments.addProcessableTransientFieldsNames(RegulatoryActImpl.FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING);
		RegulatoryActImpl regulatoryAct = (RegulatoryActImpl) persistence.readOne(arguments);
		assertThat(regulatoryAct).isNotNull();
		assertThat(regulatoryAct.getYear()).isEqualTo(Short.parseShort("2021"));
		assertThat(regulatoryAct.getName()).isEqualTo("1");
		assertThat(regulatoryAct.getEntryAuthorizationAmount()).isEqualTo(1);
		assertThat(regulatoryAct.getPaymentCreditAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getIncluded()).isEqualTo(Boolean.TRUE);
		assertThat(regulatoryAct.getIncludedAsString()).isEqualTo("Oui");
	}
	
	@Test @Order(1)
	void persistence_readRegulatoryActOne_2_not_in_version_2_projections() {
		QueryExecutorArguments arguments = new QueryExecutorArguments();
		arguments.setQuery(new Query().setIdentifier(persistence.getQueryIdentifierReadDynamicOne())).addFilterFieldsValues(persistence.getParameterNameIdentifier(), "2",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2021_1_2");
		arguments.addProjectionsFromStrings(RegulatoryActImpl.FIELD_IDENTIFIER);
		arguments.addProcessableTransientFieldsNames(RegulatoryActImpl.FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING);
		RegulatoryActImpl regulatoryAct = (RegulatoryActImpl) persistence.readOne(arguments);
		assertThat(regulatoryAct).isNotNull();
		assertThat(regulatoryAct.getYear()).isEqualTo(Short.parseShort("2021"));
		assertThat(regulatoryAct.getName()).isEqualTo("1");
		assertThat(regulatoryAct.getEntryAuthorizationAmount()).isEqualTo(1);
		assertThat(regulatoryAct.getPaymentCreditAmount()).isEqualTo(0);
		assertThat(regulatoryAct.getIncluded()).isEqualTo(null);
		assertThat(regulatoryAct.getIncludedAsString()).isEqualTo("Non");
	}
	
	/* Include */
	
	@Test @Order(2)
	void business_include_true() {
		assertor.assertRegulatoryAct("include_included_true","2021_1_1",Boolean.TRUE);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.include("2021_1_1",null,"meliane", "include_included_true");
	    });
		assertThat(exception.getMessage()).isEqualTo("Les Actes de gestion suivant sont déja inclus : include_included_true include_included_true");
	}
	
	@Test @Order(2)
	void business_include_true_existingIgnorableIsTrue() {
		assertor.assertRegulatoryAct("include_included_true","2021_1_1",Boolean.TRUE);
		business.include("2021_1_1",Boolean.TRUE,"meliane", "include_included_true");
		assertor.assertRegulatoryAct("include_included_true","2021_1_1",Boolean.TRUE);
	}
	
	@Test @Order(2)
	void business_include_false() {
		assertor.assertRegulatoryAct("include_included_false","2021_1_1",Boolean.FALSE);
		business.include("2021_1_1",Boolean.TRUE,"meliane", "include_included_false");
		assertor.assertRegulatoryAct("include_included_false","2021_1_1",Boolean.TRUE);
		assertor.assertRegulatoryActAudit("include_included_false","2021_1_1", "meliane", "INCLUSION", "MODIFICATION");
	}
	
	@Test @Order(2)
	void business_include_null() {
		assertor.assertRegulatoryAct("include_included_null","2021_1_1",Boolean.FALSE);
		business.include("2021_1_1",Boolean.TRUE,"meliane", "include_included_null");
		assertor.assertRegulatoryAct("include_included_null","2021_1_1",Boolean.TRUE);
	}
	
	@Test @Order(2)
	void business_include_not_marked0() {
		assertor.assertRegulatoryAct("not_marked0","2021_1_1",null);
		business.include("2021_1_1",Boolean.TRUE,"meliane", "not_marked0");
		assertor.assertRegulatoryAct("not_marked0","2021_1_1",Boolean.TRUE);
	}
	
	/* Exclude */
	
	@Test @Order(2)
	void business_exclude_false() {
		assertor.assertRegulatoryAct("exclude_included_false","2021_1_1",Boolean.FALSE);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.exclude("2021_1_1",null,"meliane", "exclude_included_false");
	    });
		assertThat(exception.getMessage()).isEqualTo("Les Actes de gestion suivant sont déja exclus : exclude_included_false exclude_included_false");
	}
	
	@Test @Order(2)
	void business_exclude_null() {
		assertor.assertRegulatoryAct("exclude_included_null","2021_1_1",Boolean.FALSE);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.exclude("2021_1_1",null,"meliane", "exclude_included_null");
	    });
		assertThat(exception.getMessage()).isEqualTo("Les Actes de gestion suivant sont déja exclus : exclude_included_null exclude_included_null");
	}
	
	@Test @Order(2)
	void business_exclude_true() {
		assertor.assertRegulatoryAct("exclude_included_true","2021_1_1",Boolean.TRUE);
		business.exclude("2021_1_1",null,"meliane", "exclude_included_true");
		assertor.assertRegulatoryAct("exclude_included_true","2021_1_1",Boolean.FALSE);
	}
	
	/* Include by legislative act version identifier */
	
	@Test @Order(2)
	void business_includeByLegislativeActVersionIdentifier_2021_1_2() {
		Long count = regulatoryActLegislativeActVersionPersistence.count();
		assertThat(persistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2021_1_2",Parameters.REGULATORY_ACT_INCLUDED,Boolean.TRUE))).isEqualTo(0l);
		assertor.assertRegulatoryAct("include_by_lav_2021_1_2_01","2021_1_2",null);
		assertor.assertRegulatoryAct("include_by_lav_2021_1_2_02","2021_1_2",null);
		assertor.assertRegulatoryAct("include_by_lav_2021_1_2_03","2021_1_2",null);
		
		business.includeByLegislativeActVersionIdentifier("2021_1_2", "meliane");
		
		assertThat(regulatoryActLegislativeActVersionPersistence.count()).isEqualTo(count + 3);
		assertThat(persistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2021_1_2",Parameters.REGULATORY_ACT_INCLUDED,Boolean.TRUE))).isEqualTo(3l);
		assertor.assertRegulatoryAct("include_by_lav_2021_1_2_01","2021_1_2",Boolean.TRUE);
		assertor.assertRegulatoryAct("include_by_lav_2021_1_2_02","2021_1_2",Boolean.TRUE);
		assertor.assertRegulatoryAct("include_by_lav_2021_1_2_03","2021_1_2",Boolean.TRUE);	
	}
	
	@Test @Order(2)
	void business_includeByLegislativeActVersionIdentifier_2021_2_1() {
		Long count = regulatoryActLegislativeActVersionPersistence.count();
		assertThat(persistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2021_2_1",Parameters.REGULATORY_ACT_INCLUDED,Boolean.TRUE))).isEqualTo(0l);
		assertor.assertRegulatoryAct("include_by_lav_2021_2_1_01","2021_2_1",null);
		assertor.assertRegulatoryAct("include_by_lav_2021_2_1_02","2021_2_1",null);
		assertor.assertRegulatoryAct("include_by_lav_2021_2_1_03","2021_2_1",null);
		assertor.assertRegulatoryAct("include_by_lav_2021_2_1_04","2021_2_1",null);
		
		business.includeByLegislativeActVersionIdentifier("2021_2_1", "meliane");
		
		assertThat(regulatoryActLegislativeActVersionPersistence.count()).isEqualTo(count + 4);
		assertThat(persistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2021_2_1",Parameters.REGULATORY_ACT_INCLUDED,Boolean.TRUE))).isEqualTo(4l);
		assertor.assertRegulatoryAct("include_by_lav_2021_2_1_01","2021_2_1",Boolean.TRUE);
		assertor.assertRegulatoryAct("include_by_lav_2021_2_1_02","2021_2_1",Boolean.TRUE);
		assertor.assertRegulatoryAct("include_by_lav_2021_2_1_03","2021_2_1",Boolean.TRUE);
		assertor.assertRegulatoryAct("include_by_lav_2021_2_1_04","2021_2_1",Boolean.TRUE);
	}
}