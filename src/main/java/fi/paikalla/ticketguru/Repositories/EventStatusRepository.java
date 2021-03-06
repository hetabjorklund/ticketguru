package fi.paikalla.ticketguru.Repositories;

import fi.paikalla.ticketguru.Entities.EventStatus;
import org.springframework.data.repository.CrudRepository;

public interface EventStatusRepository extends CrudRepository<EventStatus, Long> {
	
	EventStatus findByStatusName(String statusName);

}
