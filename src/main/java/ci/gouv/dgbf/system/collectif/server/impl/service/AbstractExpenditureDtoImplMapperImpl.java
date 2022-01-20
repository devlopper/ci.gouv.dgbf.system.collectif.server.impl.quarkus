package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.EntryAuthorizationImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.PaymentCreditImpl;

public abstract class AbstractExpenditureDtoImplMapperImpl implements ExpenditureDtoImplMapper, Serializable {

	@Inject
	EntryAuthorizationDtoImplMapper entryAuthorizationDtoImplMapper;
	@Inject
	PaymentCreditDtoImplMapper paymentCreditDtoImplMapper;

	@Override
	public EntryAuthorizationImpl map(EntryAuthorizationDtoImpl entity) {
		return entryAuthorizationDtoImplMapper.mapDestinationToSource(entity);
	}

	@Override
	public EntryAuthorizationDtoImpl map(EntryAuthorizationImpl entity) {
		return entryAuthorizationDtoImplMapper.mapSourceToDestination(entity);
	}

	@Override
	public PaymentCreditImpl map(PaymentCreditDtoImpl entity) {
		return paymentCreditDtoImplMapper.mapDestinationToSource(entity);
	}

	@Override
	public PaymentCreditDtoImpl map(PaymentCreditImpl entity) {
		return paymentCreditDtoImplMapper.mapSourceToDestination(entity);
	}
}