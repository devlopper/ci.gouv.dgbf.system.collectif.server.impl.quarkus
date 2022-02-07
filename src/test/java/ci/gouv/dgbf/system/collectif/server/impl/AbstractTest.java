package ci.gouv.dgbf.system.collectif.server.impl;

import javax.inject.Inject;

public abstract class AbstractTest extends org.cyk.quarkus.extension.test.AbstractTest {

	@Inject protected Assertor assertor;

}