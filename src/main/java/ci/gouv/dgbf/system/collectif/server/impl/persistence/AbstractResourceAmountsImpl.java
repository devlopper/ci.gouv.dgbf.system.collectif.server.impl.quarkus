package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;

import ci.gouv.dgbf.system.collectif.server.api.persistence.ResourceAmounts;

@MappedSuperclass @Audited
public abstract class AbstractResourceAmountsImpl extends AbstractAmountsImpl implements ResourceAmounts,Serializable {

}