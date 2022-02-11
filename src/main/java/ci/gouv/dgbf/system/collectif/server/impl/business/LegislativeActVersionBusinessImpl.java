package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Result;
import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;

@ApplicationScoped
public class LegislativeActVersionBusinessImpl extends AbstractSpecificBusinessImpl<LegislativeActVersion> implements LegislativeActVersionBusiness,Serializable {

	@Inject EntityManager entityManager;
	@Inject LegislativeActVersionPersistence persistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ExpenditureBusiness expenditureBusiness;

	@Override @Transactional
	public Result create(String code, String name, Byte number, String legislativeActIdentifier, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.LegislativeActVersion.validateCreateInputs(code, name, number,legislativeActIdentifier, auditWho, throwablesMessages,entityManager);
		throwablesMessages.throwIfNotEmpty();
		//All inputs are fine
		LegislativeActVersionImpl legislativeActVersion = create(code, name, number, (LegislativeActImpl) instances[0], auditWho, null, null, entityManager);
		// Return of message
		result.close().setName(String.format("Création de %s par %s",legislativeActVersion.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Création de %s",legislativeActVersion.getName()));
		return result;
	}
	
	public LegislativeActVersionImpl create(String code, String name, Byte number, LegislativeActImpl legislativeAct, String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager) {
		if(StringHelper.isBlank(auditFunctionality))
			auditFunctionality = CREATE_AUDIT_IDENTIFIER;
		if(auditWhen == null)
			auditWhen = LocalDateTime.now();
		//Instantiate legislative act version
		LegislativeActVersionImpl legislativeActVersion = new LegislativeActVersionImpl().setCode(code).setName(name).setNumber(number).setAct(legislativeAct);
		//Derive blank attributes
		if(legislativeActVersion.getNumber() == null)
			legislativeActVersion.setNumber(NumberHelper.get(Byte.class,persistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_IDENTIFIER,legislativeAct.getIdentifier())),Byte.valueOf("0")));
		legislativeActVersion.setNumber(NumberHelper.get(Byte.class,legislativeActVersion.getNumber()+Byte.valueOf("1")));
		if(StringHelper.isBlank(legislativeActVersion.getCode()))
			legislativeActVersion.setCode(String.format(CODE_FORMAT, legislativeActVersion.getAct().getCode(),legislativeActVersion.getNumber()));		
		if(StringHelper.isBlank(legislativeActVersion.getName()))
			legislativeActVersion.setName(String.format(NAME_FORMAT, legislativeActVersion.getNumber(),legislativeActVersion.getAct().getName()));
		legislativeActVersion.setIdentifier(legislativeActVersion.getCode());
		audit(legislativeActVersion, auditFunctionality, auditWho, auditWhen);
		//Persist instance
		entityManager.persist(legislativeActVersion);
		entityManager.flush();
		//Import expenditures
		((ExpenditureBusinessImpl)expenditureBusiness).import_(legislativeActVersion, auditWho,auditFunctionality,auditWhen,entityManager);
		return legislativeActVersion;
	}
	
	@Override @Transactional
	public Result copy(String sourceIdentifier, String destinationIdentifier, CopyOptions options,String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.LegislativeActVersion.validateCopyInputs(sourceIdentifier,destinationIdentifier, auditWho, throwablesMessages,entityManager);
		throwablesMessages.throwIfNotEmpty();
		//All inputs are fine
		LegislativeActVersionImpl legislativeActVersionSource = (LegislativeActVersionImpl) instances[0];
		LegislativeActVersionImpl legislativeActVersionTarget = (LegislativeActVersionImpl) instances[1];
		
		copy(legislativeActVersionSource, legislativeActVersionTarget, options, auditWho, COPY_AUDIT_IDENTIFIER, LocalDateTime.now(), entityManager);
		
		// Return of message
		result.close().setName(String.format("Copie de %s vers %s par %s",legislativeActVersionSource.getName(),legislativeActVersionTarget.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Copie de %s vers %s",legislativeActVersionSource.getName(),legislativeActVersionTarget.getName()));
		return result;
	}
	
	public void copy(LegislativeActVersionImpl source, LegislativeActVersionImpl destination, CopyOptions options,String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager) {
		Long count = expenditurePersistence.count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,source.getIdentifier()));
		copyByOverWrite(source, destination, options, auditWho, auditFunctionality, auditWhen, entityManager, count, 1000);
	}
	
