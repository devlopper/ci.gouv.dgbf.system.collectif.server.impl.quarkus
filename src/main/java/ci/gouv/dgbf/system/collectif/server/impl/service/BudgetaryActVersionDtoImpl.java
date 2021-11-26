package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.__kernel__.object.AbstractObject;
import org.cyk.utility.service.server.AbstractServiceImpl;

import com.fasterxml.jackson.annotation.JsonProperty;

import ci.gouv.dgbf.system.collectif.server.api.service.BudgetaryActDto;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetaryActVersionDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetaryActVersionImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class BudgetaryActVersionDtoImpl extends AbstractObject implements BudgetaryActVersionDto,Serializable {

	@JsonbProperty(value = JSON_IDENTIFIER) private String identifier;
	@JsonbProperty(value = JSON_CODE) private String code;
	@JsonbProperty(value = JSON_NAME) private String name;
	
	@JsonbProperty(value = JSON_BUDGETARY_ACT_IDENTIFIER) @JsonProperty(value = JSON_BUDGETARY_ACT_IDENTIFIER)
	String budgetaryActIdentifier;
	@JsonbProperty(value = JSON_BUDGETARY_ACT) @JsonProperty(value = JSON_BUDGETARY_ACT)
	BudgetaryActDtoImpl budgetaryAct;
	//@JsonbProperty(value = JSON_BUDGETARY_ACT) Byte number;
	//@JsonbProperty(value = JSON_BUDGETARY_ACT) LocalDateTime creationDate;
	
	@Override @JsonbProperty(value = JSON_BUDGETARY_ACT)
	public BudgetaryActVersionDto setBudgetaryAct(BudgetaryActDto budgetaryAct) {
		this.budgetaryAct = (BudgetaryActDtoImpl) budgetaryAct;
		return this;
	}
	
	static {
		AbstractServiceImpl.setProjections(BudgetaryActVersionDtoImpl.class, Map.of(
    			BudgetaryActVersionDto.JSON_IDENTIFIER,BudgetaryActVersionImpl.FIELD_IDENTIFIER
    			,BudgetaryActVersionDto.JSON_CODE,BudgetaryActVersionImpl.FIELD_CODE
    			,BudgetaryActVersionDto.JSON_NAME,BudgetaryActVersionImpl.FIELD_NAME
    			,BudgetaryActVersionDto.JSON_BUDGETARY_ACT,BudgetaryActVersionImpl.FIELD_BUDGETARY_ACT
    			,BudgetaryActVersionDto.JSON_BUDGETARY_ACT_IDENTIFIER,BudgetaryActVersionImpl.FIELD_BUDGETARY_ACT_IDENTIFIER
    			));
	}
}