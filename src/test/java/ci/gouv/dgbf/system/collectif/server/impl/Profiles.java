package ci.gouv.dgbf.system.collectif.server.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.quarkus.test.junit.QuarkusTestProfile;

public interface Profiles {
	
	public interface Persistence {
		public class Default implements QuarkusTestProfile {
			@Override
			public Map<String, String> getConfigOverrides() {
				Map<String, String> map = new HashMap<>();
				map.put("quarkus.hibernate-orm.sql-load-script", "persistence.sql");
				return map;
			}
			
			@Override
			public Set<String> tags() {
				return Set.of(Persistence.class.getSimpleName().toLowerCase()+"."+Default.class.getSimpleName().toLowerCase());
			}
		}
		
		public class Filter implements QuarkusTestProfile {
			@Override
			public Map<String, String> getConfigOverrides() {
				Map<String, String> map = new HashMap<>();
				map.put("quarkus.hibernate-orm.sql-load-script", "persistence-filter.sql");
				return map;
			}
			
			@Override
			public Set<String> tags() {
				return Set.of(Persistence.class.getSimpleName().toLowerCase()+"."+Filter.class.getSimpleName().toLowerCase());
			}
		}
	}
		
	public class Business implements QuarkusTestProfile {
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = new HashMap<>();
			map.put("quarkus.hibernate-orm.sql-load-script", "business.sql");
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Collections.singleton(Business.class.getSimpleName().toLowerCase());
		}
	}

	public interface Service {
		public class Unit implements QuarkusTestProfile {
			@Override
			public Set<String> tags() {
				return Set.of(Service.class.getSimpleName().toLowerCase()+"."+Unit.class.getSimpleName().toLowerCase());
			}
		}
		
		public class Integration implements QuarkusTestProfile {
			@Override
			public Map<String, String> getConfigOverrides() {
				Map<String, String> map = new HashMap<>();
				map.put("quarkus.hibernate-orm.sql-load-script", "service.sql");
				return map;
			}
			
			@Override
			public Set<String> tags() {
				return Set.of(Service.class.getSimpleName().toLowerCase()+"."+Integration.class.getSimpleName().toLowerCase());
			}
		}
	}
	
	public class Client implements QuarkusTestProfile {
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = new HashMap<>();
			map.put("quarkus.hibernate-orm.sql-load-script", "client.sql");
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Collections.singleton(Client.class.getSimpleName().toLowerCase());
		}
	}
	
	/**/
	
	public class Unit implements QuarkusTestProfile {
		@Override
		public Set<String> tags() {
			return Collections.singleton(Unit.class.getSimpleName().toLowerCase());
		}
	}
	
	public class Integration implements QuarkusTestProfile {
		@Override
		public Set<String> tags() {
			return Collections.singleton(Integration.class.getSimpleName().toLowerCase());
		}
	}
}