package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.LegislativeActVersion.Read.class)
public class PersistenceLegislativeActVersionReadTest {

	@Inject Assertor assertor;
	@Inject LegislativeActVersionPersistence persistence;
	
	@Test
	void readMany() {
		Collection<LegislativeActVersion> legislativeActVersions = persistence.readMany(null, null, null);
		assertThat(legislativeActVersions).hasSize(5);
	}
	
	@Test
	void readOne() {
		LegislativeActVersion legislativeActVersion = persistence.readOne("1_1");
		assertThat(legislativeActVersion).isNotNull();
	}
	
	@Test
	void readOne_projections_FIELDS_ACT_AS_STRING_CODE_NAME_NUMBER_CREATION_DATE_AS_STRING() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne("1_1",List.of(LegislativeActVersionImpl.FIELDS_ACT_AS_STRING_CODE_NAME_NUMBER_CREATION_DATE_AS_STRING));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("1_1");
		assertThat(legislativeActVersion.getCode()).isEqualTo("1_1");
		assertThat(legislativeActVersion.getName()).isEqualTo("Version du Collectif Budgétaire 2021 du 01/01/2021");
		assertThat(legislativeActVersion.getNumber()).isEqualTo(Byte.valueOf("1"));
		assertThat(legislativeActVersion.getActAsString()).isEqualTo("Collectif Budgétaire 2021 du 01/01/2021");
	}
	
	@Test
	void readOne_projections_FIELD_GENERATED_ACT_COUNT_null() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne("1_1",List.of(LegislativeActVersionImpl.FIELD_GENERATED_ACT_COUNT));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("1_1");
		assertThat(legislativeActVersion.getGeneratedActCount()).isEqualTo(null);
	}
	
	@Test
	void readOne_projections_FIELD_GENERATED_ACT_COUNT_notNull() {
		LegislativeActVersionImpl legislativeActVersion = (LegislativeActVersionImpl) persistence.readOne("2_1",List.of(LegislativeActVersionImpl.FIELD_GENERATED_ACT_COUNT));
		assertThat(legislativeActVersion).isNotNull();
		assertThat(legislativeActVersion.getIdentifier()).isEqualTo("2_1");
		assertThat(legislativeActVersion.getGeneratedActCount()).isEqualTo(Short.valueOf("1"));
	}
}