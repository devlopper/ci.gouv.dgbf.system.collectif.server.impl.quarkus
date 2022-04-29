package ci.gouv.dgbf.system.collectif.server.impl;

import java.io.Serializable;

public class ConfigSourceFactory extends org.cyk.quarkus.extension.core_.ConfigSourceFactory implements Serializable {

	protected void classForName(String name) throws ClassNotFoundException {
		Class.forName(name);
	}
}