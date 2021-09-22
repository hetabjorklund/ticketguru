package fi.paikalla.ticketguru.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fi.paikalla.ticketguru.Entities.TicketType;

public interface TicketTypeRepository extends CrudRepository<TicketType, Long> {
	
	List<TicketType> findByEventId(long id); 
	
	 

}
