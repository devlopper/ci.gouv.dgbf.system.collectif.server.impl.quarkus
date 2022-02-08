package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;
import org.cyk.utility.persistence.server.hibernate.annotation.Hibernate;
import org.cyk.utility.persistence.server.procedure.ProcedureExecutor;
import org.cyk.utility.persistence.server.procedure.ProcedureExecutorArguments;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;

@ApplicationScoped
public class ExpenditurePersistenceImpl extends AbstractSpecificPersistenceImpl<Expenditure>  implements ExpenditurePersistence,Serializable {

	@Inject @Hibernate ProcedureExecutor procedureExecutor;
	@Inject EntityManager entityManager;
	
	public ExpenditurePersistenceImpl() {
		entityClass = Expenditure.class;
		entityImplClass = ExpenditureImpl.class;
	}
	
	@Override
	public void import_(String legislativeActVersionIdentifier,String auditWho, String auditFunctionality, String auditWhat, java.sql.Date auditWhen,EntityManager entityManager) {
		ProcedureExecutorArguments arguments = new ProcedureExecutorArguments();
		arguments.setName(ExpenditureImpl.STORED_PROCEDURE_QUERY_PROCEDURE_NAME_IMPORT);
		arguments.setParameters(Map.of(
				ExpenditureImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_LEGISLATIVE_ACT_VERSION_IDENTIFIER,legislativeActVersionIdentifier
				,ExpenditureImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHO,auditWho
				,ExpenditureImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_FUNCTIONALITY,auditFunctionality
				,ExpenditureImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHAT,auditWhat
				,ExpenditureImpl.STORED_PROCEDURE_QUERY_PARAMETER_NAME_AUDIT_WHEN,auditWhen));
		arguments.setEntityManager(entityManager);
		procedureExecutor.execute(arguments);
	}

	@Override
	public void import_(String legislativeActVersionIdentifier, String auditWho, String auditFunctionality,String auditWhat, Date auditWhen) {
		import_(legislativeActVersionIdentifier, auditWho, auditFunctionality, auditWhat, auditWhen, entityManager);
	}

	public static void readAmounts(Collection<ExpenditureImpl> expenditures) {
		if(CollectionHelper.isEmpty(expenditures))
			return;
		new ExpenditureImplAmountsInitialActualMovementAdjustmentActualPlusAdjustmentReader().readThenSet(expenditures, null);
		new ExpenditureImplAmountsMovementIncludedReader().readThenSet(expenditures, null);
		expenditures.forEach(expenditure -> {
			expenditure.getEntryAuthorization(Boolean.TRUE).computeActualMinusMovementIncludedPlusAdjustment();
			expenditure.getPaymentCredit(Boolean.TRUE).computeActualMinusMovementIncludedPlusAdjustment();
		});
	}
}