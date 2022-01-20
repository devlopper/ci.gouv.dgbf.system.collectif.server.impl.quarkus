package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public abstract class AbstractResourceActivityImplNamablesReader extends AbstractResourceActivityImplReader implements Serializable {
	
	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ResourceActivityImpl.FIELD_IDENTIFIER).addFromTuple("c",AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_IDENTIFIER
				,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_CODE,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_NAME);
		arguments.getTuple().addJoins(String.format("JOIN %s e ON e.%s = t.%s",ResourceImpl.ENTITY_NAME,ResourceImpl.FIELD_ACTIVITY_IDENTIFIER,ResourceActivityImpl.FIELD_IDENTIFIER))
			.addJoins(String.format("JOIN %s c ON c.%s = e.%s",getNamableEntityName(),AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_IDENTIFIER,getNamableIdentifierFieldNameFromExpenditure()))
			;
		arguments.getGroup(Boolean.TRUE).add(String.format("c.%s,c.%s,c.%s,t.%s",AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_CODE
				,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_NAME,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_IDENTIFIER,ResourceActivityImpl.FIELD_IDENTIFIER));
		arguments.getOrder(Boolean.TRUE).asc("c", AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_CODE);
		return arguments;
	}
	
	
	protected abstract String getNamableEntityName();
	protected abstract String getNamableIdentifierFieldNameFromExpenditure();
	
	@Override
	protected Boolean isEntityHasOnlyArray(ResourceActivityImpl entity) {
		return Boolean.FALSE;
	}
}