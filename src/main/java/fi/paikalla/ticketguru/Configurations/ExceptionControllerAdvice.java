package fi.paikalla.ticketguru.Configurations;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionControllerAdvice {
	
	@ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<?> exception(AccessDeniedException e) {
		Map<String, String> response = new HashMap<String, String>(); 
		response.put("message", "You do not have access to this resource");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    } 

}
