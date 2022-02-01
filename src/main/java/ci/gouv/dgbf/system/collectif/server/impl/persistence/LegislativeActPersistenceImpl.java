package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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

	@Override
	public LegislativeAct readUsingNamedQueryReadByIdentifier(String identifier) {
		try {
			return entityManager.createNamedQuery(LegislativeActImpl.QUERY_READ_BY_IDENTIIFER,LegislativeActImpl.class).setParameter(LegislativeActImpl.FIELD_IDENTIFIER, identifier).getSingleResult();
		}catch(NoResultException exception) {
			return null;
		}	
	}

	public static void readAmounts(Collection<LegislativeActImpl> legislativeActs) {
		if(CollectionHelper.isEmpty(legislativeActs))
			return;
		new LegislativeActImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentReader().readThenSet(legislativeActs, null);
		new LegislativeActImplAmountsMovementIncludedReader().readThenSet(legislativeActs, null);
		legislativeActs.forEach(legislativeAct -> { legislativeAct.computeActualMinusMovementIncludedPlusAdjustment(); });
	}
}