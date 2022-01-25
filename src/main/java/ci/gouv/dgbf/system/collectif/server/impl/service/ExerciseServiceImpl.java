package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Exercise;
import ci.gouv.dgbf.system.collectif.server.api.service.ExerciseDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExerciseService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExerciseImpl;

@Path(ExerciseService.PATH)
public class ExerciseServiceImpl extends AbstractSpecificServiceImpl<ExerciseDto,ExerciseDtoImpl,Exercise,ExerciseImpl> implements ExerciseService,Serializable {

	@Inject ExerciseDtoImplMapper mapper;
	
	public ExerciseServiceImpl() {
		this.serviceEntityClass = ExerciseDto.class;
		this.serviceEntityImplClass = ExerciseDtoImpl.class;
		this.persistenceEntityClass = Exercise.class;
		this.persistenceEntityImplClass = ExerciseImpl.class;
	}
}