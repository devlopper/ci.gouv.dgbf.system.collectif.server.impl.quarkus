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
@TestProfile(Profiles.Client.GeneratedAct.Delete.class)
public class ClientGeneratedActDeleteTest {

	@Inject Assertor assertor;
	@Inject GeneratedActController controller;
	
	@Test
	void deleteByLegislativeActVersionIdentifier_notGenerated() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			controller.deleteByLegislativeActVersionIdentifier("1", "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("La version du collectif 1 n'a aucun actes générés");
	}
	
	@Test
	void delete_generated_notApplied() {
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", null);
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2", List.of("2","3","4","A_3","A_4"));
		
		controller.deleteByLegislativeActVersionIdentifier("2", "meliane");
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", null);
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2", null);
	}
}