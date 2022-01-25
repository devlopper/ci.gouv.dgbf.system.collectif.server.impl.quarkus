package ci.gouv.dgbf.system.collectif.server.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.client.rest.GeneratedActController;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Client.GeneratedAct.Generate.class)
public class ClientGeneratedActGenerateTest {

	@Inject Assertor assertor;
	@Inject GeneratedActController controller;
	
	@Test
	void generate_alreadyGenerated() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			controller.generateByLegislativeActVersionIdentifier("3", "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("L'acte de la version du collectif 3 a déja été généré");
	}
	
	@Test
	void generate_notYetGenerated() {
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", null);
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("3", List.of("3"));
		
		controller.generateByLegislativeActVersionIdentifier("1", "meliane");
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", List.of("1","1_1","A_1_1"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("1", List.of("1_2","1_3","1_4"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("1_1", List.of("1_1_1_1","1_1_1_2"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("A_1_1", List.of("A_1_1_1_1","A_1_1_1_2"));
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("3", List.of("3"));
	}
}