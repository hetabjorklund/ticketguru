package fi.paikalla.ticketguru.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.json.JsonPatch;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Components.ErrorResponseGenerator;
import fi.paikalla.ticketguru.Entities.*;
import fi.paikalla.ticketguru.Repositories.*;
import fi.paikalla.ticketguru.Services.EventService;
import fi.paikalla.ticketguru.Services.TicketService;

@RestController
public class EventController {
	
	@Autowired
	private EventRepository eventrepo; 
	@Autowired
	private TicketService ticketservice;
	@Autowired
	private EventService eventservice;
	@Autowired
	private ErrorResponseGenerator responsegenerator;
	@Autowired
	private TicketTypeRepository tickettyperepo;
	@Autowired
	private InvoiceRepository invoicerepo;
	@Autowired
	private TicketRepository ticketrepo;
	@Autowired
	private TGUserRepository userrepo;
	
	// DELETE
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/events/{id}") // poista yksittäinen tapahtuma id:n perusteella. Endpointia /events joka poistaisi kaikki tapahtumat, ei tarvita
	public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
		
		Map<String, String> response = new HashMap<>(); // alustetaan uusi vastaus
		
		if (this.eventrepo.findById(id).isPresent()) { // jos haetulla id:llä löytyy tapahtuma
			
			Event event = this.eventrepo.findById(id).get(); // otetaan tapahtuma talteen käsittelyä varten
			
			if (ticketservice.getTicketsByEvent(id).size() == 0) { // tarkistetaan onko tapahtumalla lippuja	
				this.eventrepo.delete(event); // jos ei, poistetaan tapahtuma
				response.put("message", "Event deleted");
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT); // palautetaan viesti ja 204. HUOM! Jostain syystä vastaukseen ei tule mukaan responsea, vain tyhjä body ja 204
			}
			
