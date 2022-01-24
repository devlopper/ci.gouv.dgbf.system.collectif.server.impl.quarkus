package ci.gouv.dgbf.system.collectif.server.impl.business;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.business.GeneratedActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.GeneratedActImpl;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Business.GeneratedAct.Delete.class)
public class BusinessGeneratedActDeleteTest {

	@Inject Assertor assertor;
	@Inject GeneratedActBusiness generatedActBusiness;
	@Inject GeneratedActPersistence generatedActPersistence;
	
	@Test
	void deleteByLegislativeActVersionIdentifier_notGenerated() {
		Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
			generatedActBusiness.deleteByLegislativeActVersionIdentifier("1", "meliane");
	    });
		assertThat(exception.getMessage()).isEqualTo("La version du collectif 1 n'a aucun actes générés");
	}
	
	@Test
	void delete_generated_notApplied() {
		generatedActPersistence.readMany(new QueryExecutorArguments()).forEach(x -> {
			System.out.println("BusinessGeneratedActDeleteTest.delete_generated_notApplied() ::: "+x+" ::: "+ ((GeneratedActImpl)x).getLegislativeActVersion());
		});
		/*assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", null);
		
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("2", List.of("3"));
		*/
		generatedActBusiness.deleteByLegislativeActVersionIdentifier("2", "meliane");
		/*
		assertor.assertGeneratedActIdentifiersByLegislativeActVersionIdentifier("1", List.of("1","1_1","A_1_1"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("1", List.of("1_2","1_3","1_4"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("1_1", List.of("1_1_1_1","1_1_1_2"));
		assertor.assertGeneratedActExpenditureIdentifiersByGeneratedActIdentifier("A_1_1", List.of("A_1_1_1_1","A_1_1_1_2"));
		*/
		
	}
}