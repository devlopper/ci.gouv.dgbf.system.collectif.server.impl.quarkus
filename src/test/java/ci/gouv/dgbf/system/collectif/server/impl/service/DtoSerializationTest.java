package ci.gouv.dgbf.system.collectif.server.impl.service;

import javax.json.bind.JsonbBuilder;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class DtoSerializationTest {

	@Test
    public void serialize_budgetaryActVersion() {
		BudgetaryActVersionDtoImpl budgetaryActVersionDto = new BudgetaryActVersionDtoImpl();
		//budgetaryActVersionDto.setIdentifier("1");
		//budgetaryActVersionDto.setCode("c");
		//budgetaryActVersionDto.setName("n");
		budgetaryActVersionDto.setBudgetaryAct(new BudgetaryActDtoImpl());
		JsonbBuilder.create().toJson(budgetaryActVersionDto);
    }
	
	@Test
    public void serialize_expenditure() {
		ExpenditureDtoImpl expenditure = new ExpenditureDtoImpl();
		expenditure.setEntryAuthorization(new EntryAuthorizationDtoImpl());
		JsonbBuilder.create().toJson(expenditure);
    }
}