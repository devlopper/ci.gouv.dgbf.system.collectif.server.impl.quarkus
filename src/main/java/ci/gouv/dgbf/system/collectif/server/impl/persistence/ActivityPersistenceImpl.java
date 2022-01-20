package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Activity;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ActivityPersistence;

@ApplicationScoped
public class ActivityPersistenceImpl extends AbstractSpecificPersistenceImpl<Activity>  implements ActivityPersistence,Serializable {

	public ActivityPersistenceImpl() {
		entityClass = Activity.class;
		entityImplClass = ActivityImpl.class;
	}
}