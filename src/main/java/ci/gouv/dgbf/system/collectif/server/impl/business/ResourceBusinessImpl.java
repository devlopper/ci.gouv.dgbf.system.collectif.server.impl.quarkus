package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.persistence.EntityManagerGetter;

import ci.gouv.dgbf.system.collectif.server.api.business.ResourceBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Resource;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImplAdjustableIsFalseReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImportableView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RevenueImpl;
import io.quarkus.vertx.ConsumeEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@ApplicationScoped
public class ResourceBusinessImpl extends AbstractExpenditureResourceBusinessImpl<Resource> implements ResourceBusiness,Serializable {

	@Override
	void __listenPostConstruct__() {
		entityClass = Resource.class;
		entityViewClass = ResourceView.class;
		entityImportableClass = ResourceImportableView.class;
		super.__listenPostConstruct__();
	}
	
	/* Adjust */
	
	@Override @Transactional
	public Result adjust(Map<String, Long> adjustments,String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		ValidatorImpl.Resource.validateAdjust(adjustments, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		Collection<String> providedIdentifiers = adjustments.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());		
		
		// Validation of adjustable
		Collection<Object[]> notAdjustable = new ResourceImplAdjustableIsFalseReader().readByIdentifiers(new ArrayList<String>(providedIdentifiers), null);
		if(CollectionHelper.isNotEmpty(notAdjustable)) {
			throwablesMessages.add(String.format("%s %s non ajustable : %s",notAdjustable.size(),Resource.NAME_PLURAL, notAdjustable.stream().map(x -> (String)x[0]).collect(Collectors.joining(","))));
		}
		throwablesMessages.throwIfNotEmpty();
		
		// Validation of objects
		Collection<ResourceImpl> resources = entityManager.createNamedQuery(ResourceImpl.QUERY_READ_BY_IDENTIIFERS, ResourceImpl.class)
				.setParameter("identifiers", adjustments.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
				.getResultList();
		ValidatorImpl.validateIdentifiers(providedIdentifiers, FieldHelper.readSystemIdentifiersAsStrings(resources), throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		// Persist of objects
		LocalDateTime auditWhen = LocalDateTime.now();
		String auditIdentifier = generateAuditIdentifier();
		resources.forEach(resource -> {
			resource.getRevenue(Boolean.TRUE).setAdjustment(adjustments.get(resource.getIdentifier()));
			audit(resource,auditIdentifier,ADJUST_AUDIT_IDENTIFIER,auditWho,auditWhen);
			entityManager.merge(resource);
		});

		// Return of message
		result.close().setName(String.format("Ajustement de %s %s(s) par %s",resources.size(),Resource.NAME,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre de %s mise à jour : %s",Resource.NAME, resources.size()));
		return result;
	}
	
	/* Import */

	@Override
	Resource instantiateForImport(LegislativeActVersion legislativeActVersion, Object[] array) {
		return new ResourceImpl().setIdentifier((String)array[0]).setActVersion(legislativeActVersion).setActivityIdentifier((String)array[1]).setEconomicNatureIdentifier((String)array[2]).setRevenue(new RevenueImpl());
	}

	/* Copy */
	
	@Override
	public Result copy(String legislativeActVersionIdentifier, String legislativeActVersionSourceIdentifier,String auditWho) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* Event */
	
	public static final String EVENT_CHANNEL_IMPORT = "resourceImport";
	@ConsumeEvent(EVENT_CHANNEL_IMPORT)
    void listenImport(EventMessage message) {
		import_(message.identifier, Boolean.TRUE, message.auditWho, EntityManagerGetter.getInstance().get(), Boolean.TRUE);
    }
	
	@AllArgsConstructor @NoArgsConstructor
	public static class EventMessage {
		String identifier;
		String auditWho;
	}
}