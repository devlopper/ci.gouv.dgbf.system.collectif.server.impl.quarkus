package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.klass.ClassHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Validator;

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

	public static interface Expenditure {
		
		static void validate(Object actionIdentifier,Expenditure expenditure,ThrowablesMessages throwablesMessages) {
			
		}
		
		static void validateAdjustmentsAvailable(Map<String,Long[]> adjustments,Collection<Object[]> arrays,Integer entryAuthorizationAvailableIndex,Integer paymentCreditAvailableIndex,ThrowablesMessages throwablesMessages) {
			adjustments.forEach((identifier,value)->{
				for(Object[] array : arrays) {
					if(identifier.equals(array[0])) {
						if(value == null || value.length < 2) {
							throwablesMessages.add(String.format("L'autorisation d'engagement et le crédit de paiement sont obligatoire pour la validation de la ligne %s", identifier));
						}else {
							addAvailableNotEnoughForAdjustmentIfTrue(identifier, "A.E.", 0, value, array, entryAuthorizationAvailableIndex, paymentCreditAvailableIndex, throwablesMessages);
							addAvailableNotEnoughForAdjustmentIfTrue(identifier, "C.P.", 1, value, array, entryAuthorizationAvailableIndex, paymentCreditAvailableIndex, throwablesMessages);
						}
					}
				}
			});
		}
		
		private static void addAvailableNotEnoughForAdjustmentIfTrue(String identifier,String amountName,Integer amountIndex,Long[] amounts,Object[] array,Integer entryAuthorizationAvailableIndex,Integer paymentCreditAvailableIndex,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue(String.format("La ligne %s à un disponible %s insuffisant(%s,%s)", identifier,amountName,amounts[amountIndex],array[entryAuthorizationAvailableIndex])
					, !Boolean.TRUE.equals(isAvailableEnoughForAdjustment((Long)array[entryAuthorizationAvailableIndex], amounts[amountIndex])));
		}
		
		static Boolean isAvailableEnoughForAdjustment(Long available,Long adjustment) {
			return NumberHelper.isGreaterThanOrEqualZero(NumberHelper.add(available,adjustment));
		}
		
		static void validateAdjust(Map<String, Long[]> adjustments,String userIdentifier,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue("Ajustements requis",MapHelper.isEmpty(adjustments));
			throwablesMessages.addIfTrue("Nom d'utilisateur requis",StringHelper.isBlank(userIdentifier));		
		}
	}
	
	public static interface Resource {
		
		static void validate(Object actionIdentifier,Resource resource,ThrowablesMessages throwablesMessages) {
			
		}

		static void validateAdjust(Map<String, Long> adjustments,String userIdentifier,ThrowablesMessages throwablesMessages) {
			throwablesMessages.addIfTrue("Ajustements requis",MapHelper.isEmpty(adjustments));
			throwablesMessages.addIfTrue("Nom d'utilisateur requis",StringHelper.isBlank(userIdentifier));		
		}
	}

	public static interface RegulatoryAct {
		
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
}