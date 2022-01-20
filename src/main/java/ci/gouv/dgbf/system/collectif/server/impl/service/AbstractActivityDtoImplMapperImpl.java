package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.AdministrativeUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EconomicNatureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureNatureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.FundingSourceImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LessorImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;

public abstract class AbstractActivityDtoImplMapperImpl implements ActivityDtoImplMapper,Serializable {

	@Inject ExpenditureNatureDtoImplMapper expenditureNatureDtoImplMapper;
	@Inject SectionDtoImplMapper sectionDtoImplMapper;
	@Inject AdministrativeUnitDtoImplMapper administrativeUnitDtoImplMapper;
	@Inject BudgetSpecializationUnitDtoImplMapper budgetSpecializationUnitDtoImplMapper;
	@Inject ActionDtoImplMapper actionDtoImplMapper;
	@Inject EconomicNatureDtoImplMapper economicNatureDtoImplMapper;
	@Inject FundingSourceDtoImplMapper fundingSourceDtoImplMapper;
	@Inject LessorDtoImplMapper lessorDtoImplMapper;
	
	@Override
	public ExpenditureNatureImpl map(ExpenditureNatureDtoImpl entity) {
		return expenditureNatureDtoImplMapper.mapDestinationToSource(entity);
	}
	
	@Override
	public ExpenditureNatureDtoImpl map(ExpenditureNatureImpl entity) {
		return expenditureNatureDtoImplMapper.mapSourceToDestination(entity);
	}
	
	@Override
	public SectionImpl map(SectionDtoImpl entity) {
		return sectionDtoImplMapper.mapDestinationToSource(entity);
	}
	
	@Override
	public SectionDtoImpl map(SectionImpl entity) {
		return sectionDtoImplMapper.mapSourceToDestination(entity);
	}
	
	@Override
	public AdministrativeUnitImpl map(AdministrativeUnitDtoImpl entity) {
		return administrativeUnitDtoImplMapper.mapDestinationToSource(entity);
	}
	
	@Override
	public AdministrativeUnitDtoImpl map(AdministrativeUnitImpl entity) {
		return administrativeUnitDtoImplMapper.mapSourceToDestination(entity);
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
	public ActionImpl map(ActionDtoImpl entity) {
		return actionDtoImplMapper.mapDestinationToSource(entity);
	}
	
	@Override
	public ActionDtoImpl map(ActionImpl entity) {
		return actionDtoImplMapper.mapSourceToDestination(entity);
	}
	
	@Override
	public ArrayList<EconomicNatureDtoImpl> mapEconomicNatures(ArrayList<EconomicNatureImpl> entity) {
		return (ArrayList<EconomicNatureDtoImpl>) economicNatureDtoImplMapper.mapSourcesToDestinations(entity);
	}
	
	@Override
	public ArrayList<EconomicNatureImpl> mapEconomicNaturesDtos(ArrayList<EconomicNatureDtoImpl> entity) {
		return (ArrayList<EconomicNatureImpl>) economicNatureDtoImplMapper.mapDestinationsToSources(entity);
	}
	
	@Override
	public ArrayList<FundingSourceDtoImpl> mapFundingSources(ArrayList<FundingSourceImpl> entity) {
		return (ArrayList<FundingSourceDtoImpl>) fundingSourceDtoImplMapper.mapSourcesToDestinations(entity);
	}
	
	@Override
	public ArrayList<FundingSourceImpl> mapFundingSourcesDtos(ArrayList<FundingSourceDtoImpl> entity) {
		return (ArrayList<FundingSourceImpl>) fundingSourceDtoImplMapper.mapDestinationsToSources(entity);
	}
	
	@Override
	public ArrayList<LessorDtoImpl> mapLessors(ArrayList<LessorImpl> entity) {
		return (ArrayList<LessorDtoImpl>) lessorDtoImplMapper.mapSourcesToDestinations(entity);
	}
	
	@Override
	public ArrayList<LessorImpl> mapLessorsDtos(ArrayList<LessorDtoImpl> entity) {
		return (ArrayList<LessorImpl>) lessorDtoImplMapper.mapDestinationsToSources(entity);
	}
}