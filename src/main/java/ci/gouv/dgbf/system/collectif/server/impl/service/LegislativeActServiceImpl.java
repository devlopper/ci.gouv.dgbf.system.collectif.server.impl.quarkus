package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeAct;
import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActDto;
import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;

@Path(LegislativeActService.PATH)
public class LegislativeActServiceImpl extends AbstractSpecificServiceImpl<LegislativeActDto,LegislativeActDtoImpl,LegislativeAct,LegislativeActImpl> implements LegislativeActService,Serializable {

	@Inject LegislativeActBusiness business;
	@Inject LegislativeActDtoImplMapper mapper;
	
	public LegislativeActServiceImpl() {
		this.serviceEntityClass = LegislativeActDto.class;
		this.serviceEntityImplClass = LegislativeActDtoImpl.class;
		this.persistenceEntityClass = LegislativeAct.class;
		this.persistenceEntityImplClass = LegislativeActImpl.class;
	}
}