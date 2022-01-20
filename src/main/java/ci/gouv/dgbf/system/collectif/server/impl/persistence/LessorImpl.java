package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Lessor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = LessorImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=LessorImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class LessorImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements Lessor,Serializable {

	@Override
	public LessorImpl setIdentifier(String identifier) {
		return (LessorImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public LessorImpl setCode(String code) {
		return (LessorImpl) super.setCode(code);
	}
	
	@Override
	public LessorImpl setName(String name) {
		return (LessorImpl) super.setName(name);
	}
	
	public static final String ENTITY_NAME = "LessorImpl";
	public static final String TABLE_NAME = "VMA_BAILLEUR";
}