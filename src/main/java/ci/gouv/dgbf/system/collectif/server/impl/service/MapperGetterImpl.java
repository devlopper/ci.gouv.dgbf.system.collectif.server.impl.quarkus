package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cyk.quarkus.extension.service.core_.AbstractMapperGetterImpl;

import io.quarkus.arc.Unremovable;

@ApplicationScoped @Unremovable
public class MapperGetterImpl extends AbstractMapperGetterImpl implements Serializable {

	@Inject LegislativeActDtoImplMapper legislativeActDtoImplMapper;
	@Inject LegislativeActVersionDtoImplMapper legislativeActVersionDtoImplMapper;
	@Inject ExpenditureDtoImplMapper expenditureDtoImplMapper;
	@Inject ResourceDtoImplMapper resourceDtoImplMapper;
	@Inject GeneratedActDtoImplMapper generatedActDtoImplMapper;
	
	@Inject SectionDtoImplMapper sectionDtoImplMapper;	
	@Inject BudgetSpecializationUnitDtoImplMapper budgetSpecializationUnitDtoImplMapper;
	@Inject ActionDtoImplMapper actionDtoImplMapper;
	@Inject ActivityDtoImplMapper activityDtoImplMapper;
	@Inject ResourceActivityDtoImplMapper resourceActivityDtoImplMapper;
	@Inject ExpenditureNatureDtoImplMapper expenditureNatureDtoImplMapper;
	@Inject EconomicNatureDtoImplMapper economicNatureDtoImplMapper;
	@Inject FundingSourceDtoImplMapper fundingSourceDtoImplMapper;
	@Inject LessorDtoImplMapper lessorDtoImplMapper;
	@Inject RegulatoryActDtoImplMapper regulatoryActDtoImplMapper;
	@Inject ExerciseDtoImplMapper exerciseDtoImplMapper;
}