package fi.paikalla.ticketguru.Repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import fi.paikalla.ticketguru.Entities.Event;

public interface EventRepository extends CrudRepository<Event, Long> {

	Event findByName(String name);
	List<Event> findByStatus(long id); // haetaan lista eventtejä statuksen perusteella
	List<Event> findByStartTime(LocalDate start); 
	List<Event> findAll();
	
}
