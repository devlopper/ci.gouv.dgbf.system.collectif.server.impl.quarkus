package ci.gouv.dgbf.system.collectif.server.impl.persistence;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Action;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Activity;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Resource;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceActivity;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @ci.gouv.dgbf.system.collectif.server.api.System @Unremovable
public class TransientFieldsProcessorImpl extends org.cyk.utility.persistence.server.TransientFieldsProcessorImpl implements Serializable {

	@Override
	protected void __process__(Class<?> klass,Collection<?> objects,Filter filter, Collection<String> fieldsNames) {
		if(Expenditure.class.equals(klass) || ExpenditureImpl.class.equals(klass))
			processExpenditures(CollectionHelper.cast(ExpenditureImpl.class, objects),fieldsNames);
		else if(Resource.class.equals(klass) || ResourceImpl.class.equals(klass))
			processResources(CollectionHelper.cast(ResourceImpl.class, objects),fieldsNames);
		else if(LegislativeAct.class.equals(klass) || LegislativeActImpl.class.equals(klass))
			processLegislativeActs(CollectionHelper.cast(LegislativeActImpl.class, objects),fieldsNames);
		else if(LegislativeActVersion.class.equals(klass) || LegislativeActVersionImpl.class.equals(klass))
			processLegislativeActVersions(CollectionHelper.cast(LegislativeActVersionImpl.class, objects),fieldsNames);
		else if(Activity.class.equals(klass) || ActivityImpl.class.equals(klass))
			processActivities(CollectionHelper.cast(ActivityImpl.class, objects),fieldsNames);
		else if(ResourceActivity.class.equals(klass) || ResourceActivityImpl.class.equals(klass))
			processResourceActivities(CollectionHelper.cast(ResourceActivityImpl.class, objects),fieldsNames);
		else if(Action.class.equals(klass) || ActionImpl.class.equals(klass))
			processActions(CollectionHelper.cast(ActionImpl.class, objects),fieldsNames);
		else if(BudgetSpecializationUnit.class.equals(klass) || BudgetSpecializationUnitImpl.class.equals(klass))
			processBudgetSpecializationUnits(CollectionHelper.cast(BudgetSpecializationUnitImpl.class, objects),fieldsNames);
		else if(RegulatoryAct.class.equals(klass) || RegulatoryActImpl.class.equals(klass))
			processRegulatoryActs(CollectionHelper.cast(RegulatoryActImpl.class, objects),filter,fieldsNames);
		else
			super.__process__(klass,objects,filter, fieldsNames);
	}
	
	public void processLegislativeActs(Collection<LegislativeActImpl> legislativeActs,Collection<String> fieldsNames) {
		for(String fieldName : fieldsNames) {
			if(LegislativeActImpl.FIELD_DEFAULT_VERSION_IDENTIFIER.equals(fieldName))
				new LegislativeActImplDefaultVersionIdentifierReader().readThenSet(legislativeActs, null);
			else if(LegislativeActImpl.FIELD_IN_PROGRESS_AS_STRING.equals(fieldName))
				new LegislativeActImplInProgressAsStringReader().readThenSet(legislativeActs, null);
			else if(LegislativeActImpl.FIELDS_STRINGS.equals(fieldName))
				new LegislativeActImplAsStringsReader().readThenSet(legislativeActs, null);
			else if(LegislativeActImpl.FIELDS_AMOUNTS.equals(fieldName))
				LegislativeActPersistenceImpl.readAmounts(legislativeActs);
			else if(LegislativeActImpl.FIELDS_AMOUNTS_MOVEMENT_INCLUDED.equals(fieldName))
				new LegislativeActImplAmountsMovementIncludedReader().readThenSet(legislativeActs, null);
			else if(LegislativeActImpl.FIELD___AUDIT__.equals(fieldName))
				new LegislativeActImplAuditReader().readThenSet(legislativeActs, null);
		}
	}
	
