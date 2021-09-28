package fi.paikalla.ticketguru.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.TicketRepository;
import fi.paikalla.ticketguru.Repositories.TicketTypeRepository;

@RestController
public class TicketController {
	@Autowired
	private TicketRepository ticketRepo;
	
	@Autowired
	private TicketTypeRepository typeRepo;
	
	@GetMapping("/tickets/event/{eventid}")
	public @ResponseBody List<Ticket> getTicketsByEvent(@PathVariable("eventid") Long eventId){
		List<Ticket> tickets = new ArrayList<>();
		List<TicketType> ticketTypes = typeRepo.findByEventId(eventId);
		
		for(TicketType type: ticketTypes) {
			tickets.addAll(type.getTickets());
		}
		
		return tickets;
	}
}
