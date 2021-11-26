package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.BudgetSpecializationUnitBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;

@ApplicationScoped
public class BudgetSpecializationUnitBusinessImpl extends AbstractSpecificBusinessImpl<BudgetSpecializationUnit> implements BudgetSpecializationUnitBusiness,Serializable {

	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	
}