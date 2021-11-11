package fi.paikalla.ticketguru.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.json.JsonPatch;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.EventStatus;
import fi.paikalla.ticketguru.Repositories.EventStatusRepository;
import fi.paikalla.ticketguru.Services.StatusService;

@RestController
public class StatusController {
	
	@Autowired
	private EventStatusRepository statusrepo;
	@Autowired
	private StatusService statusservice;
	
	// GET
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/status") // hakee kaikki statukset
	public ResponseEntity<List<EventStatus>> getAllStatuses() {
		List<EventStatus> list = (List<EventStatus>) statusrepo.findAll(); // muodostaa listan kaikista repositoryn statuksista
		return new ResponseEntity<>(list, HttpStatus.OK); // palauttaa haetun listan
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/status/{id}") // hakee statuksen id:n perusteella
	public ResponseEntity<?> getEventById(@PathVariable (value = "id") Long statusId) {
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi response
		
		Optional<EventStatus> status = statusrepo.findById(statusId); // hakee mahdollisen statuksen repositiosta id:n perusteella
		if (status.isEmpty()) { // tarkistetaan onko status tyhjä
			response.put("message", "Status not found");
			return new ResponseEntity<> (response, HttpStatus.NOT_FOUND); // mikäli status on tyhjä, palautetaan viesti ja 404-koodi
		} else {
			return new ResponseEntity<> (status, HttpStatus.OK); // mikäli status löytyy, palautetaan statuksen tiedot ja 200-koodi
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping ("/status/{id}/events") // hakee kaikki tapahtumat statuksen perusteella
	public ResponseEntity<?> getEventsByStatus(@PathVariable (value = "id") Long statusId) {
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi response
		
		Optional<EventStatus> optionalStatus = statusrepo.findById(statusId); // haetaan mahdollinen status statusreposta id:n perusteella
		if (optionalStatus.isEmpty()) { // tarkitsetaan onko status tyhjä
			response.put("message", "Status not found");
			return new ResponseEntity<> (response, HttpStatus.NOT_FOUND); // mikäli status on tyhjä, palautetaan viesti ja 404-koodi
		} else { // jos status löytyy
			EventStatus status = optionalStatus.get();// haetaan mahdollisen statuksen tiedot
			List<Event> elist = status.getEvents(); // haetaan statukseen liitetyt eventit
			if (elist.isEmpty()) { // tarkistetaan onko statukseen liitetty tapahtumia
				response.put("message", "No associated events");
				return new ResponseEntity<> (response, HttpStatus.OK); // mikäli tapahtumalista on tyhjä, palautetaan viesti ja 200-koodi
			} else { // mikäli statukseen on liitetty tapahtumia
				return new ResponseEntity<> (elist, HttpStatus.OK); // palautetaan tapahtumalista ja 200-koodi
			}
		}
	}
	
	// POST
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping ("/status")// luo uuden statuksen, VALIDOINTI HEITTÄÄ AUTOMAATTIRESPONSEN
	public ResponseEntity<?> createEventStatus (@Valid @RequestBody EventStatus status, BindingResult bindingresult) {
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi response
		
		if (bindingresult.hasErrors()) { // tarkistetaan tuleeko pyynnön mukana statukselle nimi
			response.put("message", "Status name is missing");
			return new ResponseEntity<> (response, HttpStatus.BAD_REQUEST); // jos ei statukselle ole annettu nimeä, sitä ei luoda ja palautetaan 400-koodi
		} else { // jos pyynnössä on statukselle nimi
			if (statusrepo.findByStatusName(status.getStatusName()) != null) { // tarkistetaan onko kyseinen status jo olemassa kannassa
				response.put("message", "Status already exists");
				return new ResponseEntity<> (response, HttpStatus.CONFLICT); //palauttaa olemassa olevan statuksen, jos sellainen löytyy sekä 409-koodin
			} else {
				return new ResponseEntity<> (statusrepo.save(status), HttpStatus.CREATED); // luodaan uusi status, palautetaan sen tiedot ja 201-koodi
			}
		}
	}
	
	// PATCH

	// vanha, ei ns. oikeaoppinen patch
	/*@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/status/{id}")// muokkaa statuksen nimeä, VALIDOINTI HEITTÄÄ AUTOMAATTIRESPONSEN
	public ResponseEntity<?> updateName(@PathVariable(value = "id") Long statusId, 
			@Valid @RequestBody EventStatus status, BindingResult bindingresult) {
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi response
		
		if (bindingresult.hasErrors()) { // tarkistetaan tuleeko pyynnön mukana statukselle nimi
			response.put("message", "Status name is missing");
			return new ResponseEntity<> (response, HttpStatus.BAD_REQUEST); // jos ei statukselle ole annettu nimeä, sitä ei luoda ja palautetaan 400-koodi
		} else { // jos pyynnön mukana tulee statukselle nimi
			Optional<EventStatus> estatus = statusrepo.findById(statusId); // haetaan mahdollinen status kannasta id:n perusteella
			if (estatus.isEmpty()) { // tarkistetaan löytyikö status kannasta
				response.put("message", "Status not found");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // jos status ei löydy, palautetaan viesti ja 404-koodi
			} else { 
				EventStatus newStatus = estatus.get(); // jos status löytyy kannasta, haetaan statuksen tiedot
				newStatus.setStatusName(status.getStatusName()); // status nimetään uusiksi pyynnön mukana toimitetulla nimellä
				statusrepo.save(newStatus); // tallennetaan status uudella nimellä kantaan
				return new ResponseEntity<>(newStatus, HttpStatus.OK); // palautetaan korjatut statuksen tiedot ja 200-koodi
			}
		}
	}*/
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping (path="/status/{id}", consumes = "application/json-patch+json") // muokkaa statusta osittain id:n perusteella
	public ResponseEntity<?> updateStatus(@PathVariable long id, @RequestBody JsonPatch patchDocument) throws HttpMessageNotReadableException {
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi response
		
		try {
			Optional<EventStatus> target = statusrepo.findById(id); // haetaan statusreposta mahdollinen status annetulla id:llä
			if (target.isEmpty()) { //tarkistetaan löytyykö statusta, ellei löydy
				response.put("message", "Status not found");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // palautetaan viesti ja 404-koodi
			} else { // jos status löytyy annetulla id:llä
				EventStatus patchedStatus = statusservice.patchStatus(patchDocument, id); // käytetään status patchStatus-metodin kautta
				return new ResponseEntity<>(patchedStatus, HttpStatus.OK);
			}
		} catch (HttpMessageNotReadableException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// DELETE
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/status/{id}") // poistaa statuksen id:n perusteella, mikäli siihen ei ole liitetty tapahtumia
	public ResponseEntity<?> deleteStatusById(@PathVariable(value = "id") Long statusId) {
		Map<String, String> response = new HashMap<String, String>(); // alustetaan uusi response
		
		Optional<EventStatus> status = statusrepo.findById(statusId); // haetaan mahdollinen status kannasta id:n perusteella
		if (status.isEmpty()) { // jos statusta ei löydy kannastsa
			response.put("message", "Status not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // palautetaan viesti sekä 404-koodi
		} else { // jos status löytyy kannasta
			if (status.get().getEvents().isEmpty()) { // tarkistetaan onko statukseen tapahtumalista tyhjä
				response.put("message", "Status deleted");
				statusrepo.deleteById(statusId); // poistetaan status kannasta
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT); // palautetaan viesti sekä 204-koodi
			} else { // jos statuksen tapahtumalistassa on tapahtumia
				response.put("message", "Status has associated events, deletion forbidden");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // palautetaan viesti sekä 403-koodi
			}
		}
	}
	
}
