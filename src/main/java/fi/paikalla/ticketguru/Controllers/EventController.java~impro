package fi.paikalla.ticketguru.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.EventRepository;
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
	

}
	
	
			  
	
	
	
		      

		   
