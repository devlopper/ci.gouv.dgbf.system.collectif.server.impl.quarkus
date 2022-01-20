package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Lessor;
import ci.gouv.dgbf.system.collectif.server.api.service.LessorDto;
import ci.gouv.dgbf.system.collectif.server.api.service.LessorService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LessorImpl;

@Path(LessorService.PATH)
public class LessorServiceImpl extends AbstractSpecificServiceImpl<LessorDto,LessorDtoImpl,Lessor,LessorImpl> implements LessorService,Serializable {

	@Inject LessorDtoImplMapper mapper;
	
	public LessorServiceImpl() {
		this.serviceEntityClass = LessorDto.class;
		this.serviceEntityImplClass = LessorDtoImpl.class;
		this.persistenceEntityClass = Lessor.class;
		this.persistenceEntityImplClass = LessorImpl.class;
	}
}