package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Business.LegislativeActVersion.Create.class)
public class BusinessLegislativeActVersionCreateTest {

	@Inject Assertor assertor;
	@Inject LegislativeActVersionPersistence persistence;
	@Inject LegislativeActVersionBusiness business;
	
	@Test
	void create() {
		assertThat(persistence.readOne("2021_1_1")).isNull();
		business.create(null, null,null,"2021_1", "meliane");
		assertor.assertLegislativeActVersion("2021_1_1", "2021_1_1","Version 1 Collectif Budgétaire 2021 du 01/01/2021",Byte.valueOf("1"),"2021_1");
	}
	
	@Test
	void create_exerciseIdentifierNull() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			business.create((String)null,(String)null,(Byte)null,(String)null,(String)null);
	    });
		assertThat(exception.getMessage()).isEqualTo("L'identifiant de Collectif budgétaire est requis\r\nLe nom d'utilisateur est requis");
	}
}