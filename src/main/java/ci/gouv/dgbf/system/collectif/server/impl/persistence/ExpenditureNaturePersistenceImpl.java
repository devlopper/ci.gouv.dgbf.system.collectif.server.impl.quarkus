package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNature;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNaturePersistence;

@ApplicationScoped
public class ExpenditureNaturePersistenceImpl extends AbstractSpecificPersistenceImpl<ExpenditureNature>  implements ExpenditureNaturePersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public ExpenditureNaturePersistenceImpl() {
		entityClass = ExpenditureNature.class;
		entityImplClass = ExpenditureNatureImpl.class;
	}
}