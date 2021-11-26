package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;

@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BudgetSpecializationUnitDtoImplMapper extends Mapper<BudgetSpecializationUnitImpl, BudgetSpecializationUnitDtoImpl> {

	@Override @Mapping(target = "section",ignore = true)
	BudgetSpecializationUnitImpl mapDestinationToSource(BudgetSpecializationUnitDtoImpl arg0);
	
	@Override @Mapping(target = "section",ignore = true)
	BudgetSpecializationUnitDtoImpl mapSourceToDestination(BudgetSpecializationUnitImpl arg0);
	
	SectionDtoImpl map(SectionImpl section);
	
	SectionImpl map(SectionDtoImpl section);
}