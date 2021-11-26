package ci.gouv.dgbf.system.collectif.server.impl;

import javax.enterprise.event.Observes;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.mapping.MapperClassGetter;
import org.cyk.utility.__kernel__.variable.VariableHelper;
import org.cyk.utility.__kernel__.variable.VariableName;
import org.cyk.utility.business.Validator;
import org.cyk.utility.persistence.query.EntityCounter;
import org.cyk.utility.persistence.query.EntityReader;
import org.cyk.utility.persistence.server.TransientFieldsProcessor;
import org.cyk.utility.persistence.server.query.string.RuntimeQueryStringBuilder;
import org.cyk.utility.service.server.PersistenceEntityClassGetterImpl;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetaryActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetaryActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureNatureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.BudgetSpecializationUnitDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.BudgetSpecializationUnitDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.BudgetaryActDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.BudgetaryActDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.BudgetaryActVersionDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.BudgetaryActVersionDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.ExpenditureDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.ExpenditureDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.ExpenditureNatureDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.ExpenditureNatureDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.SectionDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.SectionDtoImplMapper;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;

@Startup(value = ApplicationLifeCycleListener.ORDER)
@javax.enterprise.context.ApplicationScoped
public class ApplicationLifeCycleListener {
	public static final int ORDER = org.cyk.quarkus.extension.hibernate.orm.ApplicationLifeCycleListener.ORDER+1;

    void onStart(@Observes StartupEvent startupEvent) {
    	org.cyk.quarkus.extension.hibernate.orm.ApplicationLifeCycleListener.QUALIFIER = ci.gouv.dgbf.system.collectif.server.api.System.class;
    	DependencyInjection.setQualifierClassTo(ci.gouv.dgbf.system.collectif.server.api.System.class
    			, EntityReader.class,EntityCounter.class, RuntimeQueryStringBuilder.class,TransientFieldsProcessor.class/*, Initializer.class*/,Validator.class
    			);
    	VariableHelper.write(VariableName.SYSTEM_LOGGING_THROWABLE_PRINT_STACK_TRACE, Boolean.TRUE);
    	
    	MapperClassGetter.MAP.put(BudgetaryActDtoImpl.class, BudgetaryActDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(BudgetaryActDtoImpl.class,BudgetaryActImpl.class);
    	
    	MapperClassGetter.MAP.put(BudgetaryActVersionDtoImpl.class, BudgetaryActVersionDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(BudgetaryActVersionDtoImpl.class,BudgetaryActVersionImpl.class);
    	
    	MapperClassGetter.MAP.put(BudgetSpecializationUnitDtoImpl.class, BudgetSpecializationUnitDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(BudgetSpecializationUnitDtoImpl.class,BudgetSpecializationUnitImpl.class);
    	
    	MapperClassGetter.MAP.put(ExpenditureDtoImpl.class, ExpenditureDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(ExpenditureDtoImpl.class,ExpenditureImpl.class);
    	
    	/**/
    	
    	MapperClassGetter.MAP.put(ExpenditureNatureDtoImpl.class, ExpenditureNatureDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(ExpenditureNatureDtoImpl.class,ExpenditureNatureImpl.class);
    	
    	MapperClassGetter.MAP.put(SectionDtoImpl.class, SectionDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(SectionDtoImpl.class,SectionImpl.class);
    	
    	/**/
    	
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {               
        
    }
}