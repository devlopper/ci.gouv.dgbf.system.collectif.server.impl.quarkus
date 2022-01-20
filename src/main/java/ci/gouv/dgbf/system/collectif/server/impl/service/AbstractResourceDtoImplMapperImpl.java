package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.RevenueImpl;

public abstract class AbstractResourceDtoImplMapperImpl implements ResourceDtoImplMapper, Serializable {

	@Inject
	RevenueDtoImplMapper revenueDtoImplMapper;
	
	@Override
	public RevenueImpl map(RevenueDtoImpl entity) {
		return revenueDtoImplMapper.mapDestinationToSource(entity);
	}

	@Override
	public RevenueDtoImpl map(RevenueImpl entity) {
		return revenueDtoImplMapper.mapSourceToDestination(entity);
	}
}