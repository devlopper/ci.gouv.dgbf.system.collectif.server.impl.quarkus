package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.EntryAuthorizationImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.PaymentCreditImpl;

@org.mapstruct.Mapper
public interface ExpenditureDtoImplMapper extends Mapper<ExpenditureImpl, ExpenditureDtoImpl> {

	EntryAuthorizationDtoImpl map(EntryAuthorizationImpl entity);
	
	EntryAuthorizationImpl map(EntryAuthorizationDtoImpl entity);
	
	PaymentCreditDtoImpl map(PaymentCreditImpl entity);
	
	PaymentCreditImpl map(PaymentCreditDtoImpl entity);
}