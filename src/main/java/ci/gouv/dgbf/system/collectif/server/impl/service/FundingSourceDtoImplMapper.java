package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.FundingSourceImpl;

@org.mapstruct.Mapper
public interface FundingSourceDtoImplMapper extends Mapper<FundingSourceImpl, FundingSourceDtoImpl> {

}