	public void processLegislativeActVersions(Collection<LegislativeActVersionImpl> legislativeActVersions,Collection<String> fieldsNames) {
		for(String fieldName : fieldsNames) {
			if(LegislativeActVersionImpl.FIELD_ACT_IDENTIFIER.equals(fieldName))
				new LegislativeActVersionImplLegislativeActIdentifierReader().readThenSet(legislativeActVersions, null);
			else if(LegislativeActVersionImpl.FIELDS_ACT_AS_STRING_CODE_NAME_NUMBER.equals(fieldName))
				new LegislativeActVersionImplActAsStringCodeNameNumberReader().readThenSet(legislativeActVersions, null);
			else if(LegislativeActVersionImpl.FIELDS_CODE_NAME_NUMBER.equals(fieldName))
				new LegislativeActVersionImplCodeNameNumberReader().readThenSet(legislativeActVersions, null);
			else if(LegislativeActVersionImpl.FIELD_GENERATED_ACT_COUNT.equals(fieldName))
				new LegislativeActVersionImplGeneratedCountReader().readThenSet(legislativeActVersions, null);
			else if(LegislativeActVersionImpl.FIELDS_GENERATED_ACT_COUNT_ACT_GENERATABLE_GENERATED_ACT_DELETABLE.equals(fieldName))
				new LegislativeActVersionImplGeneratedCountActGeneratableGeneratedActDeletableReader().readThenSet(legislativeActVersions, null);
		}
	}
	
	public void processActivities(Collection<ActivityImpl> activities,Collection<String> fieldsNames) {
		for(String fieldName : fieldsNames) {
			if(ActivityImpl.FIELDS_SECTION_ADMINISTRATIVE_UNIT_EXPENDITURE_NATURE_BUDGET_SPECIALIZATION_UNIT_ACTION.equals(fieldName))
				new ActivityImplSectionAdministrativeUnitExpenditureNatureBudgetSpecializationUnitActionReader().readThenSet(activities, null);
			else if(ActivityImpl.FIELD_ECONOMIC_NATURES.equals(fieldName))
				new ActivityImplEconomicNaturesReader().readThenSet(activities, null);
			else if(ActivityImpl.FIELD_FUNDING_SOURCES.equals(fieldName))
				new ActivityImplFundingSourcesReader().readThenSet(activities, null);
			else if(ActivityImpl.FIELD_LESSORS.equals(fieldName))
				new ActivityImplLessorsReader().readThenSet(activities, null);
		}
	}
	
	public void processResourceActivities(Collection<ResourceActivityImpl> resourcesActivities,Collection<String> fieldsNames) {
		for(String fieldName : fieldsNames) {
			if(ResourceActivityImpl.FIELDS_SECTION_BUDGET_SPECIALIZATION_UNIT.equals(fieldName))
				new ResourceActivityImplSectionBudgetSpecializationUnitReader().readThenSet(resourcesActivities, null);
			else if(ResourceActivityImpl.FIELD_ECONOMIC_NATURES.equals(fieldName))
				new ResourceActivityImplEconomicNaturesReader().readThenSet(resourcesActivities, null);
		}
	}
	
	public void processActions(Collection<ActionImpl> actions,Collection<String> fieldsNames) {
		for(String fieldName : fieldsNames) {
			if(ActionImpl.FIELDS_SECTION_BUDGET_SPECIALIZATION_UNIT.equals(fieldName))
				new ActionImplSectionBudgetSpecializationUnitReader().readThenSet(actions, null);
		}
	}
	
	public void processBudgetSpecializationUnits(Collection<BudgetSpecializationUnitImpl> budgetSpecializationUnits,Collection<String> fieldsNames) {
		for(String fieldName : fieldsNames) {
			if(BudgetSpecializationUnitImpl.FIELD_SECTION.equals(fieldName))
				new BudgetSpecializationUnitImplSectionReader().readThenSet(budgetSpecializationUnits, null);
		}
	}
	
