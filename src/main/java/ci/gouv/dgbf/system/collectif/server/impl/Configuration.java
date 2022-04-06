package ci.gouv.dgbf.system.collectif.server.impl;

import java.util.concurrent.TimeUnit;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@StaticInitSafe
@ConfigMapping(prefix = "collectif")
public interface Configuration extends org.cyk.quarkus.extension.core_.Configuration {
	
	@WithName("server.client.rest.uri")
	@WithConverter(StringConverter.class)
	String serverClientRestUri();
	
	Importation importation();
	
	Copy copy();
	/**/
	
	public interface Importation {
		Executor executor();
		
		Batch batch();
	}

	public interface Copy {
		Executor executor();
		
		Batch batch();
	}
	
	public interface Executor {
		
		Timeout timeout();
		
		public interface Timeout {
			@WithDefault("5")
			Long duration();
			
			@WithDefault("MINUTES")
			TimeUnit unit();
		}
		
		Thread thread();
		
		public interface Thread {
			@WithDefault("4")
			Integer count();
		}
	}
	
	public interface Batch {
		@WithDefault("2000")
		Integer size();
	}
}