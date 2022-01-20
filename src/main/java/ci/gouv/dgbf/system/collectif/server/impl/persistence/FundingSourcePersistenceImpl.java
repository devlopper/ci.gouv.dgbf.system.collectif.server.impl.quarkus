package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.FundingSource;
import ci.gouv.dgbf.system.collectif.server.api.persistence.FundingSourcePersistence;

@ApplicationScoped
public class FundingSourcePersistenceImpl extends AbstractSpecificPersistenceImpl<FundingSource>  implements FundingSourcePersistence,Serializable {

	public FundingSourcePersistenceImpl() {
		entityClass = FundingSource.class;
		entityImplClass = FundingSourceImpl.class;
	}
}