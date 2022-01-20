package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActPersistence;

@ApplicationScoped
public class RegulatoryActPersistenceImpl extends AbstractSpecificPersistenceImpl<RegulatoryAct>  implements RegulatoryActPersistence,Serializable {

	public RegulatoryActPersistenceImpl() {
		entityClass = RegulatoryAct.class;
		entityImplClass = RegulatoryActImpl.class;
	}
}