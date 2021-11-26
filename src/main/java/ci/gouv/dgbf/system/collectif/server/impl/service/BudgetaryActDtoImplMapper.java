package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetaryActImpl;

@org.mapstruct.Mapper
public interface BudgetaryActDtoImplMapper extends Mapper<BudgetaryActImpl, BudgetaryActDtoImpl> {

	//BudgetaryActVersionDto mapBudgetaryActVersions(BudgetaryActVersion version);
	
}