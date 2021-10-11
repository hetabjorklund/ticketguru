package fi.paikalla.ticketguru.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.EventRepository;
import fi.paikalla.ticketguru.Repositories.TicketRepository;
import fi.paikalla.ticketguru.Repositories.TicketTypeRepository;

@Service
public class TicketService {
	
	@Autowired
	private TicketRepository ticketrepo;
	@Autowired
	private EventRepository eventrepo;
	@Autowired
	private TicketTypeRepository typerepo;
	
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
	
	// tarkista, onko tapahtumaan vapaita lippuja
	public Boolean hasAvailableTickets(Long id) throws Exception {
		
		try {
			Event event = eventrepo.findById(id).get();
			
			if (getTicketsByEvent(id).size() < event.getMaxCapacity()) { // jos tapahtuman lippulista on pienempi kuin maksimipaikkamäärä
				return true; // palautetaan true eli lippuja on saatavana
			}
			else { // jos lippulista ei ole pienempi kuin maksimipaikkamäärä
				return false; // palautetaan false eli lippuja ei ole saatavana
			}
		} catch (Exception e) { // jos eventin id:llä ei löydy tapahtumaa ja tulee virhe
			return false; // palautetaan false
		}
		
	}

	
	// palauttaa lipusta Eventin id:n
	public Long getEventIdFromTicket(Ticket ticket) {
		//haetaan tickettypen kautta tapahtuma
		return ticket.getTicketType().getEvent().getId();
	}
}
