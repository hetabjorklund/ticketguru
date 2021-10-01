package fi.paikalla.ticketguru.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.EventStatus;
import fi.paikalla.ticketguru.Repositories.EventRepository;
import fi.paikalla.ticketguru.Repositories.EventStatusRepository;

@RestController
public class StatusController {
	
	@Autowired
	private EventStatusRepository statusrepo;
	
	@Autowired
	private EventRepository eventrepo;
	
	@GetMapping("/eventstatuses") // hakee kaikki statukset
	public ResponseEntity<List<EventStatus>> getAllStatuses() {
		List<EventStatus> list = (List<EventStatus>) statusrepo.findAll();
		return new ResponseEntity<>(list, HttpStatus.OK);
	};
	
	//@GetMapping
	
	// GET hakee kaikki tapahtumat statuksen perusteella

	// POST luo uuden statuksen
	
	// PATCH muokkaa statuksen nimeä
	
	// DELETE merkkaa statuksen poistetuksi
	
	
}
