package fi.paikalla.ticketguru.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Repositories.EventRepository;

@Service
public class EventService {
	@Autowired
	EventRepository eventrepo; 
	
	//hakee alkpupvm perusteella
	public List<Event> getAllByStart(LocalDate date) {
		List<Event> out = new ArrayList<Event>(); 
		LocalDateTime timestamp = date.atStartOfDay(); //parametrin keskiyö
		for (Event ev : eventrepo.findAll()) { //luuppaa kaikki tapahtumat
			if (ev.getStartTime().isAfter(timestamp)) {//jos parametrin päivänä tai sen jälkeen, lisää listaan. 
				out.add(ev); 
			}
		}
		return out; //palauta lista. 
	}; 
	//hakee loppupvm perusteella
	public List<Event> getAllByEnd(LocalDate date) {
		List<Event> out = new ArrayList<Event>(); 
		LocalDateTime timestamp = date.atTime(23,59);  //muunna localdate localdatetimeksi vrk lopun perusteella
		for (Event ev : eventrepo.findAll()) {//luuppaa kaikki tapahtumat
			if (ev.getStartTime().isBefore(timestamp)) { //jos ennen, lisää listaan.
				out.add(ev); 
			}
		}
		return out; //palauta lista
	}; 
	//hakee pvm välistä
	public List<Event> getBtwDates(LocalDate start, LocalDate end) {
		List<Event> out = new ArrayList<Event>(); 
		LocalDateTime startTime = start.atStartOfDay();//muunna kuten yllä
		LocalDateTime endTime =	end.atTime(23,59);
		for (Event ev : eventrepo.findAll()) { //luuppaa kaikki tapahtumat
			if (ev.getStartTime().isAfter(startTime) && ev.getStartTime().isBefore(endTime)) { 
				//jos alkupvm jälkeen ja loppupvm ennen, lisää listaan. 
				out.add(ev); 
			};
		};
		
		return out; //palauta lista
	}

}
