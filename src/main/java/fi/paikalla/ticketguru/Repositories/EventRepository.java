package fi.paikalla.ticketguru.Repositories;
import fi.paikalla.ticketguru.Entities.Event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