	public void processExpenditures(Collection<ExpenditureImpl> expenditures,Collection<String> fieldsNames) {
		for(String fieldName : fieldsNames) {
			if(ExpenditureImpl.FIELDS_STRINGS.equals(fieldName))
				new ExpenditureImplAsStringsReader().readThenSet(expenditures, null);
			else if(ExpenditureImpl.FIELDS_AMOUNTS.equals(fieldName))
				ExpenditurePersistenceImpl.readAmounts(expenditures);
			else if(ExpenditureImpl.FIELDS_AMOUNTS_INITIAL_ACTUAL_MOVEMENT_ADJUSTMENT_ACTUAL_PLUS_ADJUSTMENT.equals(fieldName))
				new ExpenditureImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentReader().readThenSet(expenditures, null);
			else if(ExpenditureImpl.FIELDS_AMOUNTS_ACTUAL.equals(fieldName))
				new ExpenditureImplAmountsActualReader().readThenSet(expenditures, null);
			else if(ExpenditureImpl.FIELDS_AMOUNTS_AVAILABLE.equals(fieldName))
				new ExpenditureImplAmountsAvailableReader().readThenSet(expenditures, null);
			else if(ExpenditureImpl.FIELDS_AMOUNTS_INITIAL.equals(fieldName))
				new ExpenditureImplAmountsInitialReader().readThenSet(expenditures, null);
			else if(ExpenditureImpl.FIELDS_AMOUNTS_MOVEMENT_INCLUDED.equals(fieldName))
				new ExpenditureImplAmountsMovementIncludedReader().readThenSet(expenditures, null);
			else if(ExpenditureImpl.FIELDS_AUDITS_AS_STRINGS.equals(fieldName))
				new ExpenditureImplAuditsAsStringsReader().readThenSet(expenditures, null);
			else if(ExpenditureImpl.FIELD___AUDIT__.equals(fieldName))
				new ExpenditureImplAuditReader().readThenSet(expenditures, null);
		}
	}
	
	public void processResources(Collection<ResourceImpl> resources,Collection<String> fieldsNames) {
		for(String fieldName : fieldsNames) {
			if(ResourceImpl.FIELDS_STRINGS.equals(fieldName))
				new ResourceImplAsStringsReader().readThenSet(resources, null);
			else if(ResourceImpl.FIELDS_AMOUNTS.equals(fieldName))
				ResourcePersistenceImpl.readAmounts(resources);
			else if(ResourceImpl.FIELDS_AMOUNTS_INITIAL_ACTUAL_MOVEMENT_ADJUSTMENT_ACTUAL_PLUS_ADJUSTMENT.equals(fieldName))
				new ResourceImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentReader().readThenSet(resources, null);
		}
	}
	
	public void processRegulatoryActs(Collection<RegulatoryActImpl> regulatoryActs,Filter filter,Collection<String> fieldsNames) {
		String legislativeActVersionIdentifier = (String) filter.getFieldValue(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER);
		Map<String,Object> map = StringHelper.isBlank(legislativeActVersionIdentifier) ? null  : Map.of(Parameters.LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersionIdentifier);
		for(String fieldName : fieldsNames) {
			if(RegulatoryActImpl.FIELDS_YEAR_NAME_ENTRY_AUTHORIZATION_AMOUNT_PAYMENT_CREDIT_AMOUNT_INCLUDED_AND_INCLUDED_AS_STRING.equals(fieldName))				
				new RegulatoryActImplYearNameEntryAuthorizationAmountPaymentCreditAmountIncludedAndIncludedAsStringReader().readThenSet(regulatoryActs, map);
			else if(RegulatoryActImpl.FIELDS_AUDITS_AS_STRINGS.equals(fieldName))
				new RegulatoryActImplAuditsAsStringsReader().readThenSet(regulatoryActs, map);
			else if(RegulatoryActImpl.FIELD___AUDIT__.equals(fieldName))
				new RegulatoryActImplAuditReader().readThenSet(regulatoryActs, map);
		}
	}
}