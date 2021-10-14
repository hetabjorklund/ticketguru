package fi.paikalla.ticketguru.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.EventStatusRepository;
import fi.paikalla.ticketguru.Repositories.TicketRepository;
import fi.paikalla.ticketguru.Repositories.TicketTypeRepository;

@Service
public class TicketService {
	
	@Autowired
	private TicketRepository ticketrepo;
	@Autowired
	private TicketTypeRepository typerepo;
	@Autowired
	private EventStatusRepository statusrepo;	
	
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
}
