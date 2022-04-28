package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceImpl;
import org.cyk.utility.__kernel__.string.StringHelper;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ConfigurationProperty;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ConfigurationPropertyPersistence;

@ApplicationScoped
public class ConfigurationPropertyPersistenceImpl extends AbstractSpecificPersistenceImpl<ConfigurationProperty>  implements ConfigurationPropertyPersistence,Serializable {

	@Inject EntityManager entityManager;
	
	public ConfigurationPropertyPersistenceImpl() {
		entityClass = ConfigurationProperty.class;
		entityImplClass = ConfigurationPropertyImpl.class;
	}

	@Override
	public String readValueByCode(String code) {
		if(StringHelper.isBlank(code))
			return null;
		try {
			return entityManager.createNamedQuery(ConfigurationPropertyImpl.QUERY_NAME_READ_VALUE_BY_CODE,String.class).setParameter("code", code).getSingleResult();
		} catch (NoResultException exception) {
			return null;
		}
	}
}