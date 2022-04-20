package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetCategoryImpl;

@org.mapstruct.Mapper
public interface BudgetCategoryDtoImplMapper extends Mapper<BudgetCategoryImpl, BudgetCategoryDtoImpl> {

}