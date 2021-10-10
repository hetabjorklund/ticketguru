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
	
	
	public List<Event> getAllByStart(LocalDate date) {
		List<Event> out = new ArrayList<Event>(); 
		LocalDateTime timestamp = date.atStartOfDay(); 
		for (Event ev : eventrepo.findAll()) {
			if (ev.getStartTime().isAfter(timestamp)) {
				out.add(ev); 
			}
		}
		return out; 
	}; 
	
	public List<Event> getAllByEnd(LocalDate date) {
		List<Event> out = new ArrayList<Event>(); 
		LocalDateTime timestamp = date.atTime(23,59);  
		for (Event ev : eventrepo.findAll()) {
			if (ev.getStartTime().isBefore(timestamp)) {
				out.add(ev); 
			}
		}
		return out; 
	}; 
	
	public List<Event> getBtwDates(LocalDate start, LocalDate end) {
		List<Event> out = new ArrayList<Event>(); 
		LocalDateTime startTime = start.atStartOfDay();
		LocalDateTime endTime =	end.atTime(23,59);
		for (Event ev : eventrepo.findAll()) {
			if (ev.getStartTime().isAfter(startTime) && ev.getStartTime().isBefore(endTime)) {
				out.add(ev); 
			};
		};
		
		return out; 
	}

}
