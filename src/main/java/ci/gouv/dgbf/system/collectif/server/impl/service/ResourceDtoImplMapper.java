package ci.gouv.dgbf.system.collectif.server.impl.service;

import org.cyk.utility.mapping.Mapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RevenueImpl;

@org.mapstruct.Mapper
public interface ResourceDtoImplMapper extends Mapper<ResourceImpl, ResourceDtoImpl> {

	RevenueDtoImpl map(RevenueImpl entity);

	RevenueImpl map(RevenueDtoImpl entity);

	@AfterMapping
	default void listenAfterMap(ResourceImpl source, @MappingTarget ResourceDtoImpl target) {
		target.set__audit__(source.get__audit__());
	}
}