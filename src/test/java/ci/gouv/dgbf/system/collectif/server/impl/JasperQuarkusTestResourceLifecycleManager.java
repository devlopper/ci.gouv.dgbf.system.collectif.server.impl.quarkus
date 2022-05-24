package ci.gouv.dgbf.system.collectif.server.impl;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.github.tomakehurst.wiremock.WireMockServer;

import ci.gouv.dgbf.system.collectif.server.impl.client.JasperServerClient; 

public class JasperQuarkusTestResourceLifecycleManager implements io.quarkus.test.common.QuarkusTestResourceLifecycleManager {

	WireMockServer wireMockServer;
	
	@Override
	public Map<String, String> start() {
		wireMockServer = new WireMockServer(11001);
		wireMockServer.start(); 
		
		// security check request
		wireMockServer.stubFor(get(urlEqualTo(String.format("/j_spring_security_check?j_username=jasperadmin&j_password=jasperadmin")))   
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.TEXT_PLAIN)
                        .withHeader("Set-Cookie", SET_COOKIE)
                        ));
				
		// login request
		wireMockServer.stubFor(get(urlEqualTo(String.format(JasperServerClient.PATH_REST+"login?j_username=jasperadmin&j_password=jasperadmin")))   
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.TEXT_PLAIN)
                        .withHeader("Set-Cookie", SET_COOKIE)
                        ));
		
		// get report request
		wireMockServer.stubFor(get(urlEqualTo(String.format(JasperServerClient.PATH_REST+"reports%s.pdf",IDENTIFIER)))   
                .willReturn(aResponse()
                        ));
		
		// post report request
		try {
			wireMockServer.stubFor(post(urlEqualTo(String.format(JasperServerClient.PATH_REST+"reports?identifier=%s&file-type=%s&jsessionid=%s",URLEncoder.encode(IDENTIFIER, "UTF-8"),"pdf",JSESSIONID)))   
					.willReturn(aResponse()
							));
		} catch (UnsupportedEncodingException exception) {
			exception.printStackTrace();
		}
		
		// logout request
		wireMockServer.stubFor(get(urlEqualTo(String.format("/logout.htm")))   
                .willReturn(aResponse()
                        ));
		
        return Collections.singletonMap(String.format("quarkus.rest-client.%s.uri",JasperServerClient.CONFIG_KEY), wireMockServer.baseUrl()); 
	}

	@Override
	public void stop() {
		if (null != wireMockServer)
            wireMockServer.stop();  
	}
	
	public static final String IDENTIFIER = "/reports/sigobe/collectif/synthese_remaniement_avril_2022";
	//public static final String IDENTIFIER = "/reports/reports/sigobe/collectif/synthese_remaniement_avril_2022";
	public static final String JSESSIONID = "8521DB546D2C8F0736B95E9C51245DD9";
	public static final String SET_COOKIE = String.format("JSESSIONID=%s; Path=/jasperserver; HttpOnly",JSESSIONID);
}