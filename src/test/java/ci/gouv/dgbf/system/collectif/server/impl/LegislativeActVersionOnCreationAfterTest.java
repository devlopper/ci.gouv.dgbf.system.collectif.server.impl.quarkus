package ci.gouv.dgbf.system.collectif.server.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.cyk.utility.__kernel__.time.TimeHelper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.LegislativeActVersionWithOnCreationAfter.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class LegislativeActVersionOnCreationAfterTest {

	@Inject Assertor assertor;
	@Inject LegislativeActVersionPersistence persistence;
	@Inject LegislativeActVersionBusiness business;
	
	@Test @Order(1)
	void business_create() {
		assertThat(persistence.readOne("2022_1_1")).isNull();
		business.create(null, null,null,"2022_1", "meliane");
		assertThat(persistence.readOne("2022_1_1")).isNotNull();
		assertor.assertLegislativeActVersion("2022_1_1", "2022_1_1","Version 1 2022_1",Byte.valueOf("1"),"2022_1");
		TimeHelper.pause(1000l * 1);
		assertor.assertExpenditureByLegislativeActVersion("2022_1_1", List.of("2022_1_1_1","2022_1_1_2"));
	}

	public static String PA_ACTUALISER_VM(String tableName) {
		return null;
	}
}