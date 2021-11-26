package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetaryActImpl;

public abstract class AbstractBudgetaryActVersionDtoImplMapperImpl implements BudgetaryActVersionDtoImplMapper, Serializable {

	@Inject
	BudgetaryActDtoImplMapper budgetaryActDtoImplMapper;

	@Override
	public BudgetaryActImpl map(BudgetaryActDtoImpl entity) {
		return budgetaryActDtoImplMapper.mapDestinationToSource(entity);
	}

	@Override
	public BudgetaryActDtoImpl map(BudgetaryActImpl entity) {
		return budgetaryActDtoImplMapper.mapSourceToDestination(entity);
	}
}