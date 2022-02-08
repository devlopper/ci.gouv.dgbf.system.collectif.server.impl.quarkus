package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.business.GeneratedActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Business.GeneratedAct.Generate.class)
public class BusinessGeneratedActGenerateTest {

	@Inject Assertor assertor;
	@Inject GeneratedActBusiness generatedActBusiness;
	@Inject GeneratedActPersistence generatedActPersistence;
	
	@Test
	void generate_alreadyGenerated() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			generatedActBusiness.generateByLegislativeActVersionIdentifier("3", "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("La génération des actes de Version 3 du Collectif budgétaire 2021 a déja été faite");
	}
	
	@Test
	void generate_notYetGenerated() {
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", null);
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("3", List.of("3"));
		
		generatedActBusiness.generateByLegislativeActVersionIdentifier("1", "meliane");
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", List.of("1","A_1_1"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("1", List.of("1_1","1_2","1_3","1_4"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("A_1_1", List.of("A_1_1_1_1","A_1_1_1_2"));
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("3", List.of("3"));
	}
}