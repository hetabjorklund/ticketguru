package fi.paikalla.ticketguru.Components;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.EventStatus;
import fi.paikalla.ticketguru.Repositories.EventRepository;
import fi.paikalla.ticketguru.Repositories.EventStatusRepository;

@Component
public class TaskScheduling {
	@Autowired
	private EventRepository eventrepo; 
	
	@Autowired
	private EventStatusRepository statusrepo; 
	
	@Scheduled(cron= "0 0 3 * * *") //eli aina kello 03.00, esim kello 3.00 joka keskiviikko olisi "0 0 3 * * WED "
	public void updateEventStatus() {
		EventStatus upcoming = statusrepo.findByStatusName("upcoming"); //hae status tulevat
		List<Event> allEvents = eventrepo.findByStatus(upcoming.getId());//hae kaikki statuksella tulevat
		LocalDateTime current = LocalDateTime.now(); //aika nyt
		for (Event ev: allEvents) { //looppaa läpi
			if (ev.getEndTime().isBefore(current)) { //jos eventin loppuaika on ennen nykyaikaa, eli menneessä
				ev.setStatus(statusrepo.findByStatusName("past")); //vaihda status menneeksi
				eventrepo.save(ev); //tallenna
			}
		}
	}

}
