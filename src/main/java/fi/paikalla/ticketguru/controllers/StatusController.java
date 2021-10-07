package fi.paikalla.ticketguru.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.EventRepository;
import fi.paikalla.ticketguru.Repositories.EventStatusRepository;
import fi.paikalla.ticketguru.dto.TicketTypeDto;

@RestController
public class StatusController {
	
	@Autowired
	private EventStatusRepository statusrepo;
	
	@Autowired
	private EventRepository eventrepo;
	
	@GetMapping("/status") // hakee kaikki statukset
	public ResponseEntity<List<EventStatus>> getAllStatuses() {
		List<EventStatus> list = (List<EventStatus>) statusrepo.findAll(); // muodostaa listan kaikista reposition statuksista
		return new ResponseEntity<>(list, HttpStatus.OK); // palauttaa haetun listan
	}
	
	@GetMapping("/status/{id}")
	public ResponseEntity<?> getEventById(@PathVariable (value = "id") Long statusId) {
		Optional<EventStatus> status = statusrepo.findById(statusId); // hakee mahdollisen statuksen repositiosta id:n perusteella
		if (status.isEmpty()) { // tarkistetaan onko status tyhjä
			return new ResponseEntity<> (status, HttpStatus.NOT_FOUND); // mikäli status on tyhjä, palautetaan status = null ja 404-koodi
		} else {
			return new ResponseEntity<> (status, HttpStatus.OK); // mikäli status löytyy, palautetaan statuksen tiedot ja 200-koodi
		}
	}
	
	/*
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
			return new ResponseEntity<> (status, HttpStatus.BAD_REQUEST); // jos ei statukselle ole annettu nimeä, sitä ei luoda ja palautetaan 400-koodi
		} 
		if (statusrepo.findByStatusName(status.getStatusName()) != null) { // tarkistetaan onko kyseinen status jo olemassa kannassa
			return new ResponseEntity<> (statusrepo.findByStatusName(status.getStatusName()), HttpStatus.CONFLICT); //palauttaa olemassa olevan statuksen, jos sellainen löytyy sekä 409-koodin
		} else {
			return new ResponseEntity<> (statusrepo.save(status), HttpStatus.CREATED); // luodaan uusi status, palautetaan sen tiedot ja 201-koodi
		}
	}
	
	@PatchMapping("/status/{id}")// muokkaa statuksen nimeä
	public ResponseEntity<?> updateName(@PathVariable(value = "id") Long statusId, 
			@RequestBody EventStatus status) {
		Optional<EventStatus> estatus = statusrepo.findById(statusId); // haetaan mahdollinen status kannasta id:n perusteella
		if (estatus.isEmpty()) { // tarkistaa löytyikö status kannasta
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // jos status ei löydy, palautetaan 404-koodi
		} else { 
			EventStatus newStatus = estatus.get(); // jos status löytyy kannasta, haetaan statuksen tiedot
			newStatus.setStatusName(status.getStatusName()); // status nimetään uusiksi annetulla nimellä
			return new ResponseEntity<>(newStatus, HttpStatus.OK); // palautetaan korjatut statuksen tiedot ja 200-koodi
		}
		
	}
	
	@DeleteMapping("/status/{id}")// merkkaa statuksen poistetuksi
	public ResponseEntity<HashMap<String, Boolean>> deleteStatusById(@PathVariable(value = "id") Long statusId) {
		Optional<EventStatus> status = statusrepo.findById(statusId);
		if (status.isEmpty()) { // jos statusta ei löydy kannasta
			HashMap<String, Boolean> response = new HashMap<>(); // luodaan uusi hashmap responsea varten
			response.put("deleted", Boolean.FALSE); // asetetaan tiedot hashmapiin
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // palautetaan luotu hashmap sekä 404-koodi
		} else { // jos status löytyy kannasta
			statusrepo.delete(status.get());
			HashMap<String, Boolean> response = new HashMap<>(); // luodaan uusi hashmap responsea varten
			response.put("deleted", Boolean.TRUE); // asetetaan tiedot hashmapiin
			return new ResponseEntity<>(response, HttpStatus.OK); // palautetaan luotu hashmap sekä 200-koodi
		}
	}
	
	
}
