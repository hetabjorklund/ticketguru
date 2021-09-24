package fi.paikalla.ticketguru.Repositories;

import org.springframework.data.repository.CrudRepository;
import fi.paikalla.ticketguru.Entities.Event;

public interface EventRepository extends CrudRepository<Event, Long> {

	Event findByName(String name);

}
