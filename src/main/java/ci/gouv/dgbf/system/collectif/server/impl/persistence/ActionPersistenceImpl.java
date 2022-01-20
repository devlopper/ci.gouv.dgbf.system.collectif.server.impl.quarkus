package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Action;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ActionPersistence;

@ApplicationScoped
public class ActionPersistenceImpl extends AbstractSpecificPersistenceImpl<Action>  implements ActionPersistence,Serializable {

	public ActionPersistenceImpl() {
		entityClass = Action.class;
		entityImplClass = ActionImpl.class;
	}
}