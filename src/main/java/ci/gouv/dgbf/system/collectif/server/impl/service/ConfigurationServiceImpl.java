package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import ci.gouv.dgbf.system.collectif.server.api.service.ConfigurationService;
import ci.gouv.dgbf.system.collectif.server.impl.Configuration;

@Path(ConfigurationService.PATH)
public class ConfigurationServiceImpl implements ConfigurationService,Serializable {

	@Inject Configuration configuration;
	
	@Override
	public ConfigurationDto get() {
		ConfigurationDto dto = new ConfigurationDto();
		dto.setActorVisibilitiesEnabled(configuration.actor().visibilities().enabled());
		return dto;
	}

}