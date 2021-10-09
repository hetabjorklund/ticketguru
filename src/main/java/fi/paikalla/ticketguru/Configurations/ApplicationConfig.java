package fi.paikalla.ticketguru.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;

@Configuration
public class ApplicationConfig {

	 @Bean
	 public ObjectMapper objectMapper() {
		 ObjectMapper objectMapper = new ObjectMapper();
		 //objectMapper.registerModule(new JSR353Module());
		 objectMapper.findAndRegisterModules();		 
		 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	     return objectMapper;
	 }
	 
}
