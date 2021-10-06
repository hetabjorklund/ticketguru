package fi.paikalla.ticketguru.controllers;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import fi.paikalla.ticketguru.Entities.*;
import fi.paikalla.ticketguru.Repositories.*;

@RestController
public class EventController {
	
	@Autowired
	private EventRepository eventrepo; 
	
	@Autowired 
	private TicketTypeRepository typerepo; 
	
	// DELETE
	/*@DeleteMapping("/events/{id}") // poista yksittäinen tapahtuma id:n perusteella
	public ResponseEntity<Map<String, Boolean>> deleteEvent (@PathVariable(value = "id") Long eventId ) {
		 Optional<Event> ev = eventrepo.findById(eventId);
		 Map<String, Boolean> response = new HashMap<>();
		 if (ev.isEmpty()) {
			 response.put("deleted", Boolean.FALSE);
			 return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		 } else {
			 eventrepo.delete(ev.get());
			 response.put("deleted", Boolean.TRUE);
			 return new ResponseEntity<>(response,HttpStatus.OK);
		 }
			//palauttaa nyt {deleted: true/false} vastauksen. Tästä voi olla montaa mieltä	
	}*/
	
	@DeleteMapping("/events/{id}") // poista yksittäinen tapahtuma id:n perusteella. Endpointia /events joka poistaisi kaikki tapahtumat, ei tarvita
	public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
		
		if (this.eventrepo.findById(id).isPresent()) { // jos haetulla id:llä löytyy tapahtuma
			
			Event event = this.eventrepo.findById(id).get(); // otetaan tapahtuma talteen käsittelyä varten
			
			List<Ticket> ticketlist = new ArrayList<>(); 
			List<TicketType> tickettypes = typerepo.findByEventId(id); 
			for (TicketType x : tickettypes) {
				ticketlist.addAll(x.getTickets()); 
			}
			
			if (ticketlist.size() == 0) { // tarkistetaan onko tapahtumalla lippuja
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
		
	// GET
	@GetMapping("/events") // haetaan kaikki tapahtumat
	public List<Event> getEvents() {
		return (List<Event>) eventrepo.findAll(); 
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
		List<Ticket> lista = new ArrayList<Ticket>(); 
		List<TicketType> types = typerepo.findByEventId(eventId); 
		for (TicketType type : types) {
			lista.addAll(type.getTickets()); 
		}
		return lista; 		
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
	public ResponseEntity<Event> updateEvent(@Valid @RequestBody Event newEvent, @PathVariable (value = "id") Long eventId, BindingResult bindingresult) {
		
		if (bindingresult.hasErrors()) { // tarkistetaan tuleeko pyynnössä mukana tapahtuman nimi
			return new ResponseEntity<>(newEvent, HttpStatus.BAD_REQUEST); // jos ei ole, palautetaan 400
		}
		
		else {			
			if (eventId == null) {
				return new ResponseEntity<>(newEvent, HttpStatus.NOT_FOUND); // id puuttuu
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

}
