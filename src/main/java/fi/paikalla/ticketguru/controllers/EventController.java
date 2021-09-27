package fi.paikalla.ticketguru.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
	private TicketRepository tickrepo; 
	
	@Autowired 
	private TicketTypeRepository typerepo; 
	
	@Autowired
	private InvoiceRepository invrepo; 
	
	@DeleteMapping("/events/{id}") //poista event perustuen ID:hen
	public Map<String, Boolean> deleteEvent (@PathVariable(value = "id") Long eventId ) 
			throws ResourceNotFoundException {
		 Event ev = eventrepo.findById(eventId)
				 .orElseThrow(() -> new ResourceNotFoundException("Event not found for this id :: " + eventId));
		 eventrepo.delete(ev);
		 Map<String, Boolean> response = new HashMap<>();
		 response.put("deleted", Boolean.TRUE); //palauttaa nyt {deleted: true/false} vastauksen. Tästä voi olla montaa mieltä. 
		 return response;
	}
	
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
		return (List<Ticket>) tickrepo.findAll(); 
	}
	
	@GetMapping("/types/{id}") //lipputyypit per eventId
	public List<TicketType> getByEvent(@PathVariable(value = "id") Long eventId) {
		return (List<TicketType>) typerepo.findByEventId(eventId); 
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

	@PostMapping("/events") // lisää uuden tapahtuman
	public ResponseEntity<Event> lisaaTapahtuma(@RequestBody Event event) {
		
		if (event.getName() == null) { // tarkistetaan tuleeko pyynnössä mukana tapahtuman nimi
			return new ResponseEntity<>(event, HttpStatus.BAD_REQUEST); // jos uudella tapahtumalla ei ole nimeä, sitä ei luoda
		}
		if (eventrepo.findByName(event.getName()) != null) { // tarkista onko samanniminen tapahtuma jo olemassa
			return new ResponseEntity<>(eventrepo.findByName(event.getName()), HttpStatus.CONFLICT); // jos on, palauta olemassaoleva äläkä luo uutta samannimistä
		}		
		else {
			return new ResponseEntity<>(eventrepo.save(event), HttpStatus.CREATED); // jos samannimistä tapahtumaa ei ole, luo uusi ja palauta se
		}	 			
	}
	

	@GetMapping("/invoices")
	public List<Invoice> haeLaskut() {
		return (List<Invoice>) invrepo.findAll(); 
	}
	
	@GetMapping("/invoices/{id}") 
	public Optional<Invoice> getInvoiceByid(@PathVariable(value = "id") Long invId) {
		return invrepo.findById(invId);	
	}
	
	@GetMapping("/invoices/{id}/tickets") 
	public List<Ticket> getTickByInvoiceid(@PathVariable(value = "id") Long invId) {
		return (List<Ticket>) tickrepo.findByInvoiceId(invId); 
	}
	
	
	
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
