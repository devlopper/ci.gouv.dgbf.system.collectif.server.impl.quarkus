package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Activity;
import ci.gouv.dgbf.system.collectif.server.api.service.ActivityDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ActivityService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActivityImpl;

@Path(ActivityService.PATH)
public class ActivityServiceImpl extends AbstractSpecificServiceImpl<ActivityDto,ActivityDtoImpl,Activity,ActivityImpl> implements ActivityService,Serializable {

	@Inject ActivityDtoImplMapper mapper;
	
	public ActivityServiceImpl() {
		this.serviceEntityClass = ActivityDto.class;
		this.serviceEntityImplClass = ActivityDtoImpl.class;
		this.persistenceEntityClass = Activity.class;
		this.persistenceEntityImplClass = ActivityImpl.class;
	}
}