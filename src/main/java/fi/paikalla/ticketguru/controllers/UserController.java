package fi.paikalla.ticketguru.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Entities.Invoice;
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
	
	@GetMapping("/users/me") //yritys hakea omia tietoja. ei onnistu
	//@PreAuthorize("#username == authentication.principal.username")
	public ResponseEntity<?> getOwnUser (/*Authentication authentication*/){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails)principal;
			String username = userDetails.getUsername();
			TGUser currUser = userepo.findByUserName(username);
			
			return new ResponseEntity<>(currUser, HttpStatus.OK);
		} else {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Something went wrong");
			
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		
		
		
		//authentication = SecurityContextHolder.getContext().getAuthentication();
		//String currentPrincipalName = authentication.toString();
		//return currentPrincipalName;
		//TGUser res = userepo.findByUserName(username); 
		//return new ResponseEntity<>(authentication, HttpStatus.OK); 
		//todo 
		//return authentication.toString(); 
	}
	
	
	@PostMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> makeUser(@Valid @RequestBody UserDto user, BindingResult bindres) {
		if(bindres.hasErrors()) {
			return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST); 
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
	//TGUserin settereiden privaattiasetus estää täyden päivityksen,
	@PutMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> alterPassword(@PathVariable(value = "id") long userId, 
			@Valid @RequestBody UserDto user, BindingResult bindres) {
		if(bindres.hasErrors()) {
			return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST); 
		}
		Map<String, String> response = new HashMap<>();
		try {
			Optional<TGUser> opt = userepo.findById(userId);
			if (!opt.isPresent()) {
				throw new Exception();
			}
			TGUser setter = opt.get(); 
			if (setter.getUserName().equals(user.getUserName())) {//jos username sama, päivitä. 
				setter.updatePassword(user.getPassword()); 
			}
			userepo.save(setter); 
			response.put("message", "password updated");
			return new ResponseEntity<>(response, HttpStatus.OK); 
		} catch (Exception e) {
			response.put("message", "this user id does not exist");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); 
		}		
	}
	
	@DeleteMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable(value = "id") long userId) {
		Optional<TGUser> user = userepo.findById(userId); 
		Map<String, String> response = new HashMap<String, String>(); 
		if (user.isPresent()) { //jos löytyy, onko lippuja?
			List<Invoice> invoices = user.get().getInvoices(); 
			if (invoices.size() > 0) { //jos on laskuja, palauttaa kiellon. 
				response.put("message", "There are invoices associated with this user");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); 
			} else { //ei laskuja, poistetaan tyyppi
				userepo.delete(user.get()); 
				response.put("message", "User deleted");
				return new ResponseEntity<Map<String, String>>(response, HttpStatus.NO_CONTENT); //palauta no content. 
			}
		} else {//käyttäjää ei ole olemassa, palauta not found. 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
		}
		
	}
	
	
	
	

}
