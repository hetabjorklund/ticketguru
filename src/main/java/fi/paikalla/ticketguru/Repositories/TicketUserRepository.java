package fi.paikalla.ticketguru.Repositories;

import org.springframework.data.repository.CrudRepository;

import fi.paikalla.ticketguru.Entities.TicketUser;

public interface TicketUserRepository extends CrudRepository<TicketUser, Long> {

}
