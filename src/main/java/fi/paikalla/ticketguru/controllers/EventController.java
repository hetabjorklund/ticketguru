package fi.paikalla.ticketguru.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import fi.paikalla.ticketguru.Repositories.EventRepository;

@RestController
public class EventController {
	
	@Autowired
	private EventRepository eventrepo;

}
