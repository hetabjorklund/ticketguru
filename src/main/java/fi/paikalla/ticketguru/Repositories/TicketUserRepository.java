package fi.paikalla.ticketguru.Repositories;

import org.springframework.data.repository.CrudRepository;

import fi.paikalla.ticketguru.Classes.TicketUser;

public interface TicketUserRepository extends CrudRepository<TicketUser, Long> {

}
