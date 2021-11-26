package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;
import org.cyk.utility.persistence.server.query.string.CaseStringBuilder;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;

@ApplicationScoped
public class ExpenditurePersistenceImpl extends AbstractSpecificPersistenceImpl<Expenditure>  implements ExpenditurePersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public ExpenditurePersistenceImpl() {
		entityClass = Expenditure.class;
		entityImplClass = ExpenditureImpl.class;
	}
		
	@Override
	public Integer updateEntryAuthoriations(Map<String, Long> entryAuthorizations) {
		if(MapHelper.isEmpty(entryAuthorizations))
			return null;
		String query = buildUpdateEntryAuthoriationsAdjustmentsQuery(entryAuthorizations);
		if(StringHelper.isBlank(query))
			return null;
		return entityManager.createQuery(query).setParameter("identifiers", entryAuthorizations.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList())).executeUpdate();
	}
	
	public static String buildUpdateEntryAuthoriationsAdjustmentsQuery(Map<String, Long> entryAuthorizations) {
		if(MapHelper.isEmpty(entryAuthorizations))
			return null;
		StringBuilder query = new StringBuilder(String.format("UPDATE %s SET %s =",ExpenditureImpl.ENTITY_NAME,FieldHelper.join(ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION,EntryAuthorizationImpl.FIELD_ADJUSTMENT)));
		CaseStringBuilder.Case kase = new CaseStringBuilder.Case();
		entryAuthorizations.entrySet().forEach(entry -> {
			kase.add(String.format("WHEN %s='%s' THEN %s",ExpenditureImpl.FIELD_IDENTIFIER,entry.getKey(),entry.getValue()));
		});
		query.append(" "+CaseStringBuilder.getInstance().build(kase)+String.format(" WHERE %s IN :identifiers", ExpenditureImpl.FIELD_IDENTIFIER));
		return query.toString();
	}
}