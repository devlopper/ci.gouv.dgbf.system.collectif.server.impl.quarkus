package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public abstract class AbstractActivityImplNamablesReader extends AbstractActivityImplReader implements Serializable {
	/*
	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.setTupleClass(null);
		arguments.getTuple(Boolean.TRUE).getStrings().clear();
		arguments.getTuple(Boolean.TRUE).add(String.format("%s t", getNamableEntityName()));
		arguments.getProjection(Boolean.TRUE).addFromTuple("e",getNamableIdentifierFieldNameFromExpenditure());
		arguments.getProjection(Boolean.TRUE).addFromTuple("t", AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_IDENTIFIER
				,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_CODE,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_NAME);
		arguments.getPredicate(Boolean.TRUE).getStrings().clear();
		arguments.getPredicate(Boolean.TRUE).ands(String.format("EXISTS(SELECT e FROM %s e WHERE e.%s = t.identifier AND e.%s IN :%s)",ExpenditureImpl.ENTITY_NAME,getNamableIdentifierFieldNameFromExpenditure(),ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER
				,Querier.PARAMETER_NAME_IDENTIFIERS));
		return arguments;
	}

	@Override
	public Collection<Object[]> readByIdentifiers(Collection<String> identifiers, Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		Collection<Object[]> arrays = super.readByIdentifiers(identifiers, parameters);
		if(arrays != null)
			arrays.forEach(array -> {
				System.out.println("AbstractActivityImplNamablesReader.readByIdentifiers() : "+Arrays.toString(array));
			});
		return arrays;
	}
	*/
	
	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ActivityImpl.FIELD_IDENTIFIER).addFromTuple("c",AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_IDENTIFIER
				,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_CODE,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_NAME);
		arguments.getTuple().addJoins(String.format("JOIN %s e ON e.%s = t.%s",ExpenditureImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER,ActivityImpl.FIELD_IDENTIFIER))
			.addJoins(String.format("JOIN %s c ON c.%s = e.%s",getNamableEntityName(),AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_IDENTIFIER,getNamableIdentifierFieldNameFromExpenditure()))
			;
		arguments.getGroup(Boolean.TRUE).add(String.format("c.%s,c.%s,c.%s,t.%s",AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_CODE
				,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_NAME,AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_IDENTIFIER,ActivityImpl.FIELD_IDENTIFIER));
		arguments.getOrder(Boolean.TRUE).asc("c", AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl.FIELD_CODE);
		return arguments;
	}
	
	
	protected abstract String getNamableEntityName();
	protected abstract String getNamableIdentifierFieldNameFromExpenditure();
	
	@Override
	protected Boolean isEntityHasOnlyArray(ActivityImpl entity) {
		return Boolean.FALSE;
	}
}