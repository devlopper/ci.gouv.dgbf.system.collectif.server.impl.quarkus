package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersionPersistence;

@ApplicationScoped
public class BudgetaryActVersionPersistenceImpl extends AbstractSpecificPersistenceImpl<BudgetaryActVersion>  implements BudgetaryActVersionPersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public BudgetaryActVersionPersistenceImpl() {
		entityClass = BudgetaryActVersion.class;
		entityImplClass = BudgetaryActVersionImpl.class;
	}
}