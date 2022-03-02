package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.klass.ClassHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
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
		if(Boolean.TRUE.equals(ClassHelper.isInstanceOf(klass, Expenditure.class)))
			Expenditure.validate(actionIdentifier, (Expenditure) entity, throwablesMessages);
	}

	/**/
	
	static void validateAuditWho(String auditWho,ThrowablesMessages throwablesMessages) {
		throwablesMessages.addIfTrue("Le nom d'utilisateur est requis", StringHelper.isBlank(auditWho));
	}
	
	public static interface LegislativeAct {
		
		static Object[] validateCreateInputs(String code, String name, String exerciseIdentifier,LocalDate date, String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(exerciseIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.Exercise.NAME, throwablesMessages);
			throwablesMessages.addIfTrue("La date est requise", date == null);
			validateAuditWho(auditWho, throwablesMessages);
			ExerciseImpl exercise = StringHelper.isBlank(exerciseIdentifier) ? null : (ExerciseImpl) validateExistenceAndReturn(Exercise.class, exerciseIdentifier,List.of(ExerciseImpl.FIELD_IDENTIFIER,ExerciseImpl.FIELD_YEAR)
					, __inject__(ExercisePersistence.class), throwablesMessages);
			if(exercise != null && exercise.getYear() != null) {
				throwablesMessages.addIfTrue(String.format("L'année(%s) de la date doit être égale à l'année(%s) de l'exercice",date.getYear(),exercise.getYear()), date.getYear() != exercise.getYear().intValue());
			}
			return new Object[] {exercise};
		}
		
		static Object[] validateUpdateDefaultVersionInputs(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateAuditWho(auditWho, throwablesMessages);
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
			validateAuditWho(auditWho, throwablesMessages);
			LegislativeActImpl legislativeAct = StringHelper.isBlank(legislativeActIdentifier) ? null : validateExistenceAndReturn(LegislativeActImpl.class, legislativeActIdentifier,null,null, throwablesMessages,entityManager);			
			if(legislativeAct != null)
				throwablesMessages.addIfTrue(String.format("%s %sest %s en cours",legislativeAct.getName(),inProgress ? "" : "n'",inProgress ? "déja" : "pas"), legislativeAct.getInProgress() != null && inProgress == legislativeAct.getInProgress());			
			return new Object[] {legislativeAct};
		}
	}
	
	public static interface LegislativeActVersion {
		
		static Object[] validateCreateInputs(String code,String name,Byte number,String legislativeActIdentifier,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(legislativeActIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct.NAME, throwablesMessages);
			validateAuditWho(auditWho, throwablesMessages);
			
			LegislativeActImpl legislativeAct = StringHelper.isBlank(legislativeActIdentifier) ? null
					: (LegislativeActImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct.class, legislativeActIdentifier,List.of(LegislativeActImpl.FIELD_IDENTIFIER,LegislativeActImpl.FIELD_CODE
							,LegislativeActImpl.FIELD_NAME)
					, __inject__(LegislativeActPersistence.class), throwablesMessages,entityManager);
			return new Object[] {legislativeAct};
		}
		
		static Object[] validateSetAsLegislativeActDefaultVersion(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateAuditWho(auditWho, throwablesMessages);
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(legislativeActVersionIdentifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, legislativeActVersionIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_CODE,LegislativeActVersionImpl.FIELD_NAME,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
			return new Object[] {legislativeActVersion};
		}
		
		static void validateGenerateInputs(String identifier,String auditWho,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue("L'identifiant de la version du collectif est requis", StringHelper.isBlank(identifier));
			validateAuditWho(auditWho, throwablesMessages);
		}
		
		static void validateGenerate(Collection<Object[]> arrays,Boolean existingIgnorable,String auditWho,ThrowablesMessages throwablesMessages) {
			validateAuditWho(auditWho, throwablesMessages);
		}
		
		static Object[] validateGenerateActsInputs(String identifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(identifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateAuditWho(auditWho, throwablesMessages);
			
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(identifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, identifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
			return new Object[] {legislativeActVersion};
		}
		
		static Object[] validateDuplicateInputs(String identifier,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(identifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateAuditWho(auditWho, throwablesMessages);
			
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(identifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, identifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_NAME,LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages,entityManager);
			return new Object[] {legislativeActVersion};
		}
		
		static Object[] validateCopyInputs(String sourceIdentifier,String destinationIdentifier,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(sourceIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateIdentifier(destinationIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateAuditWho(auditWho, throwablesMessages);
			
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
		
		static void validate(Object actionIdentifier,Expenditure expenditure,ThrowablesMessages throwablesMessages) {
			
		}
		
		static void validateAdjustmentsAvailable(Map<String,Long[]> adjustments,Collection<Object[]> arrays,Integer entryAuthorizationAvailableIndex,Integer paymentCreditAvailableIndex,ThrowablesMessages throwablesMessages) {
			if(MapHelper.isEmpty(adjustments) || CollectionHelper.isEmpty(arrays))
				return;
			//Collection<Object[]> availablesMonitorables = new ExpenditureImplAvailableMonitorableIsNotFalseReader().readByIdentifiers(new ArrayList<String>(adjustments.keySet()), null);
			//Collection<String> availablesMonitorablesIdentifiers = CollectionHelper.isEmpty(availablesMonitorables) ? null : availablesMonitorables.stream().map(array -> (String)array[0]).collect(Collectors.toList());
			adjustments.forEach((identifier,value)->{
				//if(availablesMonitorablesIdentifiers == null || !availablesMonitorablesIdentifiers.contains(identifier))
				//	return;
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
			throwablesMessages.addIfTrue("Nom d'utilisateur requis",StringHelper.isBlank(auditWho));		
		}
		
		static Object[] validateImportInputs(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateAuditWho(auditWho, throwablesMessages);
			LegislativeActVersionImpl legislativeActVersion = StringHelper.isBlank(legislativeActVersionIdentifier) ? null
					: (LegislativeActVersionImpl) validateExistenceAndReturn(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.class, legislativeActVersionIdentifier,List.of(LegislativeActVersionImpl.FIELD_IDENTIFIER
							,LegislativeActVersionImpl.FIELD_NAME)
					, __inject__(LegislativeActVersionPersistence.class), throwablesMessages);
			return new Object[] {legislativeActVersion};
		}
		
		static void validateImport(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateImportNotRunning(legislativeActVersion, throwablesMessages, entityManager);
		}
		
		static Boolean validateImportNotRunning(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			if(!isImportRunning(legislativeActVersion, entityManager))
				return Boolean.TRUE;
			throwablesMessages.add(formatMessageImportIsRunning(legislativeActVersion));
			return Boolean.FALSE;
		}
		
		static Boolean isImportRunning(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion,EntityManager entityManager) {
			return ExpenditureBusinessImpl.IMPORT_RUNNING.contains(legislativeActVersion.getIdentifier());
		}
		
		static String formatMessageImportIsRunning(ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion legislativeActVersion) {
			return String.format("%s de %s en cours d'importation",ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure.NAME ,legislativeActVersion.getName());
		}
		
		static Object[] validateCopyAdjustmentsInputs(String legislativeActVersionIdentifier, String legislativeActVersionSourceIdentifier,String auditWho,ThrowablesMessages throwablesMessages,EntityManager entityManager) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateIdentifier(legislativeActVersionSourceIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateAuditWho(auditWho, throwablesMessages);
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
			validateAuditWho(auditWho, throwablesMessages);
		}
	}

	public static interface RegulatoryAct {
		static void validateIncludeOrExcludeInputs(Collection<String> identifiers, String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue("Les identifiants des actes de gestion sont requis", CollectionHelper.isEmpty(identifiers));
			throwablesMessages.addIfTrue("L'identifiant de la version du collectif est requis", StringHelper.isBlank(legislativeActVersionIdentifier));
			validateAuditWho(auditWho, throwablesMessages);
		}
		
		static void validateIncludeOrExclude(Collection<Object[]> arrays,Boolean include,Boolean existingIgnorable,ThrowablesMessages throwablesMessages) {
			if(Boolean.TRUE.equals(existingIgnorable))
				return;
			if(CollectionHelper.isEmpty(arrays))
				return;
			Collection<Object[]> includedOrExcluded = arrays.stream().filter(array -> Boolean.TRUE.equals(include) ? Boolean.TRUE.equals(array[1]) : (array[1] == null || Boolean.FALSE.equals(array[1]))).collect(Collectors.toList());
			if(CollectionHelper.isEmpty(includedOrExcluded))
				return;
			throwablesMessages.add(String.format("Les %s suivant sont déja %s : %s",ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct.NAME_PLURAL,Boolean.TRUE.equals(include) ? "inclus" : "exclus"
				, includedOrExcluded.stream().map(array -> (String)array[3]+" "+array[4]).collect(Collectors.joining(","))));
		}
		
		static Object[] validateIncludeByLegislativeActVersionIdentifierInputs(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(legislativeActVersionIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion.NAME, throwablesMessages);
			validateAuditWho(auditWho, throwablesMessages);
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
			validateAuditWho(auditWho, throwablesMessages);
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
			validateAuditWho(auditWho, throwablesMessages);
		}
		
		static void validateDeleteInputs(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue("L'identifiant de la version du collectif est requis", StringHelper.isBlank(legislativeActVersionIdentifier));
			validateAuditWho(auditWho, throwablesMessages);
		}
		
		static void validateGenerate(Collection<Object[]> arrays,Boolean existingIgnorable,String auditWho,ThrowablesMessages throwablesMessages) {
			validateAuditWho(auditWho, throwablesMessages);
		}
	}
}