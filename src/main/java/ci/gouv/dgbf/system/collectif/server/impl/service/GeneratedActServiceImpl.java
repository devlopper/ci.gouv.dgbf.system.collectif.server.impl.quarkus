package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.GeneratedActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.GeneratedAct;
import ci.gouv.dgbf.system.collectif.server.api.service.GeneratedActDto;
import ci.gouv.dgbf.system.collectif.server.api.service.GeneratedActService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.GeneratedActImpl;

@Path(GeneratedActService.PATH)
public class GeneratedActServiceImpl extends AbstractSpecificServiceImpl<GeneratedActDto,GeneratedActDtoImpl,GeneratedAct,GeneratedActImpl> implements GeneratedActService,Serializable {

	@Inject GeneratedActBusiness business;
	@Inject GeneratedActDtoImplMapper mapper;
	
	public GeneratedActServiceImpl() {
		this.serviceEntityClass = GeneratedActDto.class;
		this.serviceEntityImplClass = GeneratedActDtoImpl.class;
		this.persistenceEntityClass = GeneratedAct.class;
		this.persistenceEntityImplClass = GeneratedActImpl.class;
	}
}