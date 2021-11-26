package ci.gouv.dgbf.system.collectif.server.impl.persistence;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.arc.Unremovable;

@ApplicationScoped @ci.gouv.dgbf.system.collectif.server.api.System @Unremovable
public class EntityReaderImpl extends org.cyk.quarkus.extension.hibernate.orm.EntityReaderImpl implements Serializable {

}