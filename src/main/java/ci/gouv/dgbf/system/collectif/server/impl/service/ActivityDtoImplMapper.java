package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.util.ArrayList;

import org.cyk.utility.mapping.Mapper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActivityImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.AdministrativeUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EconomicNatureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureNatureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.FundingSourceImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LessorImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;

@org.mapstruct.Mapper
public interface ActivityDtoImplMapper extends Mapper<ActivityImpl, ActivityDtoImpl> {
	
	ExpenditureNatureDtoImpl map(ExpenditureNatureImpl entity);	
	ExpenditureNatureImpl map(ExpenditureNatureDtoImpl entity);
	
	SectionDtoImpl map(SectionImpl entity);	
	SectionImpl map(SectionDtoImpl entity);
	
	AdministrativeUnitDtoImpl map(AdministrativeUnitImpl entity);	
	AdministrativeUnitImpl map(AdministrativeUnitDtoImpl entity);
	
	BudgetSpecializationUnitDtoImpl map(BudgetSpecializationUnitImpl entity);	
	BudgetSpecializationUnitImpl map(BudgetSpecializationUnitDtoImpl entity);
	
	ActionDtoImpl map(ActionImpl entity);	
	ActionImpl map(ActionDtoImpl entity);
	
	ArrayList<EconomicNatureDtoImpl> mapEconomicNatures(ArrayList<EconomicNatureImpl> entity);	
	ArrayList<EconomicNatureImpl> mapEconomicNaturesDtos(ArrayList<EconomicNatureDtoImpl> entity);
	
	ArrayList<FundingSourceDtoImpl> mapFundingSources(ArrayList<FundingSourceImpl> entity);	
	ArrayList<FundingSourceImpl> mapFundingSourcesDtos(ArrayList<FundingSourceDtoImpl> entity);
	
	ArrayList<LessorDtoImpl> mapLessors(ArrayList<LessorImpl> entity);	
	ArrayList<LessorImpl> mapLessorsDtos(ArrayList<LessorDtoImpl> entity);
}