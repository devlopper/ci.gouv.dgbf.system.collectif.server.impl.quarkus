package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder.Arguments;
import org.cyk.utility.persistence.server.query.string.RuntimeQueryStringBuilder;
import org.cyk.utility.persistence.server.query.string.WhereStringBuilder.Predicate;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Parameters;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @ci.gouv.dgbf.system.collectif.server.api.System @Unremovable
public class RuntimeQueryStringBuilderImpl extends RuntimeQueryStringBuilder.AbstractImpl implements Serializable {

	@Inject BudgetaryActPersistence budgetaryActPersistence;
	@Inject BudgetaryActVersionPersistence budgetaryActVersionPersistence;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	
	@Override
	protected void setTuple(QueryExecutorArguments arguments, Arguments builderArguments) {
		super.setTuple(arguments, builderArguments);
		if(Boolean.TRUE.equals(budgetaryActPersistence.isProcessable(arguments))) {
			builderArguments.getTuple(Boolean.TRUE).add(String.format("%s t",BudgetaryActImpl.ENTITY_NAME));
			String versionIdentifier = (String) arguments.getFilterFieldValue(Parameters.BUDGETARY_ACT_VERSION_IDENTIFIER);
			if(StringHelper.isNotBlank(versionIdentifier)) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s bav ON bav.budgetaryAct = t AND bav.identifier = '%s'",BudgetaryActVersionImpl.ENTITY_NAME
						,versionIdentifier));
				arguments.removeFilterFields(Parameters.BUDGETARY_ACT_VERSION_IDENTIFIER);
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
			if(StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.SECTION_IDENTIFIER)) 
					|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.EXPENDITURE_NATURE_IDENTIFIER))
					|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.BUDGET_SPECIALIZATION_UNIT_IDENTIFIER))
					|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.ACTION_IDENTIFIER))
					|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.ACTIVITY_IDENTIFIER))
					|| StringHelper.isNotBlank((String)arguments.getFilterFieldValue(Parameters.ACTIVITY_CATEGORY_IDENTIFIER))
					) {
				builderArguments.getTuple().addJoins(String.format("JOIN %s v ON v.identifier = t.identifier",ExpenditureView.ENTITY_NAME));
			}
		}
	}
	
	@Override
	protected void populatePredicate(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		super.populatePredicate(arguments, builderArguments, predicate, filter);
		if(Boolean.TRUE.equals(expenditurePersistence.isProcessable(arguments)))
			populatePredicateExpenditure(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(budgetaryActVersionPersistence.isProcessable(arguments)))
			populatePredicateBudgetaryActVersion(arguments, builderArguments, predicate, filter);
		else if(Boolean.TRUE.equals(budgetSpecializationUnitPersistence.isProcessable(arguments)))
			populatePredicateBudgetSpecializationUnit(arguments, builderArguments, predicate, filter);
	}
	
	@Override
	protected void setOrder(QueryExecutorArguments arguments, Arguments builderArguments) {
		if(expenditurePersistence.getQueryIdentifierReadDynamic().equals(arguments.getQuery().getIdentifier())) {
			builderArguments.getOrder(Boolean.TRUE).asc("t", "identifier");
		}
		super.setOrder(arguments, builderArguments);
	}
	
	/**/
	
	public static void populatePredicateExpenditure(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGETARY_ACT_IDENTIFIER,"t"
				,FieldHelper.join(ExpenditureImpl.FIELD_BUDGETARY_ACT_VERSION,BudgetaryActVersionImpl.FIELD_BUDGETARY_ACT,BudgetaryActImpl.FIELD_IDENTIFIER));
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGETARY_ACT_VERSION_IDENTIFIER,"t"
				,FieldHelper.join(ExpenditureImpl.FIELD_BUDGETARY_ACT_VERSION,BudgetaryActImpl.FIELD_IDENTIFIER));
		
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
		
		/*
		if(arguments.getFilterField(ExpenditureQuerier.PARAMETER_NAME_IS_AVAILABLE_PLUS_ADJUSTMENT_GREATER_THAN_OR_EQUAL_ZERO) != null) {
			Boolean available = arguments.getFilterFieldValueAsBoolean(null,ExpenditureQuerier.PARAMETER_NAME_IS_AVAILABLE_PLUS_ADJUSTMENT_GREATER_THAN_OR_EQUAL_ZERO);
			if(available == null) {
				
			}else {
				predicate.add(String.format("t.entryAuthorization.adjustment + v.entryAuthorizationAvailable %s 0",Boolean.TRUE.equals(available) ? ">=" : "<"));
			}
		} */
	}
	
	public static void populatePredicateBudgetaryAct(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		if(arguments.getFilterField(Parameters.BUDGETARY_ACT_VERSION_IDENTIFIER) != null) {
			predicate.add(String.format("EXISTS(SELECT bav.identifier FROM %s bav WHERE bav.budgetaryAct.identifier = :%s)",arguments.getFilterFieldValue(Parameters.BUDGETARY_ACT_VERSION_IDENTIFIER)));
		}
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGETARY_ACT_IDENTIFIER,"t"
				,FieldHelper.join(BudgetaryActVersionImpl.FIELD_BUDGETARY_ACT,BudgetaryActImpl.FIELD_IDENTIFIER));
	}
	
	public static void populatePredicateBudgetaryActVersion(QueryExecutorArguments arguments, Arguments builderArguments, Predicate predicate,Filter filter) {
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.BUDGETARY_ACT_IDENTIFIER,"t"
				,FieldHelper.join(BudgetaryActVersionImpl.FIELD_BUDGETARY_ACT,BudgetaryActImpl.FIELD_IDENTIFIER));
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
		addEqualsIfFilterHasFieldWithPath(arguments, builderArguments, predicate, filter, Parameters.ACTIVITY_IDENTIFIER,"t"
				,ActivityImpl.FIELD_ACTION_IDENTIFIER);
	}
}