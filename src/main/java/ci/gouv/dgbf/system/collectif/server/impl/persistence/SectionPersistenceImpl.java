package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Section;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;

@ApplicationScoped
public class SectionPersistenceImpl extends AbstractSpecificPersistenceImpl<Section>  implements SectionPersistence,Serializable {

	public SectionPersistenceImpl() {
		entityClass = Section.class;
		entityImplClass = SectionImpl.class;
	}
}