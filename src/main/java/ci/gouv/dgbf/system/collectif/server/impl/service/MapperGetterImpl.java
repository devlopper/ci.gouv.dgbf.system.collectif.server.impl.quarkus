package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cyk.quarkus.extension.service.core_.AbstractMapperGetterImpl;

import io.quarkus.arc.Unremovable;

@ApplicationScoped @Unremovable
public class MapperGetterImpl extends AbstractMapperGetterImpl implements Serializable {

	@Inject BudgetaryActDtoImplMapper budgetaryActDtoImplMapper;
	@Inject BudgetaryActVersionDtoImplMapper budgetaryActVersionDtoImplMapper;
	@Inject ExpenditureDtoImplMapper expenditureDtoImplMapper;
	@Inject BudgetSpecializationUnitDtoImplMapper budgetSpecializationUnitDtoImplMapper;
	
	@Inject SectionDtoImplMapper sectionDtoImplMapper;	
	@Inject ExpenditureNatureDtoImplMapper expenditureNatureDtoImplMapper;
	
}