package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;
import org.mapstruct.ReportingPolicy;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetCategoryImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;

@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BudgetSpecializationUnitDtoImplMapper extends Mapper<BudgetSpecializationUnitImpl, BudgetSpecializationUnitDtoImpl> {

	SectionDtoImpl map(SectionImpl entity);	
	SectionImpl map(SectionDtoImpl entity);
	
	BudgetCategoryDtoImpl map(BudgetCategoryImpl entity);	
	BudgetCategoryImpl map(BudgetCategoryDtoImpl entity);
}