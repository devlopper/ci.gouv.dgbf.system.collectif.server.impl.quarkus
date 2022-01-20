package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Resource;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourcePersistence;

@ApplicationScoped
public class ResourcePersistenceImpl extends AbstractSpecificPersistenceImpl<Resource>  implements ResourcePersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public ResourcePersistenceImpl() {
		entityClass = Resource.class;
		entityImplClass = ResourceImpl.class;
	}
	
	public static void readAmounts(Collection<ResourceImpl> resources) {
		if(CollectionHelper.isEmpty(resources))
			return;
		new ResourceImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentReader().readThenSet(resources, null);
		new ResourceImplAmountsMovementIncludedReader().readThenSet(resources, null);
		resources.forEach(resource -> {
			resource.getRevenue(Boolean.TRUE).computeActualMinusMovementIncludedPlusAdjustment();
		});
	}
}