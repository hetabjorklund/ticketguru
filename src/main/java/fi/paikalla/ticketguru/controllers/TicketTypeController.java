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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Components.ErrorResponseGenerator;
import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.EventRepository;
import fi.paikalla.ticketguru.Repositories.TicketRepository;
import fi.paikalla.ticketguru.Repositories.TicketTypeRepository;
import fi.paikalla.ticketguru.dto.TicketTypeDto;

@RestController
public class TicketTypeController {
	
	@Autowired
	private TicketTypeRepository typerepo; 
	@Autowired
	private EventRepository eventrepo; 
	@Autowired
	private TicketRepository tickrepo; 
	@Autowired
	private ErrorResponseGenerator responsegenerator;
	
	// GET
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/events/{id}/types") //lipputyypit per eventId
	public ResponseEntity<List<TicketType>> getByEvent(@PathVariable(value = "id") Long eventId) {
		Optional<Event> ev = eventrepo.findById(eventId); 
		List<TicketType> list = typerepo.findByEventId(eventId);
		if (ev.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
		}
		if (list.isEmpty()) {
			return new ResponseEntity<>(list, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/types") //kaikki lipputyypit
	public ResponseEntity<List<TicketType>> getAllTypes() {
		List<TicketType> list = (List<TicketType>) typerepo.findAll();  
		if (list.isEmpty()) {
			return new ResponseEntity<>(list, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
	}
	
	// POST
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/types") //luo uusi tyyppi
	public ResponseEntity<?> makeNewType(@Valid @RequestBody TicketTypeDto type, BindingResult bindres) {
		Map<String, Object> response = new HashMap<>(); // alustetaan virhevastaus
		Map<String, String> response2 = new HashMap<>();
		if(bindres.hasErrors()) {
			response = responsegenerator.generateErrorResponseFromBindingResult(bindres); //käytetään samaa generaattoria suosiolla.
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			
		}
		Optional<Event> event = eventrepo.findById(type.getEvent()); //onko tapahtumaa olemassa?
		
		if (event.isPresent()) { //jos tapahtuma
			try {
				TicketType testi = new TicketType(event.get(), type.getType(), type.getPrice()); //luo testilippu
				List<TicketType> ticketList = typerepo.findByEventId(event.get().getId()); //hae tapahtuman liput
				for (TicketType typ:ticketList) {
					if (typ.getType().equalsIgnoreCase(testi.getType())) { //onko samanniminen lipputyyppi jo olemassa?
						response2.put("message", "This ticket type already exists"); //jos on, palauta viesti ja 400
						return new ResponseEntity<>(response2, HttpStatus.BAD_REQUEST);
					}
				}
				typerepo.save(testi); //muuten tallenna postattu tyyppi
				return new ResponseEntity<>(testi, HttpStatus.CREATED);
				
					 
			} catch (Exception e){
				return new ResponseEntity<>(type, HttpStatus.BAD_REQUEST);//palauta pyyntöobjekti (type:null) ja bad request	
			}
		}
		response2.put("Message", "Invalid event");
		return new ResponseEntity<>(response2, HttpStatus.BAD_REQUEST); //ei tapahtumaa, palauta viesti invalid event. 
	}
	
	// PUT
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PutMapping("/types/{id}") //päivitä idn perusteella
	public ResponseEntity<?> updateType(@PathVariable(value = "id") Long typeId, 
			@RequestBody TicketTypeDto type) {
		Optional<TicketType> ticktype = typerepo.findById(typeId); //onko tyyppitunnistetta olemassa, jos ei, not found
		Optional<Event> ev = eventrepo.findById(type.getEvent()); //onko pyynnön tapahtumaa olemassa, jos ei, bad request
		if (ticktype.isEmpty()) {// tyyppitunniste ei olemassa. 
			return new ResponseEntity<>(type, HttpStatus.NOT_FOUND);
		}
		if(!ev.isEmpty()) {//jos viitattu event on olemassa. 
			TicketType setting = ticktype.get(); //hae päivitettävä tyyppi
			setting.setEvent(ev.get()); //aseta viitattu tapahtuma
			try {
				if (!type.getType().isEmpty()) { //jos tyyppikuvausta ei ole, heitä bad request. 
				setting.setType(type.getType()); //päivitä kuvaus
			}
			if (type.getPrice() != 0) { //jos hinta ei ole nolla (sitä ei ole annettu), päivitä annettuun. 
				setting.setPrice(type.getPrice()); 
			}
			typerepo.save(setting); //tallenna
			return new ResponseEntity<>(setting, HttpStatus.OK); //palauta varsinainen entitettii ja ok. 
			} catch (Exception e) {
				return new ResponseEntity<>(type, HttpStatus.BAD_REQUEST); //virheet kiinni
			}			
		}
		return new ResponseEntity<>(type, HttpStatus.BAD_REQUEST);	//muut virheet kiinni.
	}
	
	// DELETE
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/types/{id}")// poistetaan idn perusteella
	public ResponseEntity<?> deleteTypeById(@PathVariable(value = "id") Long typeid) {
		Optional<TicketType> type = typerepo.findById(typeid); //löytyykö tyyppi
		Map<String, String> response = new HashMap<String, String>(); 
		if (type.isPresent()) { //jos löytyy, onko lippuja?
			List<Ticket> tickets = tickrepo.findByTicketType(type.get());
			if (tickets.size() > 0) { //jos on lippuja, palauttaa kiellon. 
				response.put("message", "There are tickets associated with this ticket type");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); 
			} else { //ei lippuja, poistetaan tyyppi
				typerepo.delete(type.get());
				response.put("message", "Ticket type deleted");
				return new ResponseEntity<Map<String, String>>(response, HttpStatus.NO_CONTENT); //palauta no content. 
			}
		} else {//tyyppiä ei ole olemassa, palauta not found. 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
		}
	}
	
}
