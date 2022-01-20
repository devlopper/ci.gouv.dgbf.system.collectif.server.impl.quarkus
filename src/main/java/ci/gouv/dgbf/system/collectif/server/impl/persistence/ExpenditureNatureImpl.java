package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNature;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ExpenditureNatureImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ExpenditureNatureImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class ExpenditureNatureImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements ExpenditureNature,Serializable {

	@Override
	public ExpenditureNatureImpl setIdentifier(String identifier) {
		return (ExpenditureNatureImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public ExpenditureNatureImpl setCode(String code) {
		return (ExpenditureNatureImpl) super.setCode(code);
	}
	
	@Override
	public ExpenditureNatureImpl setName(String name) {
		return (ExpenditureNatureImpl) super.setName(name);
	}
	
	public static final String ENTITY_NAME = "ExpenditureNatureImpl";
	public static final String TABLE_NAME = "VMA_NATURE_DEPENSE";
}