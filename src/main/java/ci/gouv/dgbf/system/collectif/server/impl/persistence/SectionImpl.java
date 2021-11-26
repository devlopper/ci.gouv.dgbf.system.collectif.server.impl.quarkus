package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = SectionImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=SectionImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class SectionImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements Section,Serializable {

	@Override
	public SectionImpl setIdentifier(String identifier) {
		return (SectionImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public SectionImpl setCode(String code) {
		return (SectionImpl) super.setCode(code);
	}
	
	@Override
	public SectionImpl setName(String name) {
		return (SectionImpl) super.setName(name);
	}
	
	public static final String ENTITY_NAME = "SectionImpl";
	public static final String TABLE_NAME = "VA_SECTION";
}