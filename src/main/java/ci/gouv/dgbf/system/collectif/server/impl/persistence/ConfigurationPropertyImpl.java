package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ConfigurationProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ConfigurationPropertyImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@NamedQueries(value = {
		@NamedQuery(name = ConfigurationPropertyImpl.QUERY_NAME_READ_VALUE_BY_CODE,query = "SELECT t.value FROM ConfigurationPropertyImpl t WHERE t.code = :code")
})
@Table(name=ConfigurationPropertyImpl.TABLE_NAME)
@Cacheable
public class ConfigurationPropertyImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements ConfigurationProperty,Serializable {

	@Column(name = COLUMN_VALUE)
	String value;
	
	@Override
	public ConfigurationPropertyImpl setIdentifier(String identifier) {
		return (ConfigurationPropertyImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public ConfigurationPropertyImpl setCode(String code) {
		return (ConfigurationPropertyImpl) super.setCode(code);
	}
	
	@Override
	public ConfigurationPropertyImpl setName(String name) {
		return (ConfigurationPropertyImpl) super.setName(name);
	}
	
	@Override
	public String toString() {
		return name+":"+value;
	}
	
	public static final String FIELD_VALUE = "value";
	
	public static final String ENTITY_NAME = "ConfigurationPropertyImpl";
	public static final String TABLE_NAME = "TA_PROPRIETE_CONFIGURATION";
	
	public static final String COLUMN_VALUE = "VALEUR";
	
	public static final String QUERY_NAME_READ_VALUE_BY_CODE = "ConfigurationPropertyImpl.ReadValueByCode";
}