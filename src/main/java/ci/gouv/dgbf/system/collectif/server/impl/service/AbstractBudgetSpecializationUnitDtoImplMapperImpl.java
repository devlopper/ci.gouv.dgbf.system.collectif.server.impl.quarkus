package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;

public abstract class AbstractBudgetSpecializationUnitDtoImplMapperImpl implements BudgetSpecializationUnitDtoImplMapper, Serializable {

	@Inject SectionDtoImplMapper sectionDtoImplMapper;
	
	@Override
	public SectionImpl map(SectionDtoImpl entity) {
		return sectionDtoImplMapper.mapDestinationToSource(entity);
	}
	
	@Override
	public SectionDtoImpl map(SectionImpl entity) {
		return sectionDtoImplMapper.mapSourceToDestination(entity);
	}
	
}