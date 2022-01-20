package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder.Arguments;
import org.cyk.utility.persistence.server.query.string.RuntimeQueryStringBuilder;
import org.cyk.utility.persistence.server.query.string.WhereStringBuilder.Predicate;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ActionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.FundingSourcePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LessorPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourcePersistence;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @ci.gouv.dgbf.system.collectif.server.api.System @Unremovable
public class RuntimeQueryStringBuilderImpl extends RuntimeQueryStringBuilder.AbstractImpl implements Serializable {

	@Inject LegislativeActPersistence actPersistence;
	@Inject LegislativeActVersionPersistence actVersionPersistence;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	@Inject ActionPersistence actionPersistence;
	@Inject ActivityPersistence activityPersistence;
	@Inject ResourceActivityPersistence resourceActivityPersistence;
	@Inject EconomicNaturePersistence economicNaturePersistence;
	@Inject FundingSourcePersistence fundingSourcePersistence;
	@Inject LessorPersistence lessorPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ResourcePersistence resourcePersistence;
	@Inject RegulatoryActPersistence regulatoryActPersistence;
	
	@Override
	protected void setTuple(QueryExecutorArguments arguments, Arguments builderArguments) {
		super.setTuple(arguments, builderArguments);
		if(Boolean.TRUE.equals(actPersistence.isProcessable(arguments))) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",LegislativeActImpl.ENTITY_NAME));
			String versionIdentifier = (String) arguments.getFilterFieldValue(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER);
			if(StringHelper.isNotBlank(versionIdentifier)) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s bav ON bav.%s = t AND bav.identifier = '%s'",LegislativeActVersionImpl.ENTITY_NAME
						,LegislativeActVersionImpl.FIELD_ACT,versionIdentifier));
				arguments.removeFilterFields(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER);
			}
		}
		
		if(Boolean.TRUE.equals(expenditurePersistence.isProcessable(arguments))) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",ExpenditureImpl.ENTITY_NAME));
			/*String identifier = (String) arguments.getFilterFieldValue(Parameters.SECTION_IDENTIFIER);
			if(StringHelper.isNotBlank(identifier)) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s v ON v.identifier = t.identifier AND v.sectionIdentifier = '%s'",ExpenditureView.ENTITY_NAME
						,identifier));
				arguments.removeFilterFields(Parameters.SECTION_IDENTIFIER);
			}
			*/
			if(Boolean.TRUE.equals(isExpenditureJoinedToView(arguments, builderArguments))) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s v ON v.identifier = t.identifier",ExpenditureView.ENTITY_NAME));
			}
		}else if(Boolean.TRUE.equals(resourcePersistence.isProcessable(arguments))) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",ResourceImpl.ENTITY_NAME));
			if(Boolean.TRUE.equals(isResourceJoinedToView(arguments, builderArguments))) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s v ON v.identifier = t.identifier",ResourceView.ENTITY_NAME));
			}
		}
	}
	
	@Override
	protected void populatePredicate(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		super.populatePredicate(arguments, builderArguments, predicate, filter);
		if(Boolean.TRUE.equals(expenditurePersistence.isProcessable(arguments)))
			populatePredicateExpenditure(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(resourcePersistence.isProcessable(arguments)))
			populatePredicateResource(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(actVersionPersistence.isProcessable(arguments)))
			populatePredicateLegislativeActVersion(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(budgetSpecializationUnitPersistence.isProcessable(arguments)))
			populatePredicateBudgetSpecializationUnit(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(actionPersistence.isProcessable(arguments)))
			populatePredicateAction(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(activityPersistence.isProcessable(arguments)))
			populatePredicateActivity(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(resourceActivityPersistence.isProcessable(arguments)))
			populatePredicateResourceActivity(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(economicNaturePersistence.isProcessable(arguments)))
			populatePredicateEconomicNature(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(fundingSourcePersistence.isProcessable(arguments)))
			populatePredicateFundingSource(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(lessorPersistence.isProcessable(arguments)))
			populatePredicateLessor(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(regulatoryActPersistence.isProcessable(arguments)))
			populatePredicateRegulatoryAct(arguments, builderArguments, predicate, filter);
	}
	
	@Override
	protected void setOrder(QueryExecutorArguments arguments, Arguments builderArguments) {
		if(expenditurePersistence.getQueryIdentifierReadDynamic().equals(arguments.getQuery().getIdentifier())) {
			if(builderArguments.getOrder() == null || CollectionHelper.isEmpty(builderArguments.getOrder().getStrings())) {
				if(Boolean.TRUE.equals(isExpenditureJoinedToView(arguments, builderArguments)))
					builderArguments.getOrder(Boolean.TRUE).asc("v", ExpenditureView.FIELD_SECTION_CODE/*, ExpenditureView.FIELD_ADMINISTRATIVE_UNIT_CODE*/, ExpenditureView.FIELD_NATURE_CODE
							, ExpenditureView.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE, ExpenditureView.FIELD_ACTION_CODE, ExpenditureView.FIELD_ACTIVITY_CODE
							, ExpenditureView.FIELD_ECONOMIC_NATURE_CODE, ExpenditureView.FIELD_FUNDING_SOURCE_CODE, ExpenditureView.FIELD_LESSOR_CODE);
				else
					builderArguments.getOrder(Boolean.TRUE).asc("t", "identifier");
			}
		}else if(resourcePersistence.getQueryIdentifierReadDynamic().equals(arguments.getQuery().getIdentifier())) {
			if(builderArguments.getOrder() == null || CollectionHelper.isEmpty(builderArguments.getOrder().getStrings())) {
				if(Boolean.TRUE.equals(isResourceJoinedToView(arguments, builderArguments)))
					builderArguments.getOrder(Boolean.TRUE).asc("v", ResourceView.FIELD_SECTION_CODE, ResourceView.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE, ResourceView.FIELD_ACTIVITY_CODE, ResourceView.FIELD_ECONOMIC_NATURE_CODE);
				else
					builderArguments.getOrder(Boolean.TRUE).asc("t", "identifier");
			}
		}else if(Boolean.TRUE.equals(actVersionPersistence.isProcessable(arguments))) {
			Boolean latest = arguments.getFilterFieldValueAsBoolean(null,Parameters.LATEST_LEGISLATIVE_ACT_VERSION);
			if(Boolean.TRUE.equals(latest)) {
				builderArguments.getOrder(Boolean.TRUE).desc("t", LegislativeActVersionImpl.FIELD_CREATION_DATE);
				arguments.setNumberOfTuples(1);
			}
			arguments.removeFilterFields(Parameters.LATEST_LEGISLATIVE_ACT_VERSION);
		}
		
		super.setOrder(arguments, builderArguments);
	}
	
	/**/
	
	public static void populatePredicateExpenditure(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_IDENTIFIER,"t"
				,FieldHelper.join(ExpenditureImpl.FIELD_ACT_VERSION,LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_IDENTIFIER));
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"t"
				,FieldHelper.join(ExpenditureImpl.FIELD_ACT_VERSION,LegislativeActImpl.FIELD_IDENTIFIER));
		
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.SECTION_IDENTIFIER,"v"
				,ExpenditureView.FIELD_SECTION_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.EXPENDITURE_NATURE_IDENTIFIER,"v"
				,ExpenditureView.FIELD_NATURE_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ADMINISTRATIVE_UNIT_IDENTIFIER,"v"
				,ExpenditureView.FIELD_ADMINISTRATIVE_UNIT_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,"v"
				,ExpenditureView.FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ACTION_IDENTIFIER,"v"
				,ExpenditureView.FIELD_ACTION_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ACTIVITY_IDENTIFIER,"v"
				,ExpenditureView.FIELD_ACTIVITY_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ECONOMIC_NATURE_IDENTIFIER,"v"
				,ExpenditureView.FIELD_ECONOMIC_NATURE_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.FUNDING_SOURCE_IDENTIFIER,"v"
				,ExpenditureView.FIELD_FUNDING_SOURCE_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LESSOR_IDENTIFIER,"v"
				,ExpenditureView.FIELD_LESSOR_IDENTIFIER);
	}
	
	public static void populatePredicateResource(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_IDENTIFIER,"t"
				,FieldHelper.join(ExpenditureImpl.FIELD_ACT_VERSION,LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_IDENTIFIER));
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,"t"
				,FieldHelper.join(ExpenditureImpl.FIELD_ACT_VERSION,LegislativeActImpl.FIELD_IDENTIFIER));
		
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.SECTION_IDENTIFIER,"v"
				,ResourceView.FIELD_SECTION_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,"v"
				,ResourceView.FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ACTIVITY_IDENTIFIER,"v"
				,ResourceView.FIELD_ACTIVITY_IDENTIFIER);
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ECONOMIC_NATURE_IDENTIFIER,"v"
				,ResourceView.FIELD_ECONOMIC_NATURE_IDENTIFIER);
	}
	
	public static void populatePredicateLegislativeAct(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		if(arguments.getFilterField(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER) != null) {
			predicate.add(String.format("EXISTS(SELECT bav.identifier FROM %s bav WHERE bav.act.identifier = :%s)",arguments.getFilterFieldValue(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER)));
		}
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_IDENTIFIER,"t"
				,FieldHelper.join(LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_IDENTIFIER));
	}
	
	public static void populatePredicateLegislativeActVersion(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.LEGISLATIVE_ACT_IDENTIFIER,"t"
				,FieldHelper.join(LegislativeActVersionImpl.FIELD_ACT,LegislativeActImpl.FIELD_IDENTIFIER));
	}
	
	public static void populatePredicateBudgetSpecializationUnit(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.SECTION_IDENTIFIER,"t"
				,BudgetSpecializationUnitImpl.FIELD_SECTION_IDENTIFIER);
	}
	
	public static void populatePredicateAdministrativeUnit(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.SECTION_IDENTIFIER,"t"
				,AdministrativeUnitImpl.FIELD_SECTION_IDENTIFIER);
	}
	
	public static void populatePredicateAction(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,"t"
				,ActionImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER);
	}
	
	public static void populatePredicateActivity(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ACTION_IDENTIFIER,"t"
				,ActivityImpl.FIELD_ACTION_IDENTIFIER);
	}
	
	public static void populatePredicateResourceActivity(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,"t"
				,ResourceActivityImpl.FIELD_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER);
	}
	
	public static void populatePredicateEconomicNature(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		if(arguments.getFilterField(Parameters.ACTIVITY_IDENTIFIER) != null) {
			predicate.add(String.format("EXISTS(SELECT e.%1$s FROM %2$s e WHERE e.%3$s = t.%4$s AND e.%5$s = :%5$s)",ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.ENTITY_NAME,ExpenditureImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER
					,EconomicNatureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER));
			filter.addField(ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER, arguments.getFilterFieldValue(Parameters.ACTIVITY_IDENTIFIER));
		}
		if(arguments.getFilterField(Parameters.RESOURCE_ACTIVITY_IDENTIFIER) != null) {
			predicate.add(String.format("EXISTS(SELECT e.%1$s FROM %2$s e WHERE e.%3$s = t.%4$s AND e.%5$s = :%5$s)",ResourceImpl.FIELD_IDENTIFIER,ResourceImpl.ENTITY_NAME,ResourceImpl.FIELD_ECONOMIC_NATURE_IDENTIFIER
					,EconomicNatureImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER));
			filter.addField(ResourceImpl.FIELD_ACTIVITY_IDENTIFIER, arguments.getFilterFieldValue(Parameters.RESOURCE_ACTIVITY_IDENTIFIER));
		}
	}
	
	public static void populatePredicateFundingSource(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		if(arguments.getFilterField(Parameters.ACTIVITY_IDENTIFIER) != null) {
			predicate.add(String.format("EXISTS(SELECT e.%1$s FROM %2$s e WHERE e.%3$s = t.%4$s AND e.%5$s = :%5$s)",ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.ENTITY_NAME,ExpenditureImpl.FIELD_FUNDING_SOURCE_IDENTIFIER
					,FundingSourceImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER));
			filter.addField(ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER, arguments.getFilterFieldValue(Parameters.ACTIVITY_IDENTIFIER));
		}
	}
	
	public static void populatePredicateLessor(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		if(arguments.getFilterField(Parameters.ACTIVITY_IDENTIFIER) != null) {
			predicate.add(String.format("EXISTS(SELECT e.%1$s FROM %2$s e WHERE e.%3$s = t.%4$s AND e.%5$s = :%5$s)",ExpenditureImpl.FIELD_IDENTIFIER,ExpenditureImpl.ENTITY_NAME,ExpenditureImpl.FIELD_LESSOR_IDENTIFIER
					,LessorImpl.FIELD_IDENTIFIER,ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER));
			filter.addField(ExpenditureImpl.FIELD_ACTIVITY_IDENTIFIER, arguments.getFilterFieldValue(Parameters.ACTIVITY_IDENTIFIER));
		}
	}
	
	public static void populatePredicateRegulatoryAct(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.YEAR);
		String legislativeActVersionIdentifier = (String) arguments.getFilterFieldValue(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER);
		if(StringHelper.isNotBlank(legislativeActVersionIdentifier)) {
			predicate.add(String.format("EXISTS(SELECT lav.identifier FROM LegislativeActVersionImpl lav JOIN LegislativeActImpl la ON la = lav.act AND lav.identifier = :%s WHERE t.year = la.year)",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER));
			filter.addField(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, legislativeActVersionIdentifier);
		}
		
		Boolean included = arguments.getFilterFieldValueAsBoolean(null,Parameters.REGULATORY_ACT_INCLUDED);
		if(included != null) {
			if(StringHelper.isNotBlank(legislativeActVersionIdentifier)) {
				predicate.add(String.format("%s EXISTS(SELECT ralav.identifier FROM RegulatoryActLegislativeActVersionImpl ralav WHERE ralav.regulatoryAct = t AND ralav.legislativeActVersion.identifier = :%s AND ralav.included IS TRUE)"
						,included ? "" : "NOT",Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER));
				filter.addField(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER, legislativeActVersionIdentifier);
			}
		}
	}
	
	/**/
	
	public static Boolean isExpenditureJoinedToView(QueryExecutorArguments arguments, Arguments builderArguments) {
		if(CollectionHelper.isNotEmpty(arguments.getProjections()) 
				&& CollectionUtils.containsAny(arguments.getProjections().stream().map(projection -> projection.getFieldName()).collect(Collectors.toList()),ExpenditureImpl.VIEW_FIELDS_NAMES))
			return Boolean.TRUE;
		
		if(CollectionHelper.isNotEmpty(arguments.getProcessableTransientFieldsNames()) 
				&& CollectionUtils.containsAny(arguments.getProcessableTransientFieldsNames(),ExpenditureImpl.VIEW_FIELDS_NAMES))
			return Boolean.TRUE;
				
		if(StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.SECTION_IDENTIFIER)) 
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.EXPENDITURE_NATURE_IDENTIFIER))
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER))
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.ACTION_IDENTIFIER))
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.ACTIVITY_IDENTIFIER))
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.ACTIVITY_CATEGORY_IDENTIFIER)))
			return Boolean.TRUE;
		
		return Boolean.FALSE;
	}
	
	public static Boolean isResourceJoinedToView(QueryExecutorArguments arguments, Arguments builderArguments) {
		if(CollectionHelper.isNotEmpty(arguments.getProjections()) 
				&& CollectionUtils.containsAny(arguments.getProjections().stream().map(projection -> projection.getFieldName()).collect(Collectors.toList()),ResourceImpl.VIEW_FIELDS_NAMES))
			return Boolean.TRUE;
		
		if(CollectionHelper.isNotEmpty(arguments.getProcessableTransientFieldsNames()) 
				&& CollectionUtils.containsAny(arguments.getProcessableTransientFieldsNames(),ResourceImpl.VIEW_FIELDS_NAMES))
			return Boolean.TRUE;
				
		if(StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.SECTION_IDENTIFIER)) 
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER))
				|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.ACTIVITY_IDENTIFIER)))
			return Boolean.TRUE;
		
		return Boolean.FALSE;
	}
}