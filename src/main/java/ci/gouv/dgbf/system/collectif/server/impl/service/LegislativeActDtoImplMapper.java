package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;

@org.mapstruct.Mapper
public interface LegislativeActDtoImplMapper extends Mapper<LegislativeActImpl, LegislativeActDtoImpl> {

	@AfterMapping
	default void listenAfterMap(LegislativeActImpl source, @MappingTarget LegislativeActDtoImpl target) {
		target.set__audit__(source.get__audit__());
	}
	
}