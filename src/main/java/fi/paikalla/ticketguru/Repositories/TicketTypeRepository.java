package fi.paikalla.ticketguru.Repositories;

import org.springframework.data.repository.CrudRepository;

import fi.paikalla.ticketguru.Classes.TicketType;

public interface TicketTypeRepository extends CrudRepository<TicketType, Long> {

}