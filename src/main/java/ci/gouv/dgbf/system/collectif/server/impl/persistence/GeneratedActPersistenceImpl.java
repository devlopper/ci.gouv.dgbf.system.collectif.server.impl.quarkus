package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActPersistence;

@ApplicationScoped
public class GeneratedActPersistenceImpl extends AbstractSpecificPersistenceImpl<GeneratedAct>  implements GeneratedActPersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public GeneratedActPersistenceImpl() {
		entityClass = GeneratedAct.class;
		entityImplClass = GeneratedActImpl.class;
	}
	
}