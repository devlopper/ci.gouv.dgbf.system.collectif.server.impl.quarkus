package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.computation.ComparisonOperator;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.business.Result;
import org.cyk.utility.persistence.EntityManagerGetter;
import org.cyk.utility.persistence.server.query.ReaderByCollection;
import org.cyk.utility.persistence.server.query.executor.field.CodeExecutor;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActivityImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EconomicNatureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EntryAuthorizationImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplAvailableMonitorableIsNotFalseReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImportableView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.FundingSourceImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LessorImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.PaymentCreditImpl;

@ApplicationScoped
public class ExpenditureBusinessImpl extends AbstractExpenditureResourceBusinessImpl<Expenditure> implements ExpenditureBusiness,Serializable {

	@Inject CodeExecutor codeExecutor;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;
	
	@Override
	void __listenPostConstruct__() {
		entityClass = Expenditure.class;
		entityViewClass = ExpenditureView.class;
		entityImportableClass = ExpenditureImportableView.class;
		super.__listenPostConstruct__();
	}
	
	@Override @Transactional
	public Result adjust(Map<String, Long[]> adjustments,String auditWho) {
		return adjust(adjustments,generateAuditIdentifier(), auditWho, ADJUST_AUDIT_IDENTIFIER);
	}
	
