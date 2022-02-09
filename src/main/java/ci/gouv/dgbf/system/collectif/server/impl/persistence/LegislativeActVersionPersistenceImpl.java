package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;

@ApplicationScoped
public class LegislativeActVersionPersistenceImpl extends AbstractSpecificPersistenceImpl<LegislativeActVersion>  implements LegislativeActVersionPersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public LegislativeActVersionPersistenceImpl() {
		entityClass = LegislativeActVersion.class;
		entityImplClass = LegislativeActVersionImpl.class;
	}
	
	public static void readAmounts(Collection<LegislativeActVersionImpl> legislativeActVersions) {
		if(CollectionHelper.isEmpty(legislativeActVersions))
			return;
		new LegislativeActVersionImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentExpectedAdjustmentExpectedAdjustmentMinusAdjustmentReader().readThenSet(legislativeActVersions, null);
		new LegislativeActVersionImplAmountsMovementIncludedReader().readThenSet(legislativeActVersions, null);
		legislativeActVersions.forEach(legislativeActVersion -> { legislativeActVersion.computeActualMinusMovementIncludedPlusAdjustment(); });
	}
}