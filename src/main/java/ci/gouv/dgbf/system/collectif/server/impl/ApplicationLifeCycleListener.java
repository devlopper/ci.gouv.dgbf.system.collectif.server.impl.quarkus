package ci.gouv.dgbf.system.collectif.server.impl;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.mapping.MapperClassGetter;
import org.cyk.utility.__kernel__.variable.VariableHelper;
import org.cyk.utility.__kernel__.variable.VariableName;
import org.cyk.utility.business.Validator;
import org.cyk.utility.persistence.entity.EntityLifeCycleListenerImpl;
import org.cyk.utility.persistence.query.EntityCounter;
import org.cyk.utility.persistence.query.EntityReader;
import org.cyk.utility.persistence.query.QueryResultMapper;
import org.cyk.utility.persistence.server.TransientFieldsProcessor;
import org.cyk.utility.persistence.server.query.RuntimeQueryBuilder;
import org.cyk.utility.persistence.server.query.string.RuntimeQueryStringBuilder;
import org.cyk.utility.persistence.server.view.MaterializedViewActualizer;
import org.cyk.utility.service.server.PersistenceEntityClassGetterImpl;

import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ActivityImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetCategoryImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetSpecializationUnitImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.EconomicNatureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExerciseImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureAvailableView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureNatureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ExpenditureView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.FundingSourceImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.GeneratedActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LegislativeActVersionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.LessorImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActExpenditureImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.RegulatoryActImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceActivityImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceImpl;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.ResourceView;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.SectionImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.ActionDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.ActionDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.ActivityDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.ActivityDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.BudgetCategoryDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.BudgetCategoryDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.BudgetSpecializationUnitDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.BudgetSpecializationUnitDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.EconomicNatureDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.EconomicNatureDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.ExerciseDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.ExerciseDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.ExpenditureDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.ExpenditureDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.ExpenditureNatureDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.ExpenditureNatureDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.FundingSourceDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.FundingSourceDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.GeneratedActDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.GeneratedActDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.LegislativeActDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.LegislativeActDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.LegislativeActVersionDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.LegislativeActVersionDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.LessorDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.LessorDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.RegulatoryActDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.RegulatoryActDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.ResourceActivityDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.ResourceActivityDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.ResourceDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.ResourceDtoImplMapper;
import ci.gouv.dgbf.system.collectif.server.impl.service.SectionDtoImpl;
import ci.gouv.dgbf.system.collectif.server.impl.service.SectionDtoImplMapper;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;

@Startup(value = ApplicationLifeCycleListener.ORDER)
@javax.enterprise.context.ApplicationScoped
public class ApplicationLifeCycleListener {
	public static final int ORDER = org.cyk.quarkus.extension.hibernate.orm.ApplicationLifeCycleListener.ORDER+1;

	@Inject Configuration configuration;
	//@Inject ProcedureExecutorGetter procedureExecutorGetter;
	//@Inject @Hibernate ProcedureExecutor procedureExecutor;
	
	@Inject MaterializedViewActualizer materializedViewActualizer;
	
