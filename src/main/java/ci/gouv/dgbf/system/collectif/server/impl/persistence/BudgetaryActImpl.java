package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = BudgetaryActImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=BudgetaryActImpl.TABLE_NAME)
public class BudgetaryActImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements BudgetaryAct,Serializable {

	@Column(name = COLUMN_SIGNATORY) String signatory;
	@Column(name = COLUMN_REFERENCE) String reference;
	@Column(name = COLUMN_CREATION_DATE) LocalDateTime creationDate;
	
	@Transient Integer year;
	@Transient Collection<BudgetaryActVersion> versions;
	
	@Override
	public BudgetaryActImpl setIdentifier(String identifier) {
		return (BudgetaryActImpl) super.setIdentifier(identifier);
	}
	
	public Collection<BudgetaryActVersion> getVersions(Boolean injectIfNull) {
		if(versions == null && Boolean.TRUE.equals(injectIfNull))
			versions = new ArrayList<>();
		return versions;
	}
	
	public static final String FIELD_YEAR = "year";
	public static final String FIELD_SIGNATORY = "signatory";
	public static final String FIELD_REFERENCE = "reference";
	public static final String FIELD_CREATION_DATE = "creationDate";
	public static final String FIELD_VERSIONS = "versions";
	
	public static final String COLUMN_SIGNATORY = "SIGNATAIRE";
	public static final String COLUMN_REFERENCE = "REFERENCE";
	public static final String COLUMN_CREATION_DATE = "DATE_CREATION";
	
	public static final String ENTITY_NAME = "BudgetaryActImpl";
	public static final String TABLE_NAME = "TA_ACTE";
}