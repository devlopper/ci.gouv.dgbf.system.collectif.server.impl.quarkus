package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ExpenditureAmounts;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true) @MappedSuperclass
public abstract class AbstractExpenditureAmountsImpl extends AbstractAmountsImpl implements ExpenditureAmounts,Serializable {

}