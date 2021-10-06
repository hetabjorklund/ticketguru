package fi.paikalla.ticketguru.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.paikalla.ticketguru.Repositories.EventRepository;
import fi.paikalla.ticketguru.Repositories.TicketRepository;

@Service
public class TicketService {
	
	@Autowired
	private TicketRepository ticketrepo;
	@Autowired
	private EventRepository eventrepo;

}
