package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = BudgetCategoryImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=BudgetCategoryImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class BudgetCategoryImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements BudgetCategory,Serializable {

	@Override
	public BudgetCategoryImpl setIdentifier(String identifier) {
		return (BudgetCategoryImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public BudgetCategoryImpl setCode(String code) {
		return (BudgetCategoryImpl) super.setCode(code);
	}
	
	@Override
	public BudgetCategoryImpl setName(String name) {
		return (BudgetCategoryImpl) super.setName(name);
	}
	
	public static final String ENTITY_NAME = "BudgetCategoryImpl";
	public static final String TABLE_NAME = "VMA_CATEGORIE_BUDGET";
}