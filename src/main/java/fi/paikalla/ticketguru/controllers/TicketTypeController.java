package fi.paikalla.ticketguru.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
	
	
	@GetMapping("/events/{id}/types") //lipputyypit per eventId
	public ResponseEntity<List<TicketType>> getByEvent(@PathVariable(value = "id") Long eventId) {
		Optional<Event> ev = eventrepo.findById(eventId); 
		List<TicketType> list = typerepo.findByEventId(eventId);
		if (ev.isEmpty()) {
			return new ResponseEntity<>(list, HttpStatus.NOT_FOUND); 
		}
		if (list.isEmpty()) {
			return new ResponseEntity<>(list, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
	}
	
	@GetMapping("/types") //kaikki lipputyypit
	public ResponseEntity<List<TicketType>> getAllTypes() {
		List<TicketType> list = (List<TicketType>) typerepo.findAll();  
		if (list.isEmpty()) {
			return new ResponseEntity<>(list, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/types") //luo uusi tyyppi
	public ResponseEntity<?> makeNewType(@RequestBody TicketTypeDto type) {
		Optional<Event> event = eventrepo.findById(type.getEvent()); //onko tapahtumaa olemassa?
		if (!event.isEmpty()) { //jos tapahtuma
			try {
				if(!type.getType().isEmpty()) { //jos lähetetyllä entiteetillä on tyyppi, luo uusi tyyppi.
					TicketType make = new TicketType(event.get(), 
					type.getType(), 
					type.getPrice()); 
				typerepo.save(make); //tallenna tyyppi
				return new ResponseEntity<TicketType>(make, HttpStatus.CREATED); //palauta varsinainen luotu objekti
				}
					 
			} catch (Exception e){
				return new ResponseEntity<>(type, HttpStatus.BAD_REQUEST);//palauta pyyntöobjekti (type:null) ja bad request	
			}
		}
		return new ResponseEntity<>(type, HttpStatus.BAD_REQUEST); //ei tapahtumaa, palauta objekti ja bad request. 
	}
	
	
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
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/types/{id}")// poistetaan idn perusteella
	public ResponseEntity<?> deleteTypeById(@PathVariable(value = "id") Long typeid) {
		Optional<TicketType> type = typerepo.findById(typeid); //löytyykö tyyppi
		Map<String, String> response = new HashMap<String, String>(); 
		if (type.isPresent()) { //jos löytyy, onko lippuja?
			List<Ticket> tickets = tickrepo.findByTicketType(type.get());
			if (tickets.size() > 0) { //jos on lippuja, palauttaa kiellon. 
				response.put("message", "There are tickets associated with this type");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); 
			} else { //ei lippuja, poistetaan tyyppi
				typerepo.delete(type.get());
				response.put("message", "Ticket Type deleted");
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT); //palauta no content. 
			}
		} else {//tyyppiä ei ole olemassa, palauta not found. 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
		}
	}
	
	
	
	

}
