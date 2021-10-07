package fi.paikalla.ticketguru.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.EventRepository;
import fi.paikalla.ticketguru.Repositories.TicketTypeRepository;
import fi.paikalla.ticketguru.dto.TicketTypeDto;

@RestController
public class TicketTypeController {
	@Autowired
	private TicketTypeRepository typerepo; 
	@Autowired
	private EventRepository eventrepo; 
	
	
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
	
	@PostMapping("/types") //luo uusi tyyppi
	public ResponseEntity<TicketTypeDto> makeNewType(@RequestBody TicketTypeDto type) {
		Optional<Event> event = eventrepo.findById(type.getEvent()); 
		if (!event.isEmpty()) {
			TicketType make = new TicketType(event.get(), 
					type.getType(), 
					type.getPrice()); 
			typerepo.save(make); 
			return new ResponseEntity<>(type, HttpStatus.CREATED); 
		}
		return new ResponseEntity<>(type, HttpStatus.BAD_REQUEST); 
	}
	
	@PutMapping("/types/{id}") //päivitä idn perusteella
	public ResponseEntity<TicketTypeDto> updateType(@PathVariable(value = "id") Long typeId, 
			@RequestBody TicketTypeDto type) {
		Optional<TicketType> ticktype = typerepo.findById(typeId); 
		Optional<Event> ev = eventrepo.findById(type.getEvent()); 
		if (ticktype.isEmpty()) {
			return new ResponseEntity<>(type, HttpStatus.NOT_FOUND);
		}
		if(!ev.isEmpty()) {
			TicketType setting = ticktype.get();
			setting.setEvent(ev.get()); 
			setting.setType(type.getType()); 
			setting.setPrice(type.getPrice()); 
			typerepo.save(setting); 
			return new ResponseEntity<>(type, HttpStatus.OK);
		}
		return new ResponseEntity<>(type, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/types/{id}")
	public ResponseEntity<HashMap<String, Boolean>> deleteTypeById(@PathVariable(value = "id") Long typeid) {
		Optional<TicketType> type = typerepo.findById(typeid); 
		if (type.isEmpty()) {
			HashMap<String, Boolean> response = new HashMap<>(); 
			response.put("deleted", Boolean.FALSE); 
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); 
		} else {
			typerepo.delete(type.get());
			HashMap<String, Boolean> response = new HashMap<>(); 
			response.put("deleted", Boolean.TRUE); 
			return new ResponseEntity<>(response, HttpStatus.OK); 
		}
	}
	
	
	
	

}
