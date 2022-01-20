package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceAmounts;
@MappedSuperclass
public abstract class AbstractResourceAmountsImpl extends AbstractAmountsImpl implements ResourceAmounts,Serializable {

}