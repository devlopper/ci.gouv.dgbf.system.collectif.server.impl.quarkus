package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.klass.ClassHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.__kernel__.time.TimeHelper;
import org.cyk.utility.business.Validator;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Exercise;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExercisePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExerciseImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @ci.gouv.dgbf.system.collectif.server.api.System @Unremovable
public class ValidatorImpl extends Validator.AbstractImpl implements Serializable {

	@Override
	protected <T> void __validate__(Class<T> klass, T entity, Object actionIdentifier,ThrowablesMessages throwablesMessages) {
		super.__validate__(klass, entity, actionIdentifier, throwablesMessages);
		if(Boolean.TRUE.equals(ClassHelper.isInstanceOf(klass, ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure.class)))
			Expenditure.validate(actionIdentifier, (ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure) entity, throwablesMessages);
	}

	/**/
	
	static Object[] validateImportInputs(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
		validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
		Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
		LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(legislativeActVersionIdentifier) ? null
				: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, legislativeActVersionIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
						,LegislativeActVersionImpl.FIELD_NAME)
				, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
		return new Object[] {legislativeActVersion};
	}
	
	static void validateImport(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,Class<?> entityClass,Set<String> running,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager
			,Boolean throwIfRunning) {
		validateImportNotRunning(legislativeActVersion,entityClass,running, throwablesMessages, entityManager,throwIfRunning);
	}
	
	static Boolean validateImportNotRunning(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,Class<?> entityClass,Set<String> running,ThrowablesMessages throwablesMessages,EntityManager entityManager
			,Boolean throwIfRunning) {
		if(throwIfRunning == null)
			throwIfRunning = Boolean.FALSE;
		Boolean isImportRunning = isImportRunning(legislativeActVersion,running, entityManager);
		if(!isImportRunning || !Boolean.TRUE.equals(throwIfRunning))
			return Boolean.TRUE;
		throwablesMessages.add(formatMessageImportIsRunning(legislativeActVersion,entityClass));
		return Boolean.FALSE;
	}
	
	static Boolean isImportRunning(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,Set<String> running,EntityManager entityManager) {
		return running.contains(legislativeActVersion.getIdentifier());
	}
	
	static String formatMessageImportIsRunning(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,Class<?> entityClass) {
		return String.format("%s de %s en cours d'importation",FieldHelper.readStatic(entityClass,"NAME") ,legislativeActVersion.getName());
	}
	
	public static interface LegislativeAct {
		
		static Object[] validateCreateInputs(String code, String name, String exerciseIdentifier,LocalDate date, String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(exerciseIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.Exercise.NAME, throwablesMessages);
			throwablesMessages.addIfTrue("La date est requise", date == null);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			ExerciseImpl exercise = StringHelper.isBlank(exerciseIdentifier) ? null : (ExerciseImpl) validateExistenceAndReturn(Exercise.class, exerciseIdentifier,List.of(ExerciseImpl.FIELD_IDENTIFIER,ExerciseImpl.FIELD_YEAR)
					, __inject__(ExercisePersistence.class), throwablesMessages);
			if(exercise != null && exercise.getYear() != null) {
				throwablesMessages.addIfTrue(String.format("L'année(%s) de la date doit être égale à l'année(%s) de l'exercice",date.getYear(),exercise.getYear()), date.getYear() != exercise.getYear().intValue());
			}
			return new Object[] {exercise};
		}
		
		static Object[] validateUpdateDefaultVersionInputs(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(legislativeActVersionIdentifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, legislativeActVersionIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_CODE,LegislativeActVersionImpl.FIELD_NAME,LegislativeActVersionImpl.FIELD_ACT)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
			if(legislativeActVersion != null) {
				throwablesMessages.addIfTrue(String.format("%s %s n'est pas lié à un collectif",ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, legislativeActVersion.getName())
						, legislativeActVersion.getAct() == null);
				if(legislativeActVersion.getAct().getDefaultVersion() != null) {
					throwablesMessages.addIfTrue(String.format("%s est déja la version par défaut de %s", legislativeActVersion.getAct().getDefaultVersion().getName(),legislativeActVersion.getAct().getName())
							, legislativeActVersionIdentifier.equals(legislativeActVersion.getAct().getDefaultVersion().getIdentifier()));
				}
			}
			return new Object[] {legislativeActVersion};
		}
		
		static Object[] validateUpdateInProgressInputs(String legislativeActIdentifier,Boolean inProgress,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(legislativeActIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct.NAME, throwablesMessages);
			throwablesMessages.addIfTrue("La valeur <<en cours>> est requise", inProgress == null);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			LegislativeActImpl legislativeAct = StringHelper.isBlank(legislativeActIdentifier) ? null : validateExistenceAndReturn(LegislativeActImpl.class, legislativeActIdentifier,null,null, throwablesMessages,entityManager);			
			if(legislativeAct != null)
				throwablesMessages.addIfTrue(String.format("%s %sest %s en cours",legislativeAct.getName(),inProgress ? "" : "n'",inProgress ? "déja" : "pas"), legislativeAct.getInProgress() != null && inProgress == legislativeAct.getInProgress());			
			return new Object[] {legislativeAct};
		}
	}
	
	public static interface LegislativeActVersion {
		
		static Object[] validateCreateInputs(String code,String name,Byte number,String legislativeActIdentifier,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(legislativeActIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct.NAME, throwablesMessages);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			
			LegislativeActImpl legislativeAct = StringHelper.isBlank(legislativeActIdentifier) ? null
					: (LegislativeActImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct.class, legislativeActIdentifier,List.of(LegislativeActImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_CODE
							,LegislativeActImpl.FIELD_NAME)
					, __inject__(LegislativeActPersistence.class), throwablesMessages,entityManager);
			return new Object[] {legislativeAct};
		}
		
		static Object[] validateSetAsLegislativeActDefaultVersion(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(legislativeActVersionIdentifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, legislativeActVersionIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_CODE,LegislativeActVersionImpl.FIELD_NAME,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
			return new Object[] {legislativeActVersion};
		}
		
		static Object[] validateUpdateAdjustableInputs(String identifier,Boolean adjustable,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(identifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			throwablesMessages.addIfTrue("La valeur <<ajustable>> est requise", adjustable == null);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(identifier) ? null : validateExistenceAndReturn(LegislativeActVersionImpl.class, identifier,null,null, throwablesMessages,entityManager);			
			if(legislativeActVersion != null)
				throwablesMessages.addIfTrue(String.format("%s %sest %s ajustable",legislativeActVersion.getName(),adjustable ? "" : "n'",adjustable ? "déja" : "pas"), legislativeActVersion.getAdjustable() != null && adjustable == legislativeActVersion.getAdjustable());			
			return new Object[] {legislativeActVersion};
		}
		
		static void validateGenerateInputs(String identifier,String auditWho,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue("L'identifiant de la version du collectif est requis", StringHelper.isBlank(identifier));
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
		}
		
		static void validateGenerate(Collection<Object[]> arrays,Boolean existingIgnorable,String auditWho,ThrowablesMessages throwablesMessages) {
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
		}
		
		static Object[] validateGenerateActsInputs(String identifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(identifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(identifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, identifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
			return new Object[] {legislativeActVersion};
		}
		
		static Object[] validateDuplicateInputs(String identifier,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(identifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(identifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, identifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_NAME,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages,entityManager);
			return new Object[] {legislativeActVersion};
		}
		
		static Object[] validateCopyInputs(String sourceIdentifier,String destinationIdentifier,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(sourceIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateIdentifier(destinationIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			
			LegislativeActVersionImpl legislativeActVersionSource = StringHelper.isBlank(sourceIdentifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, sourceIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_NAME,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages,entityManager);
			
			LegislativeActVersionImpl legislativeActVersionDestination = StringHelper.isBlank(destinationIdentifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, destinationIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_NAME,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages,entityManager);
	
			throwablesMessages.addIfTrue(String.format("Les %s (%s,%s) doivent appartenir au même %s", ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME_PLURAL,sourceIdentifier,destinationIdentifier
					,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct.NAME), legislativeActVersionSource != null && legislativeActVersionDestination != null 
					&& !legislativeActVersionSource.getActIdentifier().equals(legislativeActVersionDestination.getActIdentifier()));
				
			return new Object[] {legislativeActVersionSource,legislativeActVersionDestination};
		}
	}

	public static interface Expenditure {
		
		static void validate(Object actionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure expenditure,ThrowablesMessages throwablesMessages) {
			
		}
		
		static void validateLoad(String legislativeActVersionIdentifier,Collection<ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure> expenditures,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			throwablesMessages.addIfTrue("Dépenses requises",CollectionHelper.isEmpty(expenditures));
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
		}
		
		static void validateVerifyLoadable(String legislativeActVersionIdentifier,Collection<ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure> expenditures,ThrowablesMessages throwablesMessages) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			throwablesMessages.addIfTrue("Dépenses requises",CollectionHelper.isEmpty(expenditures));
		}
		
		static void validateAdjustmentsAvailable(Map<String,Long[]> adjustments,Collection<Object[]> arrays,Integer entryAuthorizationAvailableIndex,Integer paymentCreditAvailableIndex,ThrowablesMessages throwablesMessages) {
			if(MapHelper.isEmpty(adjustments) || CollectionHelper.isEmpty(arrays))
				return;
			adjustments.forEach((identifier,value)->{
				for(Object[] array : arrays) {
					if(identifier.equals(array[0])) {
						if(value == null || value.length < 2) {
							throwablesMessages.add(String.format("L'autorisation d'engagement et le crédit de paiement sont obligatoire pour la validation de la %s %s",ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure.NAME, identifier));
						}else {
							addAvailableNotEnoughForAdjustmentIfTrue(identifier, "A.E.", 0, value, array, entryAuthorizationAvailableIndex, paymentCreditAvailableIndex, throwablesMessages);
							addAvailableNotEnoughForAdjustmentIfTrue(identifier, "C.P.", 1, value, array, entryAuthorizationAvailableIndex, paymentCreditAvailableIndex, throwablesMessages);
						}
					}
				}
			});
		}
		
		private static void addAvailableNotEnoughForAdjustmentIfTrue(String identifier,String amountName,Integer amountIndex,Long[] amounts,Object[] array,Integer entryAuthorizationAvailableIndex,Integer paymentCreditAvailableIndex,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue(String.format("La %s %s à un disponible %s insuffisant(%s,%s)",ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure.NAME, identifier,amountName,amounts[amountIndex],array[entryAuthorizationAvailableIndex])
					, !Boolean.TRUE.equals(isAvailableEnoughForAdjustment((Long)array[entryAuthorizationAvailableIndex], amounts[amountIndex])));
		}
		
		static Boolean isAvailableEnoughForAdjustment(Long available,Long adjustment) {
			return NumberHelper.isGreaterThanOrEqualZero(NumberHelper.add(available,adjustment));
		}
		
		static void validateAdjust(Map<String, Long[]> adjustments,String auditWho,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue("Ajustements requis",MapHelper.isEmpty(adjustments));
			if(adjustments != null)
				adjustments.entrySet().forEach(entry -> {
					for(Integer index = 0; index < entry.getValue().length; index = index +1)
						throwablesMessages.addIfTrue(String.format("L'ajustement %s de %s doit être défini",index == 0 ? "de l'autorisation d'engagement" : "du crédit de paiement",entry.getKey()),entry.getValue()[index] == null);
					//throwablesMessages.addIfTrue(String.format("L'ajustement de l'autorisation d'engagement de %s doit être défini",entry.getKey()),entry.getValue()[0] == null);
					//throwablesMessages.addIfTrue(String.format("L'ajustement du crédit de paiement de %s doit être défini",entry.getKey()),entry.getValue()[1] == null);
				});
			throwablesMessages.addIfTrue("Nom d'utilisateur requis",StringHelper.isBlank(auditWho));		
		}
		
		static Object[] validateImportInputs(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(legislativeActVersionIdentifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, legislativeActVersionIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_NAME)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
			return new Object[] {legislativeActVersion};
		}
		
		static void validateImport(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,Set<String> running,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateImportNotRunning(legislativeActVersion,running, throwablesMessages, entityManager);
		}
		
		static Boolean validateImportNotRunning(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,Set<String> running,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			if(!isImportRunning(legislativeActVersion,running, entityManager))
				return Boolean.TRUE;
			throwablesMessages.add(formatMessageImportIsRunning(legislativeActVersion));
			return Boolean.FALSE;
		}
		
		static Boolean isImportRunning(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,Set<String> running,EntityManager entityManager) {
			return running.contains(legislativeActVersion.getIdentifier());
		}
		
		static String formatMessageImportIsRunning(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion) {
			return String.format("%s de %s en cours d'importation",ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure.NAME ,legislativeActVersion.getName());
		}
		
		static Object[] validateCopyAdjustmentsInputs(String legislativeActVersionIdentifier, String legislativeActVersionSourceIdentifier,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateIdentifier(legislativeActVersionSourceIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(legislativeActVersionIdentifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, legislativeActVersionIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_NAME)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
			LegislativeActVersionImpl legislativeActVersionSource = StringHelper.isBlank(legislativeActVersionSourceIdentifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, legislativeActVersionSourceIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_NAME)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
			return new Object[] {legislativeActVersion,legislativeActVersionSource};
		}
	}
	
	public static interface Resource {
		
		static void validate(Object actionIdentifier,Resource resource,ThrowablesMessages throwablesMessages) {
			
		}

		static void validateAdjust(Map<String, Long> adjustments,String auditWho,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue("Ajustements requis",MapHelper.isEmpty(adjustments));
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
		}
	}

	public static interface RegulatoryAct {
		static void validateIncludeOrExcludeInputs(Collection<String> identifiers, String legislativeActVersionIdentifier,Boolean comprehensively,String auditWho,ThrowablesMessages throwablesMessages) {
			if(!Boolean.TRUE.equals(comprehensively))
				throwablesMessages.addIfTrue("Les identifiants des actes de gestion sont requis", CollectionHelper.isEmpty(identifiers));
			throwablesMessages.addIfTrue("L'identifiant de la version du collectif est requis", StringHelper.isBlank(legislativeActVersionIdentifier));
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
		}
		
		static void validateIncludeOrExcludeInputs(Collection<String> identifiers, String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIncludeOrExcludeInputs(identifiers, legislativeActVersionIdentifier, Boolean.FALSE, auditWho, throwablesMessages);
		}
		
		static void validateSetAsIncludedOrExcludedInputs(Collection<String> identifiers, String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIncludeOrExcludeInputs(identifiers, legislativeActVersionIdentifier, Boolean.TRUE, auditWho, throwablesMessages);
		}
		
		static void validateIncludeOrExclude(Collection<Object[]> arrays,Boolean include,Boolean existingIgnorable,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,ThrowablesMessages throwablesMessages) {
			if(CollectionHelper.isEmpty(arrays) || Boolean.TRUE.equals(existingIgnorable))
				return;
			if(Boolean.TRUE.equals(include)) {
				if(legislativeActVersion.getActDate() == null)
					throwablesMessages.add(String.format("La date du %s est requise", ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct.NAME));
				arrays.forEach(array -> {
					if(array[4] == null)
						throwablesMessages.add(String.format("La date de %s est requise", array[3]));
				});
				
				if(legislativeActVersion.getActDate() != null) {
					Collection<Object[]> dateGreaterThanActDate = arrays.stream().filter(array -> array[4] != null && ((LocalDate)array[4]).isAfter(legislativeActVersion.getActDate())).collect(Collectors.toList());
					if(CollectionHelper.isNotEmpty(dateGreaterThanActDate))
						throwablesMessages.add(String.format("Les %s suivant ont une date supérieure à %s : %s",ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct.NAME_PLURAL
							, TimeHelper.formatLocalDate(legislativeActVersion.getActDate())
							, dateGreaterThanActDate.stream().map(array -> (String)array[3]).collect(Collectors.joining(","))));
				}
			}
			
			Collection<Object[]> includedOrExcluded = arrays.stream().filter(array -> Boolean.TRUE.equals(include) ? Boolean.TRUE.equals(array[1]) : (array[1] == null || Boolean.FALSE.equals(array[1]))).collect(Collectors.toList());
			if(CollectionHelper.isEmpty(includedOrExcluded))
				return;
			throwablesMessages.add(String.format("Les %s suivant sont déja %s : %s",ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct.NAME_PLURAL,Boolean.TRUE.equals(include) ? "inclus" : "exclus"
				, includedOrExcluded.stream().map(array -> (String)array[3]).collect(Collectors.joining(","))));
		}
		
		static void validateSetAsIncludedOrExcluded(Collection<Object[]> arrays,Boolean included,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,ThrowablesMessages throwablesMessages) {
			validateIncludeOrExclude(arrays, included, Boolean.TRUE, legislativeActVersion, throwablesMessages);
		}
		
		static Object[] validateIncludeByLegislativeActVersionIdentifierInputs(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(legislativeActVersionIdentifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, legislativeActVersionIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_NAME,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER,LegislativeActVersionImpl.FIELD_ACT_DATE)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
			return new Object[] {legislativeActVersion};
		}
	}
	
	public static interface GeneratedAct {
		
		static Object[] generateByLegislativeActVersionIdentifierInputs(String identifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(identifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(identifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, identifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_CODE,LegislativeActVersionImpl.FIELD_NAME,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
			return new Object[] {legislativeActVersion};
		}
		
		static void generateByLegislativeActVersionIdentifier(LegislativeActVersionImpl legislativeActVersion,String auditWho,ThrowablesMessages throwablesMessages) {
			Long generatedCount = __inject__(GeneratedActPersistence.class).count(new QueryExecutorArguments().addFilterFieldsValues(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersion.getIdentifier()));			
			throwablesMessages.addIfTrue(String.format("La génération des actes de %s a déja été faite", legislativeActVersion.getName()),NumberHelper.isGreaterThanZero(generatedCount));
		}
		
		static void validateGenerateInputs(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue("L'identifiant de la version du collectif est requis", StringHelper.isBlank(legislativeActVersionIdentifier));
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
		}
		
		static void validateDeleteInputs(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue("L'identifiant de la version du collectif est requis", StringHelper.isBlank(legislativeActVersionIdentifier));
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
		}
		
		static void validateGenerate(Collection<Object[]> arrays,Boolean existingIgnorable,String auditWho,ThrowablesMessages throwablesMessages) {
			Validator.getInstance().validateAuditWho(auditWho, throwablesMessages);
		}
	}
}