package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.klass.ClassHelper;
import org.cyk.utility.__kernel__.throwable.ThrowablesMessages;
import org.cyk.utility.business.Validator;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @ci.gouv.dgbf.system.collectif.server.api.System @Unremovable
public class ValidatorImpl extends Validator.AbstractImpl implements Serializable {

	@Override
	protected <T> void __validate__(Class<T> klass, T entity, Object actionIdentifier,ThrowablesMessages throwablesMessages) {
		super.__validate__(klass, entity, actionIdentifier, throwablesMessages);
		if(Boolean.TRUE.equals(ClassHelper.isInstanceOf(klass, Expenditure.class)))
			validate(actionIdentifier, (Expenditure) entity, throwablesMessages);
	}
	
	/**/
	
	private void validate(Object actionIdentifier,Expenditure expenditure,ThrowablesMessages throwablesMessages) {
		
	}
	
	public static void validateEntryAuthorizations(Object actionIdentifier,Map<String,Long> entryAuthorizations,ThrowablesMessages throwablesMessages) {
		
	}
}