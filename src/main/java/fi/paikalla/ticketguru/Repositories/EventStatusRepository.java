package fi.paikalla.ticketguru.Repositories;
import fi.paikalla.ticketguru.Classes.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventStatusRepository extends JpaRepository<EventStatus, Long> {

}
