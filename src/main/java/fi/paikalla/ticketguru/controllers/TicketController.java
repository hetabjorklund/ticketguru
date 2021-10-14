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
	
	@GetMapping("/tickets") // kaikki liput, vähän kustomointia vois tehdä, koska tulee aika paljon tietoa. 
	public List<Ticket> getTickets() {
		return ticketservice.getAllTickets();
	}	
	
	@GetMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<Optional<Ticket>> getTicketById(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketrepo.findById(ticketId);
		
		if(ticket.isEmpty()) {
			return new ResponseEntity<>(ticket, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(ticket, HttpStatus.OK);
		}
	}
	
	@GetMapping("/tickets/{id}/used")
	public @ResponseBody ResponseEntity<Map<String, Boolean>> getTicketUsed(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketrepo.findById(ticketId);
		Map<String, Boolean> responseMap = new HashMap<>();
		
		if(ticket.isEmpty()) {
			responseMap.put("ticketIdFound", false);
			return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
		}
		
		responseMap.put("used", ticket.get().isUsed());
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}
	
	@PostMapping("/tickets")
	public @ResponseBody ResponseEntity<Map<String, String>> createTicket(@Valid @RequestBody TicketDto ticket, BindingResult bindingResult){
		Map<String, String> response = new HashMap<>();
		if(bindingResult.hasErrors()) { // Mikäli validoinnissa on virheitä
			List<ObjectError> errors = bindingResult.getAllErrors(); // Haetaan kaikki virheet listaan
			StringBuilder strBuilder = new StringBuilder(errors.size()); // Alustetaan vastausviestiä
			
			for(ObjectError error: errors) { //Käydään kaikki virheet läpi ja lisätään niiden default message palautusviestiin
				strBuilder.append(error.getDefaultMessage() + "\n");
			}
			
			String errorMessage = strBuilder.toString().trim(); // Muutetaan palautusviesti Stringiksi ja lähetetään se clientille
			response.put("status", "400");
			response.put("message", errorMessage);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		Optional<TicketType> ticketType = typerepo.findById(ticket.getTicketType());
		Optional<Invoice> invoice = invoicerepo.findById(ticket.getInvoice());
		
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
				hasAvailableTickets = eventservice.hasAvailableTickets(eventId);
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
			} else {
				response.put("status", "400");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		}
		
		response.put("status", "400");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
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
	
	@PutMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<TicketDto> modifyTicket(@Valid @RequestBody TicketDto ticketDto, @PathVariable("id") Long ticketId, BindingResult bindingResult){
		
		
		if(bindingResult.hasErrors()) {
			
		}
		
		Optional<TicketType> ticketType = typerepo.findById(ticketDto.getTicketType());
		Optional<Invoice> invoice = invoicerepo.findById(ticketDto.getInvoice());
		Optional<Ticket> ticket = ticketrepo.findById(ticketId);
		
		if(ticket.isEmpty()) {
			return new ResponseEntity<>(ticketDto, HttpStatus.NOT_FOUND);
		}
		
		if(!ticketType.isEmpty() && !invoice.isEmpty()) {
			Ticket newTicket = ticket.get();
			newTicket.setPrice(ticketDto.getPrice());
			newTicket.setUsed(ticketDto.isUsed());
			newTicket.setTicketType(ticketType.get());
			newTicket.setInvoice(invoice.get());
			ticketrepo.save(newTicket);
			
			return new ResponseEntity<>(ticketDto, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(ticketDto, HttpStatus.BAD_REQUEST);		
	}
	
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
