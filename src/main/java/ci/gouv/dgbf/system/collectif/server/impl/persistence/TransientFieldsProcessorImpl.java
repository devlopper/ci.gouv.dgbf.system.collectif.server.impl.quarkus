package ci.gouv.dgbf.system.collectif.server.impl.persistence;
import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @ci.gouv.dgbf.system.collectif.server.api.System @Unremovable
public class TransientFieldsProcessorImpl extends org.cyk.utility.persistence.server.TransientFieldsProcessorImpl implements Serializable {

	@Override
	protected void __process__(Class<?> klass,Collection<?> objects,Filter filter, Collection<String> fieldsNames) {
		if(Expenditure.class.equals(klass) || ExpenditureImpl.class.equals(klass))
			processExpenditures(CollectionHelper.cast(ExpenditureImpl.class, objects),fieldsNames);
		else if(BudgetaryActVersion.class.equals(klass) || BudgetaryActVersionImpl.class.equals(klass))
			processBudgetaryActVersions(CollectionHelper.cast(BudgetaryActVersionImpl.class, objects),fieldsNames);
		else
			super.__process__(klass,objects,filter, fieldsNames);
	}
	
	public void processBudgetaryActVersions(Collection<BudgetaryActVersionImpl> budgetaryActVersions,Collection<String> fieldsNames) {
		for(String fieldName : fieldsNames) {
			if(BudgetaryActVersionImpl.FIELD_BUDGETARY_ACT_IDENTIFIER.equals(fieldName))
				new BudgetaryActVersionImplBudgetaryActIdentifierReader().readThenSet(budgetaryActVersions, null);
		}
	}
	
	public void processExpenditures(Collection<ExpenditureImpl> expenditures,Collection<String> fieldsNames) {
		for(String fieldName : fieldsNames) {
			if(Expenditure.PROPERTY_AS_STRINGS.equals(fieldName))
				new ExpenditureImplAsStringsReader().readThenSet(expenditures, null);
			else if(Expenditure.PROPERTY_AMOUNTS.equals(fieldName))
				new ExpenditureImplAmountsReader().readThenSet(expenditures, null);
		}
	}
}