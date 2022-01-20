package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;

public abstract class AbstractActionDtoImplMapperImpl implements ActionDtoImplMapper,Serializable {

	@Inject SectionDtoImplMapper sectionDtoImplMapper;
	@Inject BudgetSpecializationUnitDtoImplMapper budgetSpecializationUnitDtoImplMapper;
	
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
}