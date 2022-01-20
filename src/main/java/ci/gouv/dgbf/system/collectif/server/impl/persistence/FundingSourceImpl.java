package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.FundingSource;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = FundingSourceImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=FundingSourceImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class FundingSourceImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements FundingSource,Serializable {

	@Override
	public FundingSourceImpl setIdentifier(String identifier) {
		return (FundingSourceImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public FundingSourceImpl setCode(String code) {
		return (FundingSourceImpl) super.setCode(code);
	}
	
	@Override
	public FundingSourceImpl setName(String name) {
		return (FundingSourceImpl) super.setName(name);
	}
	
	public static final String ENTITY_NAME = "FundingSourceImpl";
	public static final String TABLE_NAME = "VMA_SOURCE_FINANCEMENT";
}