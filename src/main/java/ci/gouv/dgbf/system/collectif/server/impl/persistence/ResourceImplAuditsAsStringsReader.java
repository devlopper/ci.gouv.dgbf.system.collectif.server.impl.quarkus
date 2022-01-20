package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.time.TimeHelper;

public class ResourceImplAuditsAsStringsReader extends ResourceImplAuditsReader implements Serializable {

	@Override
	protected void __set__(ResourceImpl resource, Object[] array) {
		super.__set__(resource, array);
		resource.set__auditWhenAsString__(TimeHelper.formatLocalDateTime(resource.get__auditWhen__()));
		resource.set__auditWhen__(null);
	}
	
}