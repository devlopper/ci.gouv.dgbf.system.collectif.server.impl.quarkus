package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.ResourceBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Resource;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImpl;

@ApplicationScoped
public class ResourceBusinessImpl extends AbstractSpecificBusinessImpl<Resource> implements ResourceBusiness,Serializable {

	@Inject EntityManager entityManager;
	@Inject ExpenditurePersistence expenditurePersistence;
	
	@Override @Transactional
	public Result adjust(Map<String, Long> adjustments,String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		ValidatorImpl.Resource.validateAdjust(adjustments, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		Collection<String> providedIdentifiers = adjustments.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());		
		
		// Validation of objects
		Collection<ResourceImpl> resources = entityManager.createNamedQuery(ResourceImpl.QUERY_READ_BY_IDENTIIFERS, ResourceImpl.class)
				.setParameter("identifiers", adjustments.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
				.getResultList();
		ValidatorImpl.validateIdentifiers(providedIdentifiers, FieldHelper.readSystemIdentifiersAsStrings(resources), throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		// Persist of objects
		LocalDateTime auditWhen = LocalDateTime.now();
		resources.forEach(resource -> {
			resource.getRevenue(Boolean.TRUE).setAdjustment(adjustments.get(resource.getIdentifier()));
			audit(resource,ADJUST_AUDIT_IDENTIFIER,auditWho,auditWhen);
			entityManager.merge(resource);
		});

		// Return of message
		result.close().setName(String.format("Ajustement de %s ressource(s) par %s",resources.size(),auditWho)).log(getClass());
		result.addMessages(String.format("Nombre de ligne de ressource mise à jour : %s", resources.size()));
		return result;
	}
}