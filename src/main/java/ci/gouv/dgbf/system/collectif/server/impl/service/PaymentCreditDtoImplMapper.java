package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.PaymentCreditImpl;

@org.mapstruct.Mapper
public interface PaymentCreditDtoImplMapper extends Mapper<PaymentCreditImpl, PaymentCreditDtoImpl> {

}