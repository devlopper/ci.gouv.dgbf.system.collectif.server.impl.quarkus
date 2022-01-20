package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceActivity;
import ci.gouv.dgbf.system.collectif.server.api.service.ResourceActivityDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ResourceActivityService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceActivityImpl;

@Path(ResourceActivityService.PATH)
public class ResourceActivityServiceImpl extends AbstractSpecificServiceImpl<ResourceActivityDto,ResourceActivityDtoImpl,ResourceActivity,ResourceActivityImpl> implements ResourceActivityService,Serializable {

	@Inject ResourceActivityDtoImplMapper mapper;
	
	public ResourceActivityServiceImpl() {
		this.serviceEntityClass = ResourceActivityDto.class;
		this.serviceEntityImplClass = ResourceActivityDtoImpl.class;
		this.persistenceEntityClass = ResourceActivity.class;
		this.persistenceEntityImplClass = ResourceActivityImpl.class;
	}
}