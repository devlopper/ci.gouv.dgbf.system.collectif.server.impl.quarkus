package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Action;
import ci.gouv.dgbf.system.collectif.server.api.service.ActionDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ActionService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActionImpl;

@Path(ActionService.PATH)
public class ActionServiceImpl extends AbstractSpecificServiceImpl<ActionDto,ActionDtoImpl,Action,ActionImpl> implements ActionService,Serializable {

	@Inject ActionDtoImplMapper mapper;
	
	public ActionServiceImpl() {
		this.serviceEntityClass = ActionDto.class;
		this.serviceEntityImplClass = ActionDtoImpl.class;
		this.persistenceEntityClass = Action.class;
		this.persistenceEntityImplClass = ActionImpl.class;
	}
}