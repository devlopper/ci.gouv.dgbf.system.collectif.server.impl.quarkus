package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceGetterImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ActionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetCategoryPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ConfigurationPropertyPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExercisePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.FundingSourcePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LessorPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceActivityPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourcePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @Unremovable
public class SpecificPersistenceGetterImpl extends AbstractSpecificPersistenceGetterImpl implements Serializable {

	@Inject LegislativeActPersistence legislativeActPersistence;
	@Inject LegislativeActVersionPersistence legislativeActVersionPersistence;
	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject ResourcePersistence resourcePersistence;
	@Inject GeneratedActPersistence generatedActPersistence;
	@Inject GeneratedActExpenditurePersistence generatedActExpenditurePersistence;
	
	@Inject SectionPersistence sectionPersistence;
	@Inject BudgetCategoryPersistence budgetCategoryPersistence;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	@Inject ActionPersistence actionPersistence;
	@Inject ActivityPersistence activityPersistence;
	@Inject ResourceActivityPersistence resourceActivityPersistence;
	@Inject ExpenditureNaturePersistence expenditureNaturePersistence;
	@Inject EconomicNaturePersistence economicNaturePersistence;
	@Inject FundingSourcePersistence fundingSourcePersistence;
	@Inject LessorPersistence lessorPersistence;
	
	@Inject RegulatoryActPersistence regulatoryActPersistence;
	@Inject ExercisePersistence exercisePersistence;
	
	@Inject ConfigurationPropertyPersistence configurationPropertyPersistence;
}