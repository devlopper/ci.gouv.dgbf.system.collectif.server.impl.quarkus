package ci.gouv.dgbf.system.collectif.server.impl;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.string.StringHelper;

import io.quarkus.test.junit.QuarkusTestProfile;

public interface Profiles {
	
	public interface Persistence {
		
		public interface Expenditure {
			public class Default implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Default.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Default.class);
				}
			}
			
			public class Amount implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Amount.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Amount.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Persistence.buildTags(ArrayUtils.addFirst(classes, Expenditure.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Persistence.buildConfig(ArrayUtils.addFirst(classes, Expenditure.class));
			}
		}
		
		public interface Resource {
			public class Read implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Read.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Read.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Persistence.buildTags(ArrayUtils.addFirst(classes, Resource.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Persistence.buildConfig(ArrayUtils.addFirst(classes, Resource.class));
			}
		}
		
		public interface RegulatoryAct {
			public class Read implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Read.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Read.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Persistence.buildTags(ArrayUtils.addFirst(classes, RegulatoryAct.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Persistence.buildConfig(ArrayUtils.addFirst(classes, RegulatoryAct.class));
			}
		}
		
		public interface Filter {
			public class Default implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Default.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Default.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Persistence.buildTags(ArrayUtils.addFirst(classes, Filter.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Persistence.buildConfig(ArrayUtils.addFirst(classes, Filter.class));
			}
		}
		
		public static Set<String> buildTags(Class<?>...classes) {
			return Profiles.buildTags(ArrayUtils.addFirst(classes, Persistence.class));
		}
		
		public static Map<String,String> buildConfig(Class<?>...classes) {
			return Profiles.buildConfig(classes);
		}
	}

	public interface Business {
		public interface Expenditure {
			public class Adjust implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Adjust.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Adjust.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Business.buildTags(ArrayUtils.addFirst(classes, Expenditure.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Business.buildConfig(ArrayUtils.addFirst(classes, Expenditure.class));
			}
		}
		
		public interface Resource {
			public class Adjust implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Adjust.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Adjust.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Business.buildTags(ArrayUtils.addFirst(classes, Resource.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Business.buildConfig(ArrayUtils.addFirst(classes, Resource.class));
			}
		}
		
		public interface RegulatoryAct {
			public class IncludeExclude implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(IncludeExclude.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(IncludeExclude.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Business.buildTags(ArrayUtils.addFirst(classes, RegulatoryAct.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Business.buildConfig(ArrayUtils.addFirst(classes, RegulatoryAct.class));
			}
		}
		
		public interface GeneratedAct {
			public class Generate implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Generate.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Generate.class);
				}
			}
			
			public class Delete implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Delete.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Delete.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Business.buildTags(ArrayUtils.addFirst(classes, GeneratedAct.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Business.buildConfig(ArrayUtils.addFirst(classes, GeneratedAct.class));
			}
		}
		
		public static Set<String> buildTags(Class<?>...classes) {
			return Profiles.buildTags(ArrayUtils.addFirst(classes, Business.class));
		}
		
		public static Map<String,String> buildConfig(Class<?>...classes) {
			return Profiles.buildConfig(classes);
		}
	}

	public interface Service {
		
		public interface Resource {
			public class Read implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Read.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Read.class);
				}
			}
			
			public class Adjust implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Adjust.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Adjust.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Service.buildTags(ArrayUtils.addFirst(classes, Resource.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Service.buildConfig(ArrayUtils.addFirst(classes, Resource.class));
			}
		}
		
		public interface GeneratedAct {
			public class Generate implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Generate.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Generate.class);
				}
			}
			
			public class Delete implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Delete.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Delete.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Service.buildTags(ArrayUtils.addFirst(classes, GeneratedAct.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Service.buildConfig(ArrayUtils.addFirst(classes, GeneratedAct.class));
			}
		}
		
		public class Unit implements QuarkusTestProfile {
			@Override
			public Set<String> tags() {
				return Service.buildTags(Unit.class);
			}
		}
		
		public class Integration implements QuarkusTestProfile {
			@Override
			public Map<String, String> getConfigOverrides() {
				return Service.buildConfig(Integration.class);
			}
			
			@Override
			public Set<String> tags() {
				return Service.buildTags(Integration.class);
			}
		}
		
		public static Set<String> buildTags(Class<?>...classes) {
			return Profiles.buildTags(ArrayUtils.addFirst(classes, Service.class));
		}
		
		public static Map<String,String> buildConfig(Class<?>...classes) {
			return Profiles.buildConfig(classes);
		}
	}
	
	public interface Client {
		public interface Resource {
			public class Read implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Read.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Read.class);
				}
			}
			
			public class Adjust implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Adjust.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Adjust.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Client.buildTags(ArrayUtils.addFirst(classes, Resource.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Client.buildConfig(ArrayUtils.addFirst(classes, Resource.class));
			}
		}
		
		public interface GeneratedAct {
			public class Generate implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Generate.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Generate.class);
				}
			}
			
			public class Delete implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Delete.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Delete.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Client.buildTags(ArrayUtils.addFirst(classes, GeneratedAct.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Client.buildConfig(ArrayUtils.addFirst(classes, GeneratedAct.class));
			}
		}
		
		public class Default implements QuarkusTestProfile {
			@Override
			public Map<String, String> getConfigOverrides() {
				return Client.buildConfig(Client.class,Default.class);
			}
			
			@Override
			public Set<String> tags() {
				return Client.buildTags(Default.class);
			}
		}
		
		public static Set<String> buildTags(Class<?>...classes) {
			return Profiles.buildTags(ArrayUtils.addFirst(classes, Client.class));
		}
		
		public static Map<String,String> buildConfig(Class<?>...classes) {
			return Profiles.buildConfig(classes);
		}
	}
	
	/**/
	
	public static Set<String> buildTags(Set<Class<?>> classes) {
		Set<String> tags = classes.stream().map(klass -> klass.getSimpleName().toLowerCase()).collect(Collectors.toSet());
		tags.add(StringHelper.concatenate(tags, "."));
		return tags;
	}
	
	public static Set<String> buildTags(Class<?>...classes) {
		return buildTags(ArrayHelper.isEmpty(classes) ? null : Set.of(classes));
	}
	
	public static Map<String,String> buildConfig(Set<Class<?>> classes) {
		Map<String, String> config = new HashMap<>();
		config.put("quarkus.hibernate-orm.sql-load-script", String.format("sql/%s.sql", classes.stream().map(klass -> klass.getSimpleName().toLowerCase()).collect(Collectors.joining("-"))));
		return config;
	}
	
	public static Map<String,String> buildConfig(Class<?>...classes) {
		return buildConfig(ArrayHelper.isEmpty(classes) ? null : new LinkedHashSet<Class<?>>(List.of(classes)));
	}
}