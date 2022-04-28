package ci.gouv.dgbf.system.collectif.server.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;

import io.smallrye.config.ConfigSourceContext;
import io.smallrye.config.PropertiesConfigSource;

public class ConfigSourceFactory implements io.smallrye.config.ConfigSourceFactory,Serializable {

	private static final String CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_NAME = "cyk.config.source.factory.data.source.name";
	private static final String CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_USERNAME = "quarkus.datasource.username";
	private static final String CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_PASSWORD = "quarkus.datasource.password";
	private static final String CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_URL = "quarkus.datasource.jdbc.url";
	private static final String CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_QUERY_STRING = "cyk.config.source.factory.data.source.query.string";
	
	@Override
	public Iterable<ConfigSource> getConfigSources(ConfigSourceContext configSourceContext) {
		// FIXME +++++++++++++++++++++++++++++++++ AVOID INIT TO BE DONE TWICE!!! +++++++++++++++++++++++++++++++++++++++++++++
		ConfigSource dataSourceConfigSource = instantiateFromDataSource(configSourceContext);
		Collection<ConfigSource> collection = new ArrayList<>();
		if(dataSourceConfigSource != null)
			collection.add(dataSourceConfigSource);
		return collection;
	}

	private ConfigSource instantiateFromDataSource(ConfigSourceContext configSourceContext) {
		String query = getJdbcQuery(configSourceContext);
		if(StringHelper.isBlank(query)) {
			LogHelper.logFine("Query cannot be derived. Configuration will not be read from datasource", getClass());
			return null;
		}
		
		String databaseGeneration = read(configSourceContext, "quarkus.hibernate-orm.database.generation");
		if("create".equals(databaseGeneration) || "drop".equals(databaseGeneration) || "drop-and-create".equals(databaseGeneration)) {
			LogHelper.logInfo(String.format("Configuration from data source will not be read because of data generation mode : %s",databaseGeneration), getClass());
			return null;
		}
		
		if(!Boolean.TRUE.equals(loadJdbcDriver(getJdbcDriverClassName(configSourceContext))))
			return null;
		try {
			try (Connection connection = getJdbcConnection(configSourceContext)) {
				if(connection == null) {
					LogHelper.logWarning("Configuration from data source will not be read because jdbc connection cannot be acquired", getClass());
					return null;
				}
				PreparedStatement preparedStatement = connection.prepareStatement(query);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            Map<String,String> map = new HashMap<>();
	            while (resultSet.next())
	                map.put(resultSet.getString("CODE"), resultSet.getString("VALEUR"));
	            resultSet.close();
	            preparedStatement.close();
	            connection.close();
	            if(MapHelper.isEmpty(map))
	            	return null;
	            LogHelper.logInfo(String.format("Configuration from data source : %s", map), getClass());
				return new PropertiesConfigSource(map, getJdbcName(configSourceContext), 900);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	private static String getJdbcQuery(ConfigSourceContext configSourceContext) {
		return read(configSourceContext, CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_QUERY_STRING);
	}
	
	private static String getJdbcName(ConfigSourceContext configSourceContext) {
		return ValueHelper.defaultToIfBlank(read(configSourceContext, CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_NAME),"DataSourceConfigurationValues");
	}
	
	private static Connection getJdbcConnection(ConfigSourceContext configSourceContext) {
		String url = read(configSourceContext, CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_URL);
		if(StringHelper.isBlank(url)) {
			LogHelper.logWarning(String.format("Configuration data source url has not been set using %s",CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_URL), ConfigSourceFactory.class);
			return null;
		}
		String username = read(configSourceContext, CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_USERNAME);
		if(StringHelper.isBlank(username)) {
			LogHelper.logWarning(String.format("Configuration data source username has not been set using %s",CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_USERNAME), ConfigSourceFactory.class);
			return null;
		}
		String password = read(configSourceContext, CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_PASSWORD);
		if(StringHelper.isBlank(password)) {
			LogHelper.logWarning(String.format("Configuration data source password has not been set using %s",CYK_CONFIG_SOURCE_FACTORY_DATA_SOURCE_PASSWORD), ConfigSourceFactory.class);
			return null;
		}
		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	private static Boolean loadJdbcDriver(String className) {
		if(StringHelper.isBlank(className))
			return null;
		try {
			Class.forName(className);
			return Boolean.TRUE;
		} catch (ClassNotFoundException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	private static String getJdbcDriverClassName(ConfigSourceContext configSourceContext) {
		String name = read(configSourceContext,"quarkus.datasource.jdbc.driver");
		if(StringHelper.isNotBlank(name))
			return name;
		name = read(configSourceContext,"quarkus.datasource.db-kind");
		if(StringHelper.isBlank(name))
			return null;
		switch(name) {
		case "h2" : return "org.h2.Driver";
		case "oracle" : return "oracle.jdbc.driver.OracleDriver";
		case "mysql" : return "com.mysql.cj.jdbc.Driver";
		default: return null;
		}
	}
	
	private static String read(ConfigSourceContext configSourceContext,String name) {
		if(StringHelper.isBlank(name))
			return null;
		ConfigValue configValue = configSourceContext.getValue(name);
		if (configValue == null)
			return null;
		return configValue.getValue();
	}
}