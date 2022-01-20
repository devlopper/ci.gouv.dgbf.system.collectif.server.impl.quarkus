package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActResource;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = RegulatoryActResourceImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=RegulatoryActResourceImpl.TABLE_NAME)
@Cacheable
@org.hibernate.annotations.Immutable
public class RegulatoryActResourceImpl extends AbstractIdentifiableSystemScalarStringImpl implements RegulatoryActResource,Serializable {

	@Column(name = COLUMN_ACT_IDENTIFIER) String actIdentifier;
	@Column(name = COLUMN_YEAR) Short year;
	@Column(name = COLUMN_ACTIVITY_IDENTIFIER) String activityIdentifier;
	@Column(name = COLUMN_ECONOMIC_NATURE_IDENTIFIER) String economicNatureIdentifier;
	@Column(name = COLUMN_REVENUE_AMOUNT) Long revenueAmount;
	
	@Override
	public RegulatoryActResourceImpl setIdentifier(String identifier) {
		return (RegulatoryActResourceImpl) super.setIdentifier(identifier);
	}
	
	public static final String FIELD_ACT_IDENTIFIER = "actIdentifier";
	public static final String FIELD_YEAR = "year";
	public static final String FIELD_ACTIVITY_IDENTIFIER = "activityIdentifier";
	public static final String FIELD_ECONOMIC_NATURE_IDENTIFIER = "economicNatureIdentifier";
	public static final String FIELD_REVENUE_AMOUNT = "revenueAmount";
	
	public static final String ENTITY_NAME = "RegulatoryActResourceImpl";
	public static final String TABLE_NAME = "VMA_ACTE_GESTION_LIGNE_RECETTE";
	
	public static final String COLUMN_ACT_IDENTIFIER = "acte";
	public static final String COLUMN_YEAR = "exercice";
	public static final String COLUMN_ACTIVITY_IDENTIFIER = "activite";
	public static final String COLUMN_ECONOMIC_NATURE_IDENTIFIER = "nature_economique";
	public static final String COLUMN_REVENUE_AMOUNT = "montant";
}