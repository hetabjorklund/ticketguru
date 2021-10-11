package fi.paikalla.ticketguru.Services;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Repositories.EventRepository;

@Service
public class EventService {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	EventRepository eventrepo;
	
	public Event patchEvent(JsonPatch patchDocument, Long id) {
		
		// Haetaan event eventreposta (EventControllerissa tarkistettu jo että event löytyy eikä tule virheilmoitusta)
        Event originalEvent = eventrepo.findById(id).get();
        
        // Muunnetaan Event-olio JsonStructure-olioksi
        JsonStructure eventToBePatched = objectMapper.convertValue(originalEvent, JsonStructure.class);

        // Lisätään pyynnössä saatu JsonPatch haluttuun eventtiin
        JsonValue patchedEvent = patchDocument.apply(eventToBePatched);

        // Muunnetaan JsonValue-olio takaisin Event-olioksi
        Event modifiedEvent = objectMapper.convertValue(patchedEvent, Event.class);

        // Tallennetaan muokattu event eventrepoon
        eventrepo.save(modifiedEvent); 
        
        // Palautetaan muokattu event
        return modifiedEvent;
	}
}