	private Collection<ExpenditureImpl> adjust(Map<String, Long[]> adjustments,String auditIdentifier,String auditWho,String auditFunctionality,Result result) {
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		ValidatorImpl.Expenditure.validateAdjust(adjustments, auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		// Validation of adjustments
		Collection<String> providedIdentifiers = adjustments.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
		
		Collection<Object[]> availablesMonitorables = new ExpenditureImplAvailableMonitorableIsNotFalseReader().readByIdentifiers(new ArrayList<String>(adjustments.keySet()), null);
		Collection<String> availablesMonitorablesIdentifiers = CollectionHelper.isEmpty(availablesMonitorables) ? null : availablesMonitorables.stream().map(array -> (String)array[0]).collect(Collectors.toList());
		Collection<Object[]> arrays = CollectionHelper.isEmpty(availablesMonitorablesIdentifiers) ? null : new ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader().readByIdentifiers(providedIdentifiers, null);
		
		//Collection<String> identifiers = CollectionHelper.isEmpty(arrays) ? null : arrays.stream().map(array -> (String)array[0]).collect(Collectors.toList());	
		//ValidatorImpl.validateIdentifiers(providedIdentifiers, identifiers, throwablesMessages);
		ValidatorImpl.Expenditure.validateAdjustmentsAvailable(adjustments, arrays, ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader.ENTRY_AUTHORIZATION_AVAILABLE_INDEX
				, ExpenditureImplEntryAuthorizationPaymentCreditAdjustmentAvailableReader.PAYMENT_CREDIT_AVAILABLE_INDEX, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		// Validation of objects
		Collection<ExpenditureImpl> expenditures = entityManager.createNamedQuery(ExpenditureImpl.QUERY_READ_BY_IDENTIIFERS, ExpenditureImpl.class)
				.setParameter("identifiers", adjustments.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()))
				.getResultList();
		ValidatorImpl.validateIdentifiers(providedIdentifiers, FieldHelper.readSystemIdentifiersAsStrings(expenditures), throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		// Persist of objects
		Collection<ExpenditureImpl> persisted = new ArrayList<>();
		LocalDateTime auditWhen = LocalDateTime.now();
		expenditures.forEach(expenditure -> {
			if(NumberHelper.compare(expenditure.getEntryAuthorization(Boolean.TRUE).getAdjustment(), adjustments.get(expenditure.getIdentifier())[0], ComparisonOperator.EQ) && 
					NumberHelper.compare(expenditure.getPaymentCredit(Boolean.TRUE).getAdjustment(), adjustments.get(expenditure.getIdentifier())[1], ComparisonOperator.EQ))
				return;
			persisted.add(expenditure);
			expenditure.getEntryAuthorization(Boolean.TRUE).setAdjustment(adjustments.get(expenditure.getIdentifier())[0]);
			expenditure.getPaymentCredit(Boolean.TRUE).setAdjustment(adjustments.get(expenditure.getIdentifier())[1]);
			audit(expenditure,auditIdentifier,auditFunctionality,auditWho,auditWhen);
			entityManager.merge(expenditure);
		});
		
		return persisted;
	}
	
	private Result adjust(Map<String, Long[]> adjustments,String auditIdentifier,String auditWho,String auditFunctionality) {
		Result result = new Result().open();
		Collection<ExpenditureImpl> expenditures = adjust(adjustments, auditIdentifier, auditWho, auditFunctionality, result);
		// Return of message
		result.close().setName(String.format("Ajustement de %s %s(s) par %s",expenditures.size(),Expenditure.NAME,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre de %s mise à jour : %s",Expenditure.NAME, expenditures.size()));
		return result;
	}
	
	@Override @Transactional
	public Result adjustByEntryAuthorizations(Map<String, Long> entryAuthorizations,String auditWho) {
		return adjust(entryAuthorizations == null ? null : Optional.ofNullable(entryAuthorizations).get().entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> new Long[] {entry.getValue(),entry.getValue()})),generateAuditIdentifier(),auditWho,ADJUST_BY_ENTRY_AUTHORIZATIONS_AUDIT_IDENTIFIER);
	}
	
	@Override @Transactional
	public Result copy(String legislativeActVersionIdentifier, String legislativeActVersionSourceIdentifier,String auditWho) {
		Result result = new Result().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		Object[] instances = ValidatorImpl.Expenditure.validateCopyAdjustmentsInputs(legislativeActVersionIdentifier, legislativeActVersionSourceIdentifier, auditWho, throwablesMessages, entityManager);
		throwablesMessages.throwIfNotEmpty();		
		Integer count = copy((LegislativeActVersionImpl)instances[0], (LegislativeActVersionImpl)instances[1],generateAuditIdentifier(),COPY_ADJUSTMENTS_AUDIT_IDENTIFIER, auditWho,LocalDateTime.now());		
		// Return of message
		result.close().setName(String.format("Copie de %s ajustement(s) de %s vers %s par %s",count,legislativeActVersionSourceIdentifier,legislativeActVersionIdentifier,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre d'ajustements copiés : %s", count));
		return result;
	}
	
	public Integer copy(LegislativeActVersionImpl legislativeActVersion, LegislativeActVersionImpl legislativeActVersionSource,String auditIdentifier,String auditWho,String auditFunctionality,LocalDateTime auditWhen) {
		@SuppressWarnings("unchecked")
		List<Object[]> arrays = entityManager.createNamedQuery(ExpenditureImpl.QUERY_READ_FOR_COPY_BY_ACT_VERSION_IDENTIFIER_BY_SOURCE_ACT_VERSION_IDENTIFIER).setParameter("legislativeActVersionIdentifier", legislativeActVersion.getIdentifier())
				.setParameter("legislativeActVersionSourceIdentifier", legislativeActVersionSource.getIdentifier()).getResultList();
		if(CollectionHelper.isNotEmpty(arrays)) {
			List<Expenditure> expenditures = (List<Expenditure>) new ReaderByCollection.AbstractImpl<String,Expenditure>() {
				@Override
				protected Collection<Expenditure> __read__(Collection<String> identifiers) {
					return CollectionHelper.cast(Expenditure.class, entityManager.createNamedQuery(ExpenditureImpl.QUERY_READ_BY_IDENTIIFERS, ExpenditureImpl.class).setParameter("identifiers", identifiers).getResultList());
				}
			}.read(arrays.stream().map(array -> (String)array[0]).collect(Collectors.toList()));
			
			expenditures.forEach(expenditure -> {
				Object[] array = arrays.stream().filter(a -> expenditure.getIdentifier().equals(a[0])).findFirst().get();
				((ExpenditureImpl)expenditure).getEntryAuthorization(Boolean.TRUE).setAdjustment((Long)array[1]);
				((ExpenditureImpl)expenditure).getPaymentCredit(Boolean.TRUE).setAdjustment((Long)array[2]);
				audit((ExpenditureImpl)expenditure,auditIdentifier, auditFunctionality, auditWho, auditWhen);
			});
			
			ExecutorService executorService = Executors.newFixedThreadPool(configuration.copy().processing().executor().thread().count());
			List<List<Expenditure>> batches = CollectionHelper.getBatches(expenditures, configuration.copy().processing().batch().size());
			batches.forEach(batch -> {
				executorService.execute(() -> {
					updateBatch(batch, EntityManagerGetter.getInstance().get(), Boolean.TRUE, null);
				});
			});
			shutdownExecutorService(executorService,configuration.copy().processing().executor().timeout().duration(),configuration.copy().processing().executor().timeout().unit());
		}
		return CollectionHelper.getSize(arrays);
	}
	
	/* Import */
	
	@Override
	Expenditure instantiateForImport(LegislativeActVersion legislativeActVersion, Object[] array) {
		return new ExpenditureImpl().setIdentifier((String)array[0]).setActVersion(legislativeActVersion).setActivityIdentifier((String)array[1]).setEconomicNatureIdentifier((String)array[2])
				.setFundingSourceIdentifier((String)array[3]).setLessorIdentifier((String)array[4]).setEntryAuthorization(new EntryAuthorizationImpl()).setPaymentCredit(new PaymentCreditImpl());
	}
	
	/* Load */
	
	@Override @Transactional
	public Result load(String legislativeActVersionIdentifier,Collection<Expenditure> expenditures,String auditWho) {
		LoadableVerificationResult result = new LoadableVerificationResult().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		ValidatorImpl.Expenditure.validateLoad(legislativeActVersionIdentifier,expenditures,auditWho, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		verifyLoadable(expenditures, result);
		
		Collection<Expenditure> loadables = expenditures.stream().filter(expenditure -> expenditure.isLoadable()).collect(Collectors.toList());
		LogHelper.log(String.format("%s %s(s) chargeable(s)", loadables.size(),Expenditure.NAME),Result.getLogLevel(), getClass());
		Map<String,Long[]> adjustments = null;
		Collection<ExpenditureImpl> loaded = null;
		if(CollectionHelper.isNotEmpty(loadables)) {
			Collection<String> expendituresIdentifiers = entityManager.createNamedQuery(ExpenditureImpl.QUERY_READ_IDENTIFIERS_BY_ACT_VERSION_IDENTIFIER_BY_CODES,String.class)
					.setParameter("actVersionIdentifier", legislativeActVersionIdentifier)
					.setParameter("codes", loadables.stream().map(loadable -> loadable.getActivityCodeEconomicNatureCodeFundingSourceCodeLessorCode()).collect(Collectors.toSet()))
					.getResultList();
			LogHelper.log(String.format("%s %s(s) collectées par codes", CollectionHelper.getSize(expendituresIdentifiers),Expenditure.NAME),Result.getLogLevel(),getClass());
			Collection<Object[]> codesArrays = null;
			if(CollectionHelper.isNotEmpty(expendituresIdentifiers)) {
				codesArrays = entityManager.createNamedQuery(ExpenditureView.QUERY_READ_IDENTIFIER_ACTIVITY_CODE_ECONOMIC_NATURE_CODE_FUNDING_SOURCE_CODE_LESSOR_CODE_BY_IDENTIFIERS,Object[].class)
						.setParameter("identifiers", expendituresIdentifiers).getResultList();
				if(CollectionHelper.isNotEmpty(codesArrays)) {
					for(String identifier : expendituresIdentifiers) {
						Object[] array = codesArrays.stream().filter(index -> identifier.equals(index[0])).findFirst().get();
						if(array == null)
							continue;
						Expenditure loadable = loadables.stream().filter(index -> array[1].equals(index.getActivityCode()) && array[2].equals(index.getEconomicNatureCode()) && array[3].equals(index.getFundingSourceCode()) 
								&& array[4].equals(index.getLessorCode())).findFirst().get();
						if(loadable == null)
							continue;
						if(adjustments == null)
							adjustments = new LinkedHashMap<>();
						adjustments.put(identifier, new Long[] {loadable.getEntryAuthorizationAdjustment(),ValueHelper.defaultToIfNull(loadable.getPaymentCreditAdjustment(),loadable.getEntryAuthorizationAdjustment())});
					}
				}
			}

			if(MapHelper.isNotEmpty(adjustments))
				loaded = adjust(adjustments, generateAuditIdentifier(), auditWho, LOAD_ADJUSTMENTS_AUDIT_IDENTIFIER, result);
		}
		
		// Return of message
		Integer count = loaded == null ? 0 : loaded.size();
		result.close().setName(String.format("Chargement de %s %s(s) par %s", count,Expenditure.NAME,auditWho)).log(getClass());
		result.addMessages(String.format("Nombre de %s chargée(s) : %s",Expenditure.NAME, count));
		return result;
	}
	
	@Override
	public LoadableVerificationResult verifyLoadable(String legislativeActVersionIdentifier,Collection<Expenditure> expenditures) {
		LoadableVerificationResult result = new LoadableVerificationResult().open();
		ThrowablesMessages throwablesMessages = new ThrowablesMessages();
		// Validation of inputs
		ValidatorImpl.Expenditure.validateVerifyLoadable(legislativeActVersionIdentifier,expenditures, throwablesMessages);
		throwablesMessages.throwIfNotEmpty();
		
		verifyLoadable(expenditures, result);
		
		if(CollectionHelper.isEmpty(result.getMessages()))
			result.setValue(String.format("Les %s dépenses sont chargeable",expenditures.size()));
		return result;
	}
	
	private void verifyLoadable(Collection<Expenditure> expenditures,LoadableVerificationResult result) {
		verifyUndefinedCodes(expenditures, ActivityImpl.class,ExpenditureImpl.FIELD_ACTIVITY_CODE, result);
		verifyUndefinedCodes(expenditures, EconomicNatureImpl.class, ExpenditureImpl.FIELD_ECONOMIC_NATURE_CODE, result);
		verifyUndefinedCodes(expenditures, FundingSourceImpl.class, ExpenditureImpl.FIELD_FUNDING_SOURCE_CODE, result);
		verifyUndefinedCodes(expenditures, LessorImpl.class, ExpenditureImpl.FIELD_LESSOR_CODE, result);
		verifyUndefinedAmount(expenditures, ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION_ADJUSTMENT, result);
		//verifyUndefinedAmount(expenditures, ExpenditureImpl.FIELD_PAYMENT_CREDIT_ADJUSTMENT, result);
		
		verifyUnknownCodes(expenditures, ActivityImpl.class,ExpenditureImpl.FIELD_ACTIVITY_CODE, result);
		verifyUnknownCodes(expenditures, EconomicNatureImpl.class, ExpenditureImpl.FIELD_ECONOMIC_NATURE_CODE, result);
		verifyUnknownCodes(expenditures, FundingSourceImpl.class, ExpenditureImpl.FIELD_FUNDING_SOURCE_CODE, result);
		verifyUnknownCodes(expenditures, LessorImpl.class, ExpenditureImpl.FIELD_LESSOR_CODE, result);
		
		verifyDuplicates(expenditures, result);
	}
	
	private Collection<Expenditure> getUndefinedCodes(Collection<Expenditure> expenditures,Boolean isNull) {
		return expenditures.stream().filter(
				expenditure -> {
					Boolean condition = StringHelper.isBlank(expenditure.getActivityCode()) || StringHelper.isBlank(expenditure.getEconomicNatureCode())
							|| StringHelper.isBlank(expenditure.getFundingSourceCode()) || StringHelper.isBlank(expenditure.getLessorCode());
					if(!Boolean.TRUE.equals(isNull))
						condition = !condition;
					return condition;}
				)
				.collect(Collectors.toList());
	}
		
	private void verifyUndefinedCodes(Collection<Expenditure> expenditures,Class<?> klass,String codeFieldName,LoadableVerificationResult result) {
		Collection<String> undefinedIdentifiers = expenditures.stream().filter(expenditure -> {
			Boolean value = StringHelper.isBlank((String)FieldHelper.read(expenditure, codeFieldName));
			if(!Boolean.TRUE.equals(expenditure.getHasUndefined()))
				expenditure.setHasUndefined(value);
			return value;
			}).map(expenditure -> expenditure.getIdentifier()).collect(Collectors.toSet());
		if(CollectionHelper.isEmpty(undefinedIdentifiers))
			return;
		if(ActivityImpl.class.equals(klass)) {
			result.getUndefined(Boolean.TRUE).setActivities(undefinedIdentifiers);
			result.addMessages(formatMessageActivitiesCodesUndefined(undefinedIdentifiers));
		}else if(EconomicNatureImpl.class.equals(klass)) {
			result.getUndefined(Boolean.TRUE).setEconomicsNatures(undefinedIdentifiers);
			result.addMessages(formatMessageEconomicsNaturesCodesUndefined(undefinedIdentifiers));
		}else if(FundingSourceImpl.class.equals(klass)) {
			result.getUndefined(Boolean.TRUE).setFundingsSources(undefinedIdentifiers);
			result.addMessages(formatMessageFundingsSourcesCodesUndefined(undefinedIdentifiers));
		}else if(LessorImpl.class.equals(klass)) {
			result.getUndefined(Boolean.TRUE).setLessors(undefinedIdentifiers);
			result.addMessages(formatMessageLessorsCodesUndefined(undefinedIdentifiers));
		}else
			result.addMessages(formatMessageUndefined("???", undefinedIdentifiers));
	}
	
	private void verifyUndefinedAmount(Collection<Expenditure> expenditures,String amountFieldName,LoadableVerificationResult result) {
		Collection<String> undefinedIdentifiers = expenditures.stream().filter(expenditure -> {
			Boolean value = FieldHelper.read(expenditure, amountFieldName) == null;
			if(!Boolean.TRUE.equals(expenditure.getHasUndefined()))
				expenditure.setHasUndefined(value);
			return value;
			}).map(expenditure -> expenditure.getIdentifier()).collect(Collectors.toSet());
		if(CollectionHelper.isEmpty(undefinedIdentifiers))
			return;
		if(ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION_ADJUSTMENT.equals(amountFieldName)) {
			result.addMessages(formatMessageEntryAuthorizationAdjustmentUndefined(undefinedIdentifiers));
			result.getUndefined(Boolean.TRUE).setEntriesAuthorizationsAdjustments(undefinedIdentifiers);
		}else if(ExpenditureImpl.FIELD_PAYMENT_CREDIT_ADJUSTMENT.equals(amountFieldName)) {
			result.addMessages(formatMessageCreditPaymentAdjustmentUndefined(undefinedIdentifiers));
			result.getUndefined(Boolean.TRUE).setPaymentsCreditsAdjustments(undefinedIdentifiers);
		}else
			result.addMessages(formatMessageUndefined("??? AMOUNT ???", undefinedIdentifiers));
		
	}
	
	private void verifyUnknownCodes(Collection<Expenditure> expenditures,Class<?> klass,String codeFieldName,LoadableVerificationResult result) {
		Collection<String> unexistingCodes = codeExecutor.getUnexisting(klass, expenditures.stream().map(expenditure -> (String)FieldHelper.read(expenditure, codeFieldName))
				.filter(code -> StringHelper.isNotBlank(code)).collect(Collectors.toSet()));
		if(CollectionHelper.isEmpty(unexistingCodes))
			return;
		expenditures.forEach(expenditure -> {
			if(!Boolean.TRUE.equals(expenditure.getHasUnknown()))
				expenditure.setHasUnknown(unexistingCodes.contains(FieldHelper.read(expenditure, codeFieldName)));
		});
		if(ActivityImpl.class.equals(klass)) {
			result.getUnknown(Boolean.TRUE).setActivities(unexistingCodes);
			result.addMessages(formatMessageActivitiesCodesDoNotExist(unexistingCodes));
		}else if(EconomicNatureImpl.class.equals(klass)) {
			result.getUnknown(Boolean.TRUE).setEconomicsNatures(unexistingCodes);
			result.addMessages(formatMessageEconomicsNaturesCodesDoNotExist(unexistingCodes));
		}else if(FundingSourceImpl.class.equals(klass)) {
			result.getUnknown(Boolean.TRUE).setFundingsSources(unexistingCodes);
			result.addMessages(formatMessageFundingsSourcesCodesDoNotExist(unexistingCodes));
		}else if(LessorImpl.class.equals(klass)) {
			result.getUnknown(Boolean.TRUE).setLessors(unexistingCodes);
			result.addMessages(formatMessageLessorsCodesDoNotExist(unexistingCodes));
		}else
			result.addMessages(formatMessageCodesDoNotExist("???", unexistingCodes));
	}
	
	private void verifyDuplicates(Collection<Expenditure> expenditures,LoadableVerificationResult result) {
		Collection<Expenditure> duplicates = null;
		Collection<String> codes = new ArrayList<>();
		for(Expenditure expenditure : getUndefinedCodes(expenditures, Boolean.FALSE)) {
			String code = expenditure.getActivityCode()+expenditure.getEconomicNatureCode()+expenditure.getFundingSourceCode()+expenditure.getLessorCode();
			if(codes.contains(code)) {
				if(duplicates == null)
					duplicates = new ArrayList<>();
				expenditure.setIsDuplicate(Boolean.TRUE);
				duplicates.add(expenditure);
			}else
				codes.add(code);
		}
		if(CollectionHelper.isEmpty(duplicates))
			return;
		Collection<String> identifiers = duplicates.stream().map(expenditure -> expenditure.getIdentifier()).filter(identifier -> StringHelper.isNotBlank(identifier)).collect(Collectors.toList());
		if(CollectionHelper.isEmpty(identifiers))
			return;
		result.setDuplicates(identifiers);
		result.addMessages(formatMessageDuplicates(duplicates));
	}
	
	/**/
	
	public static String formatMessageUndefined(String name,Collection<String> identifiers) {
		return String.format("%s dépense(s) ayant %s non défini : %s",identifiers.size(),name,StringHelper.concatenate(identifiers,","));
	}
	
	public static String formatMessageActivitiesCodesUndefined(Collection<String> identifiers) {
		return formatMessageUndefined("code activité", identifiers);
	}
	
	public static String formatMessageEconomicsNaturesCodesUndefined(Collection<String> identifiers) {
		return formatMessageUndefined("code nature économique", identifiers);
	}
	
	public static String formatMessageFundingsSourcesCodesUndefined(Collection<String> identifiers) {
		return formatMessageUndefined("code source de financement", identifiers);
	}
	
	public static String formatMessageLessorsCodesUndefined(Collection<String> identifiers) {
		return formatMessageUndefined("code bailleur", identifiers);
	}
	
	public static String formatMessageEntryAuthorizationAdjustmentUndefined(Collection<String> identifiers) {
		return formatMessageUndefined("ajustement autorisation d'engagement", identifiers);
	}
	
	public static String formatMessageCreditPaymentAdjustmentUndefined(Collection<String> identifiers) {
		return formatMessageUndefined("ajustement crédit de paiement", identifiers);
	}
	
	/**/
	
	public static String formatMessageActivitiesCodesDoNotExist(Collection<String> codes) {
		return formatMessageCodesDoNotExist("activités ", codes);
	}
	
	public static String formatMessageEconomicsNaturesCodesDoNotExist(Collection<String> codes) {
		return formatMessageCodesDoNotExist("natures économiques ", codes);
	}
	
	public static String formatMessageFundingsSourcesCodesDoNotExist(Collection<String> codes) {
		return formatMessageCodesDoNotExist("sources de financement ", codes);
	}
	
	public static String formatMessageLessorsCodesDoNotExist(Collection<String> codes) {
		return formatMessageCodesDoNotExist("bailleurs ", codes);
	}
	
	public static String formatMessageCodesDoNotExist(String name,Collection<String> codes) {
		return String.format("%s code(s) %sinexistant : %s",codes.size(),name,StringHelper.concatenate(codes,","));
	}
	
	public static String formatMessageCodesNull(Collection<Expenditure> expenditures) {
		return String.format("%s dépense(s) ayant code activité ou code nature économique ou code source de financement ou code bailleur non défini : %s",expenditures.size()
				,expenditures.stream().map(expenditure -> expenditure.getIdentifier()).filter(identifier -> StringHelper.isNotBlank(identifier)).collect(Collectors.joining(",")));
	}
	
	public static String formatMessageDuplicates(Collection<Expenditure> expenditures) {
		return String.format("%s dépense(s) en double : %s",expenditures.size(),expenditures.stream().map(expenditure -> expenditure.getIdentifier()).filter(identifier -> StringHelper.isNotBlank(identifier)).collect(Collectors.joining(",")));
	}
}