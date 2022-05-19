package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.server.view.MaterializedViewActualizer;

import ci.gouv.dgbf.system.collectif.server.api.business.RegulatoryActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActLegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureIncludedMovementView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImplExerciseYearReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImplFromDateAsTimestampReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImplLegislativeActFromDateAsTimestampDateAsTimestampReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImplIncludedLegislativeActJoinIdentifierCodeNameReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImplIncludedLegislativeActJoinIdentifierNameDateReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActLegislativeActVersionImpl;
import io.quarkus.vertx.ConsumeEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@ApplicationScoped
public class RegulatoryActBusinessImpl extends AbstractSpecificBusinessImpl<RegulatoryAct> implements RegulatoryActBusiness,Serializable {

	@Inject RegulatoryActPersistence persistence;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;
	@Inject RegulatoryActLegislativeActVersionPersistence regulatoryActLegislativeActVersionPersistence;
	@Inject MaterializedViewActualizer materializedViewActualizer;
	@Inject EntityManager entityManager;
	
	@Override
	public Result includeByLegislativeActVersionIdentifier(String legislativeActVersionIdentifier, String auditWho) {
		Result result = includeByLegislativeActVersionIdentifierInTransaction(legislativeActVersionIdentifier, auditWho);
		materializedViewActualizer.executeAsynchronously(ExpenditureIncludedMovementView.class);
		return result;
	}
	
	@Transactional
	Result includeByLegislativeActVersionIdentifierInTransaction(String legislativeActVersionIdentifier, String auditWho) {
		return includeByLegislativeActVersionIdentifierInTransaction(legislativeActVersionIdentifier, auditWho,entityManager, Boolean.FALSE);
	}
	
