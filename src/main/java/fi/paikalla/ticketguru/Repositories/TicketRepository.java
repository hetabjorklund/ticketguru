package fi.paikalla.ticketguru.Repositories;

import org.springframework.data.repository.CrudRepository;
import fi.paikalla.ticketguru.Entities.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
	

}
