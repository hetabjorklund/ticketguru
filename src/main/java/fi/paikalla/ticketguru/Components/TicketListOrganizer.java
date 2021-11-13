package fi.paikalla.ticketguru.Components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import fi.paikalla.ticketguru.Entities.Ticket;

@Component
public class TicketListOrganizer { //Järjestelee lippulistasta hashmapin, joka näyttä kuinka monta lippua halutaan mihinkin eventiin
	public Map<Long, Integer> getTicketsPerEvent(List<Ticket> tickets){ // Järjestää lippulistan Mapiksi siten, että eventien Id:t toimivat avaimina ja kullekin eventille tarkoitettu lippujen määrä arvona
		Map<Long, Integer> ticketsPerEvent = new HashMap<>();
		
		for(Ticket ticket: tickets) { // Käydään lippulista läpi
			long eventId = ticket.getTicketType().getEvent().getId(); // Otetaan lipusta eventin id
			
			if(!ticketsPerEvent.containsKey(eventId)) { // Mikäli eventtiä vastaava avain ei ole vielä mapissa, alustetaan se yhdeksi
				ticketsPerEvent.put(eventId, 1);
			}else { // Mikäli eventiä vastaava avain on jo mapissa, kasvatetaan sen arvoa yhdellä
				ticketsPerEvent.put(eventId, ticketsPerEvent.get(eventId)+1);
			}
		}
		
		return ticketsPerEvent;
	}
}
