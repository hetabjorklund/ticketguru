package fi.paikalla.ticketguru.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.EventStatus;
import fi.paikalla.ticketguru.Repositories.EventRepository;
import fi.paikalla.ticketguru.Repositories.EventStatusRepository;

@RestController
public class StatusController {
	
	@Autowired
	private EventStatusRepository statusrepo;
	
	@Autowired
	private EventRepository eventrepo;
	
	@GetMapping("/status") // hakee kaikki statukset
	public ResponseEntity<List<EventStatus>> getAllStatuses() {
		List<EventStatus> list = (List<EventStatus>) statusrepo.findAll();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	/*
	@GetMapping("/status/{id}")
	public ResponseEntity<EventStatus> getEventById(@PathVariable (value = "id") Long statusId) {
		Optional<EventStatus> status = statusrepo.findById(statusId);
		if (status.isEmpty()) {
			return new ResponseEntity<> (status, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<> (status, HttpStatus.OK);
		}
	}
	
	
	@GetMapping ("/status/{id}/events") // hakee kaikki tapahtumat statuksen perusteella
	public ResponseEntity<List<Event>> getEventsByStatus(@PathVariable (value = "id") Long statusId) {
		Optional<EventStatus> status = statusrepo.findById(statusId);
		List<Event> elist =  eventrepo.findByStatus(status.getStatusName()); 
		if (status.isEmpty()) {
			return new ResponseEntity<>(elist, HttpStatus.NOT_FOUND); 
		} if (elist.isEmpty()) {
			return new ResponseEntity<>(elist, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(elist, HttpStatus.OK);
		}
	}
	*/
	
	@PostMapping ("/status")// POST luo uuden statuksen
	public ResponseEntity<EventStatus> createEventStatus(@RequestBody EventStatus status) {
		if (status.getStatusName() == null) { // tarkistetaan tuleeko pyynnön mukana tapahtuman nimi
			return new ResponseEntity<> (status, HttpStatus.BAD_REQUEST); // jos ei statuksella ole nimeä, sitä ei luoda
		} 
		if (statusrepo.findByStatusName(status.getStatusName()) != null) { // tarkistaa onko kyseinen status jo olemassa
			return new ResponseEntity<> (statusrepo.findByStatusName(status.getStatusName()), HttpStatus.CONFLICT); //palauttaa olemassa olevan statuksen, jos sellainen löytyy
		} else {
			return new ResponseEntity<> (statusrepo.save(status), HttpStatus.CREATED); // luodaan uusi status
		}
	}
	
	@PatchMapping("/status/{id}")// muokkaa statuksen nimeä
	public ResponseEntity<?> updateName(@PathVariable(value = "id") Long statusId, 
			@RequestBody EventStatus status) {
		Optional<EventStatus> estatus = statusrepo.findById(statusId);
		if (estatus.isEmpty()) { // tarkistaa löytyikö status, jos ei löydy kannasta
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else { // jos status löytyy kannasta, nimetään uusiksi
			EventStatus newStatus = estatus.get();
			newStatus.setStatusName(status.getStatusName());
			return new ResponseEntity<>(newStatus, HttpStatus.OK);
		}
		
	}
	
	@DeleteMapping("/status/{id}")// merkkaa statuksen poistetuksi
	public ResponseEntity<HashMap<String, Boolean>> deleteStatusById(@PathVariable(value = "id") Long statusId) {
		Optional<EventStatus> status = statusrepo.findById(statusId);
		if (status.isEmpty()) { // jos statusta ei löydy kannasta
			HashMap<String, Boolean> response = new HashMap<>();
			response.put("deleted", Boolean.FALSE);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} else { // jos status löytyy kannasta
			statusrepo.delete(status.get());
			HashMap<String, Boolean> response = new HashMap<>();
			response.put("deleted", Boolean.TRUE);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
	
	
}
