package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.AdministrativeUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.AdministrativeUnitPersistence;

@ApplicationScoped
public class AdministrativeUnitPersistenceImpl extends AbstractSpecificPersistenceImpl<AdministrativeUnit>  implements AdministrativeUnitPersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public AdministrativeUnitPersistenceImpl() {
		entityClass = AdministrativeUnit.class;
		entityImplClass = AdministrativeUnitImpl.class;
	}
	
}