package fi.paikalla.ticketguru.Configurations;

import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.datatype.jsr353.JSR353Module;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ObjectMapperConfig {

	 @Bean
	 public ObjectMapper objectMapper() {
		 ObjectMapper objectMapper = new ObjectMapper();
		 objectMapper.findAndRegisterModules();	
		 objectMapper.registerModule(new JSR353Module());
		 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	     return objectMapper;
	 }
	 
}
