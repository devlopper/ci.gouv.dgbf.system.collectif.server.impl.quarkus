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
		if((Expenditure.class.equals(klass) || ExpenditureImpl.class.equals(klass)) && fieldsNamesIndexes.containsKey(ExpenditureImpl.FIELDS_AMOUNTS_SUMS)) {
			ExpenditureImpl expenditure = new ExpenditureImpl();
			ExpenditureQueryStringBuilder.Projection.Amounts.set(expenditure,array,Boolean.TRUE,null,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,0);
			expenditure.getEntryAuthorization().setExpectedAdjustment(null);
			expenditure.getEntryAuthorization().setExpectedAdjustmentMinusAdjustment(null);
			return (T) expenditure;
		}
		if((Resource.class.equals(klass) || ResourceImpl.class.equals(klass)) && fieldsNamesIndexes.containsKey(ResourceImpl.FIELDS_AMOUNTS_SUMS)) {
			ResourceImpl resource = new ResourceImpl();
			ResourceQueryStringBuilder.Projection.Amounts.set(resource,array,Boolean.TRUE,0);
			resource.getRevenue().setExpectedAdjustment(null);
			resource.getRevenue().setExpectedAdjustmentMinusAdjustment(null);
			return (T) resource;
		}
		return super.instantiateArray(klass, fieldsNamesIndexes, array);
	}
}