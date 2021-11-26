package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.BudgetaryActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersion;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetaryActVersionDto;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetaryActVersionService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetaryActVersionImpl;

@Path(BudgetaryActVersionService.PATH)
public class BudgetaryActVersionServiceImpl extends AbstractSpecificServiceImpl<BudgetaryActVersionDto,BudgetaryActVersionDtoImpl,BudgetaryActVersion,BudgetaryActVersionImpl> implements BudgetaryActVersionService,Serializable {

	@Inject BudgetaryActVersionBusiness business;
	@Inject BudgetaryActVersionDtoImplMapper mapper;
	
	public BudgetaryActVersionServiceImpl() {
		this.serviceEntityClass = BudgetaryActVersionDto.class;
		this.serviceEntityImplClass = BudgetaryActVersionDtoImpl.class;
		this.persistenceEntityClass = BudgetaryActVersion.class;
		this.persistenceEntityImplClass = BudgetaryActVersionImpl.class;
	}
}