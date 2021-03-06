package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationReader;

@ApplicationScoped
public class ExpenditureBusinessImpl extends AbstractSpecificBusinessImpl<Expenditure> implements ExpenditureBusiness,Serializable {

	@Inject ExpenditurePersistence expenditurePersistence;
	
	@Override @Transactional
	public Result adjust(Map<String, Long> entryAuthorizations) {
		Result result = new Result().setName("ajustement").open();
		if(MapHelper.isEmpty(entryAuthorizations))
			throw new RuntimeException("Autorisations d'engagements requis");
		Collection<Object[]> objects = new ExpenditureImplEntryAuthorizationReader()
				.readByIdentifiers(entryAuthorizations.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList()), null);
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		ValidatorImpl.validateEntryAuthorizations(objects, entryAuthorizations, null);		
		throwablesMessages.throwIfNotEmpty();
		expenditurePersistence.updateEntryAuthoriations(entryAuthorizations);
		return result.close();
	}

}