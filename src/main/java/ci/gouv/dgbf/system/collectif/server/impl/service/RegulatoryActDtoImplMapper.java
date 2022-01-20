package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImpl;

@org.mapstruct.Mapper
public interface RegulatoryActDtoImplMapper extends Mapper<RegulatoryActImpl, RegulatoryActDtoImpl> {
	@AfterMapping
	default void listenAfterMap(RegulatoryActImpl source, @MappingTarget RegulatoryActDtoImpl target) {
		target.set__audit__(source.get__audit__());
	}
}