	Result includeByLegislativeActVersionIdentifierInTransaction(String legislativeActVersionIdentifier, String auditWho,EntityManager entityManager,Boolean isUserTransaction) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.RegulatoryAct.validateIncludeByLegislativeActVersionIdentifierInputs(legislativeActVersionIdentifier, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		LegislativeActVersion legislativeActVersion = (LegislativeActVersion) instances[0];
		if(Boolean.TRUE.equals(isUserTransaction))
			entityManager.getTransaction().begin();
		Integer count = includeByLegislativeActVersionIdentifier(legislativeActVersion,generateAuditIdentifier(), auditWho, INCLUDE_BY_LEGISLATIVE_ACT_IDENTIFIER_AUDIT_IDENTIFIER, LocalDateTime.now(), entityManager);
		if(Boolean.TRUE.equals(isUserTransaction))
			entityManager.getTransaction().commit();
		// Return of message
		result.close().setName(String.format("Inclusion de %s %s de %s par %s",count,RegulatoryAct.NAME_PLURAL,legislativeActVersion.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Inclusion de %s : %s",RegulatoryAct.NAME_PLURAL, count));
		return result;
	}
	
	public Integer includeByLegislativeActVersionIdentifier(LegislativeActVersion legislativeActVersion,String auditIdentifier, String auditWho, String auditFunctionality,LocalDateTime auditWhen, EntityManager entityManager) {
		Short year = ((LegislativeActVersionImpl)legislativeActVersion).getActDateYear();
		if(year == null)
			year = (Short) ArrayHelper.getElementAt(CollectionHelper.getFirst(new LegislativeActImplExerciseYearReader().setEntityManager(entityManager).readByIdentifiers(List.of(legislativeActVersion.getActIdentifier()), null)),1);
		if(year == null)
			LogHelper.logWarning(String.format("Impossible de déduire l'exercice du %s(%s)", LegislativeAct.NAME,legislativeActVersion.getActIdentifier()), getClass());
		Object[] dates = CollectionHelper.getFirst(new LegislativeActVersionImplLegislativeActFromDateAsTimestampDateAsTimestampReader().setEntityManager(entityManager).readByIdentifiers(List.of(legislativeActVersion.getIdentifier()), null));
		if(dates == null) {
			LogHelper.logWarning(String.format("Impossible de déduire la période de %s(%s)", LegislativeActVersion.NAME,legislativeActVersion.getIdentifier()), getClass());
			return null;
		}
		Collection<RegulatoryAct> regulatoryActs = CollectionHelper.cast(RegulatoryAct.class, entityManager.createNamedQuery(RegulatoryActImpl.QUERY_READ_WHERE_NOT_INCLUDED_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_BY_FROM_DATE_BY_TO_DATE,RegulatoryActImpl.class)
				.setParameter("fromDate", LegislativeActImplFromDateAsTimestampReader.buildDate((LocalDate) dates[2], year)).setParameter("toDate", dates[3])
				.setParameter("legislativeActVersionIdentifier", legislativeActVersion.getIdentifier()).getResultList());
		if(CollectionHelper.isEmpty(regulatoryActs))
			return null;
		Collection<Object[]> arrays = new RegulatoryActImplIncludedLegislativeActJoinIdentifierCodeNameReader().setEntityManager(entityManager).readByIdentifiers(FieldHelper.readSystemIdentifiersAsStrings(regulatoryActs)
				, Map.of(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier()));
		include(regulatoryActs, legislativeActVersion, arrays, Boolean.TRUE,auditIdentifier, auditWho, auditFunctionality, auditWhen, entityManager);
		return regulatoryActs.size();
	}
	
	@Override
	public Result include(Collection<String> identifiers, String legislativeActVersionIdentifier,Boolean existingIgnorable,String auditWho) {
		Result result = includeInTransaction(identifiers, legislativeActVersionIdentifier, existingIgnorable, auditWho);
		materializedViewActualizer.executeAsynchronously(ExpenditureIncludedMovementView.class);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	Result includeInTransaction(Collection<String> identifiers, String legislativeActVersionIdentifier,Boolean existingIgnorable,String auditWho) {
		//1 - Validate preconditions
		Object[] data = validate(identifiers, legislativeActVersionIdentifier, existingIgnorable,Boolean.TRUE,auditWho);
		Collection<Object[]> arrays = (Collection<Object[]>) data[0];
		Collection<RegulatoryAct> regulatoryActs = (Collection<RegulatoryAct>) data[1];
		LegislativeActVersion legislativeActVersion = (LegislativeActVersion) data[2];
		Result result = (Result) data[3];
		
		LocalDateTime auditWhen = LocalDateTime.now();
		include(regulatoryActs, legislativeActVersion, arrays, existingIgnorable,generateAuditIdentifier(), auditWho, INCLUDE_AUDIT_IDENTIFIER, auditWhen, entityManager);
		return result.close().log(getClass());
	}
	
	public void include(Collection<RegulatoryAct> regulatoryActs, LegislativeActVersion legislativeActVersion,Collection<Object[]> arrays,Boolean existingIgnorable,String auditIdentifier,String auditWho, String auditFunctionality,LocalDateTime auditWhen, EntityManager entityManager) {		
		//1 - Update to TRUE where FALSE OR NULL (NOT TRUE)
		__update__(arrays, Boolean.TRUE,auditIdentifier,auditWho,auditFunctionality,auditWhen,entityManager);
		//2 - Create
		__create__(arrays, regulatoryActs, legislativeActVersion,auditIdentifier,auditWho,auditFunctionality,auditWhen,entityManager);
	}

	@Override
	public Result include(String legislativeActIdentifier,Boolean existingIgnorable,String auditWho, String... identifiers) {
		return include(CollectionHelper.listOf(Boolean.TRUE, identifiers), legislativeActIdentifier,existingIgnorable,auditWho);
	}

	@Override
	public Result exclude(Collection<String> identifiers, String legislativeActVersionIdentifier,Boolean existingIgnorable,String auditWho) {
		Result result = excludeInTransaction(identifiers, legislativeActVersionIdentifier, existingIgnorable, auditWho);
		materializedViewActualizer.executeAsynchronously(ExpenditureIncludedMovementView.class);
		return result;
	}

	@Override
	public Result exclude(String legislativeActIdentifier,Boolean existingIgnorable,String auditWho, String... identifiers) {
		return exclude(CollectionHelper.listOf(Boolean.TRUE, identifiers), legislativeActIdentifier,existingIgnorable,auditWho);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	Result excludeInTransaction(Collection<String> identifiers, String legislativeActVersionIdentifier,Boolean existingIgnorable,String auditWho) {
		//1 - Validate preconditions
		Object[] data = validate(identifiers, legislativeActVersionIdentifier, existingIgnorable,Boolean.FALSE,auditWho);
		Collection<Object[]> arrays = (Collection<Object[]>) data[0];
		Result result = (Result) data[3];
		
		LocalDateTime auditWhen = LocalDateTime.now();
		//2 - Update to FALSE where TRUE
		__update__(arrays, Boolean.FALSE,generateAuditIdentifier(),auditWho,EXCLUDE_AUDIT_IDENTIFIER,auditWhen,entityManager);
		return result.close().log(getClass());
	}

	/**/
	
	private void __create__(Collection<Object[]> arrays,Collection<RegulatoryAct> regulatoryActs,LegislativeActVersion legislativeActVersion,String auditIdentifier,String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager) {
		Collection<Object[]> createsArrays = arrays.stream().filter(array -> StringHelper.isBlank((String)array[2])).collect(Collectors.toList());
		if(CollectionHelper.isNotEmpty(createsArrays)) {
			createsArrays.forEach(array -> {
				RegulatoryActLegislativeActVersionImpl regulatoryActLegislativeActVersion = new RegulatoryActLegislativeActVersionImpl();
				regulatoryActLegislativeActVersion.setRegulatoryAct(CollectionHelper.getFirst(regulatoryActs.stream().filter(ra -> ra.getIdentifier().equals(array[0])).collect(Collectors.toList())));
				regulatoryActLegislativeActVersion.setLegislativeActVersion(legislativeActVersion);
				regulatoryActLegislativeActVersion.setIdentifier(String.format("%s_%s", regulatoryActLegislativeActVersion.getRegulatoryAct().getIdentifier(),regulatoryActLegislativeActVersion.getLegislativeActVersion().getIdentifier()));
				regulatoryActLegislativeActVersion.setIncluded(Boolean.TRUE);
				audit(regulatoryActLegislativeActVersion,auditIdentifier, auditFunctionality, auditWho, auditWhen);
				entityManager.persist(regulatoryActLegislativeActVersion);
			});
		}
	}
	
	private void __update__(Collection<Object[]> arrays,Boolean include,String auditIdentifier,String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager) {
		Collection<Object[]> updatesArrays = arrays.stream().filter(array -> (Boolean.TRUE.equals(include) ? !Boolean.TRUE.equals(array[1]) : Boolean.TRUE.equals(array[1])) && StringHelper.isNotBlank((String)array[2])).collect(Collectors.toList());
		if(CollectionHelper.isNotEmpty(updatesArrays)) {
			Collection<RegulatoryActLegislativeActVersionImpl> updates = updatesArrays.stream().map(array -> entityManager.find(RegulatoryActLegislativeActVersionImpl.class, array[2])).collect(Collectors.toList());
			if(CollectionHelper.getSize(updatesArrays) != CollectionHelper.getSize(updates))
				throw new RuntimeException("Certains objets à mettre à jour non trouvés");
			for(RegulatoryActLegislativeActVersionImpl regulatoryActLegislativeActVersion : updates) {
				regulatoryActLegislativeActVersion.setIncluded(Boolean.TRUE.equals(include));
				audit(regulatoryActLegislativeActVersion,auditIdentifier, auditFunctionality, auditWho, auditWhen);
				entityManager.merge(regulatoryActLegislativeActVersion);
			}
		}
	}
	
	private Object[] validate(Collection<String> identifiers, String legislativeActVersionIdentifier,Boolean existingIgnorable,Boolean include,String auditWho) {
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		ValidatorImpl.RegulatoryAct.validateIncludeOrExcludeInputs(identifiers, legislativeActVersionIdentifier, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		Collection<Object[]> arrays = new RegulatoryActImplIncludedLegislativeActJoinIdentifierNameDateReader().readByIdentifiers(identifiers, Map.of(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersionIdentifier));
		LegislativeActVersion legislativeActVersion = legislativeActVersionPersistence.readOne(legislativeActVersionIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER,LegislativeActVersionImpl.FIELD_ACT_DATE));
		ValidatorImpl.RegulatoryAct.validateIncludeOrExclude(arrays,include,existingIgnorable,legislativeActVersion, throwablesMessages);
		Collection<RegulatoryAct> regulatoryActs = persistence.readManyByIdentifiers(identifiers,List.of(RegulatoryActImpl.FIELD_IDENTIFIER));
		
		ValidatorImpl.validateIdentifiers(identifiers, FieldHelper.readSystemIdentifiersAsStrings(regulatoryActs), throwablesMessages);
		ValidatorImpl.validateIdentifiers(List.of(legislativeActVersionIdentifier),legislativeActVersion == null ? null : List.of(legislativeActVersion.getIdentifier()), throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		Result result = new Result().setName(String.format("%sclusion des actes de gestion à une version du collectif",Boolean.TRUE.equals(include) ? "In" : "Ex")).open();
		return new Object[] {arrays,regulatoryActs,legislativeActVersion,result};
	}

	/* Event */
	
	public static final String EVENT_CHANNEL_INCLUDE_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER = "includeByLegislativeActVersionIdentifier";
	@ConsumeEvent(EVENT_CHANNEL_INCLUDE_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER)
    void listenIncludeByLegislativeActVersionIdentifier(EventMessage message) {
		includeByLegislativeActVersionIdentifierInTransaction(message.identifier,message.auditWho,EntityManagerGetter.getInstance().get(),Boolean.TRUE);
    }
	
	@AllArgsConstructor @NoArgsConstructor
	public static class EventMessage {
		String identifier;
		String auditWho;
	}
}