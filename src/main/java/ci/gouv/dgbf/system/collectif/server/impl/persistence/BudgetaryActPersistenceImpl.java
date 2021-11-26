package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActPersistence;

@ApplicationScoped
public class BudgetaryActPersistenceImpl extends AbstractSpecificPersistenceImpl<BudgetaryAct>  implements BudgetaryActPersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public BudgetaryActPersistenceImpl() {
		entityClass = BudgetaryAct.class;
		entityImplClass = BudgetaryActImpl.class;
	}
	
}