	public void copyByOverWrite(LegislativeActVersionImpl source, LegislativeActVersionImpl destination, CopyOptions options,String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager,Long count,Integer batchSize) {
		//1 - Copy adjustments
		List<Integer> batchSizes = NumberHelper.getProportions(count.intValue(), batchSize);		
		LogHelper.logInfo(String.format("Traitement par lot de %s. Nombre de lot = %s", batchSize,CollectionHelper.getSize(batchSizes)), getClass());
		if(batchSizes != null)
			for(Integer index =0; index < batchSizes.size(); index = index + 1) {
				Collection<ExpenditureImpl> expendituresDestinations = entityManager.createNamedQuery(ExpenditureImpl.QUERY_READ_BY_ACT_VERSION_IDENTIFIER, ExpenditureImpl.class).setParameter("actVersionIdentifier", destination.getIdentifier())
						.setFirstResult(index*batchSize).setMaxResults(batchSizes.get(index)).getResultList();
				LogHelper.logInfo(String.format("Traitement du lot %s/%s | %s",index+1,batchSizes.size(),CollectionHelper.getSize(expendituresDestinations)), getClass());
				if(CollectionHelper.isEmpty(expendituresDestinations))
					continue;
				Collection<ExpenditureImpl> expendituresSources = CollectionHelper.cast(ExpenditureImpl.class, expenditurePersistence.readMany(new QueryExecutorArguments()
						.addProjectionsFromStrings(ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,ExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER,ExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER,ExpenditureImpl.FIELD_LESSOR_IDENTIFIER
								,ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION,ExpenditureImpl.FIELD_PAYMENT_CREDIT)
						.addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,source.getIdentifier()
								,Parameters.ACTIVITIES_IDENTIFIERS,expendituresDestinations.stream().map(x -> x.getActivityIdentifier()).collect(Collectors.toSet())
								,Parameters.ECONOMIC_NATURES_IDENTIFIERS,expendituresDestinations.stream().map(x -> x.getEconomicNatureIdentifier()).collect(Collectors.toSet())
								,Parameters.FUNDING_SOURCES_IDENTIFIERS,expendituresDestinations.stream().map(x -> x.getFundingSourceIdentifier()).collect(Collectors.toSet())
								,Parameters.LESSORS_IDENTIFIERS,expendituresDestinations.stream().map(x -> x.getLessorIdentifier()).collect(Collectors.toSet())
								)));
				expendituresDestinations.forEach(expenditureDestination -> {
					Boolean found = null;
					if(expendituresSources != null)
						for(ExpenditureImpl expenditureSource : expendituresSources) {
							if(Boolean.TRUE.equals(ExpenditureImpl.areEqualByActivityEconomicNatureFundingSourceLessor(expenditureSource, expenditureDestination))) {
								expenditureDestination.setEntryAuthorization(expenditureSource.getEntryAuthorization());
								expenditureDestination.setPaymentCredit(expenditureSource.getPaymentCredit());
								found = Boolean.TRUE;
								break;
							}
						}
					if(found == null)
						LogHelper.logWarning(String.format("\t%s n'a pas été trouvé dans %s", expenditureDestination.getIdentifier(),source.getName()), getClass());
					audit(expenditureDestination, auditFunctionality, auditWho, auditWhen);
					entityManager.merge(expenditureDestination);
				});
				entityManager.flush();
				entityManager.clear();
				System.gc();
			}
		//2 - Copy regulatories acts
	}
	
	public void copyByMerge(LegislativeActVersionImpl source, LegislativeActVersionImpl destination, CopyOptions options,String auditWho,String auditFunctionality,LocalDateTime auditWhen,EntityManager entityManager) {
		
	}
	
	@Override @Transactional
	public Result duplicate(String identifier, String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.LegislativeActVersion.validateDuplicateInputs(identifier, auditWho, throwablesMessages,entityManager);
		throwablesMessages.throwIfNotEmpty();
		//All inputs are fine
		LocalDateTime auditWhen = LocalDateTime.now();
		LegislativeActVersionImpl legislativeActVersionSource = (LegislativeActVersionImpl) instances[0];
		LegislativeActVersionImpl legislativeActVersionDestination = create(null, null, null, entityManager.find(LegislativeActImpl.class, legislativeActVersionSource.getActIdentifier()), auditWho, DUPLICATE_AUDIT_IDENTIFIER, auditWhen, entityManager);
		
		copy(legislativeActVersionSource, legislativeActVersionDestination, null, auditWho, DUPLICATE_AUDIT_IDENTIFIER, auditWhen, entityManager);
		
		// Return of message
		result.close().setName(String.format("Duplication de %s par %s",legislativeActVersionSource.getName(),auditWho)).log(getClass());
		result.addMessages(String.format("Duplication de %s",legislativeActVersionSource.getName()));
		return result;
	}
	
	private static final String CODE_FORMAT = "%s_%s";
	private static final String NAME_FORMAT = "Version %s %s";
}