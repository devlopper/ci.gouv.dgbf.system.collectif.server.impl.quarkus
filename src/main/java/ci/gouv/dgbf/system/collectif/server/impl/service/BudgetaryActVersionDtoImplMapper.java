package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetaryActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetaryActVersionImpl;

@org.mapstruct.Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BudgetaryActVersionDtoImplMapper extends Mapper<BudgetaryActVersionImpl, BudgetaryActVersionDtoImpl> {

	BudgetaryActDtoImpl map(BudgetaryActImpl entity);
	
	BudgetaryActImpl map(BudgetaryActDtoImpl entity);
	
}