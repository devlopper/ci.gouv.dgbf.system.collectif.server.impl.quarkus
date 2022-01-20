package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = LegislativeActVersionImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=LegislativeActVersionImpl.TABLE_NAME)
public class LegislativeActVersionImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements LegislativeActVersion,Serializable {

	@NotNull @ManyToOne @JoinColumn(name = COLUMN_ACT) private LegislativeActImpl act;
	@Transient private String actIdentifier;
	@NotNull @Column(name = COLUMN_NUMBER) private Byte number;
	@NotNull @Column(name = COLUMN_CREATION_DATE) private LocalDateTime creationDate;
	
	@Override
	public LegislativeActVersionImpl setIdentifier(String identifier) {
		return (LegislativeActVersionImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public LegislativeActVersionImpl setCode(String code) {
		return (LegislativeActVersionImpl) super.setCode(code);
	}
	
	@Override
	public LegislativeActVersionImpl setName(String name) {
		return (LegislativeActVersionImpl) super.setName(name);
	}
	
	@Override
	public LegislativeActVersion setAct(LegislativeAct act) {
		this.act = (LegislativeActImpl) act;
		return this;
	}
	
	public static final String FIELD_ACT = "act";
	public static final String FIELD_ACT_IDENTIFIER = "actIdentifier";
	public static final String FIELD_NUMBER = "number";
	public static final String FIELD_CREATION_DATE = "creationDate";
	public static final String FIELD_EXPENDITURE_NATURES = "expenditureNatures";
	public static final String FIELD_EXPENDITURE_NATURES_SUMS_AND_TOTAL = "expenditureNaturesSumsAndTotal";
	
	public static final String COLUMN_ACT = "ACTE";
	public static final String COLUMN_NUMBER = "NUMERO";
	public static final String COLUMN_CREATION_DATE = "DATE_CREATION";
	
	public static final String ENTITY_NAME = "LegislativeActVersionImpl";
	public static final String TABLE_NAME = "TA_VERSION_ACTE";
}