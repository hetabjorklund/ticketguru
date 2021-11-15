package fi.paikalla.ticketguru.Components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Component
public class ErrorResponseGenerator {
	public Map<String, Object> generateErrorResponseFromBindingResult(BindingResult bindingResult){
		Map<String, Object> response = new HashMap<>();
		
		List<ObjectError> errors = bindingResult.getAllErrors(); // Haetaan kaikki virheet listaan
		StringBuilder strBuilder = new StringBuilder(errors.size()); // Alustetaan vastausviestiä
		
		for(ObjectError error: errors) { //Käydään kaikki virheet läpi ja lisätään niiden default message palautusviestiin
			strBuilder.append(error.getDefaultMessage() + ". ");
		}
		
		String errorMessage = strBuilder.toString().trim(); // Muutetaan palautusviesti Stringiksi ja lähetetään se clientille
		response.put("status", "400");
		response.put("message", errorMessage);
		
		return response;
	}
	
	public Map<String, String> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
	    Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
	    Map<String, String> response = new HashMap<>();
	    String errorMessage = "";
	    if (!violations.isEmpty()) {
	        StringBuilder builder = new StringBuilder();
	        violations.forEach(violation -> builder.append(violation.getMessage() + ". "));
	        errorMessage = builder.toString().trim();
	    } else {
	        errorMessage = "ConstraintViolationException occured.";
	    }
	    
	    response.put("message", errorMessage);
	    return response;
	 }
}
