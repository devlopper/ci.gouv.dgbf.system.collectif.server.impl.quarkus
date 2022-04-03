package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @ci.gouv.dgbf.system.collectif.server.api.System @Unremovable
public class QueryResultMapperImpl extends org.cyk.utility.persistence.query.QueryResultMapper.AbstractImpl implements Serializable {
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T instantiateArray(Class<T> klass, Map<String, Integer> fieldsNamesIndexes, Object[] array) {
		if((Expenditure.class.equals(klass) || ExpenditureImpl.class.equals(klass)) && fieldsNamesIndexes.containsKey(ExpenditureImpl.FIELDS_AMOUNTS_SUMS)) {
			ExpenditureImpl expenditure = new ExpenditureImpl();
			ExpenditureQueryStringBuilder.Projection.setAmounts(expenditure,array,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,0);
			expenditure.getEntryAuthorization().setExpectedAdjustment(null);
			expenditure.getEntryAuthorization().setExpectedAdjustmentMinusAdjustment(null);
			return (T) expenditure;
		}
		return super.instantiateArray(klass, fieldsNamesIndexes, array);
	}
	
}