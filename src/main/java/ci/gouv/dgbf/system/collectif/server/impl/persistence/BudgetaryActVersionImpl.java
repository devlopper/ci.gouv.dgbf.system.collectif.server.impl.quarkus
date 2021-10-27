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
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = BudgetaryActVersionImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=BudgetaryActVersionImpl.TABLE_NAME)
public class BudgetaryActVersionImpl extends AbstractIdentifiableSystemScalarStringIdentifiableBusinessStringNamableImpl implements BudgetaryActVersion,Serializable {

	@NotNull @ManyToOne @JoinColumn(name = COLUMN_BUDGETARY_ACT) private BudgetaryActImpl budgetaryAct;
	@NotNull @Column(name = COLUMN_NUMBER) private Byte number;
	@Column(name = COLUMN_CREATION_DATE) private LocalDateTime creationDate;
	
	@Override
	public BudgetaryActVersionImpl setIdentifier(String identifier) {
		return (BudgetaryActVersionImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public BudgetaryActVersionImpl setCode(String code) {
		return (BudgetaryActVersionImpl) super.setCode(code);
	}
	
	@Override
	public BudgetaryActVersionImpl setName(String name) {
		return (BudgetaryActVersionImpl) super.setName(name);
	}
	
	@Override
	public BudgetaryActVersion setBudgetaryAct(BudgetaryAct budgetaryAct) {
		this.budgetaryAct = (BudgetaryActImpl) budgetaryAct;
		return this;
	}
	
	public static final String FIELD_BUDGETARY_ACT = "budgetaryAct";
	public static final String FIELD_NUMBER = "number";
	public static final String FIELD_CREATION_DATE = "creationDate";
	public static final String FIELD_EXPENDITURE_NATURES = "expenditureNatures";
	public static final String FIELD_EXPENDITURE_NATURES_SUMS_AND_TOTAL = "expenditureNaturesSumsAndTotal";
	
	public static final String COLUMN_BUDGETARY_ACT = "ACTE";
	public static final String COLUMN_NUMBER = "NUMERO";
	public static final String COLUMN_CREATION_DATE = "DATE_CREATION";
	
	public static final String ENTITY_NAME = "BudgetaryActVersionImpl";
	public static final String TABLE_NAME = "TA_VERSION_ACTE";
}