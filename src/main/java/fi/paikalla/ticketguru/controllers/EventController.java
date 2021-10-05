package fi.paikalla.ticketguru.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.Invoice;
import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.EventRepository;
import fi.paikalla.ticketguru.Repositories.InvoiceRepository;
import fi.paikalla.ticketguru.Repositories.TicketRepository;
import fi.paikalla.ticketguru.Repositories.TicketTypeRepository;

@RestController
public class EventController {
	
	@Autowired
	private EventRepository eventrepo; 
	
	@Autowired
	private TicketRepository ticketrepo; 
	
	@Autowired 
	private TicketTypeRepository typerepo; 
	
	// DELETE
	@DeleteMapping("/events/{id}") //poista event perustuen ID:hen
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
	}
	
	// GET
	@GetMapping("/events") //kaikki tapahtumat
	public List<Event> getEvents() {
		return (List<Event>) eventrepo.findAll(); 
	}
	
	@GetMapping("/events/{id}")
	public @ResponseBody ResponseEntity<Optional<Event>> getEventById(@PathVariable("id") Long eventId){
		Optional<Event> event = eventrepo.findById(eventId);
		if(event.isEmpty()) {
			return new ResponseEntity<>(event, HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<>(event, HttpStatus.OK);
		}
	}
	
	@GetMapping("/tickets") //kaikki liput, vähän kustomointia vois tehdä, koska tulee aika paljon tietoa. 
	public List<Ticket> getTickets() {
		return (List<Ticket>) ticketrepo.findAll(); 
	}	
	
	@GetMapping("/events/{id}/tickets") //palauttaa eventId perusteella listan lippuja koko viittauksineen. 
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
					
			try {
				// tarkista onko tapahtuma jo olemassa; jotta tapahtumaa pidetään samana, sekä sen nimi että aloitusaika pitää olla sama
				if (eventrepo.findByName(event.getName()) != null && eventrepo.findByName(event.getName()).getStartTime().equals(event.getStartTime())) { 

					return new ResponseEntity<>(eventrepo.findByName(event.getName()), HttpStatus.CONFLICT); // jos on, älä luo uutta samannimistä vaan palauta olemassaoleva ja 409
				}		
				else {
					return new ResponseEntity<>(eventrepo.save(event), HttpStatus.CREATED); // jos samannimistä tapahtumaa ei ole, luo uusi ja palauta se
				}
			} catch (Exception e) { // KESKEN
				throw new HttpMessageNotReadableException("trallalaa");
//				(HttpStatus.METHOD_NOT_ALLOWED); // oikeasti bad request mutta tässä 405 jotta erottuu missä ollaan
		    }
		}
	}
	
	
	// PUT
	@PutMapping(path = "/events/{id}") // muokkaa haluttua eventtiä id:n perusteella
	public ResponseEntity<Event> updateEvent(@RequestBody Event newEvent, @PathVariable (value = "id") Long eventId) {
		if (eventId == null) {
			return new ResponseEntity<>(newEvent, HttpStatus.NOT_FOUND); //id puuttuu
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
