package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.LegislativeActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.LegislativeActVersion;
import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActVersionDto;
import ci.gouv.dgbf.system.collectif.server.api.service.LegislativeActVersionService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;

@Path(LegislativeActVersionService.PATH)
public class LegislativeActVersionServiceImpl extends AbstractSpecificServiceImpl<LegislativeActVersionDto,LegislativeActVersionDtoImpl,LegislativeActVersion,LegislativeActVersionImpl> implements LegislativeActVersionService,Serializable {

	@Inject LegislativeActVersionBusiness business;
	@Inject LegislativeActVersionDtoImplMapper mapper;
	
	public LegislativeActVersionServiceImpl() {
		this.serviceEntityClass = LegislativeActVersionDto.class;
		this.serviceEntityImplClass = LegislativeActVersionDtoImpl.class;
		this.persistenceEntityClass = LegislativeActVersion.class;
		this.persistenceEntityImplClass = LegislativeActVersionImpl.class;
	}
	
	@Override
	public Response create(String identifier, String name, Byte number, String actIdentifier, String auditWho) {
		return buildResponseOk(business.create(identifier, name, number, actIdentifier, auditWho));
	}
	
	@Override
	public Response duplicate(String identifier, String auditWho) {
		return buildResponseOk(business.duplicate(identifier, auditWho));
	}
}