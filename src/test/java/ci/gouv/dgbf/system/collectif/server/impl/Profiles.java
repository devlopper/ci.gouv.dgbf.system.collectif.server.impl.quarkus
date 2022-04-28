package ci.gouv.dgbf.system.collectif.server.impl;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.quarkus.extension.test.Profile;

import io.quarkus.test.junit.QuarkusTestProfile;

public interface Profiles extends org.cyk.quarkus.extension.test.Profile {

	public class Configuration implements QuarkusTestProfile{
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = Profile.buildConfig(Configuration.class);
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Profile.buildTags(Configuration.class);
		}
	}
	
	public class Actor implements QuarkusTestProfile{
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = Profile.buildConfig(Actor.class);
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Profile.buildTags(Actor.class);
		}
	}
	
	public class ActorWithVisibilitiesDisabled implements QuarkusTestProfile{
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = Profile.buildConfig(ActorWithVisibilitiesDisabled.class);
			map.put("collectif.actor.visibilities.enabled", "false");
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Profile.buildTags(ActorWithVisibilitiesDisabled.class);
		}
	}
	
	public class LegislativeAct implements QuarkusTestProfile{
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = Profile.buildConfig(LegislativeAct.class);
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Profile.buildTags(LegislativeAct.class);
		}
	}
	
	public class LegislativeActVersion implements QuarkusTestProfile{
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = Profile.buildConfig(LegislativeActVersion.class);
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Profile.buildTags(LegislativeActVersion.class);
		}
	}
	
	public class Expenditure implements QuarkusTestProfile{
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = Profile.buildConfig(Expenditure.class);
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Profile.buildTags(Expenditure.class);
		}
	}
	
	public class Resource implements QuarkusTestProfile{
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = Profile.buildConfig(Resource.class);
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Profile.buildTags(Resource.class);
		}
	}
	
	public class ReadOnlyEntity implements QuarkusTestProfile{
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = Profile.buildConfig(ReadOnlyEntity.class);
			map.put("collectif.actor.visibilities.enabled", "false");
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Profile.buildTags(ReadOnlyEntity.class);
		}
	}
	
	public class RegulatoryAct implements QuarkusTestProfile{
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = Profile.buildConfig(RegulatoryAct.class);
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Profile.buildTags(RegulatoryAct.class);
		}
	}
	
	public class GeneratedAct implements QuarkusTestProfile{
		@Override
		public Map<String, String> getConfigOverrides() {
			Map<String, String> map = Profile.buildConfig(GeneratedAct.class);
			return map;
		}
		
		@Override
		public Set<String> tags() {
			return Profile.buildTags(GeneratedAct.class);
		}
	}
	
	/**/
	
	public interface Persistence {
		
		public interface Exercise {
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
				return Persistence.buildTags(ArrayUtils.addFirst(classes, Exercise.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Persistence.buildConfig(ArrayUtils.addFirst(classes, Exercise.class));
			}
		}
		
		public interface Expenditure {
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
			
			public class Import implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Import.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Import.class);
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
		
		public interface LegislativeAct {
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
				return Persistence.buildTags(ArrayUtils.addFirst(classes, LegislativeAct.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Persistence.buildConfig(ArrayUtils.addFirst(classes, LegislativeAct.class));
			}
		}
		
		public interface LegislativeActVersion {
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
				return Persistence.buildTags(ArrayUtils.addFirst(classes, LegislativeActVersion.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Persistence.buildConfig(ArrayUtils.addFirst(classes, LegislativeActVersion.class));
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
			return Profile.buildTags(ArrayUtils.addFirst(classes, Persistence.class));
		}
		
		public static Map<String,String> buildConfig(Class<?>...classes) {
			return Profile.buildConfig(classes);
		}
	}

	public interface Business {
		public interface LegislativeAct {
			public class Create implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Create.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Create.class);
				}
			}
			
			public class UpdateDefaultVersion implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(UpdateDefaultVersion.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(UpdateDefaultVersion.class);
				}
			}
			
			public class UpdateInProgress implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(UpdateInProgress.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(UpdateInProgress.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Business.buildTags(ArrayUtils.addFirst(classes, LegislativeAct.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Business.buildConfig(ArrayUtils.addFirst(classes, LegislativeAct.class));
			}
		}
		
		public interface LegislativeActVersion {
			public class Create implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Create.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Create.class);
				}
			}
			
			public class Copy implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Copy.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Copy.class);
				}
			}
			
			public class Duplicate implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Duplicate.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Duplicate.class);
				}
			}
			
			public static Set<String> buildTags(Class<?>...classes) {
				return Business.buildTags(ArrayUtils.addFirst(classes, LegislativeActVersion.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Business.buildConfig(ArrayUtils.addFirst(classes, LegislativeActVersion.class));
			}
		}
		
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
			
			public class Import implements QuarkusTestProfile {
				@Override
				public Map<String, String> getConfigOverrides() {
					return buildConfig(Import.class);
				}
				
				@Override
				public Set<String> tags() {
					return buildTags(Import.class);
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
			return Profile.buildTags(ArrayUtils.addFirst(classes, Business.class));
		}
		
		public static Map<String,String> buildConfig(Class<?>...classes) {
			return Profile.buildConfig(classes);
		}
	}

	public interface Service {
		
		public interface LegislativeAct {
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
				return Service.buildTags(ArrayUtils.addFirst(classes, LegislativeAct.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Service.buildConfig(ArrayUtils.addFirst(classes, LegislativeAct.class));
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
		
		public interface Exercise {
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
				return Service.buildTags(ArrayUtils.addFirst(classes, Exercise.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Service.buildConfig(ArrayUtils.addFirst(classes, Exercise.class));
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
			return Profile.buildTags(ArrayUtils.addFirst(classes, Service.class));
		}
		
		public static Map<String,String> buildConfig(Class<?>...classes) {
			return Profile.buildConfig(classes);
		}
	}
	
	public interface Client {
		public interface LegislativeAct {
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
				return Client.buildTags(ArrayUtils.addFirst(classes, LegislativeAct.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Client.buildConfig(ArrayUtils.addFirst(classes, LegislativeAct.class));
			}
		}
		
		public interface LegislativeActVersion {
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
				return Client.buildTags(ArrayUtils.addFirst(classes, LegislativeActVersion.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Client.buildConfig(ArrayUtils.addFirst(classes, LegislativeActVersion.class));
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
		
		public interface Exercise {
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
				return Client.buildTags(ArrayUtils.addFirst(classes, Exercise.class));
			}
			
			public static Map<String,String> buildConfig(Class<?>...classes) {
				return Client.buildConfig(ArrayUtils.addFirst(classes, Exercise.class));
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
			return Profile.buildTags(ArrayUtils.addFirst(classes, Client.class));
		}
		
		public static Map<String,String> buildConfig(Class<?>...classes) {
			return Profile.buildConfig(classes);
		}
	}
	
}