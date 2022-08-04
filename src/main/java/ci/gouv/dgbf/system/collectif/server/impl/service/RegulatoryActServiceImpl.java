package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.RegulatoryActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.RegulatoryAct;
import ci.gouv.dgbf.system.collectif.server.api.service.RegulatoryActDto;
import ci.gouv.dgbf.system.collectif.server.api.service.RegulatoryActService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImpl;

@Path(RegulatoryActService.PATH)
public class RegulatoryActServiceImpl extends AbstractSpecificServiceImpl<RegulatoryActDto,RegulatoryActDtoImpl,RegulatoryAct,RegulatoryActImpl> implements RegulatoryActService,Serializable {

	@Inject RegulatoryActBusiness business;
	@Inject RegulatoryActDtoImplMapper mapper;
	
	public RegulatoryActServiceImpl() {
		this.serviceEntityClass = RegulatoryActDto.class;
		this.serviceEntityImplClass = RegulatoryActDtoImpl.class;
		this.persistenceEntityClass = RegulatoryAct.class;
		this.persistenceEntityImplClass = RegulatoryActImpl.class;
	}

	@Override
	public Response include(List<String> identifiers, String legislativeActVersionIdentifier,Boolean existingIgnorable,String auditWho) {
		return buildResponseOk(business.include(identifiers, legislativeActVersionIdentifier, existingIgnorable,auditWho));
	}

	@Override
	public Response exclude(List<String> identifiers, String legislativeActVersionIdentifier,Boolean existingIgnorable,String auditWho) {
		return buildResponseOk(business.exclude(identifiers, legislativeActVersionIdentifier, existingIgnorable,auditWho));
	}

	@Override
	public Response includeComprehensively(List<String> identifiers, String legislativeActVersionIdentifier, String auditWho) {
		return buildResponseOk(business.includeComprehensively(identifiers, legislativeActVersionIdentifier,auditWho));
	}
}