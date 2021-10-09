package fi.paikalla.ticketguru.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
			return new ResponseEntity<> ("Status not found", HttpStatus.NOT_FOUND); // mikäli status on tyhjä, palautetaan status = null ja 404-koodi
		} else {
			return new ResponseEntity<> (status, HttpStatus.OK); // mikäli status löytyy, palautetaan statuksen tiedot ja 200-koodi
		}
	}
	
	// ONGELMA: MILLÄ SAA HAETTUA TAPAHTUMALISTAN? Nyt heittää 500:sen
	@GetMapping ("/status/{id}/events") // hakee kaikki tapahtumat statuksen perusteella
	public ResponseEntity<?> getEventsByStatus2(@PathVariable (value = "id") Long statusId) {
		Optional<EventStatus> status = statusrepo.findById(statusId); // haetaan mahdollinen status statusreposta id:n perusteella
		try {
			List<Event> elist =  eventrepo.findByStatus(statusId); // haetaan lista tapahtumista statuksen id:n perusteella
			if (status.isEmpty()) { // tarkistetaan löytyikö status kannasta
				return new ResponseEntity<>("Status not found", HttpStatus.NOT_FOUND); // jos statusta ei löydy, palautetaan viesti sekä 404-koodi
			} if (elist.isEmpty()) { // tarkistetaan onko statukseen liitetty tapahtumia
				return new ResponseEntity<>(elist, HttpStatus.OK); // jos tapahtumalista on tyhjä, palautuu tyhjä lista ja 200-koodi
			} else {
				return new ResponseEntity<>(elist, HttpStatus.OK); // palautetaan tapahtumalista ja 200-koodi
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Status not found", HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping ("/status")// luo uuden statuksen, VALIDOINTI HEITTÄÄ AUTOMAATTIRESPONSEN
	public ResponseEntity<?> createEventStatus (@Valid @RequestBody EventStatus status, BindingResult bindingresult) {
		if (bindingresult.hasErrors()) { // tarkistetaan tuleeko pyynnön mukana statukselle nimi
			return new ResponseEntity<> ("Status name is missing", HttpStatus.BAD_REQUEST); // jos ei statukselle ole annettu nimeä, sitä ei luoda ja palautetaan 400-koodi
		} else { // jos pyynnössä on statukselle nimi
			if (statusrepo.findByStatusName(status.getStatusName()) != null) { // tarkistetaan onko kyseinen status jo olemassa kannassa
				return new ResponseEntity<> (statusrepo.findByStatusName(status.getStatusName()), HttpStatus.CONFLICT); //palauttaa olemassa olevan statuksen, jos sellainen löytyy sekä 409-koodin
			} else {
				return new ResponseEntity<> (statusrepo.save(status), HttpStatus.CREATED); // luodaan uusi status, palautetaan sen tiedot ja 201-koodi
			}
		}
	}
	
	@PatchMapping("/status/{id}")// muokkaa statuksen nimeä, VALIDOINTI HEITTÄÄ AUTOMAATTIRESPONSEN
	public ResponseEntity<?> updateName(@PathVariable(value = "id") Long statusId, 
			@Valid @RequestBody EventStatus status, BindingResult bindingresult) {
		if (bindingresult.hasErrors()) { // tarkistetaan tuleeko pyynnön mukana statukselle nimi
			return new ResponseEntity<> ("Status name is missing", HttpStatus.BAD_REQUEST); // jos ei statukselle ole annettu nimeä, sitä ei luoda ja palautetaan 400-koodi
		} else { // jos pyynnön mukana tulee statukselle nimi
			Optional<EventStatus> estatus = statusrepo.findById(statusId); // haetaan mahdollinen status kannasta id:n perusteella
			if (estatus.isEmpty()) { // tarkistetaan löytyikö status kannasta
				return new ResponseEntity<>("Status not found", HttpStatus.NOT_FOUND); // jos status ei löydy, palautetaan viesti ja 404-koodi
			} else { 
				EventStatus newStatus = estatus.get(); // jos status löytyy kannasta, haetaan statuksen tiedot
				newStatus.setStatusName(status.getStatusName()); // status nimetään uusiksi pyynnön mukana toimitetulla nimellä
				statusrepo.save(newStatus); // tallennetaan status uudella nimellä kantaan
				return new ResponseEntity<>(newStatus, HttpStatus.OK); // palautetaan korjatut statuksen tiedot ja 200-koodi
			}
		}
	}
	
	@DeleteMapping("/status/{id}") // poistaa statuksen id:n perusteella, mikäli siihen ei ole liitetty tapahtumia
	public ResponseEntity<?> deleteStatusById(@PathVariable(value = "id") Long statusId) {
		Optional<EventStatus> status = statusrepo.findById(statusId); // haetaan mahdollinen status kannasta id:n perusteella
		if (status.isEmpty()) { // jos statusta ei löydy kannastsa
			return new ResponseEntity<>("Status not found", HttpStatus.NOT_FOUND); // palautetaan teskti sekä 404-koodi
		} else { // jos status löytyy kannasta
			if (status.get().getEvent().isEmpty()) { // tarkistetaan onko statukseen tapahtumalista tyhjä
				statusrepo.deleteById(statusId); // poistetaan status kannasta
				return new ResponseEntity<>("Status deleted", HttpStatus.NO_CONTENT); // palautetaan teksti sekä 204-koodi
			} else { // jos statuksen tapahtumalistassa on tapahtumia
				return new ResponseEntity<>("Status has associated events, deletion forbidden", HttpStatus.FORBIDDEN); // palautetaan teksti sekä 403-koodi
			}
		}
	}
	
	/*
	@DeleteMapping("/status/{id}")// merkkaa statuksen poistetuksi
	public ResponseEntity<HashMap<String, Boolean>> deleteStatusById(@PathVariable(value = "id") Long statusId) {
		Optional<EventStatus> status = statusrepo.findById(statusId); // heataan mahdollinen status kannasta id:n perusteella
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
	*/
	
}
