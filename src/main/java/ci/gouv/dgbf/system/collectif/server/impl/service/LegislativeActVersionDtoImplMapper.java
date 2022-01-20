package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;

@org.mapstruct.Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface LegislativeActVersionDtoImplMapper extends Mapper<LegislativeActVersionImpl, LegislativeActVersionDtoImpl> {

	LegislativeActDtoImpl map(LegislativeActImpl entity);
	
	LegislativeActImpl map(LegislativeActDtoImpl entity);
	
}