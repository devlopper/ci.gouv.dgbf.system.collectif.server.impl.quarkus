package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplAuditsReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD___AUDIT_WHO__,ExpenditureImpl.FIELD___AUDIT_FUNCTIONALITY__
				,ExpenditureImpl.FIELD___AUDIT_WHAT__,ExpenditureImpl.FIELD___AUDIT_WHEN__);
		return arguments;
	}
	
	@Override
	protected void __set__(ExpenditureImpl expenditure, Object[] array) {
		Integer index = 1;
		expenditure.set__auditWho__(getAsString(array, index++));
		expenditure.set__auditFunctionality__(getAsString(array, index++));
		expenditure.set__auditWhat__(getAsString(array, index++));
		expenditure.set__auditWhen__((LocalDateTime) array[index++]);
	}
	
}