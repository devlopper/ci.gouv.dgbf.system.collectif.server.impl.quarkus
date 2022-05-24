package ci.gouv.dgbf.system.collectif.server.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import ci.gouv.dgbf.system.collectif.server.impl.client.JasperServerClient;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Profiles.Jasper.class)
@QuarkusTestResource(JasperQuarkusTestResourceLifecycleManager.class)
public class JasperServerClientTest extends org.cyk.quarkus.extension.test.AbstractTest {
	
	@Inject @RestClient JasperServerClient client;
	
	@Test
	void login() {
		Response response = client.login("jasperadmin", "jasperadmin");
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getHeaders().keySet()).contains("Set-Cookie");
	}
	
	@Test
	void logout() {
		Response response = client.logout(JasperQuarkusTestResourceLifecycleManager.SET_COOKIE);
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}
	
	@Test
	void checkSecurity() {
		Response response = client.checkSecurity("jasperadmin", "jasperadmin");
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}
	
	@Test
	void getReport() {
		Response response = client.getReport(new HashMap<String, String>(Map.of(JasperServerClient.QUERY_PARAMETER_REPORT_IDENTIFIER,JasperQuarkusTestResourceLifecycleManager.IDENTIFIER
				,JasperServerClient.QUERY_PARAMETER_FILE_TYPE,"pdf",JasperServerClient.QUERY_PARAMETER_SESSION_IDENTIFIER,JasperQuarkusTestResourceLifecycleManager.JSESSIONID)));
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}
	
	@Test
	void getJSESSIONID() {
		assertThat(JasperServerClient.getJSESSIONID(JasperQuarkusTestResourceLifecycleManager.SET_COOKIE)).isEqualTo(JasperQuarkusTestResourceLifecycleManager.JSESSIONID);
	}
}