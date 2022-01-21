package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringAuditedImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActLegislativeActVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = RegulatoryActLegislativeActVersionImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=RegulatoryActLegislativeActVersionImpl.TABLE_NAME,uniqueConstraints = {
		@UniqueConstraint(name = "ACTE_GESTION_VERSION_ACTE_UK1"
				,columnNames = {RegulatoryActLegislativeActVersionImpl.COLUMN_REGULATORY_ACT,RegulatoryActLegislativeActVersionImpl.COLUMN_LEGISLATIVE_ACT_VERSION})
})
@AttributeOverrides(value= {
		@AttributeOverride(name = RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHO__,column = @Column(name=RegulatoryActLegislativeActVersionImpl.COLUMN___AUDIT_WHO__,nullable = false))
		,@AttributeOverride(name = RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHAT__,column = @Column(name=RegulatoryActLegislativeActVersionImpl.COLUMN___AUDIT_WHAT__,nullable = false))
		,@AttributeOverride(name = RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHEN__,column = @Column(name=RegulatoryActLegislativeActVersionImpl.COLUMN___AUDIT_WHEN__,nullable = false))
		,@AttributeOverride(name = RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_FUNCTIONALITY__,column = @Column(name=RegulatoryActLegislativeActVersionImpl.COLUMN___AUDIT_FUNCTIONALITY__,nullable = false))
})
public class RegulatoryActLegislativeActVersionImpl extends AbstractIdentifiableSystemScalarStringAuditedImpl implements RegulatoryActLegislativeActVersion,Serializable {

	@NotNull @ManyToOne @JoinColumn(name = COLUMN_REGULATORY_ACT,nullable = false) RegulatoryActImpl regulatoryAct;
	@NotNull @ManyToOne @JoinColumn(name = COLUMN_LEGISLATIVE_ACT_VERSION,nullable = false) LegislativeActVersionImpl legislativeActVersion;
	@NotNull @Column(name = COLUMN_INCLUDED,nullable = false) Boolean included;
	
	@Override
	public RegulatoryActLegislativeActVersionImpl setIdentifier(String identifier) {
		return (RegulatoryActLegislativeActVersionImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public RegulatoryActLegislativeActVersionImpl setRegulatoryAct(RegulatoryAct regulatoryAct) {
		this.regulatoryAct = (RegulatoryActImpl) regulatoryAct;
		return this;
	}
	
	@Override
	public RegulatoryActLegislativeActVersionImpl setLegislativeActVersion(LegislativeActVersion legislativeActVersion) {
		this.legislativeActVersion = (LegislativeActVersionImpl) legislativeActVersion;
		return this;
	}
	
	public static final String FIELD_REGULATORY_ACT = "regulatoryAct";
	public static final String FIELD_LEGISLATIVE_ACT_VERSION = "legislativeActVersion";
	public static final String FIELD_INCLUDED = "included";
	public static final String FIELD_ACTS_GENERATED = "actsGenerated";
	
	public static final String ENTITY_NAME = "RegulatoryActLegislativeActVersionImpl";
	public static final String TABLE_NAME = "TA_ACTE_GESTION_VERSION_ACTE";
	
	public static final String COLUMN_REGULATORY_ACT = "ACTE_GESTION";
	public static final String COLUMN_LEGISLATIVE_ACT_VERSION = "VERSION_ACTE";
	public static final String COLUMN_INCLUDED = "INCLUS";
	public static final String COLUMN_ACTS_GENERATED = "ACTES_GENERES";
	
	public static final String COLUMN___AUDIT_WHO__ = "AUDIT_ACTEUR";
	public static final String COLUMN___AUDIT_WHAT__ = "AUDIT_ACTION";
	public static final String COLUMN___AUDIT_FUNCTIONALITY__ = "AUDIT_FONCTIONNALITE";
	public static final String COLUMN___AUDIT_WHEN__ = "AUDIT_DATE";
}