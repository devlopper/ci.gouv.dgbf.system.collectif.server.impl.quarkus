package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceActivity;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceActivityPersistence;

@ApplicationScoped
public class ResourceActivityPersistenceImpl extends AbstractSpecificPersistenceImpl<ResourceActivity>  implements ResourceActivityPersistence,Serializable {

	public ResourceActivityPersistenceImpl() {
		entityClass = ResourceActivity.class;
		entityImplClass = ResourceActivityImpl.class;
	}
}