package fi.paikalla.ticketguru.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Components.ErrorResponseGenerator;
import fi.paikalla.ticketguru.Entities.Invoice;
import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.InvoiceRepository;
import fi.paikalla.ticketguru.Repositories.TicketRepository;
import fi.paikalla.ticketguru.Repositories.TicketTypeRepository;
import fi.paikalla.ticketguru.Services.EventService;
import fi.paikalla.ticketguru.Services.TicketService;
import fi.paikalla.ticketguru.dto.TicketDto;

@RestController
public class TicketController {
	
	@Autowired
	private TicketRepository ticketrepo;
	@Autowired
	private TicketTypeRepository typerepo;
	@Autowired
	private InvoiceRepository invoicerepo;
	@Autowired
	private TicketService ticketservice;
	@Autowired
	private EventService eventservice;
	@Autowired
	private ErrorResponseGenerator errResGenerator;
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/tickets") // kaikki liput, vähän kustomointia vois tehdä, koska tulee aika paljon tietoa. 
	public List<Ticket> getTickets() {
		return ticketservice.getAllTickets();
	}	
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<Optional<Ticket>> getTicketById(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketrepo.findById(ticketId);
		
		if(ticket.isEmpty()) {
			return new ResponseEntity<>(ticket, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(ticket, HttpStatus.OK);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/tickets/{id}/used")
	public @ResponseBody ResponseEntity<?> getTicketUsed(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketrepo.findById(ticketId);
		Map<String, String> responseMap = new HashMap<>();
		
		if(ticket.isEmpty()) {
			responseMap.put("message", "Ticket with the given Id was not found");
			return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND); // status 404
		}
		
		responseMap.put("used", ticket.get().isUsed() + "");
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PostMapping("/tickets")
	public @ResponseBody ResponseEntity<?> createTicket(@Valid @RequestBody TicketDto ticket, BindingResult bindingResult){
		Map<String, String> response = new HashMap<>();
		
		if(bindingResult.hasErrors()) { // Mikäli validoinnissa on virheitä
			response = errResGenerator.generateErrorResponseFromBindingResult(bindingResult); // components-kansiosta luokan ErrorResponseGenerator metodi, joka ottaa syötteenä BindingResult-olion ja luo siitä responsen
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // messagena bindingresultin virheet, statuksena 400
		}
		
		Optional<TicketType> ticketType = typerepo.findById(ticket.getTicketType());
		Optional<Invoice> invoice = invoicerepo.findById(ticket.getInvoice());
		
		if(ticketType.isEmpty() || invoice.isEmpty()) { // Mikäli TicketDto:ssa annettua ticketTypeä tai invoicea ei löytynyt tietokannasta
			response.put("status", "400");
			response.put("message", "Bad Request");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		Ticket newTicket = new Ticket( // luodaan uusi ticketti
				ticketType.get(),
				ticket.getPrice(),
				invoice.get()
			);
		
		long eventId = ticketservice.getEventIdFromTicket(newTicket); // TicketServicen metodi, joka ottaa syötteenä ticketin ja palauttaa eventin Id:n
		boolean hasAvailableTickets;
		
		try {
			hasAvailableTickets = eventservice.hasAvailableTickets(eventId); // EventServicen metodi, joka tarkastaa, onko tapahtumaan lippuja jäljellä
		} catch(Exception e) {
			hasAvailableTickets = false;
		}
		
		//Mikäli lippuja on jäljellä, luodaan uusi lippu. Mikäli lippuja ei ole jäljellä, palautetaan BAD_REQUEST
		if(hasAvailableTickets) {
			ticketrepo.save(newTicket);
			response = new HashMap<>();
			response.put("status", "201");
			response.put("message", "Ticket succesfully created");
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
		
		response.put("status", "400");
		response.put("message", "The event is already sold out");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PatchMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<Optional<Ticket>> markTicketAsUsed(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketrepo.findById(ticketId);
		
		if(!ticket.isEmpty()) {
			Ticket usedTicket = ticket.get();
			if(usedTicket.isUsed()) {
				return new ResponseEntity<>(ticket, HttpStatus.BAD_REQUEST);
			}
			usedTicket.setUsed(true);
			ticketrepo.save(usedTicket);
			return new ResponseEntity<>(ticket, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(ticket, HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PutMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<?> modifyTicket(@Valid @RequestBody TicketDto ticketDto, @PathVariable("id") Long ticketId, BindingResult bindingResult){
		Map<String, String> response = new HashMap<>();
		String message;
		
		if(bindingResult.hasErrors()) {
			response = errResGenerator.generateErrorResponseFromBindingResult(bindingResult);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // Messagena bindingresultin virheviestit ja statuksena 400
		}
		
		Optional<TicketType> ticketType = typerepo.findById(ticketDto.getTicketType());
		Optional<Invoice> invoice = invoicerepo.findById(ticketDto.getInvoice());
		Optional<Ticket> ticket = ticketrepo.findById(ticketId);
		
		if(ticket.isEmpty()) { // Mikäli annetulla Id:llä ei löydy lippua kannasta
			message = "Ticket with the given Id was not found";
			response.put("message", message);
			response.put("status", "404");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		if(ticketType.isEmpty() || invoice.isEmpty()) { // Mikäli Dto:ssa annettua ticketypen tai invoicen id:Tä ei löyty kannasta
			message = "Invalid Tickettype or invoice";
			response.put("message", message);
			response.put("status", "400");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		Ticket newTicket = ticket.get();
		newTicket.setPrice(ticketDto.getPrice());
		newTicket.setUsed(ticketDto.isUsed());
		newTicket.setTicketType(ticketType.get());
		newTicket.setInvoice(invoice.get());
		ticketrepo.save(newTicket);
		
		return new ResponseEntity<>(newTicket, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@DeleteMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<Optional<Ticket>> deleteTicket(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketrepo.findById(ticketId);
		
		if(ticket.isEmpty()) {
			return new ResponseEntity<>(ticket, HttpStatus.NOT_FOUND);
		}
		
		ticketrepo.delete(ticket.get());
		return new ResponseEntity<>(ticket, HttpStatus.NO_CONTENT);
	}
}
