package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.persistence.server.query.string.QueryStringBuilder;

public class ExpenditureImplEntryAuthorizationReader extends AbstractExpenditureImplReader implements Serializable {

	@Override
	protected QueryStringBuilder.Arguments instantiateQueryStringBuilderArguments() {
		QueryStringBuilder.Arguments arguments =  super.instantiateQueryStringBuilderArguments();
		arguments.getProjection(Boolean.TRUE).addFromTuple("t",ExpenditureImpl.FIELD_IDENTIFIER
				,FieldHelper.join(ExpenditureImpl.FIELD_ENTRY_AUTHORIZATION,EntryAuthorizationImpl.FIELD_ADJUSTMENT));
		return arguments;
	}
	
}