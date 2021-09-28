package fi.paikalla.ticketguru.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Entities.Invoice;
import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.InvoiceRepository;
import fi.paikalla.ticketguru.Repositories.TicketRepository;
import fi.paikalla.ticketguru.Repositories.TicketTypeRepository;
import fi.paikalla.ticketguru.dto.TicketDto;

@RestController
public class TicketController {
	@Autowired
	private TicketRepository ticketRepo;
	
	@Autowired
	private TicketTypeRepository typeRepo;
	
	@Autowired
	private InvoiceRepository invoiceRepo;
	
	@GetMapping("tickets/{id}")
	public @ResponseBody ResponseEntity<Optional<Ticket>> getTicketById(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketRepo.findById(ticketId);
		
		if(ticket.isEmpty()) {
			return new ResponseEntity<>(ticket, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(ticket, HttpStatus.OK);
		}
	}
	
	@GetMapping("tickets/{id}/used")
	public @ResponseBody ResponseEntity<Boolean> getTicketUsed(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketRepo.findById(ticketId);
		
		if(ticket.isEmpty()) {
			throw new ResourceNotFoundException("Ticket with the given id was not found");
		}
		
		return new ResponseEntity<>(ticket.get().getUsed(), HttpStatus.OK);
	}
	
	@GetMapping("/tickets/event/{eventid}")
	public @ResponseBody List<Ticket> getTicketsByEvent(@PathVariable("eventid") Long eventId){
		List<Ticket> tickets = new ArrayList<>();
		List<TicketType> ticketTypes = typeRepo.findByEventId(eventId);
		
		for(TicketType type: ticketTypes) {
			tickets.addAll(type.getTickets());
		}
		
		return tickets;
	}
	
	@PostMapping("tickets/addticket")
	public @ResponseBody ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticket){
		Optional<TicketType> ticketType = typeRepo.findById(ticket.getTicketType());
		Optional<Invoice> invoice = invoiceRepo.findById(ticket.getInvoice());
		
		if(!ticketType.isEmpty() && !invoice.isEmpty()) {
			Ticket newTicket = new Ticket(
				ticketType.get(),
				ticket.getPrice(),
				invoice.get()
			);
			
			ticketRepo.save(newTicket);
			
			return new ResponseEntity<>(ticket, HttpStatus.CREATED);
		}
		
		
		return new ResponseEntity<>(ticket, HttpStatus.BAD_REQUEST);
	}
}
