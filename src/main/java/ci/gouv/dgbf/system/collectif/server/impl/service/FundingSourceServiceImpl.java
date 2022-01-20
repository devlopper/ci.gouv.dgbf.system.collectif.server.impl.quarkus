package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.FundingSource;
import ci.gouv.dgbf.system.collectif.server.api.service.FundingSourceDto;
import ci.gouv.dgbf.system.collectif.server.api.service.FundingSourceService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.FundingSourceImpl;

@Path(FundingSourceService.PATH)
public class FundingSourceServiceImpl extends AbstractSpecificServiceImpl<FundingSourceDto,FundingSourceDtoImpl,FundingSource,FundingSourceImpl> implements FundingSourceService,Serializable {

	@Inject FundingSourceDtoImplMapper mapper;
	
	public FundingSourceServiceImpl() {
		this.serviceEntityClass = FundingSourceDto.class;
		this.serviceEntityImplClass = FundingSourceDtoImpl.class;
		this.persistenceEntityClass = FundingSource.class;
		this.persistenceEntityImplClass = FundingSourceImpl.class;
	}
}