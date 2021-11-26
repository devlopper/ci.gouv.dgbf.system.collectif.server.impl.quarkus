package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.__kernel__.object.AbstractObject;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.ActionDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActionImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ActionDtoImpl extends AbstractObject implements ActionDto,Serializable {

	@JsonbProperty(value = JSON_IDENTIFIER) private String identifier;
	@JsonbProperty(value = JSON_CODE) private String code;
	@JsonbProperty(value = JSON_NAME) private String name;
	
	static {
		AbstractServiceImpl.setProjections(ActionDtoImpl.class, Map.of(
				ActionDto.JSON_IDENTIFIER,ActionImpl.FIELD_IDENTIFIER
    			,ActionDto.JSON_CODE,ActionImpl.FIELD_CODE
    			,ActionDto.JSON_NAME,ActionImpl.FIELD_NAME
    			));
	}
}