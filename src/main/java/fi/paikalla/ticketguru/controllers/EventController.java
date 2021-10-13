package fi.paikalla.ticketguru.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.json.JsonPatch;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import fi.paikalla.ticketguru.Entities.*;
import fi.paikalla.ticketguru.Repositories.*;
import fi.paikalla.ticketguru.Services.EventService;
import fi.paikalla.ticketguru.Services.TicketService;

@RestController
public class EventController {
	
	@Autowired
	private EventRepository eventrepo; 
	@Autowired
	private TicketService ticketservice;

	@Autowired
	private EventService eventservice; 
	
	@DeleteMapping("/events/{id}") // poista yksittäinen tapahtuma id:n perusteella. Endpointia /events joka poistaisi kaikki tapahtumat, ei tarvita
	public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
		
		if (this.eventrepo.findById(id).isPresent()) { // jos haetulla id:llä löytyy tapahtuma
			
			Event event = this.eventrepo.findById(id).get(); // otetaan tapahtuma talteen käsittelyä varten
			
			if (ticketservice.getTicketsByEvent(id).size() == 0) { // tarkistetaan onko tapahtumalla lippuja	
				this.eventrepo.delete(event); // jos ei, poistetaan tapahtuma
				return new ResponseEntity<String>("Event deleted", HttpStatus.NO_CONTENT); // palautetaan viesti ja 204
			}
			
			else { // jos tapahtumalla on lippuja
				return new ResponseEntity<String>("Event has associated tickets, deletion forbidden", HttpStatus.FORBIDDEN); // palautetaan viesti ja 403
			}
		}
		
		else { // eli jos haetulla id:llä ei löydy tapahtumaa
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // palautetaan 404
		}
	}	
	
		
	// hae kaikki tapahtumat parametreilla ja ilman. 
	@GetMapping("/events") // haetaan kaikki tapahtumat
	public ResponseEntity<?> getEvents(@RequestParam(required = false) String start, 
			@RequestParam(required = false) String end) {//parametrit pyydetään merkkijonoina jotta virhetilanteesta saadaan kiinni
		//jos parametrejä ei ole, haetaan kaikki normaalisti
		if (start == null && end == null) {
			return new ResponseEntity<List<Event>>((List<Event>) eventrepo.findAll(), HttpStatus.OK); 
		}//try-catch jotta pvm muutosvirheet kiinnni kauniisti
		try {//jos ei loppupvm, haetaan alkupvm eteenpäin
			if (end == null) {
				//konvertoi merkkijonon => pvm
				LocalDate date1 = LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE);
				return new ResponseEntity<List<Event>>(eventservice.getAllByStart(date1), HttpStatus.OK); 
			}//jos ei alkupvm, haetaan loppupvm asti. 
			if (start == null) {
				LocalDate date2 = LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE);
				return new ResponseEntity<List<Event>>(eventservice.getAllByEnd(date2), HttpStatus.OK); 
			}
			//jos molemmat, haetaan pvm väliltä
			return new ResponseEntity<List<Event>>(eventservice.getBtwDates(
					LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE),
					LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE)), HttpStatus.OK);
			
		} catch (Exception e) {//molempien date konvertointivirheet kiinni täällä
			return new ResponseEntity<>("Check dates", HttpStatus.BAD_REQUEST); 
		}
	}
	
	@GetMapping("/events/{id}") // haetaan yksittäinen tapahtuma id:n perusteella
	public @ResponseBody ResponseEntity<Optional<Event>> getEventById(@PathVariable("id") Long eventId){
		Optional<Event> event = eventrepo.findById(eventId);
		if (event.isEmpty()) {
			return new ResponseEntity<>(event, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(event, HttpStatus.OK);
		}
	}
	
	@GetMapping("/events/{id}/tickets") // palauttaa tapahtuman liput koko viittauksineen eventId:n perusteella 
	public List<Ticket> getTicketsByEvent(@PathVariable(value = "id") Long eventId) {
		return ticketservice.getTicketsByEvent(eventId);		
	}	

	// POST
	@PostMapping("/events") // lisää uuden tapahtuman
	public ResponseEntity<Event> addEvent(@Valid @RequestBody Event event, BindingResult bindingresult) throws Exception {
		
		if (bindingresult.hasErrors()) { // tarkistetaan tuleeko pyynnössä mukana tapahtuman nimi
			return new ResponseEntity<>(event, HttpStatus.BAD_REQUEST); // jos uudella tapahtumalla ei ole nimeä, sitä ei luoda vaan palautetaan 400
		}
		
		else { // jos tapahtumalla on nimi					

			// tarkista onko tapahtuma jo olemassa; jotta tapahtumaa pidetään samana, sekä sen nimi että aloitusaika pitää olla sama
			if (eventrepo.findByName(event.getName()) != null && eventrepo.findByName(event.getName()).getStartTime().equals(event.getStartTime())) { 

				return new ResponseEntity<>(eventrepo.findByName(event.getName()), HttpStatus.CONFLICT); // jos on, älä luo uutta samannimistä vaan palauta olemassaoleva ja 409
			}		
			else {
				return new ResponseEntity<>(eventrepo.save(event), HttpStatus.CREATED); // jos samannimistä tapahtumaa ei ole, luo uusi ja palauta se
			}
		}
	}
	
	// PUT
	@PutMapping(path = "/events/{id}") // muokkaa haluttua eventtiä id:n perusteella
	public ResponseEntity<?> updateEvent(@Valid @RequestBody Event newEvent, @PathVariable (value = "id") Long eventId, BindingResult bindingresult) {
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi response
		
		if (bindingresult.hasErrors()) { // tarkistetaan tuleeko pyynnössä mukana tapahtuman nimi
			response.put("message", "Event name is missing");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // jos ei ole, palautetaan 400
		}
		
		else {			
			if (eventId == null) {
				response.put("message", "Id is missing");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // id puuttuu
			}
			else {
				return eventrepo.findById(eventId) // annettu id löytyy, tiedot muokataan
						.map(event -> {
							event.setName(newEvent.getName());
							event.setAddress(newEvent.getAddress());
							event.setMaxCapacity(newEvent.getMaxCapacity());
							event.setStartTime(newEvent.getStartTime());
							event.setEndTime(newEvent.getEndTime());
							event.setEndOfPresale(newEvent.getEndOfPresale());
							event.setStatus(newEvent.getStatus());
							event.setDescription(newEvent.getDescription());
							eventrepo.save(event);
							return new ResponseEntity<>(event, HttpStatus.OK);
						})
						.orElseGet(() -> { // annettua id:tä ei löydy, luodaan uusi tapahtuma
							eventrepo.save(newEvent); 
							return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
						});
			}
		}	
	}
	
	// PATCH
	@PatchMapping (path="/events/{id}", consumes = "application/json-patch+json") //muokkaa osittain haluttua eventtiä id:n perusteella
	public ResponseEntity<?> partiallyUpdateEvent(@PathVariable long id, @RequestBody JsonPatch patchDocument) {
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi response
		
		Optional<Event> target = eventrepo.findById(id); // haetaan eventreposta mahdollinen event annetulla id:llä
		if (target.isEmpty()) { //tarkistetaan löytyykö eventiä, ellei löydy
			response.put("message", "Event not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // palautetaan viesti ja 404-koodi
		} else { // jos event löytyy annetulla id:llä
			Event patchedEvent = eventservice.patchEvent(patchDocument, id); // käytetään event patchEvent-metodin kautta
			return new ResponseEntity<>(patchedEvent, HttpStatus.OK);
		}
	}

}
