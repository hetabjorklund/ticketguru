package fi.paikalla.ticketguru.Services;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.paikalla.ticketguru.Entities.EventStatus;
import fi.paikalla.ticketguru.Repositories.EventStatusRepository;

@Service
public class StatusService {
	
	@Autowired
	private EventStatusRepository statusrepo;
	@Autowired
	private ObjectMapper objectmapper;
	
	// statuksen PATCH-toiminto	
	public EventStatus patchStatus(JsonPatch patchDocument, Long id) {
		
		// Haetaan status statusreposta (StatusControllerissa tarkistettu jo että status löytyy eikä tule virheilmoitusta)
        EventStatus originalStatus = statusrepo.findById(id).get();   
        
        // Muunnetaan Status-olio JsonStructure-olioksi
        JsonStructure statusToBePatched = objectmapper.convertValue(originalStatus, JsonStructure.class);
        
        // Lisätään pyynnössä saatu JsonPatch haluttuun statukseen
        JsonValue patchedStatus = patchDocument.apply(statusToBePatched);
        
        // Muunnetaan JsonValue-olio takaisin Status-olioksi
        EventStatus modifiedStatus = objectmapper.convertValue(patchedStatus, EventStatus.class);
        
        // Tallennetaan muokattu status statusrepoon        
        statusrepo.save(modifiedStatus);      
        
        // Palautetaan muokattu status
        return modifiedStatus;
	
	}

}
