package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Section;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;

@ApplicationScoped
public class SectionPersistenceImpl extends AbstractSpecificPersistenceImpl<Section>  implements SectionPersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public SectionPersistenceImpl() {
		entityClass = Section.class;
		entityImplClass = SectionImpl.class;
	}
}