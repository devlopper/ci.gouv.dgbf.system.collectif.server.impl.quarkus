package ci.gouv.dgbf.system.collectif.server.impl.client;

import java.io.Serializable;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Path("/")
@RegisterRestClient(configKey = ActorClient.CONFIG_KEY)
public interface ActorClient {
	static String CONFIG_KEY = "actor";
	
	@GET
	@Path("open/domaine/obtenir-visibles-par-acteur")
	@Produces({MediaType.APPLICATION_JSON})
	List<VisibilityDto> getVisibilities(@QueryParam("code_type_domaine") String scopeTypeCode,@QueryParam("nom_utilisateur") String username);
	
	@Getter @Setter @Accessors(chain=true)
	public class VisibilityDto implements Serializable {

		@JsonbProperty(value = "identifiant") String identifier;
		@JsonbProperty(value = "code") String code;
		@JsonbProperty(value = "libelle") String name;
		
		@Override
		public String toString() {
			return String.format("identifiant=%s, code=%s, name=%s", identifier,code,name);
		}
	}
	
	/**/
	
	String CODE_TYPE_DOMAINE_CATEGORIE_BUDGET = "CATEGORIE_BUDGET";
	String CODE_TYPE_DOMAINE_SECTION = "SECTION";
	String CODE_TYPE_DOMAINE_USB = "USB";
	String CODE_TYPE_DOMAINE_ACTION = "ACTION";
}