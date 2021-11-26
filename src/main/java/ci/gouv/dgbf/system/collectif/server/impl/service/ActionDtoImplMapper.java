package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActionImpl;

@org.mapstruct.Mapper
public interface ActionDtoImplMapper extends Mapper<ActionImpl, ActionDtoImpl> {

}