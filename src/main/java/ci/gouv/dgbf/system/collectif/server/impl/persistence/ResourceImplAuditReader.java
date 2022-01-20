package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

public class ResourceImplAuditReader extends ResourceImplAuditsAsStringsReader implements Serializable {

	@Override
	protected void __set__(ResourceImpl resource, Object[] array) {
		super.__set__(resource, array);
		resource.set__audit__(String.format(FORMAT, resource.get__auditFunctionality__(),resource.get__auditWho__(),resource.get__auditWhenAsString__()));
		resource.set__auditWho__(null);
		resource.set__auditFunctionality__(null);
		resource.set__auditWhat__(null);
		resource.set__auditWhen__(null);
	}
	
	public static final String FORMAT = "%s par %s le %s";
}