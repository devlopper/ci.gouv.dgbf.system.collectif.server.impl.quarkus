package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.persistence.EntityManagerGetter;

import ci.gouv.dgbf.system.collectif.server.api.business.ResourceBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Resource;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EntryAuthorizationImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImportableView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.PaymentCreditImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImpl;

@ApplicationScoped
public class ResourceBusinessImpl extends AbstractExpenditureResourceBusinessImpl<Resource> implements ResourceBusiness,Serializable {

	@Inject EntityManager entityManager;
	@Inject ExpenditurePersistence expenditurePersistence;
	
	/* Adjust */
	
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
		result.close().setName(String.format("Ajustement de %s %s(s) par %s",resources.size(),Resource.NAME,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre de %s mise à jour : %s",Resource.NAME, resources.size()));
		return result;
	}
	
	/* Import */

	@Override
	public Result import_(String legislativeActVersionIdentifier, Boolean throwIfRunning, String auditWho) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result import_(String legislativeActVersionIdentifier, String auditWho) {
		// TODO Auto-generated method stub
		return null;
	}

	/* Copy */
	
	@Override
	public Result copy(String legislativeActVersionIdentifier, String legislativeActVersionSourceIdentifier,
			String auditWho) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String getImportAuditIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Boolean isImportRunning(LegislativeActVersion legislativeActVersion, EntityManager entityManager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String formatMessageImportIsRunning(LegislativeActVersion legislativeActVersion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void updateMaterializedView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	Long countImportable(LegislativeActVersion legislativeActVersion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	List<Object[]> readImportable(LegislativeActVersion legislativeActVersion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	List<Resource> instantiate(LegislativeActVersion legislativeActVersion, List<Object[]> arrays, String auditWho,
			String auditFunctionality, LocalDateTime auditWhen) {
		// TODO Auto-generated method stub
		return null;
	}
}