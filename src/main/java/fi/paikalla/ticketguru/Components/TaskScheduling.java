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
	
	
	@Scheduled(cron= "0 3 * * *") //eli aina kello 03.00, esim kello 3.00 joka keskiviikko olisi "0 3 * * 3"
	public void updateEventStatus() {
		EventStatus upcoming = statusrepo.findByStatusName("upcoming"); //hae status tulevat
		List<Event> allEvents = eventrepo.findByStatus(upcoming.getId());//hae kaikki statuksella tulevat
		LocalDateTime current = LocalDateTime.now(); //aika nyt
		for (Event ev: allEvents) { //looppaa läpi
			if (ev.getEndTime().isBefore(current)) { //jos eventin loppuaika on ennen nykyaikaa, eli menneessä
				ev.setStatus(statusrepo.findByStatusName("passed")); //vaihda status menneeksi
				eventrepo.save(ev); //tallenna
			}
		}
	}
	
	/*//Aikataulutuksen testaus
	 * @Scheduled(fixedRate = 20000)
	public void toggleStat() {
		EventStatus upcoming = statusrepo.findByStatusName("upcoming");
		EventStatus passed = statusrepo.findByStatusName("passed");
		
		List<Event> allEvents = (List<Event>) eventrepo.findAll();
		for (Event ev: allEvents) {
			if(ev.getStatus().getId().equals(upcoming.getId())) {
				ev.setStatus(passed);
				eventrepo.save(ev);
				System.out.println("Changed to passed");
			} else {
				ev.setStatus(upcoming);
				eventrepo.save(ev);
				System.out.println("Changed to upcoming");
			}
		}
	 * 
	 * }
	 * 
	 */
	
		
		
	
	
	
	

}
