package fi.paikalla.ticketguru.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import fi.paikalla.ticketguru.Entities.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
	
	List<Ticket> findByInvoiceId(long id); 

}
