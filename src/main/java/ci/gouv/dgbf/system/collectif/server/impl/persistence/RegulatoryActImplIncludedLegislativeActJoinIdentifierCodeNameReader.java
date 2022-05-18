package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class RegulatoryActImplIncludedLegislativeActJoinIdentifierCodeNameReader extends RegulatoryActImplIncludedReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("p",RegulatoryActLegislativeActVersionImpl.FIELD_IDENTIFIER);
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",RegulatoryActImpl.FIELD_CODE,RegulatoryActImpl.FIELD_NAME);		
		return arguments;
	}
	
	@Override
	public RegulatoryActImplIncludedLegislativeActJoinIdentifierCodeNameReader setEntityManager(EntityManager entityManager) {
		return (RegulatoryActImplIncludedLegislativeActJoinIdentifierCodeNameReader) super.setEntityManager(entityManager);
	}
	
	@Override
	protected void __set__(RegulatoryActImpl regulatoryAct, Object[] array, Integer index) {
		super.__set__(regulatoryAct, array, index);
		regulatoryAct.setCode(getAsString(array, index++));
		regulatoryAct.setName(getAsString(array, index++));
		regulatoryAct.setLegislativeActJoinIdentifier(getAsString(array, index++));
	}
}