package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNature;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = EconomicNatureImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=EconomicNatureImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class EconomicNatureImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements EconomicNature,Serializable {

	@Override
	public EconomicNatureImpl setIdentifier(String identifier) {
		return (EconomicNatureImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public EconomicNatureImpl setCode(String code) {
		return (EconomicNatureImpl) super.setCode(code);
	}
	
	@Override
	public EconomicNatureImpl setName(String name) {
		return (EconomicNatureImpl) super.setName(name);
	}
	
	public static final String ENTITY_NAME = "EconomicNatureImpl";
	public static final String TABLE_NAME = "VMA_NATURE_ECONOMIQUE";
}