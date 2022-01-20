package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = LegislativeActImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=LegislativeActImpl.TABLE_NAME)
public class LegislativeActImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements LegislativeAct,Serializable {

	@NotNull @Column(name = COLUMN_YEAR,nullable = false) Short year;
	
	@Transient /*@Column(name = COLUMN_SIGNATORY)*/ String signatory;
	@Transient /*@Column(name = COLUMN_REFERENCE)*/ String reference;
	@Transient /*@Column(name = COLUMN_CREATION_DATE)*/ LocalDateTime creationDate;
	
	//@Transient Integer year;
	
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
	public static final String FIELD_CREATION_DATE = "creationDate";
	public static final String FIELD_VERSIONS = "versions";
	
	public static final String COLUMN_YEAR = "EXERCICE";
	public static final String COLUMN_SIGNATORY = "SIGNATAIRE";
	public static final String COLUMN_REFERENCE = "REFERENCE";
	public static final String COLUMN_CREATION_DATE = "DATE_CREATION";
	
	public static final String ENTITY_NAME = "LegislativeActImpl";
	public static final String TABLE_NAME = "TA_ACTE";
}