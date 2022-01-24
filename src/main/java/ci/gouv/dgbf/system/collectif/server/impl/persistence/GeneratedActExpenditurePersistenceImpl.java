package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActExpenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActExpenditurePersistence;

@ApplicationScoped
public class GeneratedActExpenditurePersistenceImpl extends AbstractSpecificPersistenceImpl<GeneratedActExpenditure>  implements GeneratedActExpenditurePersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public GeneratedActExpenditurePersistenceImpl() {
		entityClass = GeneratedActExpenditure.class;
		entityImplClass = GeneratedActExpenditureImpl.class;
	}
	
}