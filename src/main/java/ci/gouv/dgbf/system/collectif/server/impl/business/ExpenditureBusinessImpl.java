package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationAdjustmentAvailableReader;

@ApplicationScoped
public class ExpenditureBusinessImpl extends AbstractSpecificBusinessImpl<Expenditure> implements ExpenditureBusiness,Serializable {

	@Inject ExpenditurePersistence expenditurePersistence;
	
	@Override @Transactional
	public Result adjust(Map<String, Long> entryAuthorizations) {
		Result result = new Result().setName("ajustement").open();
		if(MapHelper.isEmpty(entryAuthorizations))
			throw new RuntimeException("Autorisations d'engagements requis");
		Collection<Object[]> arrays = new ExpenditureImplEntryAuthorizationAdjustmentAvailableReader()
				.readByIdentifiers(entryAuthorizations.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList()), null);
		Collection<String> identifiers = CollectionHelper.isEmpty(arrays) ? null : arrays.stream().map(array -> (String)array[0]).collect(Collectors.toList());
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		ValidatorImpl.validateIdentifiers(entryAuthorizations.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()), identifiers, throwablesMessages);
		ValidatorImpl.validateEntryAuthorizationsAvailable(entryAuthorizations, arrays
				, ExpenditureImplEntryAuthorizationAdjustmentAvailableReader.ENTRY_AUTHORIZATION_AVAILABLE_INDEX, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		expenditurePersistence.updateEntryAuthoriations(entryAuthorizations);
		return result.close();
	}

}