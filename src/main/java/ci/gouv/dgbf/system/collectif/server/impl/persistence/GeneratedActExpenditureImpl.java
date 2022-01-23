package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringAuditedImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActExpenditure;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = GeneratedActExpenditureImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=GeneratedActExpenditureImpl.TABLE_NAME,uniqueConstraints = {
		@UniqueConstraint(name = "ACTE_DEPENSE_UK1",columnNames = {GeneratedActExpenditureImpl.COLUMN_ACT,GeneratedActExpenditureImpl.COLUMN_EXPENDITURE})
})
public class GeneratedActExpenditureImpl extends AbstractIdentifiableSystemScalarStringAuditedImpl implements GeneratedActExpenditure,Serializable {

	@NotNull @ManyToOne @JoinColumn(name = COLUMN_ACT,nullable = false,updatable = false) GeneratedActImpl act;
	@NotNull @ManyToOne @JoinColumn(name = COLUMN_EXPENDITURE,nullable = false,updatable = false) ExpenditureImpl expenditure;
	
	@Override
	public GeneratedActExpenditureImpl setIdentifier(String identifier) {
		return (GeneratedActExpenditureImpl) super.setIdentifier(identifier);
	}
	
	public static final String FIELD_ACT = "act";
	public static final String FIELD_EXPENDITURE = "expenditure";
	
	public static final String ENTITY_NAME = "GeneratedActExpenditureImpl";
	public static final String TABLE_NAME = "TA_ACTE_GENERE_DEPENSE";
	
	public static final String COLUMN_ACT = "ACTE_GENERE";
	public static final String COLUMN_EXPENDITURE = "DEPENSE";
}