package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.json.bind.annotation.JsonbProperty;

import org.cyk.utility.service.entity.AbstractIdentifiableSystemScalarStringAuditedImpl;
import org.cyk.utility.service.server.AbstractServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.service.ResourceDto;
import ci.gouv.dgbf.system.collectif.server.api.service.RevenueDto;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImpl;
import io.quarkus.arc.Unremovable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @NoArgsConstructor @RequestScoped @Unremovable
public class ResourceDtoImpl extends AbstractIdentifiableSystemScalarStringAuditedImpl implements ResourceDto,Serializable {

	@JsonbProperty(value = JSON_REVENUE) RevenueDtoImpl revenue;	
	@JsonbProperty(value = JSON_BUDGETARY_ACT_AS_STRING) String actAsString;
	@JsonbProperty(value = JSON_BUDGETARY_ACT_VERSION_AS_STRING) String actVersionAsString;
	@JsonbProperty(value = JSON_SECTION_AS_STRING) String sectionAsString;
	@JsonbProperty(value = JSON_BUDGET_SPECIALIZATION_UNIT_AS_STRING) String budgetSpecializationUnitAsString;
	@JsonbProperty(value = JSON_ACTIVITY_AS_STRING) String activityAsString;
	@JsonbProperty(value = JSON_ECONOMIC_NATURE_AS_STRING) String economicNatureAsString;
	
	@Override @JsonbProperty(ResourceDto.JSON_IDENTIFIER)
	public ResourceDtoImpl setIdentifier(String identifier) {
		return (ResourceDtoImpl) super.setIdentifier(identifier);
	}
	
	@Override @JsonbProperty(ResourceDto.JSON_IDENTIFIER)
	public String getIdentifier() {
		return super.getIdentifier();
	}
	
	@Override
	public ResourceDto setRevenue(RevenueDto revenue) {
		this.revenue = (RevenueDtoImpl) revenue;
		return this;
	}
	
	@Override @JsonbProperty(value = ResourceDto.JSON___AUDIT__)
	public ResourceDtoImpl set__audit__(String __audit__) {
		return (ResourceDtoImpl) super.set__audit__(__audit__);
	}
	
	@Override @JsonbProperty(value = ResourceDto.JSON___AUDIT__)
	public String get__audit__() {
		return super.get__audit__();
	}
	
	@Override @JsonbProperty(value = ResourceDto.JSON___AUDIT_FUNCTIONALITY__)
	public String get__auditFunctionality__() {
		return super.get__auditFunctionality__();
	}
	
	@Override @JsonbProperty(value = ResourceDto.JSON___AUDIT_WHAT__)
	public String get__auditWhat__() {
		return super.get__auditWhat__();
	}
	
	@Override @JsonbProperty(value = ResourceDto.JSON___AUDIT_WHO__)
	public String get__auditWho__() {
		return super.get__auditWho__();
	}
	
	@Override @JsonbProperty(value = ResourceDto.JSON___AUDIT_WHEN__)
	public String get__auditWhenAsString__() {
		return super.get__auditWhenAsString__();
	}
	
	static {
		Map<String,String> map = new HashMap<>();
		map.putAll(Map.of(
				JSON_IDENTIFIER,ResourceImpl.FIELD_IDENTIFIER
    			,JSON_REVENUE,ResourceImpl.FIELD_REVENUE
    			,JSON_SECTION_AS_STRING,ResourceImpl.FIELD_SECTION_AS_STRING
    			,JSON_BUDGET_SPECIALIZATION_UNIT_AS_STRING,ResourceImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING
    			,JSONS_STRINGS,ResourceImpl.FIELDS_STRINGS
    			,JSONS_AMOUTNS,ResourceImpl.FIELDS_AMOUNTS
    			,JSONS_AMOUTNS_WITHOUT_AVAILABLE,ResourceImpl.FIELDS_AMOUNTS_WITHOUT_AVAILABLE
    			,JSON___AUDIT__,ResourceImpl.FIELD___AUDIT__
    			));
		AbstractServiceImpl.setProjections(ResourceDtoImpl.class, map);
	}
}