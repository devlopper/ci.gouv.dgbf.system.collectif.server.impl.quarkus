package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.impl.Assertor;
import ci.gouv.dgbf.system.collectif.server.impl.Profiles;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Persistence.class)
public class ExpenditurePersistenceTest {

	@Inject Assertor assertor;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject UserTransaction userTransaction;
	
	@Test
	void readEntryAuthorization() {
		Collection<Object[]> objects = new ExpenditureImplEntryAuthorizationReader().readByIdentifiers(List.of("1"), null);
		assertThat(objects).hasSize(1);
		assertThat(CollectionHelper.getElementAt(objects, 0)[0]).isEqualTo("1");
		assertThat(CollectionHelper.getElementAt(objects, 0)[1]).isEqualTo(0l);
	}
	
	@Test
	void buildUpdateEntryAuthoriationsAdjustmentsQuery_one() {
		assertThat(ExpenditurePersistenceImpl.buildUpdateEntryAuthoriationsAdjustmentsQuery(Map.of("1",2l)))
		.isEqualTo("UPDATE ExpenditureImpl SET entryAuthorization.adjustment = CASE WHEN identifier='1' THEN 2 END WHERE identifier IN :identifiers");
	}
	
	@Test
	void buildUpdateEntryAuthoriationsAdjustmentsQuery_many() {
		Map<String,Long> map = new LinkedHashMap<>();
		map.put("1",2l);
		map.put("2",-3l);
		assertThat(ExpenditurePersistenceImpl.buildUpdateEntryAuthoriationsAdjustmentsQuery(map))
		.isEqualTo("UPDATE ExpenditureImpl SET entryAuthorization.adjustment = CASE WHEN identifier='1' THEN 2 WHEN identifier='2' THEN -3 END WHERE identifier IN :identifiers");
	}
	
	@Test
	void updateEntryAuthoriations_one() {
		assertor.assertEntryAuthorization("uea_one_01", 0l);
		try {
			userTransaction.begin();
			expenditurePersistence.updateEntryAuthoriations(Map.of("uea_one_01",3l));
			userTransaction.commit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}		
		assertor.assertEntryAuthorization("uea_one_01", 3l);
	}
	
	@Test
	void updateEntryAuthoriations_many() {
		assertor.assertEntryAuthorization("uea_many_01", 0l);
		assertor.assertEntryAuthorization("uea_many_02", -2l);
		try {
			userTransaction.begin();
			expenditurePersistence.updateEntryAuthoriations(Map.of("uea_many_01",3l,"uea_many_02",5l));
			userTransaction.commit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}		
		assertor.assertEntryAuthorization("uea_many_01", 3l);
		assertor.assertEntryAuthorization("uea_many_02", 5l);
	}
}