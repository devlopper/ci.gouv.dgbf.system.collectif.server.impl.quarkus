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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = LegislativeActImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=LegislativeActImpl.TABLE_NAME)
@NamedQueries(value = {
		@NamedQuery(name = LegislativeActImpl.QUERY_READ_BY_IDENTIIFER,query = "SELECT t FROM LegislativeActImpl t WHERE t.identifier = :identifier")
})
@AttributeOverrides(value= {
		@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_WHO__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_WHO__,nullable = false))
		,@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_WHAT__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_WHAT__,nullable = false))
		,@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_WHEN__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_WHEN__,nullable = false))
		,@AttributeOverride(name = LegislativeActImpl.FIELD___AUDIT_FUNCTIONALITY__,column = @Column(name=LegislativeActImpl.COLUMN___AUDIT_FUNCTIONALITY__,nullable = false))
})
public class LegislativeActImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableAuditedImpl implements LegislativeAct,Serializable {

	@Column(name = COLUMN_EXERCISE) String exerciseIdentifier;
	@Transient ExerciseImpl exercise;
	
	@ManyToOne @JoinColumn(name = COLUMN_DEFAULT_VERSION) LegislativeActVersionImpl defaultVersion;
	@NotNull @Column(name = COLUMN_IN_PROGRESS,nullable = false) Boolean inProgress;
	@Transient String versionIdentifier;
	
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
	
	public static final String FIELD_EXERCISE_IDENTIFIER = "exerciseIdentifier";
	public static final String FIELD_EXERCISE = "exercise";
	public static final String FIELD_DEFAULT_VERSION = "defaultVersion";
	public static final String FIELD_IN_PROGRESS = "inProgress";
	public static final String FIELD_VERSION_IDENTIFIER = "versionIdentifier";
	
	public static final String FIELD_SIGNATORY = "signatory";
	public static final String FIELD_REFERENCE = "reference";
	public static final String FIELD_VERSIONS = "versions";
	
	public static final String ENTITY_NAME = "LegislativeActImpl";
	public static final String TABLE_NAME = "TA_COLLECTIF";
	
	public static final String COLUMN_EXERCISE = "EXERCICE";
	public static final String COLUMN_DEFAULT_VERSION = "VERSION_PAR_DEFAUT";
	public static final String COLUMN_IN_PROGRESS = "EN_COURS";
	public static final String COLUMN_SIGNATORY = "SIGNATAIRE";
	public static final String COLUMN_REFERENCE = "REFERENCE";
	
	public static final String COLUMN___AUDIT_WHO__ = "AUDIT_ACTEUR";
	public static final String COLUMN___AUDIT_WHAT__ = "AUDIT_ACTION";
	public static final String COLUMN___AUDIT_FUNCTIONALITY__ = "AUDIT_FONCTIONNALITE";
	public static final String COLUMN___AUDIT_WHEN__ = "AUDIT_DATE";
	
	public static final String QUERY_READ_BY_IDENTIIFER = "LegislativeActImpl.readByIdentifier";
}