package fi.paikalla.ticketguru.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Entities.TGUser;
import fi.paikalla.ticketguru.Repositories.TGUserRepository;
import fi.paikalla.ticketguru.dto.UserDto;

@RestController
public class UserController {
	@Autowired
	TGUserRepository userepo; 
	
	
	
	
	
	
	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<TGUser>> getUsers(){
		List<TGUser> list = (List<TGUser>) userepo.findAll(); 
		return new ResponseEntity<>(list, HttpStatus.OK); 
	}
	/*
	@GetMapping("/users/me") //yritys hakea omia tietoja. ei onnistu
	//@PreAuthorize("#username == authentication.principal.username")
	public String getOwnUser (Authentication authentication){
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//String currentPrincipalName = authentication.
		//TGUser res = userepo.findByUserName(username); 
		//return new ResponseEntity<>(authentication, HttpStatus.OK); 
		//todo 
		return authentication.toString(); 
	}
	*/
	
	@PostMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> makeUser(@Valid UserDto user, BindingResult bindres) {
		if(bindres.hasErrors()) {
			List<String> errors = bindres.getAllErrors().stream()
					.map(e -> e.getDefaultMessage())
					.collect(Collectors.toList());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); 
		}
		try {
			for (TGUser find : userepo.findAll()) {
				if (find.getUserName().equals(user.getUserName())) {
					throw new Exception(); 
				}
			}
			TGUser create = new TGUser(user.getFirstName(), 
					user.getLastName(), 
					user.getUserName(), 
					user.getPassword()); 
			userepo.save(create); 
			
			return new ResponseEntity<>(create, HttpStatus.CREATED); 
	
		} catch (Exception e) {
			Map<String, String> response = new HashMap<>(); 
			response.put("message", "this username already exists");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); 
		}
		
	}
	

}
