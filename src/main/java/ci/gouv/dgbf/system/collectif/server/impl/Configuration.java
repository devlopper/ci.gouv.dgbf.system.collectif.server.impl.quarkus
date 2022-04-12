package ci.gouv.dgbf.system.collectif.server.impl;

import org.cyk.quarkus.extension.core_.configuration.processing.Importation;
import org.cyk.quarkus.extension.core_.configuration.processing.Copy;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithName;

@StaticInitSafe
@ConfigMapping(prefix = "collectif")
public interface Configuration extends org.cyk.quarkus.extension.core_.configuration.Configuration {
	
	@WithName("server.client.rest.uri")
	@WithConverter(StringConverter.class)
	String serverClientRestUri();
	
	Importation importation();
	
	Copy copy();
	
}