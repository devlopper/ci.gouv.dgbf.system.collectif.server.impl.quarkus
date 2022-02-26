package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.ExpenditureBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureDto.AdjustmentDto;
import ci.gouv.dgbf.system.collectif.server.api.service.ExpenditureService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;

@Path(ExpenditureService.PATH)
public class ExpenditureServiceImpl extends AbstractSpecificServiceImpl<ExpenditureDto,ExpenditureDtoImpl,Expenditure,ExpenditureImpl> implements ExpenditureService,Serializable {

	@Inject ExpenditureBusiness business;
	@Inject ExpenditureDtoImplMapper mapper;
	
	public ExpenditureServiceImpl() {
		this.serviceEntityClass = ExpenditureDto.class;
		this.serviceEntityImplClass = ExpenditureDtoImpl.class;
		this.persistenceEntityClass = Expenditure.class;
		this.persistenceEntityImplClass = ExpenditureImpl.class;
	}
	
	@Override
	public Response adjust(List<AdjustmentDto> adjustmentsDtos,String auditWho) {
		return buildResponseOk(business.adjust(adjustmentsDtos == null ? null : Optional.ofNullable(adjustmentsDtos).get().stream()
				.collect(Collectors.toMap(dto -> dto.getIdentifier(), dto -> new Long[] {dto.getEntryAuthorization(),dto.getPaymentCredit()})),auditWho));
	}
	
	@Override
	public Response adjustByEntryAuthorizations(List<AdjustmentDto> adjustmentsDtos,String auditWho) {
		return buildResponseOk(business.adjustByEntryAuthorizations(adjustmentsDtos == null ? null : Optional.ofNullable(adjustmentsDtos).get().stream()
				.collect(Collectors.toMap(dto -> dto.getIdentifier(), dto -> dto.getEntryAuthorization())),auditWho));
	}
	
	@Override
	public Response import_(String legislativeActVersionIdentifier,String auditWho) {
		return buildResponseOk(business.import_(legislativeActVersionIdentifier,auditWho));
	}
}