package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.persistence.query.Query;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.query.RuntimeQueryBuilder;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @ci.gouv.dgbf.system.collectif.server.api.System @Unremovable
public class RuntimeQueryBuilderImpl extends RuntimeQueryBuilder.AbstractImpl implements Serializable {

	@Override
	protected void setTupleFieldsNamesIndexesFromFieldsNames(QueryExecutorArguments queryExecutorArguments, Query query) {
		Boolean amountSumable = queryExecutorArguments.getFilterBackup() == null ? null : ValueHelper.convertToBoolean(queryExecutorArguments.getFilterBackup().getFieldValue(Parameters.AMOUNT_SUMABLE));
		if(Boolean.TRUE.equals(amountSumable)) {
			query.setResultClass(Object[].class);
			query.setTupleFieldsNamesIndexesFromFieldsNames(ExpenditureImpl.FIELDS_AMOUNTS_SUMS);
		}else
			super.setTupleFieldsNamesIndexesFromFieldsNames(queryExecutorArguments, query);
	}
	
}