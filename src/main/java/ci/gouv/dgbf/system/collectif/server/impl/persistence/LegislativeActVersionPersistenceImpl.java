package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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
	
	@Override
	public LegislativeActVersion readUsingNamedQueryReadByIdentifier(String identifier,EntityManager entityManager) {
		try {
			return (entityManager == null ? this.entityManager : entityManager).createNamedQuery(LegislativeActVersionImpl.QUERY_READ_BY_IDENTIIFER,LegislativeActVersionImpl.class).setParameter(LegislativeActVersionImpl.FIELD_IDENTIFIER, identifier).getSingleResult();
		}catch(NoResultException exception) {
			return null;
		}	
	}
	
	public static void readAmounts(Collection<LegislativeActVersionImpl> legislativeActVersions) {
		if(CollectionHelper.isEmpty(legislativeActVersions))
			return;
		new LegislativeActVersionImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentExpectedAdjustmentExpectedAdjustmentMinusAdjustmentReader().readThenSet(legislativeActVersions, null);
		new LegislativeActVersionImplAmountsMovementIncludedReader().readThenSet(legislativeActVersions, null);
		legislativeActVersions.forEach(legislativeActVersion -> { legislativeActVersion.computeActualMinusMovementIncludedPlusAdjustment(); });
	}
}