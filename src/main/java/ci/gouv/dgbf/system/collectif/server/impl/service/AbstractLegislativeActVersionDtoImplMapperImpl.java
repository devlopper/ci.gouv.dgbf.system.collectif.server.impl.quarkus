package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;

public abstract class AbstractLegislativeActVersionDtoImplMapperImpl implements LegislativeActVersionDtoImplMapper, Serializable {

	@Inject
	LegislativeActDtoImplMapper actDtoImplMapper;

	@Override
	public LegislativeActImpl map(LegislativeActDtoImpl entity) {
		return actDtoImplMapper.mapDestinationToSource(entity);
	}

	@Override
	public LegislativeActDtoImpl map(LegislativeActImpl entity) {
		return actDtoImplMapper.mapSourceToDestination(entity);
	}
}