			else { // jos tapahtumalla on lippuja
				response.put("message", "Event has associated tickets, deletion forbidden");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // palautetaan viesti ja 403
			}
		}		
		else { // eli jos haetulla id:llä ei löydy tapahtumaa
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // palautetaan 404
		}
	}	
	
	// GET
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/events") //hae kaikki tapahtumat parametreilla ja ilman 
	public ResponseEntity<?> getEvents(@RequestParam(required = false) String start, @RequestParam(required = false) String end) {
		//parametrit pyydetään merkkijonoina jotta virhetilanteesta saadaan kiinni
		//jos parametrejä ei ole, haetaan kaikki normaalisti
		if (start == null && end == null) {
			return new ResponseEntity<List<Event>>((List<Event>) eventrepo.findAll(), HttpStatus.OK); 
		}//try-catch jotta pvm muutosvirheet kiinnni kauniisti
		try {//jos ei loppupvm, haetaan alkupvm eteenpäin
			if (end == null) {
				//konvertoi merkkijonon => pvm
				LocalDate date1 = LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE);
				return new ResponseEntity<List<Event>>(eventservice.getAllByStart(date1), HttpStatus.OK); 
			}//jos ei alkupvm, haetaan loppupvm asti 
			if (start == null) {
				LocalDate date2 = LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE);
				return new ResponseEntity<List<Event>>(eventservice.getAllByEnd(date2), HttpStatus.OK); 
			}
			//jos molemmat, haetaan pvm väliltä
			return new ResponseEntity<List<Event>>(eventservice.getBtwDates(
					LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE),
					LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE)), HttpStatus.OK);
			
		} catch (Exception e) {//molempien date konvertointivirheet kiinni täällä
			return new ResponseEntity<>("Check dates", HttpStatus.BAD_REQUEST); 
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/events/{id}") // haetaan yksittäinen tapahtuma id:n perusteella
	public @ResponseBody ResponseEntity<Optional<Event>> getEventById(@PathVariable("id") Long eventId){
		Optional<Event> event = eventrepo.findById(eventId);
		if (event.isEmpty()) {
			return new ResponseEntity<>(event, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(event, HttpStatus.OK);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/events/{id}/tickets") // palauttaa tapahtuman liput koko viittauksineen eventId:n perusteella 
	public List<Ticket> getTicketsByEvent(@PathVariable(value = "id") Long eventId) {
		return ticketservice.getTicketsByEvent(eventId);		
	}	

	// POST
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/events") // lisää uuden tapahtuman
	public ResponseEntity<?> addEvent(@Valid @RequestBody Event event, BindingResult bindingresult) {
		
		Map<String, Object> response = new HashMap<>(); // alustetaan uusi vastaus
				
		if (bindingresult.hasErrors()) { // tarkistetaan onko pyynnössä validointivirheitä
			response = responsegenerator.generateErrorResponseFromBindingResult(bindingresult);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // jos on, ei luoda uutta tapahtumaa vaan palautetaan viesti ja 400
		}		
		else { // jos validointivirheitä ei ole				
			// tarkista onko tapahtuma jo olemassa; jotta tapahtumaa pidetään samana, sekä sen nimi että aloitusaika pitää olla sama
			if (eventrepo.findByName(event.getName()) != null && eventrepo.findByName(event.getName()).getStartTime().equals(event.getStartTime())) {								
				response.put("message", "Event already exists");				
				return new ResponseEntity<>(response, HttpStatus.CONFLICT); // jos on, älä luo uutta samannimistä vaan palauta viesti ja 409
			}		
			else {
				return new ResponseEntity<>(eventrepo.save(event), HttpStatus.CREATED); // jos samannimistä tapahtumaa ei ole, luo uusi ja palauta se
			}
		}
	}
	
	// luo tapahtumaan oviliput ennakkomyynnin loputtua
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PostMapping("/events/{id}/tickets")
	public ResponseEntity<?> createDoorTickets(@PathVariable Long id, @RequestParam double price) throws Exception {
		
		Map<String, String> response = new HashMap<>(); // alustetaan uusi vastaus
		
		try {					
			// hae tapahtuma
			Optional<Event> target = eventrepo.findById(id);
			if (target.isEmpty()) {
				response.put("message", "Event not found");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // jos tapahtumaa ei ole, palautetaan 404			
			}
			else {
				
				Event event = target.get();
				LocalDateTime today = LocalDateTime.now();
				
				// testaamista varten today, joka on tulevaisuudessa
				//LocalDateTime today = LocalDateTime.parse("2030-12-01T00:00:00");
				
				if (event.getEndOfPresale().isBefore(today)) { // tarkista onko ennakkomyynti jo loppunut
					
					TicketType doorticket = new TicketType(event, "ovilippu", price); // luo uusi tickettype "ovilippu"
					tickettyperepo.save(doorticket);					
					
					int ticketsLeft = eventservice.getAvailableCapacityOfEvent(id); // laske vapaiden lippujen määrä
					
					if (ticketsLeft > 0) { // jos lippuja on jäljellä				
						
						// luodaan dummy-lasku johon oviliput voi liittää
						Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
											
						if (principal instanceof UserDetails) {
							UserDetails userDetails = (UserDetails)principal;
							String username = userDetails.getUsername();
							TGUser currUser = userrepo.findByUserName(username);
							Invoice dummyInvoice = new Invoice(currUser); 
							invoicerepo.save(dummyInvoice);
							
							// luo jäljelläoleva määrä lippuja tyyppiä "ovilippu"
							for (int i = 0; i < ticketsLeft; i++) {
								Ticket ticket = new Ticket(doorticket, dummyInvoice); 
								ticketrepo.save(ticket);
								dummyInvoice.getTickets().add(ticket); // liitetään oviliput dummy-invoicen lippulistaan
							}							
							invoicerepo.save(dummyInvoice);							
						}							
						
						String content = ticketsLeft + " door tickets created";
						response.put("message", content);
						return new ResponseEntity<>(response, HttpStatus.CREATED); // palautetaan vastaus ja 201						
					}
					else { // jos lippuja ei ole jäljellä
						response.put("message", "The event is already sold out. There are no tickets left");
						return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // palautetaan viesti ja 400					
					}					
				} else { // jos ennakkomyynti ei ole vielä loppunut
					response.put("message", "The presale has not yet ended");
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // palautetaan viesti ja 400					
				}							
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
	}
		
	// PUT
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(path = "/events/{id}") // muokkaa haluttua eventtiä id:n perusteella
	public ResponseEntity<?> updateEvent(@Valid @RequestBody Event newEvent, @PathVariable (value = "id") Long eventId, BindingResult bindingresult) {
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi response
		
		if (bindingresult.hasErrors()) { // tarkistetaan tuleeko pyynnössä mukana tapahtuman nimi
			response.put("message", "Event name is missing");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // jos ei ole, palautetaan 400
		}
		
		else {			
			if (eventId == null) {
				response.put("message", "Id is missing");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // id puuttuu
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
	
	// PATCH
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping (path="/events/{id}", consumes = "application/json-patch+json") //muokkaa osittain haluttua eventtiä id:n perusteella
	public ResponseEntity<?> partiallyUpdateEvent(@PathVariable long id, @RequestBody JsonPatch patchDocument) {
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi response
		
		Optional<Event> target = eventrepo.findById(id); // haetaan eventreposta mahdollinen event annetulla id:llä
		if (target.isEmpty()) { //tarkistetaan löytyykö eventiä, ellei löydy
			response.put("message", "Event not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // palautetaan viesti ja 404-koodi
		} else { // jos event löytyy annetulla id:llä
			Event patchedEvent = eventservice.patchEvent(patchDocument, id); // käytetään event patchEvent-metodin kautta
			return new ResponseEntity<>(patchedEvent, HttpStatus.OK);
		}
	}

}
