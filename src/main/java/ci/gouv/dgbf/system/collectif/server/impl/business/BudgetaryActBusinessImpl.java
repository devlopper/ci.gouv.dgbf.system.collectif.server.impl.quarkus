package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.BudgetaryActBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryAct;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActPersistence;

@ApplicationScoped
public class BudgetaryActBusinessImpl extends AbstractSpecificBusinessImpl<BudgetaryAct> implements BudgetaryActBusiness,Serializable {

	@Inject BudgetaryActPersistence budgetaryActPersistence;
	
}