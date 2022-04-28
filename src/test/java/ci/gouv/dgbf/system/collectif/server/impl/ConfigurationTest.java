package ci.gouv.dgbf.system.collectif.server.impl;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Configuration.class)
public class ConfigurationTest extends org.cyk.quarkus.extension.test.AbstractTest {
	
	@Inject Configuration configuration;
	
	@Test
	void actor_visibilities_enabled() {
		assertThat(configuration.actor().visibilities().enabled()).isTrue();
	}
	
	@Test
	void budgetCategory_defaultIdentifier() {
		assertThat(configuration.budgetCategory().defaultIdentifier()).isEqualTo("1");
	}
}