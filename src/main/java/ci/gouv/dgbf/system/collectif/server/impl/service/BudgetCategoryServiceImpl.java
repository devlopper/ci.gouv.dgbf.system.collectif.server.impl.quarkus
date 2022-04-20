package ci.gouv.dgbf.system.collectif.server.impl.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.cyk.utility.service.server.AbstractSpecificServiceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.BudgetCategory;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetCategoryDto;
import ci.gouv.dgbf.system.collectif.server.api.service.BudgetCategoryService;
import ci.gouv.dgbf.system.collectif.server.impl.persistence.BudgetCategoryImpl;

@Path(BudgetCategoryService.PATH)
public class BudgetCategoryServiceImpl extends AbstractSpecificServiceImpl<BudgetCategoryDto,BudgetCategoryDtoImpl,BudgetCategory,BudgetCategoryImpl> implements BudgetCategoryService,Serializable {

	@Inject BudgetCategoryDtoImplMapper mapper;
	
	public BudgetCategoryServiceImpl() {
		this.serviceEntityClass = BudgetCategoryDto.class;
		this.serviceEntityImplClass = BudgetCategoryDtoImpl.class;
		this.persistenceEntityClass = BudgetCategory.class;
		this.persistenceEntityImplClass = BudgetCategoryImpl.class;
	}
}