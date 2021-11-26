package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Action;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ActionImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ActionImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class ActionImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements Action,Serializable {

	@Override
	public ActionImpl setIdentifier(String identifier) {
		return (ActionImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public ActionImpl setCode(String code) {
		return (ActionImpl) super.setCode(code);
	}
	
	@Override
	public ActionImpl setName(String name) {
		return (ActionImpl) super.setName(name);
	}
	
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER = "budgetSpecializationUnitIdentifier";
	
	public static final String ENTITY_NAME = "ActionImpl";
	public static final String TABLE_NAME = "VA_ACTION";
}