package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.file.excel.Sheet;
import org.cyk.utility.file.excel.SheetGetter;
import org.cyk.utility.file.excel.SheetReader;
import org.cyk.utility.file.excel.WorkBook;
import org.cyk.utility.file.excel.WorkBookGetter;
import org.cyk.utility.persistence.server.AbstractSpecificPersistenceImpl;

import ci.gouv.dgbf.system.collectif.server.api.persistence.Expenditure;
import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditurePersistence;

@ApplicationScoped
public class ExpenditurePersistenceImpl extends AbstractSpecificPersistenceImpl<Expenditure>  implements ExpenditurePersistence,Serializable {

	@Inject WorkBookGetter workBookGetter;
	@Inject SheetGetter sheetGetter;
	@Inject SheetReader sheetReader;
	
	public ExpenditurePersistenceImpl() {
		entityClass = Expenditure.class;
		entityImplClass = ExpenditureImpl.class;
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

	@Override
	public Collection<Expenditure> readFromFileExcel(byte[] bytes, Integer activityCodeColumnIndex,Integer economicNatureCodeColumnIndex, Integer fundingSourceCodeColumnIndex,Integer lessorCodeColumnIndex) {
		if(bytes == null || bytes.length == 0)
			return null;
		WorkBook workBook = workBookGetter.get(bytes);
		Sheet sheet = sheetGetter.get(workBook, 0);
		String[][] arrays = sheetReader.read(sheet);
		if(arrays == null || arrays.length == 0)
			return null;
		Collection<Expenditure> collection = new ArrayList<>();
		for(String[] array : arrays) {
			collection.add(new ExpenditureImpl().setActivityCode(array[activityCodeColumnIndex]).setEconomicNatureCode(array[economicNatureCodeColumnIndex]).setFundingSourceCode(array[fundingSourceCodeColumnIndex])
					.setLessorCode(array[lessorCodeColumnIndex]));
		}
		return collection;
	}
}