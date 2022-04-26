package ci.gouv.dgbf.system.collectif.server.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.MediaType;

import org.cyk.utility.__kernel__.collection.CollectionHelper;

import com.github.tomakehurst.wiremock.WireMockServer;

import ci.gouv.dgbf.system.collectif.server.impl.client.ActorClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*; 

public class QuarkusTestResourceLifecycleManager implements io.quarkus.test.common.QuarkusTestResourceLifecycleManager {

	WireMockServer wireMockServer;
	
	@Override
	public Map<String, String> start() {
		wireMockServer = new WireMockServer(11000);
		wireMockServer.start(); 
		
		visibilities("christian",ActorClient.CODE_TYPE_DOMAINE_CATEGORIE_BUDGET, List.of(new ActorClient.VisibilityDto().setIdentifier("1"),new ActorClient.VisibilityDto().setIdentifier("2")));
		visibilities("unknown",ActorClient.CODE_TYPE_DOMAINE_CATEGORIE_BUDGET, null);
		
		visibilities("christian",ActorClient.CODE_TYPE_DOMAINE_SECTION, List.of(new ActorClient.VisibilityDto().setIdentifier("1"),new ActorClient.VisibilityDto().setIdentifier("2")));
		visibilities("unknown",ActorClient.CODE_TYPE_DOMAINE_SECTION, null);
		
		visibilities("christian",ActorClient.CODE_TYPE_DOMAINE_USB, List.of(new ActorClient.VisibilityDto().setIdentifier("1"),new ActorClient.VisibilityDto().setIdentifier("2")));
		visibilities("unknown",ActorClient.CODE_TYPE_DOMAINE_USB, null);
		
		visibilities("christian",ActorClient.CODE_TYPE_DOMAINE_ACTION, List.of(new ActorClient.VisibilityDto().setIdentifier("1"),new ActorClient.VisibilityDto().setIdentifier("2")));
		visibilities("unknown",ActorClient.CODE_TYPE_DOMAINE_ACTION, null);
		
        return Collections.singletonMap(String.format("quarkus.rest-client.%s.uri",ActorClient.CONFIG_KEY), wireMockServer.baseUrl()); 
	}
	
	private void visibilities(String username,String codeTypeDomaine,Collection<ActorClient.VisibilityDto> visibilities) {
		wireMockServer.stubFor(get(urlEqualTo(String.format("/open/domaine/obtenir-visibles-par-acteur?code_type_domaine=%s&nom_utilisateur=%s",codeTypeDomaine,username)))   
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                        .withBody(CollectionHelper.isEmpty(visibilities) ? "" : JsonbBuilder.create().toJson(visibilities))
                        ));
	}

	@Override
	public void stop() {
		if (null != wireMockServer)
            wireMockServer.stop();  
	}
}