package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedAct;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = GeneratedActImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=GeneratedActImpl.TABLE_NAME)
@NamedQueries(value = {
		@NamedQuery(name = GeneratedActImpl.QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIIFER,query = "SELECT t FROM GeneratedActImpl t WHERE t.legislativeActVersion.identifier = :legislativeActVersionIdentifier")
})
@AttributeOverrides(value= {
		@AttributeOverride(name = GeneratedActImpl.FIELD___AUDIT_IDENTIFIER__,column = @Column(name=GeneratedActImpl.COLUMN___AUDIT_IDENTIFIER__,nullable = false))
		,@AttributeOverride(name = GeneratedActImpl.FIELD___AUDIT_WHO__,column = @Column(name=GeneratedActImpl.COLUMN___AUDIT_WHO__,nullable = false))
		,@AttributeOverride(name = GeneratedActImpl.FIELD___AUDIT_WHAT__,column = @Column(name=GeneratedActImpl.COLUMN___AUDIT_WHAT__,nullable = false))
		,@AttributeOverride(name = GeneratedActImpl.FIELD___AUDIT_WHEN__,column = @Column(name=GeneratedActImpl.COLUMN___AUDIT_WHEN__,nullable = false))
		,@AttributeOverride(name = GeneratedActImpl.FIELD___AUDIT_FUNCTIONALITY__,column = @Column(name=GeneratedActImpl.COLUMN___AUDIT_FUNCTIONALITY__,nullable = false))
})
@AuditOverrides({
	@AuditOverride(forClass = AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl.class)
})
public class GeneratedActImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl implements GeneratedAct,Serializable {

	@NotNull @ManyToOne @JoinColumn(name = COLUMN_LEGISLATIVE_ACT_VERSION,nullable = false,updatable = false) @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED) LegislativeActVersionImpl legislativeActVersion;
	@NotNull @Column(name = COLUMN_TYPE,nullable = false,updatable = false) @Enumerated(EnumType.STRING) @Audited Type type;
	@NotNull @Column(name = COLUMN_ACT_SOURCE_IDENTIFIER,nullable = false,updatable = false) @Audited String actSourceIdentifier;
	@NotNull @Column(name = COLUMN_APPLIED,nullable = false) @Audited Boolean applied;
	
	@Override
	public GeneratedActImpl setIdentifier(String identifier) {
		return (GeneratedActImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public GeneratedActImpl setCode(String code) {
		return (GeneratedActImpl) super.setCode(code);
	}
	
	@Override
	public GeneratedActImpl setName(String name) {
		return (GeneratedActImpl) super.setName(name);
	}
	
	@Override @Transient
	public String get__auditIdentifier__() {
		return super.get__auditIdentifier__();
	}
	
	public static final String FIELD_LEGISLATIVE_ACT_VERSION = "legislativeActVersion";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_ACT_SOURCE_IDENTIFIER = "actSourceIdentifier";
	public static final String FIELD_APPLIED = "applied";
	
	public static final String ENTITY_NAME = "GeneratedActImpl";
	public static final String TABLE_NAME = "TA_ACTE_GENERE";
	
	public static final String COLUMN_LEGISLATIVE_ACT_VERSION = "VERSION_COLLECTIF";
	public static final String COLUMN_TYPE = "TYPE";
	public static final String COLUMN_ACT_SOURCE_IDENTIFIER = "ACTE_SOURCE";
	public static final String COLUMN_APPLIED = "APPLIQUE";
	
	public static final String COLUMN___AUDIT_IDENTIFIER__ = "AUDIT_IDENTIFIANT";
	public static final String COLUMN___AUDIT_WHO__ = "AUDIT_ACTEUR";
	public static final String COLUMN___AUDIT_WHAT__ = "AUDIT_ACTION";
	public static final String COLUMN___AUDIT_FUNCTIONALITY__ = "AUDIT_FONCTIONNALITE";
	public static final String COLUMN___AUDIT_WHEN__ = "AUDIT_DATE";
	
	public static final String QUERY_READ_BY_LEGISLATIVE_ACT_VERSION_IDENTIIFER = "GeneratedActImpl.readByLegislativeActVersionIdentifier";
}