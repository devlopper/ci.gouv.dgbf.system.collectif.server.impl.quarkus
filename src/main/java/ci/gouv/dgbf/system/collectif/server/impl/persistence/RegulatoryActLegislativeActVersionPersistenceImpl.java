package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActLegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActLegislativeActVersionPersistence;

@ApplicationScoped
public class RegulatoryActLegislativeActVersionPersistenceImpl extends AbstractSpecificPersistenceImpl<RegulatoryActLegislativeActVersion>  implements RegulatoryActLegislativeActVersionPersistence,Serializable {

	public RegulatoryActLegislativeActVersionPersistenceImpl() {
		entityClass = RegulatoryActLegislativeActVersion.class;
		entityImplClass = RegulatoryActLegislativeActVersionImpl.class;
	}
}