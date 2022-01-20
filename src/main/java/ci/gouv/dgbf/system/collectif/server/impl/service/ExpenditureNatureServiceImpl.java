package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNature;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureNatureDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureNatureService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureNatureImpl;

@Path(ExpenditureNatureService.PATH)
public class ExpenditureNatureServiceImpl extends AbstractSpecificServiceImpl<ExpenditureNatureDto,ExpenditureNatureDtoImpl,ExpenditureNature,ExpenditureNatureImpl> implements ExpenditureNatureService,Serializable {

	@Inject ExpenditureNatureDtoImplMapper mapper;
	
	public ExpenditureNatureServiceImpl() {
		this.serviceEntityClass = ExpenditureNatureDto.class;
		this.serviceEntityImplClass = ExpenditureNatureDtoImpl.class;
		this.persistenceEntityClass = ExpenditureNature.class;
		this.persistenceEntityImplClass = ExpenditureNatureImpl.class;
	}
}