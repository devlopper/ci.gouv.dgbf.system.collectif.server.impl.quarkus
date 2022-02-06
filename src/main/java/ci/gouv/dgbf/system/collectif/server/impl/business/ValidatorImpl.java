package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
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

import ci.gouv.dgbf.system.collectif.server.api.persistence.Exercise;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExercisePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
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

	static void validateLegislativeActVersionIdentifier(String legislativeActVersionIdentifier,ThrowablesMessages throwablesMessages) {
		throwablesMessages.addIfTrue("L'identifiant de la version du collectif est requis", StringHelper.isBlank(legislativeActVersionIdentifier));
	}
	
	static void validateAuditWho(String auditWho,ThrowablesMessages throwablesMessages) {
		throwablesMessages.addIfTrue("Le nom d'utilisateur est requis", StringHelper.isBlank(auditWho));
	}
	
	public static interface LegislativeAct {
		
		static Object[] validateCreateInputs(String code, String name, String exerciseIdentifier, String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(exerciseIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.Exercise.NAME, throwablesMessages);
			validateAuditWho(auditWho, throwablesMessages);
			ExerciseImpl exercise = StringHelper.isBlank(exerciseIdentifier) ? null : (ExerciseImpl) validateExistenceAndReturn(Exercise.class, exerciseIdentifier,List.of(ExerciseImpl.FIELD_IDENTIFIER,ExerciseImpl.FIELD_YEAR)
					, __inject__(ExercisePersistence.class), throwablesMessages);
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
		
		static void validateUpdateInProgressInputs(String legislativeActIdentifier,Boolean inProgress,String auditWho,ThrowablesMessages throwablesMessages) {
			validateIdentifier(legislativeActIdentifier,ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct.NAME, throwablesMessages);
			throwablesMessages.addIfTrue("La valeur <<en cours>> est requise", inProgress == null);
			validateAuditWho(auditWho, throwablesMessages);
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
	}

	public static interface Expenditure {
		
		static void validate(Object actionIdentifier,Expenditure expenditure,ThrowablesMessages throwablesMessages) {
			
		}
		
		static void validateAdjustmentsAvailable(Map<String,Long[]> adjustments,Collection<Object[]> arrays,Integer entryAuthorizationAvailableIndex,Integer paymentCreditAvailableIndex,ThrowablesMessages throwablesMessages) {
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
			throwablesMessages.addIfTrue("Nom d'utilisateur requis",StringHelper.isBlank(auditWho));		
		}
		
		static void validateImport(String legislativeActVersionIdentifier,String auditWho,ThrowablesMessages throwablesMessages) {
			validateLegislativeActVersionIdentifier(legislativeActVersionIdentifier, throwablesMessages);
			validateAuditWho(auditWho, throwablesMessages);		
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
	}
	
	public static interface GeneratedAct {
		
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