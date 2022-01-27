package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.cyk.utility.persistence.entity.AbstractIdentifiableSystemScalarStringAuditedImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Resource;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Revenue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) 
@Entity(name = ResourceImpl.ENTITY_NAME) @Access(AccessType.FIELD)
@Table(name=ResourceImpl.TABLE_NAME,uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				ResourceImpl.COLUMN_ACT_VERSION,ResourceImpl.COLUMN_ACTIVITY_IDENTIFIER,ResourceImpl.COLUMN_ECONOMIC_NATURE_IDENTIFIER
		})
})
@NamedQueries(value = {
		@NamedQuery(name = ResourceImpl.QUERY_READ_BY_IDENTIIFERS,query = "SELECT t FROM ResourceImpl t WHERE t.identifier IN :identifiers")
})
@AttributeOverrides(value= {
		@AttributeOverride(name = ResourceImpl.FIELD___AUDIT_WHO__,column = @Column(name=ResourceImpl.COLUMN___AUDIT_WHO__,nullable = false))
		,@AttributeOverride(name = ResourceImpl.FIELD___AUDIT_WHAT__,column = @Column(name=ResourceImpl.COLUMN___AUDIT_WHAT__,nullable = false))
		,@AttributeOverride(name = ResourceImpl.FIELD___AUDIT_WHEN__,column = @Column(name=ResourceImpl.COLUMN___AUDIT_WHEN__,nullable = false))
		,@AttributeOverride(name = ResourceImpl.FIELD___AUDIT_FUNCTIONALITY__,column = @Column(name=ResourceImpl.COLUMN___AUDIT_FUNCTIONALITY__,nullable = false))
})
@NamedStoredProcedureQueries(value = {
		@NamedStoredProcedureQuery(
			name = ResourceImpl.STORED_PROCEDURE_QUERY_PROCEDURE_NAME_IMPORT
			,procedureName = ResourceImpl.STORED_PROCEDURE_QUERY_PROCEDURE_NAME_IMPORT
			,parameters = {
				@StoredProcedureParameter(name = ResourceImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHO , mode = ParameterMode.IN,type = String.class)
				,@StoredProcedureParameter(name = ResourceImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_FUNCTIONALITY , mode = ParameterMode.IN,type = String.class)
				,@StoredProcedureParameter(name = ResourceImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHAT , mode = ParameterMode.IN,type = String.class)
				,@StoredProcedureParameter(name = ResourceImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHEN , mode = ParameterMode.IN,type = java.sql.Date.class)
			}
		)
	})
public class ResourceImpl extends AbstractIdentifiableSystemScalarStringAuditedImpl implements Resource,Serializable {

	@NotNull @Column(name = COLUMN_ACTIVITY_IDENTIFIER,nullable = false)
	String activityIdentifier;
	
	@NotNull @Column(name = COLUMN_ECONOMIC_NATURE_IDENTIFIER,nullable = false)
	String economicNatureIdentifier;
	
	@Valid
	@Embedded
	@AttributeOverrides({@AttributeOverride(name = RevenueImpl.FIELD_ADJUSTMENT,column = @Column(name=COLUMN_REVENUE_ADJUSTMENT,nullable = false))})
	RevenueImpl revenue;
	
	@NotNull @ManyToOne @JoinColumn(name = COLUMN_ACT_VERSION)
	LegislativeActVersionImpl actVersion;
	
	@Transient String actAsString;
	@Transient String actVersionAsString;
	@Transient String sectionAsString;
	@Transient String budgetSpecializationUnitAsString;
	@Transient String activityAsString;
	@Transient String economicNatureAsString;
	
	@Override
	public ResourceImpl setIdentifier(String identifier) {
		return (ResourceImpl) super.setIdentifier(identifier);
	}
	
	@Override
	public Resource setRevenue(Revenue revenue) {
		this.revenue = (RevenueImpl) revenue;
		return this;
	}
	
	public RevenueImpl getRevenue(Boolean instantiateIfNull) {
		if(revenue == null && Boolean.TRUE.equals(instantiateIfNull))
			revenue = new RevenueImpl();
		return revenue;
	}
	
	@Override
	public Resource setActVersion(LegislativeActVersion actVersion) {
		this.actVersion = (LegislativeActVersionImpl) actVersion;
		return this;
	}
	
	public static final String FIELD_ACTIVITY_IDENTIFIER = "activityIdentifier";
	public static final String FIELD_ECONOMIC_NATURE_IDENTIFIER = "economicNatureIdentifier";
	
	public static final String FIELD_ACT_VERSION = "actVersion";
	public static final String FIELD_REVENUE = "revenue";
	public static final String FIELD_SECTION_AS_STRING = "sectionAsString";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING = "budgetSpecializationUnitAsString";
	public static final String FIELDS_STRINGS = "strings";
	public static final String FIELDS_AMOUNTS = "amounts";
	public static final String FIELDS_AMOUNTS_INITIAL_ACTUAL_MOVEMENT_ADJUSTMENT_ACTUAL_PLUS_ADJUSTMENT = "amountsInitialActualMovementAdjustmentActualPlusAdjustment";
	public static final String FIELDS_AMOUNTS_INITIAL = "amountsInitial";
	public static final String FIELDS_AMOUNTS_ACTUAL = "amountsActual";
	public static final String FIELDS_AMOUNTS_AVAILABLE = "amountsAvailable";
	public static final String FIELDS_AMOUNTS_MOVEMENT_INCLUDED = "amountsMovementIncluded";
	public static final String FIELDS_AUDITS_AS_STRINGS = "auditsAsStrings";
		
	public static final String ENTITY_NAME = "ResourceImpl";
	public static final String TABLE_NAME = "TA_RECETTE";
	
	public static final String COLUMN_IDENTIFIER = "IDENTIFIANT";
	public static final String COLUMN_ACTIVITY_IDENTIFIER = "ACTIVITE";
	public static final String COLUMN_ECONOMIC_NATURE_IDENTIFIER = "NATURE_ECONOMIQUE";
	public static final String COLUMN_REVENUE_ADJUSTMENT = RevenueImpl.COLUMN_ADJUSTMENT;
	public static final String COLUMN_ACT_VERSION = "VERSION_COLLECTIF";
	
	public static final String COLUMN___AUDIT_WHO__ = "AUDIT_ACTEUR";
	public static final String COLUMN___AUDIT_WHAT__ = "AUDIT_ACTION";
	public static final String COLUMN___AUDIT_FUNCTIONALITY__ = "AUDIT_FONCTIONNALITE";
	public static final String COLUMN___AUDIT_WHEN__ = "AUDIT_DATE";
	
	public static final String QUERY_READ_BY_IDENTIIFERS = ENTITY_NAME+".readByIdentifiers";
	
	public static final String[] VIEW_FIELDS_NAMES = {FIELDS_STRINGS,FIELDS_AMOUNTS_INITIAL_ACTUAL_MOVEMENT_ADJUSTMENT_ACTUAL_PLUS_ADJUSTMENT};
	
	public static final String STORED_PROCEDURE_QUERY_PROCEDURE_NAME_IMPORT = "PA_IMPORTER_RECETTE";
	public static final String STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHO = "audit_acteur";
	public static final String STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_FUNCTIONALITY = "audit_fonctionnalite";
	public static final String STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHAT = "audit_action";
	public static final String STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHEN = "audit_date";
}