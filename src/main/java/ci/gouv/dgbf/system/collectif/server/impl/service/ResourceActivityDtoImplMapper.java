package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.util.ArrayList;

import org.cyk.utility.mapping.Mapper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EconomicNatureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceActivityImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;

@org.mapstruct.Mapper
public interface ResourceActivityDtoImplMapper extends Mapper<ResourceActivityImpl, ResourceActivityDtoImpl> {
	
	SectionDtoImpl map(SectionImpl entity);	
	SectionImpl map(SectionDtoImpl entity);
	
	BudgetSpecializationUnitDtoImpl map(BudgetSpecializationUnitImpl entity);	
	BudgetSpecializationUnitImpl map(BudgetSpecializationUnitDtoImpl entity);

	ArrayList<EconomicNatureDtoImpl> mapEconomicNatures(ArrayList<EconomicNatureImpl> entity);	
	ArrayList<EconomicNatureImpl> mapEconomicNaturesDtos(ArrayList<EconomicNatureDtoImpl> entity);
}