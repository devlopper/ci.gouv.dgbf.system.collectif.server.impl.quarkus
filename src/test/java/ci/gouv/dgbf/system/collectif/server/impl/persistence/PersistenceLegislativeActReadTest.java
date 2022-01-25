package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.LegislativeAct.Read.class)
public class PersistenceLegislativeActReadTest {

	@Inject Assertor assertor;
	@Inject LegislativeActPersistence persistence;
	
	@Test
	void readMany() {
		Collection<LegislativeAct> legislativeActs = persistence.readMany(null, null, null);
		assertThat(legislativeActs).hasSize(3);
	}
	
	@Test
	void readOne() {
		LegislativeAct legislativeAct = persistence.readOne("1");
		assertThat(legislativeAct).isNotNull();
	}
	
	@Test
	void readMany_2020() {
		Collection<LegislativeAct> legislativeActs = persistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.EXERCISE_YEAR, 2020));
		assertThat(legislativeActs).isNull();
	}
	
	@Test
	void readMany_2021() {
		Collection<LegislativeAct> legislativeActs = persistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.EXERCISE_YEAR, 2021));
		assertThat(legislativeActs).isNotNull();
		assertThat(legislativeActs.stream().map(x ->x.getIdentifier()).collect(Collectors.toList())).containsExactly("1","2");
	}
	
	@Test
	void readMany_2022() {
		Collection<LegislativeAct> legislativeActs = persistence.readMany(new QueryExecutorArguments().addFilterField(Parameters.EXERCISE_YEAR, 2022));
		assertThat(legislativeActs).isNotNull();
		assertThat(legislativeActs.stream().map(x ->x.getIdentifier()).collect(Collectors.toList())).containsExactly("3");
	}
}