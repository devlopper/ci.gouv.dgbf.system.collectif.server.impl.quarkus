package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ResourceImplAuditsReader extends AbstractResourceImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ResourceImpl.FIELD_IDENTIFIER,ResourceImpl.FIELD___AUDIT_WHO__,ResourceImpl.FIELD___AUDIT_FUNCTIONALITY__
				,ResourceImpl.FIELD___AUDIT_WHAT__,ResourceImpl.FIELD___AUDIT_WHEN__);
		return arguments;
	}
	
	@Override
	protected void __set__(ResourceImpl resource, Object[] array) {
		Integer index = 1;
		resource.set__auditWho__(getAsString(array, index++));
		resource.set__auditFunctionality__(getAsString(array, index++));
		resource.set__auditWhat__(getAsString(array, index++));
		resource.set__auditWhen__((LocalDateTime) array[index++]);
	}
	
}