package ci.gouv.dgbf.system.collectif.server.impl;
import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.ext.Provider;

import ci.gouv.dgbf.system.collectif.server.impl.client.JasperServerClient;

@Provider
public class ClientRequestFilter implements javax.ws.rs.client.ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
    	if (JasperServerClient.isGetReportRequest(requestContext))
    		JasperServerClient.prepareGetReportRequest(requestContext);
    }

}