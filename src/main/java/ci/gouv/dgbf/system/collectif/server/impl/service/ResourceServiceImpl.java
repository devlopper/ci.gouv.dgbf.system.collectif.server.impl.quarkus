package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.ResourceBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Resource;
import ci.gouv.dgbf.system.collectif.server.api.service.ResourceDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ResourceDto.AdjustmentDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ResourceService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImpl;

@Path(ResourceService.PATH)
public class ResourceServiceImpl extends AbstractSpecificServiceImpl<ResourceDto,ResourceDtoImpl,Resource,ResourceImpl> implements ResourceService,Serializable {

	@Inject ResourceBusiness business;
	@Inject ResourceDtoImplMapper mapper;
	
	public ResourceServiceImpl() {
		this.serviceEntityClass = ResourceDto.class;
		this.serviceEntityImplClass = ResourceDtoImpl.class;
		this.persistenceEntityClass = Resource.class;
		this.persistenceEntityImplClass = ResourceImpl.class;
	}
	
	@Override
	public Response adjust(List<AdjustmentDto> adjustmentsDtos,String auditWho) {
		return buildResponseOk(business.adjust(adjustmentsDtos == null ? null : Optional.ofNullable(adjustmentsDtos).get().stream()
				.collect(Collectors.toMap(dto -> dto.getIdentifier(), dto -> dto.getRevenue())),auditWho));
	}
}