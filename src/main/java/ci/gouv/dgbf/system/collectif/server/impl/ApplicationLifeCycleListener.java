package ci.gouv.dgbf.system.collectif.server.impl;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cyk.utility.persistence.query.CountQueryIdentifierGetter;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@javax.enterprise.context.ApplicationScoped
public class ApplicationLifeCycleListener {

	@Inject CountQueryIdentifierGetter countQueryIdentifierGetter;
	
    void onStart(@Observes StartupEvent startupEvent) {
    	/*DependencyInjection.setQualifierClassTo(org.cyk.system.file.server.api.System.class
    			, EntityReader.class,EntityCounter.class
    			, RuntimeQueryStringBuilder.class
    			,TransientFieldsProcessor.class, Initializer.class,Validator.class
    			);
    	VariableHelper.write(VariableName.SYSTEM_LOGGING_THROWABLE_PRINT_STACK_TRACE, Boolean.TRUE);
    	MapperClassGetter.MAP.put(FileDtoImpl.class, FileDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(FileDtoImpl.class,FileImpl.class);
    	DefaultProjectionsGetterImpl.MAP.put(File.class, List.of(FileImpl.FIELD_IDENTIFIER,FileImpl.FIELD_NAME_AND_EXTENSION));
    	DefaultSortOrdersGetterImpl.MAP.put(File.class, Map.of(FileImpl.FIELD_NAME,SortOrder.ASCENDING));
    	*/
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {               
        
    }
}