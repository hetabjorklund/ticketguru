package fi.paikalla.ticketguru.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.EventStatusRepository;
import fi.paikalla.ticketguru.Repositories.TicketRepository;
import fi.paikalla.ticketguru.Repositories.TicketTypeRepository;
import net.bytebuddy.utility.RandomString;

@Service
public class TicketService {
	
	@Autowired
	private TicketRepository ticketrepo;
	@Autowired
	private TicketTypeRepository typerepo;
	@Autowired
	private EventStatusRepository statusrepo;
	@Autowired
	private ObjectMapper objectmapper;
	
	// lipun PATCH-toiminto
	public void patchTicket(JsonPatch patchDocument, String code) {
		
		// Haetaan lippu ticketreposta (TicketControllerissa tarkistettu jo että lippu löytyy eikä tule virheilmoitusta)
        Ticket originalTicket = ticketrepo.findByCode(code).get();        
	    
        // Muunnetaan Ticket-olio JsonStructure-olioksi
	    JsonStructure ticketToBePatched = objectmapper.convertValue(originalTicket, JsonStructure.class);
	    
	    // Lisätään pyynnössä saatu JsonPatch haluttuun lippuun
	    JsonValue patchedTicket = patchDocument.apply(ticketToBePatched);
	    
	    // Muunnetaan JsonValue-olio takaisin Ticket-olioksi
	    Ticket modifiedTicket = objectmapper.convertValue(patchedTicket, Ticket.class);

	    // Tallennetaan muokattu lippu ticketrepoon
	    ticketrepo.save(modifiedTicket);         
	}		
		
	// hae kaikki liput
	public List<Ticket> getAllTickets() {
		return (List<Ticket>) ticketrepo.findAll();
	}
	
	// hae tietyn tapahtuman kaikki liput
	public List<Ticket> getTicketsByEvent(Long id) {

		List<Ticket> ticketlist = new ArrayList<>(); 
		List<TicketType> types = typerepo.findByEventId(id); 
		for (TicketType type : types) {
			ticketlist.addAll(type.getTickets()); 
		}
		return ticketlist; 		
	}
	
	// palauttaa lipusta Eventin id:n
	public Long getEventIdFromTicket(Ticket ticket) {
		//haetaan tickettypen kautta tapahtuma
		return ticket.getTicketType().getEvent().getId();
	}
	
	// hae kaikkien peruuntuneiden tapahtumien liput (esim. hyvitystä varten)
	public List<Ticket> getTicketsOfCancelledEvents() throws Exception { //
		
		List<Ticket> cancelledTickets = new ArrayList<>(); // luodaan tyhjä peruttujen lippujen lista
		String statusName = "cancelled"; // statuksen nimeksi "cancelled" tai "peruttu", riippuen käytetäänkö tietokannassa suomea vai englantia

		try {
			List<Event> cancelledEvents = statusrepo.findByStatusName(statusName).getEvents(); // haetaan kaikki tapahtumat joiden status on cancelled/peruttu	
			
			for (Event e : cancelledEvents) { // käydään tapahtumalista läpi			
				Long eventID = e.getId();			
				cancelledTickets.addAll(getTicketsByEvent(eventID)); // lisätään jokaisen perutun tapahtuman liput palautettavaan listaan			
			}
			return cancelledTickets; // palautetaan lista
			
		} catch (Exception e) { // jos statuksen nimellä ei löydy mitään
			return cancelledTickets; // palautetaan tyhjä lista
		}
		
	}
	
	// Luodaan uusi koodi lipulle
	public String generateNewTicketCode(Ticket ticket) {
		String code = ticket.getCode();
		boolean isCodeAvailable = checkTicketCodeAvailability(code); // Tarkastetaan, onko lipun koodi jo käytössä
		
		while(!isCodeAvailable) { // Luodaan satunnaisia lippukoodeja niin kauan, että saadaan aikaan käyttämätön koodi
			code = RandomString.make(12);
			isCodeAvailable = checkTicketCodeAvailability(code);
		}
		
		return code;
	}
	
	// Tarkastetaan, onko parametrina annettu koodi jo käytössä tietokannassa
	public boolean checkTicketCodeAvailability(String code) {
		Optional<Ticket> ticket = ticketrepo.findByCode(code);
		
		if(ticket.isPresent()) {
			return false;
		}
		
		return true;
	}
	
}
