package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.BudgetCategoryDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetCategoryImpl;
import io.quarkus.arc.Unremovable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @NoArgsConstructor @RequestScoped @Unremovable
public class BudgetCategoryDtoImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements BudgetCategoryDto,Serializable {

	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public BudgetCategoryDtoImpl setIdentifier(String identifier) {
		return (BudgetCategoryDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public BudgetCategoryDtoImpl setCode(String code) {
		return (BudgetCategoryDtoImpl) super.setCode(code);
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public String getCode() {
		return super.getCode();
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public BudgetCategoryDtoImpl setName(String name) {
		return (BudgetCategoryDtoImpl) super.setName(name);
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public String getName() {
		return super.getName();
	}
	
	static {
		AbstractServiceImpl.setProjections(BudgetCategoryDtoImpl.class, Map.of(
    			JSON_IDENTIFIER,BudgetCategoryImpl.FIELD_IDENTIFIER
    			,JSON_CODE,BudgetCategoryImpl.FIELD_CODE
    			,JSON_NAME,BudgetCategoryImpl.FIELD_NAME
    			));
	}
}