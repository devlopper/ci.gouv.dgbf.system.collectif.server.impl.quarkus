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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringAuditedImpl;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActLegislativeActVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = RegulatoryActLegislativeActVersionImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=RegulatoryActLegislativeActVersionImpl.TABLE_NAME,uniqueConstraints = {
		@UniqueConstraint(name = "ACTE_GESTION_VERSION_COLLECTIF_UK1"
				,columnNames = {RegulatoryActLegislativeActVersionImpl.COLUMN_REGULATORY_ACT,RegulatoryActLegislativeActVersionImpl.COLUMN_LEGISLATIVE_ACT_VERSION})
})
@AttributeOverrides(value= {
		@AttributeOverride(name = RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_IDENTIFIER__,column = @Column(name=RegulatoryActLegislativeActVersionImpl.COLUMN___AUDIT_IDENTIFIER__,nullable = false))
		,@AttributeOverride(name = RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHO__,column = @Column(name=RegulatoryActLegislativeActVersionImpl.COLUMN___AUDIT_WHO__,nullable = false))
		,@AttributeOverride(name = RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHAT__,column = @Column(name=RegulatoryActLegislativeActVersionImpl.COLUMN___AUDIT_WHAT__,nullable = false))
		,@AttributeOverride(name = RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_WHEN__,column = @Column(name=RegulatoryActLegislativeActVersionImpl.COLUMN___AUDIT_WHEN__,nullable = false))
		,@AttributeOverride(name = RegulatoryActLegislativeActVersionImpl.FIELD___AUDIT_FUNCTIONALITY__,column = @Column(name=RegulatoryActLegislativeActVersionImpl.COLUMN___AUDIT_FUNCTIONALITY__,nullable = false))
})
@AuditOverrides({
	@AuditOverride(forClass = AbstractIdentifiableSystemScalarStringAuditedImpl.class)
})
@AuditTable(value = RegulatoryActLegislativeActVersionImpl.AUDIT_TABLE_NAME)
@NamedQueries(value = {
		@NamedQuery(name = RegulatoryActLegislativeActVersionImpl.QUERY_READ_TO_BE_EXCLUDED_COMPREHENSIVELY_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_BY_REGULATORIES_ACTS_IDENTIFIERS
			,query = RegulatoryActLegislativeActVersionImpl.QUERY_STRING_READ_TO_BE_EXCLUDED_COMPREHENSIVELY_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER
			+" AND t.legislativeActVersion.identifier = :legislativeActVersionIdentifier AND t.regulatoryAct.identifier NOT IN :identifiers")
		,@NamedQuery(name = RegulatoryActLegislativeActVersionImpl.QUERY_READ_TO_BE_EXCLUDED_COMPREHENSIVELY_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER
				,query = RegulatoryActLegislativeActVersionImpl.QUERY_STRING_READ_TO_BE_EXCLUDED_COMPREHENSIVELY_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER)
})
public class RegulatoryActLegislativeActVersionImpl extends AbstractIdentifiableSystemScalarStringAuditedImpl implements RegulatoryActLegislativeActVersion,Serializable {

	@NotNull @ManyToOne @JoinColumn(name = COLUMN_REGULATORY_ACT,nullable = false) @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED) RegulatoryActImpl regulatoryAct;
	@NotNull @ManyToOne @JoinColumn(name = COLUMN_LEGISLATIVE_ACT_VERSION,nullable = false) @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED) LegislativeActVersionImpl legislativeActVersion;
	@NotNull @Column(name = COLUMN_INCLUDED,nullable = false) @Audited(withModifiedFlag = true,modifiedColumnName = COLUMN_INCLUDED+"_MOD") Boolean included;
	
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
	public static final String TABLE_NAME = "TA_ACTE_GESTION_V_COLLECTIF";
	public static final String AUDIT_TABLE_NAME = "TA_ACTE_GESTION_V_COL_AUD";
	
	public static final String COLUMN_REGULATORY_ACT = "ACTE_GESTION";
	public static final String COLUMN_LEGISLATIVE_ACT_VERSION = "VERSION_COLLECTIF";
	public static final String COLUMN_INCLUDED = "INCLUS";
	public static final String COLUMN_ACTS_GENERATED = "ACTES_GENERES";
	
	public static final String COLUMN___AUDIT_IDENTIFIER__ = "AUDIT_IDENTIFIANT";
	public static final String COLUMN___AUDIT_WHO__ = "AUDIT_ACTEUR";
	public static final String COLUMN___AUDIT_WHAT__ = "AUDIT_ACTION";
	public static final String COLUMN___AUDIT_FUNCTIONALITY__ = "AUDIT_FONCTIONNALITE";
	public static final String COLUMN___AUDIT_WHEN__ = "AUDIT_DATE";
	
	public static final String QUERY_READ_TO_BE_EXCLUDED_COMPREHENSIVELY_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER_BY_REGULATORIES_ACTS_IDENTIFIERS = "RegulatoryActLegislativeActVersionImpl.readToBeExcludedComprehensivelyByLegislativeActIdentifierByRegulatoriesActsIdentifiers";
	public static final String QUERY_READ_TO_BE_EXCLUDED_COMPREHENSIVELY_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER = "RegulatoryActLegislativeActVersionImpl.readToBeExcludedComprehensivelyByLegislativeActIdentifier";
	public static final String QUERY_STRING_READ_TO_BE_EXCLUDED_COMPREHENSIVELY_BY_LEGISLATIVE_ACT_VERSION_IDENTIFIER = "SELECT t.regulatoryAct.identifier,t.included,t.identifier FROM RegulatoryActLegislativeActVersionImpl t WHERE t.included = TRUE AND t.legislativeActVersion.identifier = :legislativeActVersionIdentifier";
}