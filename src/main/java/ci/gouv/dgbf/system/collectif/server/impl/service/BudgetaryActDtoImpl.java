package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.BudgetaryActDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class BudgetaryActDtoImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements BudgetaryActDto,Serializable {

	//@JsonbProperty(value = JSON_BUDGETARY_ACT_VERSIONS)
	//List<BudgetaryActVersionDto> budgetaryActVersions;

	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public BudgetaryActDtoImpl setIdentifier(String identifier) {
		return (BudgetaryActDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(value = JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public BudgetaryActDtoImpl setCode(String code) {
		return (BudgetaryActDtoImpl) super.setCode(code);
	}
	
	@Override @JsonbProperty(value = JSON_CODE)
	public String getCode() {
		return super.getCode();
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public BudgetaryActDtoImpl setName(String name) {
		return (BudgetaryActDtoImpl) super.setName(name);
	}
	
	@Override @JsonbProperty(value = JSON_NAME)
	public String getName() {
		return super.getName();
	}
}