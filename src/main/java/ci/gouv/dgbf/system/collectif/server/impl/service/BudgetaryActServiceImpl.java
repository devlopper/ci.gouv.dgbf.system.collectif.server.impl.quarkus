package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.BudgetaryActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryAct;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetaryActDto;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetaryActService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetaryActImpl;

@Path(BudgetaryActService.PATH)
public class BudgetaryActServiceImpl extends AbstractSpecificServiceImpl<BudgetaryActDto,BudgetaryActDtoImpl,BudgetaryAct,BudgetaryActImpl> implements BudgetaryActService,Serializable {

	@Inject BudgetaryActBusiness business;
	@Inject BudgetaryActDtoImplMapper mapper;
	
	public BudgetaryActServiceImpl() {
		this.serviceEntityClass = BudgetaryActDto.class;
		this.serviceEntityImplClass = BudgetaryActDtoImpl.class;
		this.persistenceEntityClass = BudgetaryAct.class;
		this.persistenceEntityImplClass = BudgetaryActImpl.class;
	}
}