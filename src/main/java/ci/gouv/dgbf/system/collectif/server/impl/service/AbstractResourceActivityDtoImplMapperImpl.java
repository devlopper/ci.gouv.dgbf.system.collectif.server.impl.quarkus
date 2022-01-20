package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EconomicNatureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;

public abstract class AbstractResourceActivityDtoImplMapperImpl implements ResourceActivityDtoImplMapper,Serializable {

	@Inject SectionDtoImplMapper sectionDtoImplMapper;
	@Inject BudgetSpecializationUnitDtoImplMapper budgetSpecializationUnitDtoImplMapper;
	@Inject EconomicNatureDtoImplMapper economicNatureDtoImplMapper;
	
	@Override
	public SectionImpl map(SectionDtoImpl entity) {
		return sectionDtoImplMapper.mapDestinationToSource(entity);
	}
	
	@Override
	public SectionDtoImpl map(SectionImpl entity) {
		return sectionDtoImplMapper.mapSourceToDestination(entity);
	}
	
	@Override
	public BudgetSpecializationUnitImpl map(BudgetSpecializationUnitDtoImpl entity) {
		return budgetSpecializationUnitDtoImplMapper.mapDestinationToSource(entity);
	}
	
	@Override
	public BudgetSpecializationUnitDtoImpl map(BudgetSpecializationUnitImpl entity) {
		return budgetSpecializationUnitDtoImplMapper.mapSourceToDestination(entity);
	}
	
	@Override
	public ArrayList<EconomicNatureDtoImpl> mapEconomicNatures(ArrayList<EconomicNatureImpl> entity) {
		return (ArrayList<EconomicNatureDtoImpl>) economicNatureDtoImplMapper.mapSourcesToDestinations(entity);
	}
	
	@Override
	public ArrayList<EconomicNatureImpl> mapEconomicNaturesDtos(ArrayList<EconomicNatureDtoImpl> entity) {
		return (ArrayList<EconomicNatureImpl>) economicNatureDtoImplMapper.mapDestinationsToSources(entity);
	}
}