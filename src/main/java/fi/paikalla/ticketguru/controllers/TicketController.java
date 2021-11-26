package fi.paikalla.ticketguru.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.json.JsonPatch;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

@Validated
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
	
	// GET
	
	// hae kaikki liput
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/tickets")  
	public List<Ticket> getTickets() {
		return ticketservice.getAllTickets();
	}	
	
	// hae lippu id:n perusteella
	/*@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<Optional<Ticket>> getTicketById(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketrepo.findById(ticketId);
		
		if(ticket.isEmpty()) {
			return new ResponseEntity<>(ticket, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(ticket, HttpStatus.OK);
		}
	}*/
	
	// hae lippu koodin perusteella
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/tickets/{code}")
	public @ResponseBody ResponseEntity<Optional<Ticket>> getTicketByCode(@PathVariable("code") String ticketCode){
		Optional<Ticket> ticket = ticketrepo.findByCode(ticketCode);
		
		if(ticket.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(ticket, HttpStatus.OK);
		}
	}
	
	// tarkista, onko lippu käytetty id:n perusteella
	/*@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/tickets/{id}/used")
	public @ResponseBody ResponseEntity<?> getTicketUsed(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketrepo.findById(ticketId);
		Map<String, String> responseMap = new HashMap<>();
		
		if(ticket.isEmpty()) {
			responseMap.put("message", "Ticket with the given id was not found");
			return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND); // status 404
		}		
		responseMap.put("used", ticket.get().isUsed() + "");
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}*/
	
	// tarkista, onko lippu käytetty koodin perusteella
	/*@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/tickets/{code}/used")
	public @ResponseBody ResponseEntity<?> getTicketUsed(@PathVariable("code") String ticketCode){
		Optional<Ticket> ticket = ticketrepo.findByCode(ticketCode);
		
		Map<String, String> responseMap = new HashMap<>();
		
		if(ticket.isEmpty()) {
			responseMap.put("message", "Ticket with the given code was not found");
			return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND); // status 404
		}		
		responseMap.put("used", ticket.get().isUsed() + "");
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}*/
	
	// PATCH
	
	// merkitse lippu käytetyksi id:n perusteella
	/*@PreAuthorize("hasAnyRole('ADMIN','USER')")
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
	}*/
	
	// lipuntarkistus: tarkista koodin perusteella, onko lippu jo käytetty ja jos ei, merkitse lippu käytetyksi 
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PatchMapping (path="/tickets/{code}/used", consumes = "application/json-patch+json")
	public ResponseEntity<?> checkTicket(@PathVariable String code, @RequestBody JsonPatch patchDocument) {
			
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi vastaus		
		Optional<Ticket> target = ticketrepo.findByCode(code); // haetaan ticketreposta mahdollinen lippu annetulla koodilla
		
		if (target.isEmpty()) { // jos lippua ei löydy
			response.put("message", "Ticket not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // palautetaan viesti ja 404
		}
		else { // jos lippu löytyy annetulla koodilla
			
			Ticket currentTicket = target.get(); // otetaan lippuolio talteen käsittelyä varten
			
			if (currentTicket.isUsed()) { // jos lippu on jo käytetty
				response.put("message", "Ticket has already been used. Ticket is not valid");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // palautetaan viesti ja 400		
			}
			else { // jos lippu ei ole käytetty
				ticketservice.patchTicket(patchDocument, code); // viedään lippu ticketservicen patchTicket-metodille (joka merkitsee sen käytetyksi ja tallentaa ticketrepoon)
				response.put("message", "Ticket is valid");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}				
		}
	}
	
	// Lipun Patchin kierto
	/*@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PutMapping("/tickets/{code}/used")
	public ResponseEntity<?> checkTicket(@PathVariable String code){
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi vastaus
		Optional<Ticket> target = ticketrepo.findByCode(code); // haetaan ticketreposta mahdollinen lippu annetulla koodilla
		
		if(target.isEmpty()) {
			response.put("message", "Ticket not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // palautetaan viesti ja 404
		}
		
		Ticket ticket = target.get();
		
		if(ticket.isUsed()) { //jos lippu on jo käytetty
			response.put("message", "Ticket has already been used. Ticket is not valid");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // palautetaan viesti ja 400		
		}
		
		ticket.setUsed(true);
		ticketrepo.save(ticket);
		response.put("message", "Ticket is valid");
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}*/
	
	
	// POST
	
	// luo uusi lippu
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PostMapping("/tickets")
	public @ResponseBody ResponseEntity<?> createTicket(@RequestBody @NotEmpty(message = "List of tickets cannot be empty.") List<@Valid TicketDto> ticketDtos, BindingResult bindingResult){
		Map<String, Object> response = new HashMap<>();
		
		if(bindingResult.hasErrors()) { // Mikäli validoinnissa on virheitä
			response = errResGenerator.generateErrorResponseFromBindingResult(bindingResult); // components-kansiosta luokan ErrorResponseGenerator metodi, joka ottaa syötteenä BindingResult-olion ja luo siitä responsen
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // messagena bindingresultin virheet, statuksena 400
		}
		
		String message = "";
		HttpStatus status = HttpStatus.CREATED;
		
		List<Ticket> tickets = new ArrayList<>();
		
		boolean areTicketsValid = true;
		
		for(TicketDto ticket: ticketDtos) { // Käydään lippulista läpi lippu kerrallaan
			Optional<TicketType> ticketType = typerepo.findById(ticket.getTicketType());
			Optional<Invoice> invoice = invoicerepo.findById(ticket.getInvoice());
			
			if(ticketType.isEmpty() || invoice.isEmpty()) { // Mikäli TicketDto:ssa annettua ticketTypeä tai invoicea ei löytynyt tietokannasta
				status = HttpStatus.BAD_REQUEST;
				message = "Either ticket type or invoice was not found; tickettype_id=" + ticket.getTicketType() + ", invoice_id=" + ticket.getInvoice();
				areTicketsValid = false;
				break;
			}
			
			Ticket newTicket = new Ticket( // luodaan uusi ticketti
					ticketType.get(),
					ticket.getPrice(),
					invoice.get()
				);
			
			boolean isCodeAvailable = ticketservice.checkTicketCodeAvailability(newTicket.getCode()); // Tarkistetaan, onko lipun satunnaisesti luotu koodi käytettävissä
			
			// Mikäli koodi ei ole käytettävissä, luodaan uusi käyttämätön koodi
			if(!isCodeAvailable) {
				String code = ticketservice.generateNewTicketCode(newTicket);
				newTicket.setCode(code);
			}
			
			/*long eventId = ticketservice.getEventIdFromTicket(newTicket); // TicketServicen metodi, joka ottaa syötteenä ticketin ja palauttaa eventin Id:n
			boolean hasAvailableTickets;
			
			try {
				hasAvailableTickets = eventservice.hasAvailableTickets(eventId); // EventServicen metodi, joka tarkastaa, onko tapahtumaan lippuja jäljellä
			} catch(Exception e) {
				hasAvailableTickets = false;
			}*/
			
			//Mikäli lippuja on jäljellä, luodaan uusi lippu. Mikäli lippuja ei ole jäljellä, palautetaan BAD_REQUEST
			/*if(!hasAvailableTickets) {
				status = HttpStatus.BAD_REQUEST;
				message = "The event is already sold out; event_id=" + newTicket.getTicketType().getEvent().getId() + ", event_name='" + newTicket.getTicketType().getEvent().getName() + "'";
				break;
			}*/
			
			tickets.add(newTicket);
			
			/*ticketrepo.save(newTicket);
			status = HttpStatus.CREATED;
			message = "Tickets succesfully created";*/
		}
		
		if(areTicketsValid) {
			boolean hasTicketsAvailable = eventservice.checkTicketAvailability(tickets); // Käydään lippulista läpi ja katsotaan, onko eventeissä tarpeeksi lippuja jäljellä
		
			if(!hasTicketsAvailable) { // Mikäli lippuja ei ole jäljellä, lippujen luontia ei suoriteta
				message = "Some of the events do not have enough tickets available";
				status = HttpStatus.BAD_REQUEST;
				response.put("message", message);
			}else {
				message = "Tickets created succesfully";
				
				for(Ticket ticket: tickets) { // Mikäli kaikki ok, liput luodaan
					ticketrepo.save(ticket);
				}
				
				response.put("tickets", tickets);
			}
		}
		
		
		response.put("message", message);
		return new ResponseEntity<>(response, status);
	}
	
	// PUT
	
	// muokkaa olemassaolevaa lippua
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PutMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<?> modifyTicket(@Valid @RequestBody TicketDto ticketDto, BindingResult bindingResult, @PathVariable("id") Long ticketId){
		Map<String, Object> response = new HashMap<>();
		String message;
		
		if(bindingResult.hasErrors()) { // Mikäli validoinnissa on virheitä
			response = errResGenerator.generateErrorResponseFromBindingResult(bindingResult); // components-kansiosta luokan ErrorResponseGenerator metodi, joka ottaa syötteenä BindingResult-olion ja luo siitä responsen
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // messagena bindingresultin virheet, statuksena 400
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
		
		Long eventId = ticketservice.getEventIdFromTicket(newTicket);
		
		boolean hasAvailableTickets;
		
		try {
			hasAvailableTickets = eventservice.hasAvailableTickets(eventId);
		}catch(Exception e) {
			hasAvailableTickets = false;
		}
		
		if(!hasAvailableTickets) {
			message = "Event is already sold out";
			response.put("message", message);
			response.put("status", "400");
			
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		ticketrepo.save(newTicket);
		
		message = "Ticket modified succesfully";
		response.put("message", message);
		response.put("status", "200");
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	// DELETE
	
	// poista yksi lippu id:n perusteella
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@DeleteMapping("/tickets/{id}")
	public @ResponseBody ResponseEntity<?> deleteTicket(@PathVariable("id") Long ticketId){
		Optional<Ticket> ticket = ticketrepo.findById(ticketId);
		
		if(ticket.isEmpty()) {
			Map<String, String> response = new HashMap<>();
			String message = "Ticket with the given Id was not found";
			String status = "404";
			response.put("message", message);
			response.put("status", status);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		ticketrepo.delete(ticket.get());
		return new ResponseEntity<>(ticket, HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handle(ConstraintViolationException constraintViolationException) {
	   Map<String, String> response = errResGenerator.handleConstraintViolationException(constraintViolationException);
	   return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	 }
}
