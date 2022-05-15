package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Resource;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @ci.gouv.dgbf.system.collectif.server.api.System @Unremovable
public class QueryResultMapperImpl extends org.cyk.utility.persistence.query.QueryResultMapper.AbstractImpl implements Serializable {
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T instantiateArray(Class<T> klass, Map<String, Integer> fieldsNamesIndexes, Object[] array) {
		if((Expenditure.class.equals(klass) || ExpenditureImpl.class.equals(klass)) && (fieldsNamesIndexes.containsKey(ExpenditureImpl.FIELDS_AMOUNTS_SUMS) 
				|| fieldsNamesIndexes.containsKey(ExpenditureImpl.FIELDS_AMOUNTS_WITHOUT_INCLUDED_MOVEMENT_AND_AVAILABLE) || fieldsNamesIndexes.containsKey(ExpenditureImpl.FIELDS_AMOUNTS_WITH_INCLUDED_MOVEMENT_ONLY)
				|| fieldsNamesIndexes.containsKey(ExpenditureImpl.FIELDS_AMOUNTS_WITH_AVAILABLE_ONLY)) || fieldsNamesIndexes.containsKey(ExpenditureImpl.FIELDS_AMOUNTS_WITH_INCLUDED_MOVEMENT_AND_AVAILABLE_ONLY)) {
			ExpenditureImpl expenditure = new ExpenditureImpl();
			if(fieldsNamesIndexes.containsKey(ExpenditureImpl.FIELDS_AMOUNTS_WITHOUT_INCLUDED_MOVEMENT_AND_AVAILABLE))
				ExpenditureQueryStringBuilder.Projection.Amounts.set(expenditure,new ExpenditureQueryStringBuilder.Projection.Amounts.SetArguments().setArray(array).setAdjustment(Boolean.TRUE).setView(Boolean.TRUE)
						.setAdjustmentLessThanZero(Boolean.TRUE).setAdjustmentGreaterThanZero(Boolean.TRUE)
						.setIndex(0));	
				//ExpenditureQueryStringBuilder.Projection.Amounts.set(expenditure,array,Boolean.TRUE,null,Boolean.TRUE,Boolean.FALSE,Boolean.FALSE,0);
			else if(fieldsNamesIndexes.containsKey(ExpenditureImpl.FIELDS_AMOUNTS_WITH_INCLUDED_MOVEMENT_ONLY))
				ExpenditureQueryStringBuilder.Projection.Amounts.set(expenditure,array,Boolean.FALSE,null,Boolean.FALSE,Boolean.TRUE,Boolean.FALSE,0);
			else if(fieldsNamesIndexes.containsKey(ExpenditureImpl.FIELDS_AMOUNTS_WITH_AVAILABLE_ONLY))
				ExpenditureQueryStringBuilder.Projection.Amounts.set(expenditure,array,Boolean.FALSE,null,Boolean.FALSE,Boolean.FALSE,Boolean.TRUE,0);
			else if(fieldsNamesIndexes.containsKey(ExpenditureImpl.FIELDS_AMOUNTS_WITH_INCLUDED_MOVEMENT_AND_AVAILABLE_ONLY))
				ExpenditureQueryStringBuilder.Projection.Amounts.set(expenditure,array,Boolean.FALSE,null,Boolean.FALSE,Boolean.TRUE,Boolean.TRUE,0);
			else
				ExpenditureQueryStringBuilder.Projection.Amounts.set(expenditure,array,Boolean.TRUE,null,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,0);
			return (T) expenditure;
		}
		if((Resource.class.equals(klass) || ResourceImpl.class.equals(klass)) && fieldsNamesIndexes.containsKey(ResourceImpl.FIELDS_AMOUNTS_SUMS)) {
			ResourceImpl resource = new ResourceImpl();
			ResourceQueryStringBuilder.Projection.Amounts.set(resource,array,Boolean.TRUE,0);
			return (T) resource;
		}
		return super.instantiateArray(klass, fieldsNamesIndexes, array);
	}
}