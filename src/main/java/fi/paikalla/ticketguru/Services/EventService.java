package fi.paikalla.ticketguru.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.paikalla.ticketguru.Components.TicketListOrganizer;
import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Repositories.EventRepository;

@Service
public class EventService {

	@Autowired
	private ObjectMapper objectmapper;
	
	@Autowired
	private EventRepository eventrepo;
	
	@Autowired
	private TicketService ticketservice;
	
	@Autowired
	private TicketListOrganizer ticketlistorganizer;
	
	// eventin PATCH-toiminto
	public Event patchEvent(JsonPatch patchDocument, Long id) {		
		// Haetaan event eventreposta (EventControllerissa tarkistettu jo että event löytyy eikä tule virheilmoitusta)
        Event originalEvent = eventrepo.findById(id).get();        
        // Muunnetaan Event-olio JsonStructure-olioksi
        JsonStructure eventToBePatched = objectmapper.convertValue(originalEvent, JsonStructure.class);
        // Lisätään pyynnössä saatu JsonPatch haluttuun eventtiin
        JsonValue patchedEvent = patchDocument.apply(eventToBePatched);
        // Muunnetaan JsonValue-olio takaisin Event-olioksi
        Event modifiedEvent = objectmapper.convertValue(patchedEvent, Event.class);
        // Tallennetaan muokattu event eventrepoon
        eventrepo.save(modifiedEvent);         
        // Palautetaan muokattu event
        return modifiedEvent;
	}
	
	//hakee tapahtumat alkpupvm perusteella
	public List<Event> getAllByStart(LocalDate date) {
		List<Event> out = new ArrayList<Event>(); 
		LocalDateTime timestamp = date.atStartOfDay(); //parametrin keskiyö
		for (Event ev : eventrepo.findAll()) { //luuppaa kaikki tapahtumat
			if (ev.getStartTime().isAfter(timestamp)) {//jos parametrin päivänä tai sen jälkeen, lisää listaan 
				out.add(ev); 
			}
		}
		return out; //palauta lista. 
	};
	
	//hakee tapahtumat loppupvm perusteella
	public List<Event> getAllByEnd(LocalDate date) {
		List<Event> out = new ArrayList<Event>(); 
		LocalDateTime timestamp = date.atTime(23,59);  //muunna localdate localdatetimeksi vrk lopun perusteella
		for (Event ev : eventrepo.findAll()) {//luuppaa kaikki tapahtumat
			if (ev.getStartTime().isBefore(timestamp)) { //jos ennen, lisää listaan
				out.add(ev); 
			}
		}
		return out; //palauta lista
	}; 
	
	//hakee tapahtumat pvm välistä
	public List<Event> getBtwDates(LocalDate start, LocalDate end) {
		List<Event> out = new ArrayList<Event>(); 
		LocalDateTime startTime = start.atStartOfDay();//muunna kuten yllä
		LocalDateTime endTime =	end.atTime(23,59);
		for (Event ev : eventrepo.findAll()) { //luuppaa kaikki tapahtumat
			if (ev.getStartTime().isAfter(startTime) && ev.getStartTime().isBefore(endTime)) { 
				//jos alkupvm jälkeen ja loppupvm ennen, lisää listaan 
				out.add(ev); 
			};
		};		
		return out; //palauta lista
	}
	
	// tapahtuman myymättömien lippujen määrä
	public Integer getAvailableCapacityOfEvent(Long eventId) throws Exception {
		
		try {
			Event event = eventrepo.findById(eventId).get();
			int ticketsLeft = event.getMaxCapacity() - getNumberOfSoldTicketsofEvent(eventId);
			return ticketsLeft;
		} catch (Exception e) { // jos tapahtuman id:llä ei löydy tapahtumaa
			return -1;
		}
		
	}
	
	// tapahtuman myytyjen lippujen määrä
	public Integer getNumberOfSoldTicketsofEvent(Long eventId) throws Exception {
		
		try {
			int ticketsSold = ticketservice.getTicketsByEvent(eventId).size();
			return ticketsSold;
		} catch (Exception e) { // jos tapahtuman id:llä ei löydy tapahtumaa
			return -1;
		}
		
	}
	
	// tarkista, onko tapahtumaan vapaita lippuja
	public Boolean hasAvailableTickets(Long eventId) throws Exception {
		
		try {
			Event event = eventrepo.findById(eventId).get();
				
			if (getNumberOfSoldTicketsofEvent(eventId) < event.getMaxCapacity()) { // jos tapahtumaan myytyjen lippujen määrä on pienempi kuin tapahtuman maksimipaikkamäärä
				return true; // palautetaan true eli lippuja on saatavana
			}
			else { // jos lippulista ei ole pienempi kuin maksimipaikkamäärä
				return false; // palautetaan false eli lippuja ei ole saatavana
			}
		} catch (Exception e) { // jos eventin id:llä ei löydy tapahtumaa ja tulee virhe
			return false; // palautetaan false
		}			
	}
	
	public boolean checkTicketAvailability(List<Ticket> tickets) { // Tarkastetaan lippulistasta, onko kussakin eventissä tarpeeksi lippuja myytävänä
		boolean hasAvailableTickets = true;
		
		Map<Long, Integer> ticketsPerEvent = ticketlistorganizer.getTicketsPerEvent(tickets); // TicketListOrganizer-komponentin metodi, joka rakentaa lippulistasta mapin, jossa avaimena on eventin id, ja arvona kullekin eventille haluttu lippujen määrä
		
		for(Map.Entry<Long, Integer> entry: ticketsPerEvent.entrySet()) { //Käydään mappi läpi avain-arvo-pari kerrallaan
			Long eventId = entry.getKey();
			int ticketAmount = entry.getValue();
			
			try {
				int unsoldTickets = this.getAvailableCapacityOfEvent(eventId); // Haetaan eventin perusteella myymättömät liput
				
				if(ticketAmount > unsoldTickets) { // Mikäli lippuja ei ole jäljellä tarpeeksi, palautetaan false
					hasAvailableTickets = false;
				}
				
			}catch(Exception e) {
				hasAvailableTickets = false;
			}
			
		}
		
		return hasAvailableTickets;
	}
}
