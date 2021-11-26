package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;

@Path(ExpenditureService.PATH)
public class ExpenditureServiceImpl extends AbstractSpecificServiceImpl<ExpenditureDto,ExpenditureDtoImpl,Expenditure,ExpenditureImpl> implements ExpenditureService,Serializable {

	@Inject ExpenditureBusiness business;
	@Inject ExpenditureDtoImplMapper mapper;
	
	public ExpenditureServiceImpl() {
		this.serviceEntityClass = ExpenditureDto.class;
		this.serviceEntityImplClass = ExpenditureDtoImpl.class;
		this.persistenceEntityClass = Expenditure.class;
		this.persistenceEntityImplClass = ExpenditureImpl.class;
	}
	
}