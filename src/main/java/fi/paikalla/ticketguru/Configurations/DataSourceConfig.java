package fi.paikalla.ticketguru.Configurations;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false) // kokeile kommentoida piiloon tämä rivi ja kaikki luokan sisältö paitsi "public class DataSourceConfig"
public class DataSourceConfig { // älä kommentoi pois
	
	@Bean // kommentoi piiloon tästä...
    @ConfigurationProperties(prefix = "application.datasource")
    public DataSource dataSource() {
		return DataSourceBuilder.create().build();
    }	
	
	public Connection kokeilu() throws SQLException {		
		DataSource datasource = dataSource();		
		return datasource.getConnection();		
	} // ... tänne

}
