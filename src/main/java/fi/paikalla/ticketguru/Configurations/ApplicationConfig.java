package fi.paikalla.ticketguru.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ApplicationConfig {

	 @Bean
	 public ObjectMapper objectMapper() {
		 ObjectMapper objectMapper = new ObjectMapper();
		 objectMapper.findAndRegisterModules();		 
		 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	     return objectMapper;
	 }
	 
}
