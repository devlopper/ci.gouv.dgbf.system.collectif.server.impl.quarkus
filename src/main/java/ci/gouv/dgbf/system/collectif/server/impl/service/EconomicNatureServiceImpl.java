package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.EconomicNature;
import ci.gouv.dgbf.system.collectif.server.api.service.EconomicNatureDto;
import ci.gouv.dgbf.system.collectif.server.api.service.EconomicNatureService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EconomicNatureImpl;

@Path(EconomicNatureService.PATH)
public class EconomicNatureServiceImpl extends AbstractSpecificServiceImpl<EconomicNatureDto,EconomicNatureDtoImpl,EconomicNature,EconomicNatureImpl> implements EconomicNatureService,Serializable {

	@Inject EconomicNatureDtoImplMapper mapper;
	
	public EconomicNatureServiceImpl() {
		this.serviceEntityClass = EconomicNatureDto.class;
		this.serviceEntityImplClass = EconomicNatureDtoImpl.class;
		this.persistenceEntityClass = EconomicNature.class;
		this.persistenceEntityImplClass = EconomicNatureImpl.class;
	}
}