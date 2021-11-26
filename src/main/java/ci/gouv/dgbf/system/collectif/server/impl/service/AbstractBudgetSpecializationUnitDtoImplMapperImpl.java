package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;

public abstract class AbstractBudgetSpecializationUnitDtoImplMapperImpl implements BudgetSpecializationUnitDtoImplMapper, Serializable {

	@Inject
	SectionDtoImplMapper sectionDtoImplMapper;

	@Override
	public SectionImpl map(SectionDtoImpl section) {
		return sectionDtoImplMapper.mapDestinationToSource(section);
	}
	
	@Override
	public SectionDtoImpl map(SectionImpl section) {
		return sectionDtoImplMapper.mapSourceToDestination(section);
	}
}