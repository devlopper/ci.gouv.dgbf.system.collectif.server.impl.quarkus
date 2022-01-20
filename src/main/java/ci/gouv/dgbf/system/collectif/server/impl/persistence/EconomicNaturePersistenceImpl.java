package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNature;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNaturePersistence;

@ApplicationScoped
public class EconomicNaturePersistenceImpl extends AbstractSpecificPersistenceImpl<EconomicNature>  implements EconomicNaturePersistence,Serializable {

	public EconomicNaturePersistenceImpl() {
		entityClass = EconomicNature.class;
		entityImplClass = EconomicNatureImpl.class;
	}
}