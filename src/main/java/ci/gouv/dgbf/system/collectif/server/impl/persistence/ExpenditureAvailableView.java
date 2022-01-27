package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ExpenditureAvailableView.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ExpenditureAvailableView.TABLE_NAME)
public class ExpenditureAvailableView extends AbstractIdentifiableSystemScalarStringImpl implements Serializable{

	@Column(name = COLUMN_ENTRY_AUTHORIZATION) Long entryAuthorization;
	@Column(name = COLUMN_PAYMENT_CREDIT) Long paymentCredit;

	public static final String FIELD_ENTRY_AUTHORIZATION = "entryAuthorization";
	public static final String FIELD_PAYMENT_CREDIT = "paymentCredit";
	
	public static final String ENTITY_NAME = "ExpenditureAvailableView";
	public static final String TABLE_NAME = "VA_DEPENSE_DISPONIBLE";
	
	public static final String COLUMN_ENTRY_AUTHORIZATION = "AE";
	public static final String COLUMN_PAYMENT_CREDIT = "CP";
}