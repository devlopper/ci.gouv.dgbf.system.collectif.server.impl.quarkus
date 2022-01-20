package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;

@org.mapstruct.Mapper
public interface ActionDtoImplMapper extends Mapper<ActionImpl, ActionDtoImpl> {

	SectionDtoImpl map(SectionImpl entity);	
	SectionImpl map(SectionDtoImpl entity);
	
	BudgetSpecializationUnitDtoImpl map(BudgetSpecializationUnitImpl entity);	
	BudgetSpecializationUnitImpl map(BudgetSpecializationUnitDtoImpl entity);
}