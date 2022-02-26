package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.RegulatoryActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActLegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImplIncludedLegislativeActJoinIdentifierCodeNameReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActLegislativeActVersionImpl;

@ApplicationScoped
public class RegulatoryActBusinessImpl extends AbstractSpecificBusinessImpl<RegulatoryAct> implements RegulatoryActBusiness,Serializable {

	@Inject RegulatoryActPersistence persistence;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;
	@Inject RegulatoryActLegislativeActVersionPersistence regulatoryActLegislativeActVersionPersistence;
	@Inject EntityManager entityManager;

	@Override
	public Result includeByLegislativeActVersionIdentifier(String legislativeActVersionIdentifier, String auditWho) {
		Result result = new Result().open();
		//ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		//Object[] instances = ValidatorImpl.RegulatoryAct.validateIncludeByLegislativeActVersionIdentifierInputs(legislativeActVersionIdentifier, auditWho, throwablesMessages);
		//throwablesMessages.throwIfNotEmpty();
		
		// Return of message
		//result.close().setName(String.format("Ajustement de %s %s(s) par %s",expenditures.size(),Expenditure.NAME,auditWho)).log(getClass());
		//result.addMessages(String.format("Nombre de %s mise à jour : %s",Expenditure.NAME, expenditures.size()));
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override @Transactional
	public Result include(Collection<String> identifiers, String legislativeActVersionIdentifier,Boolean existingIgnorable,String auditWho) {
		//1 - Validate preconditions
		Object[] data = validate(identifiers, legislativeActVersionIdentifier, existingIgnorable,Boolean.TRUE,auditWho);
		Collection<Object[]> arrays = (Collection<Object[]>) data[0];
		Collection<RegulatoryAct> regulatoryActs = (Collection<RegulatoryAct>) data[1];
		LegislativeActVersion legislativeActVersion = (LegislativeActVersion) data[2];
		Result result = (Result) data[3];
		
		LocalDateTime auditWhen = LocalDateTime.now();
		//2 - Update to TRUE where FALSE OR NULL (NOT TRUE)
		__update__(arrays, Boolean.TRUE,auditWho,INCLUDE_AUDIT_IDENTIFIER,auditWhen);
		//3 - Create
		__create__(arrays, regulatoryActs, legislativeActVersion,auditWho,INCLUDE_AUDIT_IDENTIFIER,auditWhen);
		return result.close().log(getClass());
	}

	@Override @Transactional
	public Result include(String legislativeActIdentifier,Boolean existingIgnorable,String auditWho, String... identifiers) {
		return include(CollectionHelper.listOf(Boolean.TRUE, identifiers), legislativeActIdentifier,existingIgnorable,auditWho);
	}

	@SuppressWarnings("unchecked")
	@Override @Transactional
	public Result exclude(Collection<String> identifiers, String legislativeActVersionIdentifier,Boolean existingIgnorable,String auditWho) {
		//1 - Validate preconditions
		Object[] data = validate(identifiers, legislativeActVersionIdentifier, existingIgnorable,Boolean.FALSE,auditWho);
		Collection<Object[]> arrays = (Collection<Object[]>) data[0];
		Result result = (Result) data[3];
		
		LocalDateTime auditWhen = LocalDateTime.now();
		//2 - Update to FALSE where TRUE
		__update__(arrays, Boolean.FALSE,auditWho,EXCLUDE_AUDIT_IDENTIFIER,auditWhen);
		return result.close().log(getClass());
	}

	@Override @Transactional
	public Result exclude(String legislativeActIdentifier,Boolean existingIgnorable,String auditWho, String... identifiers) {
		return exclude(CollectionHelper.listOf(Boolean.TRUE, identifiers), legislativeActIdentifier,existingIgnorable,auditWho);
	}

	/**/
	
	private void __create__(Collection<Object[]> arrays,Collection<RegulatoryAct> regulatoryActs,LegislativeActVersion legislativeActVersion,String auditWho,String auditFunctionality,LocalDateTime auditWhen) {
		Collection<Object[]> createsArrays = arrays.stream().filter(array -> StringHelper.isBlank((String)array[2])).collect(Collectors.toList());
		if(CollectionHelper.isNotEmpty(createsArrays)) {
			createsArrays.forEach(array -> {
				RegulatoryActLegislativeActVersionImpl regulatoryActLegislativeActVersion = new RegulatoryActLegislativeActVersionImpl();
				regulatoryActLegislativeActVersion.setRegulatoryAct(CollectionHelper.getFirst(regulatoryActs.stream().filter(ra -> ra.getIdentifier().equals(array[0])).collect(Collectors.toList())));
				regulatoryActLegislativeActVersion.setLegislativeActVersion(legislativeActVersion);
				regulatoryActLegislativeActVersion.setIdentifier(String.format("%s_%s", regulatoryActLegislativeActVersion.getRegulatoryAct().getIdentifier(),regulatoryActLegislativeActVersion.getLegislativeActVersion().getIdentifier()));
				regulatoryActLegislativeActVersion.setIncluded(Boolean.TRUE);
				audit(regulatoryActLegislativeActVersion, auditFunctionality, auditWho, auditWhen);
				entityManager.persist(regulatoryActLegislativeActVersion);
			});
		}
	}
	
	private void __update__(Collection<Object[]> arrays,Boolean include,String auditWho,String auditFunctionality,LocalDateTime auditWhen) {
		Collection<Object[]> updatesArrays = arrays.stream().filter(array -> (Boolean.TRUE.equals(include) ? !Boolean.TRUE.equals(array[1]) : Boolean.TRUE.equals(array[1])) && StringHelper.isNotBlank((String)array[2])).collect(Collectors.toList());
		if(CollectionHelper.isNotEmpty(updatesArrays)) {
			Collection<RegulatoryActLegislativeActVersionImpl> updates = updatesArrays.stream().map(array -> entityManager.find(RegulatoryActLegislativeActVersionImpl.class, array[2])).collect(Collectors.toList());
			if(CollectionHelper.getSize(updatesArrays) != CollectionHelper.getSize(updates))
				throw new RuntimeException("Certains objets à mettre à jour non trouvés");
			for(RegulatoryActLegislativeActVersionImpl regulatoryActLegislativeActVersion : updates) {
				regulatoryActLegislativeActVersion.setIncluded(Boolean.TRUE.equals(include));
				audit(regulatoryActLegislativeActVersion, auditFunctionality, auditWho, auditWhen);
				entityManager.merge(regulatoryActLegislativeActVersion);
			}
		}
	}
	
	private Object[] validate(Collection<String> identifiers, String legislativeActVersionIdentifier,Boolean existingIgnorable,Boolean include,String auditWho) {
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		ValidatorImpl.RegulatoryAct.validateIncludeOrExcludeInputs(identifiers, legislativeActVersionIdentifier, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		Collection<Object[]> arrays = new RegulatoryActImplIncludedLegislativeActJoinIdentifierCodeNameReader().readByIdentifiers(identifiers, Map.of(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersionIdentifier));
		ValidatorImpl.RegulatoryAct.validateIncludeOrExclude(arrays,include,existingIgnorable, throwablesMessages);
		Collection<RegulatoryAct> regulatoryActs = persistence.readManyByIdentifiers(identifiers,List.of(RegulatoryActImpl.FIELD_IDENTIFIER));
		LegislativeActVersion legislativeActVersion = legislativeActVersionPersistence.readOne(legislativeActVersionIdentifier,List.of(LegislativeActImpl.FIELD_IDENTIFIER));
		ValidatorImpl.validateIdentifiers(identifiers, FieldHelper.readSystemIdentifiersAsStrings(regulatoryActs), throwablesMessages);
		ValidatorImpl.validateIdentifiers(List.of(legislativeActVersionIdentifier),legislativeActVersion == null ? null : List.of(legislativeActVersion.getIdentifier()), throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		Result result = new Result().setName(String.format("%sclusion des actes de gestion à une version du collectif",Boolean.TRUE.equals(include) ? "In" : "Ex")).open();
		return new Object[] {arrays,regulatoryActs,legislativeActVersion,result};
	}
}