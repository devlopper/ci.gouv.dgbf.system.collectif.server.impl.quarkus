package ci.gouv.dgbf.system.collectif.server.impl;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.collection.CollectionHelper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationReader;

@ApplicationScoped
public class Assertor {

	public void assertEntryAuthorization(String identifier,Long value) {
		Collection<Object[]> arrays = new ExpenditureImplEntryAuthorizationReader().readByIdentifiers(List.of(identifier), null);
		assertThat(arrays).hasSize(1);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[0]).isEqualTo(identifier);
		assertThat(CollectionHelper.getElementAt(arrays, 0)[1]).isEqualTo(value);
	}
	
}