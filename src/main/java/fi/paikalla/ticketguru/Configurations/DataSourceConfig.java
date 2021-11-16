package fi.paikalla.ticketguru.Configurations;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration(proxyBeanMethods = false)
public class DataSourceConfig {
	
	/*@Bean // kommentoi piiloon tästä...
    @ConfigurationProperties(prefix = "application.datasource")
    public DataSource dataSource() {
		return DataSourceBuilder.create().build();
    }	
	
	public Connection dataSourceConnection() throws SQLException {		
		DataSource datasource = dataSource();		
		return datasource.getConnection();		
	}*/

}
