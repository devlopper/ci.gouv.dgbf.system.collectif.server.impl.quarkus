package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedAct;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = GeneratedActImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=GeneratedActImpl.TABLE_NAME)
public class GeneratedActImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements GeneratedAct,Serializable {

	@NotNull @ManyToOne @JoinColumn(name = COLUMN_LEGISLATIVE_ACT_VERSION,nullable = false,updatable = false) LegislativeActVersionImpl legislativeActVersion;
	@NotNull @Column(name = COLUMN_TYPE,nullable = false,updatable = false) Type type;
	@NotNull @Column(name = COLUMN_ACT_SOURCE_IDENTIFIER,nullable = false,updatable = false) String actSourceIdentifier;
	@NotNull @Column(name = COLUMN_APPLIED,nullable = false) Boolean applied;
	
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
}