    void onStart(@Observes StartupEvent startupEvent) {
    	org.cyk.quarkus.extension.hibernate.orm.ApplicationLifeCycleListener.QUALIFIER = ci.gouv.dgbf.system.collectif.server.api.System.class;
    	
    	//procedureExecutorGetter.setProcedureExecutor(procedureExecutor);
    	
    	DependencyInjection.setQualifierClassTo(ci.gouv.dgbf.system.collectif.server.api.System.class
    			, EntityReader.class,EntityCounter.class, RuntimeQueryBuilder.class, RuntimeQueryStringBuilder.class,QueryResultMapper.class,TransientFieldsProcessor.class/*, Initializer.class*/,Validator.class
    			);
    	VariableHelper.write(VariableName.SYSTEM_LOGGING_THROWABLE_PRINT_STACK_TRACE, Boolean.TRUE);
    	
    	EntityLifeCycleListenerImpl.useFrenchValues();
    	
    	MapperClassGetter.MAP.put(ExerciseDtoImpl.class, ExerciseDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(ExerciseDtoImpl.class,ExerciseImpl.class);
    	
    	MapperClassGetter.MAP.put(LegislativeActDtoImpl.class, LegislativeActDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(LegislativeActDtoImpl.class,LegislativeActImpl.class);
    	
    	MapperClassGetter.MAP.put(LegislativeActVersionDtoImpl.class, LegislativeActVersionDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(LegislativeActVersionDtoImpl.class,LegislativeActVersionImpl.class);
    	
    	MapperClassGetter.MAP.put(ExpenditureDtoImpl.class, ExpenditureDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(ExpenditureDtoImpl.class,ExpenditureImpl.class);
    	
    	MapperClassGetter.MAP.put(ResourceDtoImpl.class, ResourceDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(ResourceDtoImpl.class,ResourceImpl.class);
    	
    	/**/
    	
    	MapperClassGetter.MAP.put(SectionDtoImpl.class, SectionDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(SectionDtoImpl.class,SectionImpl.class);
    	
    	MapperClassGetter.MAP.put(BudgetSpecializationUnitDtoImpl.class, BudgetSpecializationUnitDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(BudgetSpecializationUnitDtoImpl.class,BudgetSpecializationUnitImpl.class);
    	
    	MapperClassGetter.MAP.put(BudgetCategoryDtoImpl.class, BudgetCategoryDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(BudgetCategoryDtoImpl.class,BudgetCategoryImpl.class);
    	
    	MapperClassGetter.MAP.put(ActionDtoImpl.class, ActionDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(ActionDtoImpl.class,ActionImpl.class);
    	
    	MapperClassGetter.MAP.put(ActivityDtoImpl.class, ActivityDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(ActivityDtoImpl.class,ActivityImpl.class);
    	
    	MapperClassGetter.MAP.put(ResourceActivityDtoImpl.class, ResourceActivityDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(ResourceActivityDtoImpl.class,ResourceActivityImpl.class);
    	
    	MapperClassGetter.MAP.put(ExpenditureNatureDtoImpl.class, ExpenditureNatureDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(ExpenditureNatureDtoImpl.class,ExpenditureNatureImpl.class);
    	
    	MapperClassGetter.MAP.put(EconomicNatureDtoImpl.class, EconomicNatureDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(EconomicNatureDtoImpl.class,EconomicNatureImpl.class);
    	
    	MapperClassGetter.MAP.put(FundingSourceDtoImpl.class, FundingSourceDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(FundingSourceDtoImpl.class,FundingSourceImpl.class);
    	
    	MapperClassGetter.MAP.put(LessorDtoImpl.class, LessorDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(LessorDtoImpl.class,LessorImpl.class);
    	
    	MapperClassGetter.MAP.put(RegulatoryActDtoImpl.class, RegulatoryActDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(RegulatoryActDtoImpl.class,RegulatoryActImpl.class);
    	
    	MapperClassGetter.MAP.put(GeneratedActDtoImpl.class, GeneratedActDtoImplMapper.class);
    	PersistenceEntityClassGetterImpl.MAP.put(GeneratedActDtoImpl.class,GeneratedActImpl.class);
    	/**/

    	//ContainerRequestFilter.LEVEL = Level.INFO;
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {               
        
    }

    @Scheduled(cron = "{collectif.materialized-view-actualization.processing.cron}")
	void actualizeMaterializedView() {
    	materializedViewActualizer.execute(List.of(BudgetCategoryImpl.class,SectionImpl.class,BudgetSpecializationUnitImpl.class,ActionImpl.class,ActivityImpl.class,ExpenditureNatureImpl.class,ResourceActivityImpl.class
    			,EconomicNatureImpl.class,FundingSourceImpl.class,LessorImpl.class,RegulatoryActImpl.class,RegulatoryActExpenditureImpl.class,ExpenditureView.class,ResourceView.class,ExpenditureAvailableView.class
    			/*,ExpenditureActualAtLegislativeActDateView.class,ExpenditureIncludedMovementView.class*/), null);
    }
}