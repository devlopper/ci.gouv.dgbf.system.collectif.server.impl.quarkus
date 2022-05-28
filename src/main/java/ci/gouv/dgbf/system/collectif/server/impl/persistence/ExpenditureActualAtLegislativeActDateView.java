package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ExpenditureActualAtLegislativeActDateView.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ExpenditureActualAtLegislativeActDateView.TABLE_NAME)
@org.hibernate.annotations.Immutable
public class ExpenditureActualAtLegislativeActDateView extends AbstractExpenditureEntryAuthorizationPaymentCreditView implements Serializable{

	@Column(name = COLUMN_NUMBER) Short number;
	
	public static final String FIELD_NUMBER = "number";
	
	public static final String ENTITY_NAME = "ExpenditureActualAtLegislativeActDateView";
	public static final String TABLE_NAME = "VMA_DEPENSE_ACTUEL_COLLECTIF";
	
	public static final String COLUMN_NUMBER = "numero";
}