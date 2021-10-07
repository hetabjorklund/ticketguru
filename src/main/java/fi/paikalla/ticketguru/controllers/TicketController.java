package fi.paikalla.ticketguru.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Entities.Invoice;
import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.InvoiceRepository;
import fi.paikalla.ticketguru.Repositories.TicketRepository;
import fi.paikalla.ticketguru.Repositories.TicketTypeRepository;
import fi.paikalla.ticketguru.Services.TicketService;
import fi.paikalla.ticketguru.dto.TicketDto;

@RestController
public class TicketController {
	
	@Autowired
	private TicketRepository ticketRepo;
	@Autowired
	private TicketTypeRepository typeRepo;
	@Autowired
	private InvoiceRepository invoiceRepo;
	@Autowired
	private TicketService ticketservice;
	
	@GetMapping("/tickets") // kaikki liput, vähän kustomointia vois tehdä, koska tulee aika paljon tietoa. 
	public List<Ticket> getTickets() {
		return ticketservice.getAllTickets();
	}	
	
	@GetMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<Optional<Ticket>> getTicketById(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketRepo.findById(ticketId);
		
		if(ticket.isEmpty()) {
			return new ResponseEntity<>(ticket, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(ticket, HttpStatus.OK);
		}
	}
	
	@GetMapping("/tickets/{id}/used")
	public @ResponseBody ResponseEntity<Map<String, Boolean>> getTicketUsed(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketRepo.findById(ticketId);
		Map<String, Boolean> responseMap = new HashMap<>();
		
		if(ticket.isEmpty()) {
			responseMap.put("ticketIdFound", false);
			return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
		}
		
		responseMap.put("used", ticket.get().isUsed());
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}
	
	@PostMapping("/tickets")
	public @ResponseBody ResponseEntity<String> createTicket(@Valid @RequestBody TicketDto ticket){
		Optional<TicketType> ticketType = typeRepo.findById(ticket.getTicketType());
		Optional<Invoice> invoice = invoiceRepo.findById(ticket.getInvoice());
		
		if(!ticketType.isEmpty() && !invoice.isEmpty()) {
			Ticket newTicket = new Ticket(
				ticketType.get(),
				ticket.getPrice(),
				invoice.get()
			);
			
			//haetaan lipusta eventId ja tarkastetaan, onko kyseiseen tapahtumaan lippuja myytävänä
			long eventId = ticketservice.getEventIdFromTicket(newTicket);
			boolean hasAvailableTickets;
			
			try {
				hasAvailableTickets = ticketservice.hasAvailableTickets(eventId);
			} catch(Exception e) {
				hasAvailableTickets = false;
			}
			
			//Mikäli lippuja on jäljellä, luodaan uusi lippu. Mikäli lippuja ei ole jäljellä, palautetaan BAD_REQUEST
			if(hasAvailableTickets) {
				ticketRepo.save(newTicket);
				return new ResponseEntity<>("Ticket succesfully created", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("The event is sold out", HttpStatus.BAD_REQUEST);
			}
		}
		
		return new ResponseEntity<>("No ticket type or invoice found with the given id's", HttpStatus.BAD_REQUEST);
	}
	
	@PatchMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<Optional<Ticket>> markTicketAsUsed(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketRepo.findById(ticketId);
		
		if(!ticket.isEmpty()) {
			Ticket usedTicket = ticket.get();
			if(usedTicket.isUsed()) {
				return new ResponseEntity<>(ticket, HttpStatus.BAD_REQUEST);
			}
			usedTicket.setUsed(true);
			ticketRepo.save(usedTicket);
			return new ResponseEntity<>(ticket, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(ticket, HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<TicketDto> modifyTicket(@Valid @RequestBody TicketDto ticketDto, @PathVariable("id") Long ticketId){
		Optional<TicketType> ticketType = typeRepo.findById(ticketDto.getTicketType());
		Optional<Invoice> invoice = invoiceRepo.findById(ticketDto.getInvoice());
		Optional<Ticket> ticket = ticketRepo.findById(ticketId);
		
		if(ticket.isEmpty()) {
			return new ResponseEntity<>(ticketDto, HttpStatus.NOT_FOUND);
		}
		
		if(!ticketType.isEmpty() && !invoice.isEmpty()) {
			Ticket newTicket = ticket.get();
			newTicket.setPrice(ticketDto.getPrice());
			newTicket.setUsed(ticketDto.isUsed());
			newTicket.setTicketType(ticketType.get());
			newTicket.setInvoice(invoice.get());
			ticketRepo.save(newTicket);
			
			return new ResponseEntity<>(ticketDto, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(ticketDto, HttpStatus.BAD_REQUEST);		
	}
	
	@DeleteMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<Optional<Ticket>> deleteTicket(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketRepo.findById(ticketId);
		
		if(ticket.isEmpty()) {
			return new ResponseEntity<>(ticket, HttpStatus.NOT_FOUND);
		}
		
		ticketRepo.delete(ticket.get());
		return new ResponseEntity<>(ticket, HttpStatus.NO_CONTENT);
	}
}
