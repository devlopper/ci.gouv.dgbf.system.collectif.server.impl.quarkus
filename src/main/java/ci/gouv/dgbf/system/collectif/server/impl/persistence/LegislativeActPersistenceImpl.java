package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;

@ApplicationScoped
public class LegislativeActPersistenceImpl extends AbstractSpecificPersistenceImpl<LegislativeAct>  implements LegislativeActPersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public LegislativeActPersistenceImpl() {
		entityClass = LegislativeAct.class;
		entityImplClass = LegislativeActImpl.class;
	}

	public static void readAmounts(Collection<LegislativeActImpl> legislativeActs) {
		if(CollectionHelper.isEmpty(legislativeActs))
			return;
		new LegislativeActImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentExpectedAdjustmentExpectedAdjustmentMinusAdjustmentReader().readThenSet(legislativeActs, null);
		new LegislativeActImplAmountsMovementIncludedReader().readThenSet(legislativeActs, null);
		legislativeActs.forEach(legislativeAct -> { legislativeAct.computeActualMinusMovementIncludedPlusAdjustment(); });
	}
}