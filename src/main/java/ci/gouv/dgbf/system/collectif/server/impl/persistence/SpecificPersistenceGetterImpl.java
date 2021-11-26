package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cyk.quarkus.extension.hibernate.orm.AbstractSpecificPersistenceGetterImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetSpecializationUnitPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetaryActVersionPersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureNaturePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;
import ci.gouv.dgbf.system.collectif.server.api.persistence.SectionPersistence;
import io.quarkus.arc.Unremovable;

@ApplicationScoped @Unremovable
public class SpecificPersistenceGetterImpl extends AbstractSpecificPersistenceGetterImpl implements Serializable {

	@Inject ExpenditurePersistence expenditurePersistence;
	@Inject BudgetaryActPersistence budgetaryActPersistence;
	@Inject BudgetaryActVersionPersistence budgetaryActVersionPersistence;
	@Inject BudgetSpecializationUnitPersistence budgetSpecializationUnitPersistence;
	
	@Inject SectionPersistence sectionPersistence;	
	@Inject ExpenditureNaturePersistence expenditureNaturePersistence;
	
}