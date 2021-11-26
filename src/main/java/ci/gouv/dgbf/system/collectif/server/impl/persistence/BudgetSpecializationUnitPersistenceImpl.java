package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;

@ApplicationScoped
public class BudgetSpecializationUnitPersistenceImpl extends AbstractSpecificPersistenceImpl<BudgetSpecializationUnit>  implements BudgetSpecializationUnitPersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public BudgetSpecializationUnitPersistenceImpl() {
		entityClass = BudgetSpecializationUnit.class;
		entityImplClass = BudgetSpecializationUnitImpl.class;
	}
	
}