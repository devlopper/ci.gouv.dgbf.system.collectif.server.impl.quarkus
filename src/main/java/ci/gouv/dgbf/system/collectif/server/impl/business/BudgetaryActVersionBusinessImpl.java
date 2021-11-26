package ci.gouv.dgbf.system.collectif.server.impl.business;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cyk.utility.business.server.AbstractSpecificBusinessImpl;

import ci.gouv.dgbf.system.collectif.server.api.business.BudgetaryActVersionBusiness;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersion;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersionPersistence;

@ApplicationScoped
public class BudgetaryActVersionBusinessImpl extends AbstractSpecificBusinessImpl<BudgetaryActVersion> implements BudgetaryActVersionBusiness,Serializable {

	@Inject BudgetaryActVersionPersistence budgetaryActVersionPersistence;
	
}