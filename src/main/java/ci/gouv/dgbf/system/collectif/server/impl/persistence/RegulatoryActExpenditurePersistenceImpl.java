package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActExpenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActExpenditurePersistence;

@ApplicationScoped
public class RegulatoryActExpenditurePersistenceImpl extends AbstractSpecificPersistenceImpl<RegulatoryActExpenditure>  implements RegulatoryActExpenditurePersistence,Serializable {

	public RegulatoryActExpenditurePersistenceImpl() {
		entityClass = RegulatoryActExpenditure.class;
		entityImplClass = RegulatoryActExpenditureImpl.class;
	}
}