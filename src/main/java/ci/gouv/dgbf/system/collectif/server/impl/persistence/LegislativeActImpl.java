package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = LegislativeActImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=LegislativeActImpl.TABLE_NAME)
@AttributeOverrides(value= {
		@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_WHO__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_WHO__,nullable = false))
		,@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_WHAT__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_WHAT__,nullable = false))
		,@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_WHEN__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_WHEN__,nullable = false))
		,@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_FUNCTIONALITY__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_FUNCTIONALITY__,nullable = false))
})
public class LegislativeActImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl implements LegislativeAct,Serializable {

	@NotNull @Column(name = COLUMN_YEAR,nullable = false) Short year;
	
	@Override
	public LegislativeActImpl setIdentifier(String identifier) {
		return (LegislativeActImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public LegislativeActImpl setCode(String code) {
		return (LegislativeActImpl) super.setCode(code);
	}
	
	@Override
	public LegislativeActImpl setName(String name) {
		return (LegislativeActImpl) super.setName(name);
	}
	
	public static final String FIELD_YEAR = "year";
	public static final String FIELD_SIGNATORY = "signatory";
	public static final String FIELD_REFERENCE = "reference";
	public static final String FIELD_VERSIONS = "versions";
	
	public static final String ENTITY_NAME = "LegislativeActImpl";
	public static final String TABLE_NAME = "TA_COLLECTIF";
	
	public static final String COLUMN_YEAR = "EXERCICE";
	public static final String COLUMN_SIGNATORY = "SIGNATAIRE";
	public static final String COLUMN_REFERENCE = "REFERENCE";
	
	public static final String COLUMN___AUDIT_WHO__ = "AUDIT_ACTEUR";
	public static final String COLUMN___AUDIT_WHAT__ = "AUDIT_ACTION";
	public static final String COLUMN___AUDIT_FUNCTIONALITY__ = "AUDIT_FONCTIONNALITE";
	public static final String COLUMN___AUDIT_WHEN__ = "AUDIT_DATE";
}