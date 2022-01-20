package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;

@ApplicationScoped
public class ExpenditurePersistenceImpl extends AbstractSpecificPersistenceImpl<Expenditure>  implements ExpenditurePersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public ExpenditurePersistenceImpl() {
		entityClass = Expenditure.class;
		entityImplClass = ExpenditureImpl.class;
	}
	
	public static void readAmounts(Collection<ExpenditureImpl> expenditures) {
		if(CollectionHelper.isEmpty(expenditures))
			return;
		new ExpenditureImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentReader().readThenSet(expenditures, null);
		new ExpenditureImplAmountsMovementIncludedReader().readThenSet(expenditures, null);
		expenditures.forEach(expenditure -> {
			expenditure.getEntryAuthorization(Boolean.TRUE).computeActualMinusMovementIncludedPlusAdjustment();
			expenditure.getPaymentCredit(Boolean.TRUE).computeActualMinusMovementIncludedPlusAdjustment();
		});
	}
}