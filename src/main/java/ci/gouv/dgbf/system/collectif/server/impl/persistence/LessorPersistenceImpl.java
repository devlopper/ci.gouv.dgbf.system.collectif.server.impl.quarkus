package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Lessor;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LessorPersistence;

@ApplicationScoped
public class LessorPersistenceImpl extends AbstractSpecificPersistenceImpl<Lessor>  implements LessorPersistence,Serializable {

	public LessorPersistenceImpl() {
		entityClass = Lessor.class;
		entityImplClass = LessorImpl.class;
	}
}