package ci.gouv.dgbf.system.collectif.server.impl;

import java.util.List;

import org.cyk.quarkus.extension.core_.configuration.When;
import org.cyk.quarkus.extension.core_.configuration.processing.Copy;
import org.cyk.quarkus.extension.core_.configuration.processing.Importation;
import org.cyk.quarkus.extension.core_.configuration.processing.MaterializedViewActualization;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@StaticInitSafe
@ConfigMapping(prefix = "collectif")
public interface Configuration extends org.cyk.quarkus.extension.core_.configuration.Configuration {
	
	@WithName("server.client.rest.uri")
	@WithConverter(StringConverter.class)
	String serverClientRestUri();
	
	MaterializedViewActualization materializedViewActualization();
	
	Importation importation();
	
	Copy copy();

	BudgetCategory budgetCategory();
	
	interface BudgetCategory {
		@WithConverter(StringConverter.class)
		@WithDefault("1")
		String defaultIdentifier();
	}
	
	LegislativeActVersion legislativeActVersion();
	
	interface LegislativeActVersion {
		
		Creation creation();
		
		interface Creation {
			@WithDefault("WHILE")
			When whenRegulatoryActIncluded();
			
			@WithDefault("WHILE")
			When whenExpenditureImported();
			
			@WithDefault("WHILE")
			When whenResourceImported();
		}	
	}
	
	Actor actor();
	
	interface Actor {
		Visibilities visibilities();
		
		interface Visibilities {
			@WithDefault("true")
			Boolean enabled();
			
			//@WithDefault("CATEGORIE_BUDGET,SECTION,USB,ACTION")
			@WithDefault("CATEGORIE_BUDGET,SECTION,USB")
			List<String> scopesTypes();
		}
	}	
}