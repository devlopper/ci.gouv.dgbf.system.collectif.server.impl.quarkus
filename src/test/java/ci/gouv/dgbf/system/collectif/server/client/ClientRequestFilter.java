package ci.gouv.dgbf.system.collectif.server.client;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.ext.Provider;

@Provider
public class ClientRequestFilter implements javax.ws.rs.client.ClientRequestFilter {

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		//System.out.println("ClientRequestFilter.filter() ::: "+requestContext.getUri());
	}

}
