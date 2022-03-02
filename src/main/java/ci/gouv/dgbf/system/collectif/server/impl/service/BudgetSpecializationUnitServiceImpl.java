package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetSpecializationUnitDto;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetSpecializationUnitService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;

@Path(BudgetSpecializationUnitService.PATH)
public class BudgetSpecializationUnitServiceImpl extends AbstractSpecificServiceImpl<BudgetSpecializationUnitDto,BudgetSpecializationUnitDtoImpl,BudgetSpecializationUnit,BudgetSpecializationUnitImpl> implements BudgetSpecializationUnitService,Serializable {

	@Inject BudgetSpecializationUnitDtoImplMapper mapper;
	
	public BudgetSpecializationUnitServiceImpl() {
		this.serviceEntityClass = BudgetSpecializationUnitDto.class;
		this.serviceEntityImplClass = BudgetSpecializationUnitDtoImpl.class;
		this.persistenceEntityClass = BudgetSpecializationUnit.class;
		this.persistenceEntityImplClass = BudgetSpecializationUnitImpl.class;
	}
}