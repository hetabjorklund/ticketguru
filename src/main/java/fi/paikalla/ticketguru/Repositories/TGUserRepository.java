package fi.paikalla.ticketguru.Repositories;

import org.springframework.data.repository.CrudRepository;
import fi.paikalla.ticketguru.Entities.TGUser;

public interface TGUserRepository extends CrudRepository<TGUser, Long> {

}
