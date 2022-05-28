package ci.gouv.dgbf.system.collectif.server.impl;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.time.TimeHelper;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.rest.ResponseHelper;
import org.cyk.utility.service.EntityReader;
import org.cyk.utility.service.client.SpecificServiceGetter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness.LoadableVerificationResult;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.service.EntryAuthorizationDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureService;
import ci.gouv.dgbf.system.collectif.server.impl.business.ExpenditureBusinessImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EntryAuthorizationImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplAmountsReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationAdjustmentAvailableReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationAdjustmentReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImportableView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImportedView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureQueryStringBuilder;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.PaymentCreditImpl;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Expenditure.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class ExpenditureTest {

	@Inject EntityManager entityManager;
	@Inject Assertor assertor;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ExpenditureBusiness expenditureBusiness;
	
	Integer expectedCount = 17;
	
	@Test @Order(1)
	void verifyLoadable(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setActivityCode("1").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).isNull();
		assertThat(result.getMap()).isNull();
	}
	
	@Test @Order(1)
	void verifyLoadable_undefined_activity_code(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setIdentifier("id1").setActivityCode(null).setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessageActivitiesCodesUndefined(List.of("id1")));
		assertThat(result.getUndefined().getActivities()).containsExactly("id1");
	}
	
	@Test @Order(1)
	void verifyLoadable_undefined_economic_nature_code(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setIdentifier("id1").setActivityCode("1").setEconomicNatureCode("").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessageEconomicsNaturesCodesUndefined(List.of("id1")));
		assertThat(result.getUndefined().getEconomicsNatures()).containsExactly("id1");
	}
	
	@Test @Order(1)
	void verifyLoadable_undefined_funding_source_code(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setIdentifier("id1").setActivityCode("1").setEconomicNatureCode("1").setFundingSourceCode(" ").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getUndefined().getFundingsSources()).containsExactly("id1");
	}
	
	@Test @Order(1)
	void verifyLoadable_undefined_lessor_code(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setIdentifier("id1").setActivityCode("1").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("     ").setEntryAuthorizationAdjustment(0l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessageLessorsCodesUndefined(List.of("id1")));
		assertThat(result.getUndefined().getLessors()).containsExactly("id1");
	}
	
	@Test @Order(1)
	void verifyLoadable_undefined_entry_authorization_adjustment(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setIdentifier("id1").setActivityCode("1").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(null));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessageEntryAuthorizationAdjustmentUndefined(List.of("id1")));
		assertThat(result.getUndefinedEntriesAuthorizationsAdjustments()).containsExactly("id1");
	}
	
	@Test @Order(1)
	void verifyLoadable_unknown_activity_code(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setActivityCode("unknown_activity_code").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessageActivitiesCodesDoNotExist(List.of("unknown_activity_code")));
		assertThat(result.getUnknown().getActivities()).containsExactly("unknown_activity_code");
	}
	
	@Test @Order(1)
	void verifyLoadable_unknown_economicNature_code(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setActivityCode("1").setEconomicNatureCode("unknown_economicNature_code").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessageEconomicsNaturesCodesDoNotExist(List.of("unknown_economicNature_code")));
	}
	
	@Test @Order(1)
	void verifyLoadable_unknown_fundingsSources_code(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setActivityCode("1").setEconomicNatureCode("1").setFundingSourceCode("unknown_fundingSource_code").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessageFundingsSourcesCodesDoNotExist(List.of("unknown_fundingSource_code")));
	}
	
	@Test @Order(1)
	void verifyLoadable_unknown_lessor_code(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setActivityCode("1").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("unknown_lessor_code").setEntryAuthorizationAdjustment(0l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessageLessorsCodesDoNotExist(List.of("unknown_lessor_code")));
	}
	
	@Test @Order(1)
	void verifyLoadable_unknown_all_code(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setActivityCode("uac").setEconomicNatureCode("uenc").setFundingSourceCode("ufsc").setLessorCode("ulc").setEntryAuthorizationAdjustment(0l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessageActivitiesCodesDoNotExist(List.of("uac")),ExpenditureBusinessImpl.formatMessageEconomicsNaturesCodesDoNotExist(List.of("uenc"))
				,ExpenditureBusinessImpl.formatMessageFundingsSourcesCodesDoNotExist(List.of("ufsc")),ExpenditureBusinessImpl.formatMessageLessorsCodesDoNotExist(List.of("ulc")));
	}
	
	@Test @Order(1)
	void verifyLoadable_duplicates(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setIdentifier("1").setActivityCode("1").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		expenditures.add(new ExpenditureImpl().setIdentifier("2").setActivityCode("2").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		expenditures.add(new ExpenditureImpl().setIdentifier("3").setActivityCode("1").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_1",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessageDuplicates(List.of("3")));
		assertThat(result.getDuplicates()).containsExactly("3");
	}
	
	@Test @Order(1)
	void verifyLoadable_entry_authorization_available_not_enough(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setIdentifier("1").setActivityCode("1").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		expenditures.add(new ExpenditureImpl().setIdentifier("2").setActivityCode("a05").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(-10000000l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_3",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessageEntryAuthorizationAvailableIsNotEnough(List.of("2")));
		assertThat(result.getEntryAuthorizationAvailableIsNotEnough()).containsExactly("2");
	}
	
	@Test @Order(1)
	void verifyLoadable_credit_payment_available_not_enough(){
		Collection<Expenditure> expenditures = new ArrayList<>();
		expenditures.add(new ExpenditureImpl().setIdentifier("1").setActivityCode("1").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l));
		expenditures.add(new ExpenditureImpl().setIdentifier("2").setActivityCode("a05").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l).setPaymentCreditAdjustment(-10000000l));
		LoadableVerificationResult result = expenditureBusiness.verifyLoadable("2022_1_3",expenditures);
		assertThat(result).isNotNull();
		assertThat(result.getMessages()).containsExactly(ExpenditureBusinessImpl.formatMessagePaymentCreditAvailableIsNotEnough(List.of("2")));
		assertThat(result.getPaymentCreditAvailableIsNotEnough()).containsExactly("2");
	}
	
	@Test @Order(1)
	void queryStringBuilder_projections_amount() {
		assertThat(ExpenditureQueryStringBuilder.Projection.Amounts.get("t", "entryAuthorization.initial",null))
			.isEqualTo("COALESCE(t.entryAuthorization.initial,0l)");
	}
	
	@Test @Order(1)
	void queryStringBuilder_projections_amount_lowerThanZero() {
		assertThat(ExpenditureQueryStringBuilder.Projection.Amounts.get("t", "entryAuthorization.initial",null,Boolean.TRUE))
			.isEqualTo("CASE WHEN COALESCE(t.entryAuthorization.initial,0l) < 0l THEN COALESCE(t.entryAuthorization.initial,0l) ELSE 0l END");
	}
	
	@Test @Order(1)
	void queryStringBuilder_projections_amountSum() {
		assertThat(ExpenditureQueryStringBuilder.Projection.Amounts.get("t", "entryAuthorization.initial",Boolean.TRUE))
			.isEqualTo("SUM(COALESCE(t.entryAuthorization.initial,0l))");
	}
	
	@Test @Order(1)
	void queryStringBuilder_predicate_getMovementIncludedEqualZero() {
		assertThat(ExpenditureQueryStringBuilder.Predicate.getMovementIncludedEqualZero(Boolean.TRUE))
			.isEqualTo("((im.entryAuthorization IS NULL OR im.entryAuthorization = 0l) OR (im.paymentCredit IS NULL OR im.paymentCredit = 0l))");
	}
	
	@Test @Order(1)
	void getJoinRegulatoryActExpenditure() {
		assertThat(ExpenditureImpl.getJoinRegulatoryActExpenditure()).isEqualTo("JOIN RegulatoryActExpenditureImpl rae ON rae.year = exercise.year AND rae.activityIdentifier = t.activityIdentifier AND "
				+ "rae.economicNatureIdentifier = t.economicNatureIdentifier AND rae.fundingSourceIdentifier = t.fundingSourceIdentifier AND rae.lessorIdentifier = t.lessorIdentifier");
	}
	
	@Test @Order(1)
	void persistence_sumsAmounts() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments().addProjectionsFromStrings().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2",Parameters.AMOUNT_SUMABLE,Boolean.TRUE));
		assertThat(expenditure).isNotNull();
		assertor.assertExpenditureAmounts(expenditure.getEntryAuthorization(),new EntryAuthorizationImpl().setInitial(11l).setMovement(8l).setActual(19l).setAdjustment(33l).setAvailable(-91l).setMovementIncluded(-23l)
				.setActualMinusMovementIncludedPlusAdjustment(75l).setAvailableMinusMovementIncludedPlusAdjustment(-35l));
		assertor.assertExpenditureAmounts(expenditure.getPaymentCredit(),new PaymentCreditImpl().setInitial(5l).setMovement(12l).setActual(17l).setAdjustment(7l).setAvailable(-95l).setMovementIncluded(-23l)
				.setActualMinusMovementIncludedPlusAdjustment(47l).setAvailableMinusMovementIncludedPlusAdjustment(-65l));
	}
	
	@Test @Order(1)
	void persistence_sumsAmounts_withoutIncludedMovmentAndAvailable() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments().addProjectionsFromStrings().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2"
				,Parameters.AMOUNT_SUMABLE,Boolean.TRUE,Parameters.AMOUNT_SUMABLE_WITHOUT_INCLUDED_MOVEMENT_AND_AVAILABLE,Boolean.TRUE));
		assertThat(expenditure).isNotNull();
		assertor.assertExpenditureAmounts(expenditure.getEntryAuthorization(),new EntryAuthorizationImpl().setInitial(11l).setMovement(8l).setActual(19l).setAdjustment(33l).setAvailable(null).setMovementIncluded(null)
				.setActualMinusMovementIncludedPlusAdjustment(null).setAvailableMinusMovementIncludedPlusAdjustment(null));
		assertor.assertExpenditureAmounts(expenditure.getPaymentCredit(),new PaymentCreditImpl().setInitial(5l).setMovement(12l).setActual(17l).setAdjustment(7l).setAvailable(null).setMovementIncluded(null)
				.setActualMinusMovementIncludedPlusAdjustment(null).setAvailableMinusMovementIncludedPlusAdjustment(null));
		
		assertThat(expenditure.getEntryAuthorization().getActualAtLegislativeActDate()).isEqualTo(123l);
		assertThat(expenditure.getEntryAuthorization().getAdjustmentLessThanZero()).isEqualTo(0l);
		assertThat(expenditure.getEntryAuthorization().getAdjustmentGreaterThanZero()).isEqualTo(33l);
		
		assertThat(expenditure.getPaymentCredit().getActualAtLegislativeActDate()).isEqualTo(456l);
		assertThat(expenditure.getPaymentCredit().getAdjustmentLessThanZero()).isEqualTo(0l);
		assertThat(expenditure.getPaymentCredit().getAdjustmentGreaterThanZero()).isEqualTo(7l);
	}
	
	@Test @Order(1)
	void persistence_sumsAmounts_includedMovementOnly() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments().addProjectionsFromStrings().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2"
				,Parameters.AMOUNT_SUMABLE,Boolean.TRUE,Parameters.AMOUNT_SUMABLE_WITH_INCLUDED_MOVEMENT_ONLY,Boolean.TRUE));
		assertThat(expenditure).isNotNull();
		assertor.assertExpenditureAmounts(expenditure.getEntryAuthorization(),new EntryAuthorizationImpl().setInitial(null).setMovement(null).setActual(null).setAdjustment(null).setAvailable(null).setMovementIncluded(-23l)
				.setActualMinusMovementIncludedPlusAdjustment(null).setAvailableMinusMovementIncludedPlusAdjustment(null));
		assertor.assertExpenditureAmounts(expenditure.getPaymentCredit(),new PaymentCreditImpl().setInitial(null).setMovement(null).setActual(null).setAdjustment(null).setAvailable(null).setMovementIncluded(-23l)
				.setActualMinusMovementIncludedPlusAdjustment(null).setAvailableMinusMovementIncludedPlusAdjustment(null));
	}
	
	@Test @Order(1)
	void persistence_sumsAmounts_availableOnly() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments().addProjectionsFromStrings().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2"
				,Parameters.AMOUNT_SUMABLE,Boolean.TRUE,Parameters.AMOUNT_SUMABLE_WITH_AVAILABLE_ONLY,Boolean.TRUE));
		assertThat(expenditure).isNotNull();
		assertor.assertExpenditureAmounts(expenditure.getEntryAuthorization(),new EntryAuthorizationImpl().setInitial(null).setMovement(null).setActual(null).setAdjustment(null).setAvailable(-91l).setMovementIncluded(null)
				.setActualMinusMovementIncludedPlusAdjustment(null).setAvailableMinusMovementIncludedPlusAdjustment(null));
		assertor.assertExpenditureAmounts(expenditure.getPaymentCredit(),new PaymentCreditImpl().setInitial(null).setMovement(null).setActual(null).setAdjustment(null).setAvailable(-95l).setMovementIncluded(null)
				.setActualMinusMovementIncludedPlusAdjustment(null).setAvailableMinusMovementIncludedPlusAdjustment(null));
	}
	
	@Test @Order(1)
	void persistence_sumsAmounts_includedMovementOnlyAndAvailable() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments().addProjectionsFromStrings().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2"
				,Parameters.AMOUNT_SUMABLE,Boolean.TRUE,Parameters.AMOUNT_SUMABLE_WITH_INCLUDED_MOVEMENT_AND_AVAILABLE_ONLY,Boolean.TRUE));
		assertThat(expenditure).isNotNull();
		assertor.assertExpenditureAmounts(expenditure.getEntryAuthorization(),new EntryAuthorizationImpl().setInitial(null).setMovement(null).setActual(null).setAdjustment(null).setAvailable(-91l).setMovementIncluded(-23l)
				.setActualMinusMovementIncludedPlusAdjustment(null).setAvailableMinusMovementIncludedPlusAdjustment(null));
		assertor.assertExpenditureAmounts(expenditure.getPaymentCredit(),new PaymentCreditImpl().setInitial(null).setMovement(null).setActual(null).setAdjustment(null).setAvailable(-95l).setMovementIncluded(-23l)
				.setActualMinusMovementIncludedPlusAdjustment(null).setAvailableMinusMovementIncludedPlusAdjustment(null));
	}
	
	@Test @Order(1)
	void persistence_includedMovementAndAvailableOnly() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELDS_AMOUNTS_WITH_INCLUDED_MOVEMENT_AND_AVAILABLE_ONLY)
				.addFilterFieldsValues(Parameters.EXPENDITURES_IDENTIFIERS,List.of("2022_1_2_1")));
		assertThat(expenditure).isNotNull();
		assertor.assertExpenditureAmounts(expenditure.getEntryAuthorization(),new EntryAuthorizationImpl().setInitial(null).setMovement(null).setActual(null).setAdjustment(null).setAvailable(4l).setMovementIncluded(0l)
				.setActualMinusMovementIncludedPlusAdjustment(null).setAvailableMinusMovementIncludedPlusAdjustment(null));
		assertor.assertExpenditureAmounts(expenditure.getPaymentCredit(),new PaymentCreditImpl().setInitial(null).setMovement(null).setActual(null).setAdjustment(null).setAvailable(5l).setMovementIncluded(0l)
				.setActualMinusMovementIncludedPlusAdjustment(null).setAvailableMinusMovementIncludedPlusAdjustment(null));
	}
	
	@Test @Order(1)
	void persistence_amountsWithAdjustmentLessThanZeroGreaterThanZeroOnly() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELDS_AMOUNTS_WITH_ADJUSTMENT_LESS_THAN_ZERO_GREATER_THAN_ZERO_ONLY)
				.addFilterFieldsValues(Parameters.EXPENDITURES_IDENTIFIERS,List.of("2022_2_1_2")));
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getEntryAuthorization()).as("AE").isNotNull();
		assertThat(expenditure.getEntryAuthorization().getAdjustmentLessThanZero()).isEqualTo(-10l);
		assertThat(expenditure.getEntryAuthorization().getAdjustmentGreaterThanZero()).isEqualTo(0l);
		
		assertThat(expenditure.getPaymentCredit()).as("CP").isNotNull();
		assertThat(expenditure.getPaymentCredit().getAdjustmentLessThanZero()).isEqualTo(0l);
		assertThat(expenditure.getPaymentCredit().getAdjustmentGreaterThanZero()).isEqualTo(15l);
	}
	
	@Test @Order(1)
	void persistence_readAmounts_array() {
		Collection<ExpenditureImpl> expenditures = new ExpenditureImplAmountsReader().readByIdentifiersThenInstantiate(List.of("2022_1_2_9"), null);
		assertThat(expenditures).isNotNull();
		ExpenditureImpl expenditure = expenditures.iterator().next();
		assertor.assertExpenditureAmounts(expenditure.getEntryAuthorization(),new EntryAuthorizationImpl().setInitial(0l).setMovement(0l).setActual(0l).setAdjustment(33l).setAvailable(-100l).setMovementIncluded(0l)
				.setActualMinusMovementIncludedPlusAdjustment(33l).setAvailableMinusMovementIncludedPlusAdjustment(-67l));
		assertor.assertExpenditureAmounts(expenditure.getPaymentCredit(),new PaymentCreditImpl().setInitial(2l).setMovement(10l).setActual(12l).setAdjustment(0l).setAvailable(-100l).setMovementIncluded(0l)
				.setActualMinusMovementIncludedPlusAdjustment(12l).setAvailableMinusMovementIncludedPlusAdjustment(-100l));
	}
	
	@Test @Order(1)
	void persistence_readExpenditureImportedView() {
		Collection<ExpenditureImportedView> expenditures = entityManager.createQuery("SELECT t FROM ExpenditureImportedView t",ExpenditureImportedView.class).getResultList();
		assertThat(expenditures).isNotNull();
		assertThat(FieldHelper.readSystemIdentifiersAsStrings(expenditures)).containsExactlyInAnyOrder("2021_1_1_1","2021_1_1_2","2021_1_1_3","2021_1_1_4","2021_1_1_5");
	}
	
	@Test @Order(1)
	void persistence_readExpenditureImportableView() {
		Collection<ExpenditureImportableView> expenditures = entityManager.createQuery("SELECT t FROM ExpenditureImportableView t",ExpenditureImportableView.class).getResultList();
		assertThat(expenditures).isNotNull();
		assertThat(FieldHelper.readSystemIdentifiersAsStrings(expenditures)).containsExactlyInAnyOrder("2021_1_2_1","2021_1_2_2","2021_1_2_3","2021_1_2_4","2021_1_2_5");
	}
	
	@Test @Order(1)
    public void service_verifyLoadable() {
		io.restassured.response.Response response = given().when()
				//.log().all()
				.header("Content-Type", "application/json")
				.body(JsonbBuilder.create().toJson(List.of(new ExpenditureDto.LoadDto().setActivity("1").setEconomicNature("1").setFundingSource("1").setLessor("1"))))
				.queryParam(ExpenditureDto.JSON_LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2022_1_1")
				.post("/api/depenses/verification-chargeable");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	//.header(ResponseHelper.HEADER_X_TOTAL_COUNT, "13")
        	//.body(ExpenditureDto.JSON_IDENTIFIER, hasItems("2022_1_2_1"))
        	;
    }
	
	@Test @Order(1)
    public void service_verifyLoadable_unknown_activity_code() {
		io.restassured.response.Response response = given().when()
				//.log().all()
				.header("Content-Type", "application/json")
				.body(JsonbBuilder.create().toJson(List.of(new ExpenditureDto.LoadDto().setActivity("uac").setEconomicNature("1").setFundingSource("1").setLessor("1"))))
				.queryParam(ExpenditureDto.JSON_LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2022_1_1")
				.post("/api/depenses/verification-chargeable");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	//.header("codes_activites_inconnus", "uac")
        	.body(ExpenditureService.LoadableVerificationResultDto.JSON_UNKNOWN+".activites", hasItems("uac"))
        	;
    }
	
	@Test @Order(1)
    public void service_get_many() {
		io.restassured.response.Response response = given().when()
				//.log().all()
				.get("/api/depenses");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.header(ResponseHelper.HEADER_X_TOTAL_COUNT, expectedCount.toString())
        	.body(ExpenditureDto.JSON_IDENTIFIER, hasItems("2022_1_2_1"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_get_one_entryAuthorization() {
		io.restassured.response.Response response = given()
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,ExpenditureImpl.FIELD_IDENTIFIER)
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION)
				//.log().all()
				.when().get("/api/depenses/2022_1_2_1");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(ExpenditureDto.JSON_IDENTIFIER, equalTo("2022_1_2_1"))
        	.body(FieldHelper.join(ExpenditureDto.JSON_ENTRY_AUTHORIZATION,EntryAuthorizationDto.JSON_ADJUSTMENT), equalTo(0))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_get_one_paymentCredit() {
		io.restassured.response.Response response = given()
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,ExpenditureImpl.FIELD_IDENTIFIER)
				.param(EntityReader.PARAMETER_NAME_PROJECTIONS,ExpenditureImpl.FIELD_PAYMENT_CREDIT)
				//.log().all()
				.when().get("/api/depenses/2022_1_2_1");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(ExpenditureDto.JSON_IDENTIFIER, equalTo("2022_1_2_1"))
        	.body(FieldHelper.join(ExpenditureDto.JSON_PAYMENT_CREDIT,EntryAuthorizationDto.JSON_ADJUSTMENT), equalTo(7))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_get_one() {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().get("/api/depenses/2022_1_2_1");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(ExpenditureDto.JSON_IDENTIFIER, equalTo("2022_1_2_1"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_get_one_audit() {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().param(ExpenditureService.PARAMETER_NAME_PROJECTIONS, ExpenditureDto.JSON___AUDIT__).get("/api/depenses/2022_1_2_1");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(ExpenditureDto.JSON_IDENTIFIER, equalTo("2022_1_2_1"))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_get_one_notfound() {
		io.restassured.response.Response response = given()
				//.log().all()
				.when().get("/api/depenses/0");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.NOT_FOUND.getStatusCode());
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_count() {
		io.restassured.response.Response response = given().when().get("/api/depenses/nombre");
		response.then()
		//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	.body(equalTo(expectedCount.toString()))
        	;
		assertThat(response.getHeaders().asList().stream().map(header -> header.getName()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
    }
	
	@Test @Order(1)
    public void service_get_amounts_sums() {
		io.restassured.response.Response response = given().when().param("f", JsonbBuilder.create().toJson(new Filter.Dto().addField(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2022_1_2")))
				//.log().all()
				.get("/api/depenses/sommation-montants");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	//.body(ExpenditureDto.JSON_IDENTIFIER, hasItems("2022_1_2_1"))
        	;
    }
	
	@Test @Order(1)
    public void service_get_amounts_sums_without_includedMovementAndAvailable() {
		io.restassured.response.Response response = given().when().param("f", JsonbBuilder.create().toJson(new Filter.Dto().addField(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2022_1_2")
				.addField(Parameters.AMOUNT_SUMABLE, "true").addField(Parameters.AMOUNT_SUMABLE_WITHOUT_INCLUDED_MOVEMENT_AND_AVAILABLE, "true")))
				//.log().all()
				.get("/api/depenses/sommation-montants");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	//.body(ExpenditureDto.JSON_IDENTIFIER, hasItems("2022_1_2_1"))
        	;
    }
	
	@Test @Order(1)
    public void service_get_amounts_sums_with_includedMovementOnly() {
		io.restassured.response.Response response = given().when().param("f", JsonbBuilder.create().toJson(new Filter.Dto().addField(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2022_1_2")
				.addField(Parameters.AMOUNT_SUMABLE_WITH_INCLUDED_MOVEMENT_ONLY, "true")))
				//.log().all()
				.get("/api/depenses/sommation-montants");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	//.body(ExpenditureDto.JSON_IDENTIFIER, hasItems("2022_1_2_1"))
        	;
    }
	
	@Test @Order(1)
    public void service_get_amounts_sums_with_availableOnly() {
		io.restassured.response.Response response = given().when().param("f", JsonbBuilder.create().toJson(new Filter.Dto().addField(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, "2022_1_2")
				.addField(Parameters.AMOUNT_SUMABLE_WITH_AVAILABLE_ONLY, "true")))
				//.log().all()
				.get("/api/depenses/sommation-montants");
		response.then()
			//.log().all()
        	.statusCode(Response.Status.OK.getStatusCode())
        	//.body(ExpenditureDto.JSON_IDENTIFIER, hasItems("2022_1_2_1"))
        	;
    }
	
	@Test @Order(1)
    public void client_get_many() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.class).get(null,null,null, null, null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo(expectedCount.toString());
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure> expenditures = ResponseHelper.getEntityAsListFromJson(ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.class,response);
		assertThat(expenditures).hasSize(expectedCount);
		assertThat(expenditures.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).contains("2022_1_2_1");
		
		response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.class).get(null,null, null,null, null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo(expectedCount.toString());
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		expenditures = ResponseHelper.getEntityAsListFromJson(ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.class,response);
		assertThat(expenditures).hasSize(expectedCount);
		assertThat(expenditures.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).contains("2022_1_2_1");
    }
	
	@Test @Order(1)
    public void client_get_many_asStrings() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.class).get(null,null,null, List.of("astrings"), null, null, null, null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaderString(ResponseHelper.HEADER_X_TOTAL_COUNT)).isEqualTo(expectedCount.toString());
		assertThat(response.getHeaders().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
		.contains(ResponseHelper.HEADER_PROCESSING_START_TIME,ResponseHelper.HEADER_PROCESSING_END_TIME,ResponseHelper.HEADER_PROCESSING_DURATION);
		
		List<ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure> expenditures = ResponseHelper.getEntityAsListFromJson(ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.class,response);
		assertThat(expenditures).hasSize(expectedCount);
		assertThat(expenditures.stream().map(e -> e.getIdentifier()).collect(Collectors.toList())).contains("2022_1_2_1");
    }
	
	@Test @Order(1)
    public void client_get_one() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.class).getByIdentifier("2022_1_2_1", null,null,null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure expenditure = ResponseHelper.getEntity(ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.class,response);
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getIdentifier()).isEqualTo("2022_1_2_1");
		assertThat(expenditure.getEntryAuthorization()).isNull();
    }
	
	@Test @Order(1)
    public void client_get_one_entryAuthorization() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.class).getByIdentifier("2022_1_2_1"
				,null,null, List.of(ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION,ExpenditureImpl.FIELD_PAYMENT_CREDIT));
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure expenditure = ResponseHelper.getEntity(ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.class,response);
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getIdentifier()).isEqualTo("2022_1_2_1");
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
		assertThat(expenditure.getEntryAuthorization().getAdjustment()).isEqualTo(0l);
		assertThat(expenditure.getPaymentCredit().getAdjustment()).isEqualTo(7l);
    }
	
	@Test @Order(1)
    public void client_count() {
		Response response = DependencyInjection.inject(SpecificServiceGetter.class).get(ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.class).count(null,null,null);
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Long count = ResponseHelper.getEntityAsLong(response);
		assertThat(count).isEqualTo(expectedCount.longValue());
    }
	
	/* Import */
	
	@Test @Order(2)
	void business_import_2021_1_2() {
		assertor.assertExpenditureByLegislativeActVersion("2021_1_2", null);
		expenditureBusiness.import_("2021_1_2", "meliane");
		assertor.assertExpenditureByLegislativeActVersion("2021_1_2", List.of("2021_1_2_1","2021_1_2_2","2021_1_2_3","2021_1_2_4","2021_1_2_5"));
	}
	
	@Test @Order(2)
	void business_import_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.import_(null,null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Version collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
	
	@Test @Order(2)
	void business_import_running() {
		new Thread() {
			public void run() {
				expenditureBusiness.import_("2021_1_running","christian");
			}
		}.start();
		TimeHelper.pause(1l * 500);
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.import_("2021_1_running","christian");
	    });
		assertThat(exception.getMessage()).isEqualTo("Dépense de 2021_1_running en cours d'importation");
		TimeHelper.pause(1l * 2000);
		expenditureBusiness.import_("2021_1_running","christian");
	}
	
	/* Copy */
	
	@Test @Order(2)
	void business_copyAdjustments_2022_1_2_to_2022_1_3() {
		assertor.assertEntryAuthorization("2022_1_3_1", 0l);
		assertor.assertPaymentCredit("2022_1_3_1", 0l);
		expenditureBusiness.copy("2022_1_3","2022_1_2", "meliane");
		assertor.assertEntryAuthorization("2022_1_3_1", 0l);
		assertor.assertPaymentCredit("2022_1_3_1", 7l);
	}
	
	/* Amounts */
	
	@Test @Order(3)
	void persistence_readExpenditureOne_entryAuthorization() {
		Expenditure expenditure = expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne())).addFilterField("identifier", "2022_1_2_1")
				.addProjectionsFromStrings(ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION));
		assertThat(expenditure).isNotNull();
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
	}
	
	@Test @Order(3)
	void persistence_readExpenditureOne_amounts_1() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ExpenditureImpl.class))
				.addFilterField("identifier", "2022_1_2_1").addProjectionsFromStrings(ExpenditureImpl.FIELDS_AMOUNTS)
				);
		assertThat(expenditure).isNotNull();
		
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
		assertThat(expenditure.getEntryAuthorization().getInitial()).isEqualTo(1l);
		assertThat(expenditure.getEntryAuthorization().getActual()).isEqualTo(2l);
		assertThat(expenditure.getEntryAuthorization().getMovement()).isEqualTo(1l);
		assertThat(expenditure.getEntryAuthorization().getAdjustment()).isEqualTo(0l);
		assertThat(expenditure.getEntryAuthorization().getActualPlusAdjustment()).isEqualTo(2l);
		assertThat(expenditure.getEntryAuthorization().getMovementIncluded()).isEqualTo(0l);
		
		assertThat(expenditure.getPaymentCredit()).isNotNull();
		assertThat(expenditure.getPaymentCredit().getInitial()).isEqualTo(3l);
		assertThat(expenditure.getPaymentCredit().getActual()).isEqualTo(5l);
		assertThat(expenditure.getPaymentCredit().getMovement()).isEqualTo(2l);
		assertThat(expenditure.getPaymentCredit().getAdjustment()).isEqualTo(7l);
		assertThat(expenditure.getPaymentCredit().getActualPlusAdjustment()).isEqualTo(12l);
		assertThat(expenditure.getPaymentCredit().getMovementIncluded()).isEqualTo(0l);
		
		assertThat(expenditure.getActAsString()).isNull();
	}
	
	@Test @Order(3)
	void persistence_readExpenditureOne_amounts_3() {
		ExpenditureImpl expenditure = (ExpenditureImpl) expenditurePersistence.readOne(new QueryExecutorArguments()
				.setQuery(new Query().setIdentifier(expenditurePersistence.getQueryIdentifierReadDynamicOne()).setTupleClass(ExpenditureImpl.class))
				.addFilterField("identifier", "2022_1_2_3").addProjectionsFromStrings(ExpenditureImpl.FIELDS_AMOUNTS)
				);
		assertThat(expenditure).isNotNull();
		
		assertThat(expenditure.getEntryAuthorization()).isNotNull();
		assertThat(expenditure.getEntryAuthorization().getInitial()).isEqualTo(10l);
		assertThat(expenditure.getEntryAuthorization().getActual()).isEqualTo(17l);
		assertThat(expenditure.getEntryAuthorization().getMovement()).isEqualTo(7l);
		assertThat(expenditure.getEntryAuthorization().getAdjustment()).isEqualTo(0l);
		assertThat(expenditure.getEntryAuthorization().getActualPlusAdjustment()).isEqualTo(17l);
		assertThat(expenditure.getEntryAuthorization().getMovementIncluded()).isEqualTo(-25l);
		
		assertThat(expenditure.getPaymentCredit()).isNotNull();
		assertThat(expenditure.getActAsString()).isNull();
	}
	
	@Test @Order(3)
	void persistence_readExpenditureOne_movementIncluded_1() {
		assertor.assertExpenditureMovementIncluded("2022_1_2_1", 0l, 0l);
	}
	
	@Test @Order(3)
	void persistence_readExpenditureOne_movementIncluded_2() {
		assertor.assertExpenditureMovementIncluded("2022_1_2_2", 0l, 0l);
	}
	
	@Test @Order(3)
	void persistence_readExpenditureOne_movementIncluded_3() {
		assertor.assertExpenditureMovementIncluded("2022_1_2_3", -25l, -25l);
	}
	
	@Test @Order(3)
	void persistence_readEntryAuthorizationAdjustment() {
		Collection<Object[]> objects = new ExpenditureImplEntryAuthorizationAdjustmentReader().readByIdentifiers(List.of("2022_1_2_1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo(0l);
	}
	
	@Test @Order(3)
	void persistence_readEntryAuthorizationAdjustmentAvailable() {
		Collection<Object[]> objects = new ExpenditureImplEntryAuthorizationAdjustmentAvailableReader().readByIdentifiers(List.of("2022_1_2_1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo(0l);
	}
	
	@Test @Order(3)
	void persistence_read_2022_1_2_ADJUSTMENTS_NOT_EQUAL_ZERO() {
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2",Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO,Boolean.TRUE));
		assertThat(expenditures).isNotNull();
		assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_1_2_1","2022_1_2_9");
	}
	
	@Test @Order(3)
	void persistence_read_2022_1_2_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO() {
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2",Parameters.INCLUDED_MOVEMENT_NOT_EQUAL_ZERO,Boolean.TRUE));
		assertThat(expenditures).isNotNull();
		assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_1_2_3","2022_1_2_4","2022_1_2_5");
	}
	
	@Test @Order(3)
	void persistence_count_2022_1_2_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO() {
		Long count = expenditurePersistence.count(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2",Parameters.INCLUDED_MOVEMENT_NOT_EQUAL_ZERO,Boolean.TRUE));
		assertThat(count).isNotNull();
		assertThat(count).isEqualTo(3);
	}
	
	@Test @Order(3)
	void persistence_read_2022_1_2_ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO() {
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2",Parameters.ADJUSTMENTS_NOT_EQUAL_ZERO_OR_INCLUDED_MOVEMENT_NOT_EQUAL_ZERO,Boolean.TRUE));
		assertThat(expenditures).isNotNull();
		assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_1_2_1","2022_1_2_3","2022_1_2_4","2022_1_2_5","2022_1_2_9");
	}
	
	@Test @Order(3)
	void persistence_read_2022_1_2_9_AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_LESS_THAN_ZERO() {
		Collection<Expenditure> expenditures = expenditurePersistence.readMany(new QueryExecutorArguments().addProjectionsFromStrings(ExpenditureImpl.FIELD_IDENTIFIER)
				.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"2022_1_2",Parameters.AVAILABLE_MINUS_INCLUDED_MOVEMENT_PLUS_ADJUSTMENT_LESS_THAN_ZERO,Boolean.TRUE));
		assertThat(expenditures).isNotNull();
		assertThat(expenditures.stream().map(x -> x.getIdentifier()).collect(Collectors.toList())).containsExactly("2022_1_2_4","2022_1_2_5","2022_1_2_9");
	}
	
	/* Adjust */
	
	@Test @Order(4)
	void business_adjust_adjustableIsFalse() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjustByEntryAuthorizations(Map.of("2022_2_2_1",3l),"anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("1 Dépenses non ajustable : 2022_2_2_1");
	}
	
	@Test @Order(4)
	void business_adjust() {
		assertor.assertExpenditureAudits("2022_1_3_5", "christian", "AJUSTEMENT", "MODIFICATION", TimeHelper.toMillisecond(LocalDateTime.of(2000, 1, 2, 1, 1)));
		assertor.assertExpenditureAudit("2022_1_3_5", "AJUSTEMENT par christian le");
		assertor.assertEntryAuthorization("2022_1_3_5", 0l);
		assertor.assertPaymentCredit("2022_1_3_5", 0l);
		expenditureBusiness.adjust(Map.of("2022_1_3_5",new Long[] {3l,1l}),"meliane");
		assertor.assertEntryAuthorization("2022_1_3_5", 3l);
		assertor.assertPaymentCredit("2022_1_3_5", 1l);
		assertor.assertExpenditureAudits("2022_1_3_5", "meliane", ExpenditureBusiness.ADJUST_AUDIT_IDENTIFIER, "MODIFICATION");
		assertor.assertExpenditureAudit("2022_1_3_5", ExpenditureBusiness.ADJUST_AUDIT_IDENTIFIER+" par meliane le");
		
		String auditIdentifier = entityManager.createQuery("SELECT t.__auditIdentifier__ FROM ExpenditureImpl t WHERE t.identifier = :identifier",String.class).setParameter("identifier", "2022_1_3_5").getSingleResult();
		
		//Again with same values
		expenditureBusiness.adjust(Map.of("2022_1_3_5",new Long[] {3l,1l}),"meliane");
		assertor.assertEntryAuthorization("2022_1_3_5", 3l);
		assertor.assertPaymentCredit("2022_1_3_5", 1l);
		assertor.assertExpenditureAudits("2022_1_3_5", "meliane", ExpenditureBusiness.ADJUST_AUDIT_IDENTIFIER, "MODIFICATION");
		assertor.assertExpenditureAudit("2022_1_3_5", ExpenditureBusiness.ADJUST_AUDIT_IDENTIFIER+" par meliane le");
		
		assertThat(entityManager.createQuery("SELECT t.__auditIdentifier__ FROM ExpenditureImpl t WHERE t.identifier = :identifier",String.class).setParameter("identifier", "2022_1_3_5").getSingleResult()).as("audit identifier").isEqualTo(auditIdentifier);
	}
	
	@Test @Order(4)
	void business_adjust_availableNotMonitorable() {
		assertor.assertEntryAuthorization("2022_2_1_1", 0l);
		assertor.assertPaymentCredit("2022_2_1_1", 0l);
		expenditureBusiness.adjust(Map.of("2022_2_1_1",new Long[] {-1l,-2l}),"meliane");
		assertor.assertEntryAuthorization("2022_2_1_1", -1l);
		assertor.assertPaymentCredit("2022_2_1_1", -2l);
	}
	
	@Test @Order(4)
	void business_adjustByEntryAuthorizations() {
		assertor.assertEntryAuthorization("2022_1_3_4", 0l);
		assertor.assertPaymentCredit("2022_1_3_4", 0l);
		expenditureBusiness.adjustByEntryAuthorizations(Map.of("2022_1_3_4",3l),"sandrine");
		assertor.assertEntryAuthorization("2022_1_3_4", 3l);
		assertor.assertPaymentCredit("2022_1_3_4", 3l);
		assertor.assertExpenditureAudits("2022_1_3_4", "sandrine", ExpenditureBusiness.ADJUST_AUDIT_IDENTIFIER+"_PAR_AE", "MODIFICATION");
	}
	
	@Test @Order(4)
	void business_adjustByEntryAuthorizations_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjustByEntryAuthorizations(null,"anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("Ajustements requis");
	}
	
	@Test @Order(4)
	void business_adjustByEntryAuthorizations_identifierNotExist() {
		Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjustByEntryAuthorizations(Map.of("identifier_not_exist",0l),"anonymous");
	    });
	}
	
	@Test @Order(6)
	void business_adjust_entry_authorization_adjustment_null_payment_credit_adjustment_notNull() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjust(Map.of("2022_1_3_5",new Long[] {null,2l}), "christian");
	    });
		assertThat(exception.getMessage()).isEqualTo("L'ajustement de l'autorisation d'engagement de 2022_1_3_5 doit être défini");
	}
	
	@Test @Order(4)
	void business_adjustByEntryAuthorizations_availableNotEnough() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjustByEntryAuthorizations(Map.of("2022_1_3_6",-2l),"anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("La Dépense 2022_1_3_6 à un disponible A.E. insuffisant(-2,0)\r\nLa Dépense 2022_1_3_6 à un disponible C.P. insuffisant(-2,0)");
	}
	
	@Test @Order(4)
	void business_adjust_availableNotEnough() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjust(Map.of("2022_1_3_6",new Long[] {-2l,-2l}),"anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("La Dépense 2022_1_3_6 à un disponible A.E. insuffisant(-2,0)\r\nLa Dépense 2022_1_3_6 à un disponible C.P. insuffisant(-2,0)");
	}
	
	@Test @Order(4)
	void business_adjust_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.adjust(null,"anonymous");
	    });
		assertThat(exception.getMessage()).isEqualTo("Ajustements requis");
	}
	
	@Test @Order(5)
    public void client_adjustByEntryAuthorizations() {
		assertor.assertEntryAuthorization("2022_1_3_4", 3l);
		assertor.assertPaymentCredit("2022_1_3_4", 3l);
		Response response = ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.getService().adjustByEntryAuthorizations(List.of(new ExpenditureDto.AdjustmentDto().setIdentifier("2022_1_3_4").setEntryAuthorization(17l)),"test");
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertor.assertEntryAuthorization("2022_1_3_4", 17l);
		assertor.assertPaymentCredit("2022_1_3_4", 17l);
    }
	
	@Test @Order(5)
    public void client_adjust() {
		assertor.assertEntryAuthorization("2022_1_3_5", 3l);
		assertor.assertPaymentCredit("2022_1_3_5", 1l);
		Response response = ci.gouv.dgbf.system.collectif.server.client.rest.Expenditure.getService().adjust(List.of(new ExpenditureDto.AdjustmentDto().setIdentifier("2022_1_3_5").setEntryAuthorization(7l).setPaymentCredit(5l)),"test");
		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertor.assertEntryAuthorization("2022_1_3_5", 7l);
		assertor.assertPaymentCredit("2022_1_3_5", 5l);
    }
	
	@Test @Order(6)
	void business_load_null() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			expenditureBusiness.load("2022_1_3", null, "christian");
	    });
		assertThat(exception.getMessage()).isEqualTo("Dépenses requises");
	}
	
	@Test @Order(6)
    public void business_load() {
		assertor.assertEntryAuthorization("2022_1_3_5", 7l);
		assertor.assertPaymentCredit("2022_1_3_5", 5l);
		assertor.assertEntryAuthorization("2022_1_3_6", 0l);
		assertor.assertPaymentCredit("2022_1_3_6", 0l);
		
		expenditureBusiness.load("2022_1_3", List.of(new ExpenditureImpl().setActivityCode("a05").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(0l)), "christian");
		assertor.assertEntryAuthorization("2022_1_3_5", 0l);
		assertor.assertPaymentCredit("2022_1_3_5", 0l);
		assertor.assertEntryAuthorization("2022_1_3_6", 0l);
		assertor.assertPaymentCredit("2022_1_3_6", 0l);
		
		expenditureBusiness.load("2022_1_3", List.of(new ExpenditureImpl().setActivityCode("a05").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(12l).setPaymentCreditAdjustment(25l)), "christian");
		assertor.assertEntryAuthorization("2022_1_3_5", 12l);
		assertor.assertPaymentCredit("2022_1_3_5", 25l);
		assertor.assertEntryAuthorization("2022_1_3_6", 0l);
		assertor.assertPaymentCredit("2022_1_3_6", 0l);
		
		expenditureBusiness.load("2022_1_3", List.of(new ExpenditureImpl().setActivityCode("a05").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(null).setPaymentCreditAdjustment(25l)), "christian");
		assertor.assertEntryAuthorization("2022_1_3_5", 12l);
		assertor.assertPaymentCredit("2022_1_3_5", 25l);
		assertor.assertEntryAuthorization("2022_1_3_6", 0l);
		assertor.assertPaymentCredit("2022_1_3_6", 0l);
		
		expenditureBusiness.load("2022_1_3", List.of(new ExpenditureImpl().setActivityCode("a05").setEconomicNatureCode("1").setFundingSourceCode("1").setLessorCode("1").setEntryAuthorizationAdjustment(-10000l).setPaymentCreditAdjustment(25l)), "christian");
		assertor.assertEntryAuthorization("2022_1_3_5", 12l);
		assertor.assertPaymentCredit("2022_1_3_5", 25l);
		assertor.assertEntryAuthorization("2022_1_3_6", 0l);
		assertor.assertPaymentCredit("2022_1_3_6", 0l);
    }
	
	/**/
	
	public static String PA_ACTUALISER_VM(String tableName) {
		TimeHelper.pause(1l * 1000l);
		return null;
	}
}