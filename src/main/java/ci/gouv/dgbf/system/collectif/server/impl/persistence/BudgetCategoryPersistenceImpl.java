package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetCategory;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetCategoryPersistence;

@ApplicationScoped
public class BudgetCategoryPersistenceImpl extends AbstractSpecificPersistenceImpl<BudgetCategory>  implements BudgetCategoryPersistence,Serializable {

	public BudgetCategoryPersistenceImpl() {
		entityClass = BudgetCategory.class;
		entityImplClass = BudgetCategoryImpl.class;
	}
}