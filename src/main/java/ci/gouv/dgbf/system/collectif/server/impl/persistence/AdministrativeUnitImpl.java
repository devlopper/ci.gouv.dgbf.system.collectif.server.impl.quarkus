package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.AdministrativeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = AdministrativeUnitImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=AdministrativeUnitImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class AdministrativeUnitImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements AdministrativeUnit,Serializable {

	@Override
	public AdministrativeUnitImpl setIdentifier(String identifier) {
		return (AdministrativeUnitImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public AdministrativeUnitImpl setCode(String code) {
		return (AdministrativeUnitImpl) super.setCode(code);
	}
	
	@Override
	public AdministrativeUnitImpl setName(String name) {
		return (AdministrativeUnitImpl) super.setName(name);
	}
	
	public static final String FIELD_SECTION_IDENTIFIER = "sectionIdentifier";
	
	public static final String ENTITY_NAME = "AdministrativeUnitImpl";
	public static final String TABLE_NAME = "VA_USB";
}