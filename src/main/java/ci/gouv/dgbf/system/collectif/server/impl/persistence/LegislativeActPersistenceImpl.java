package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;

@ApplicationScoped
public class LegislativeActPersistenceImpl extends AbstractSpecificPersistenceImpl<LegislativeAct>  implements LegislativeActPersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public LegislativeActPersistenceImpl() {
		entityClass = LegislativeAct.class;
		entityImplClass = LegislativeActImpl.class;
	}
	
}