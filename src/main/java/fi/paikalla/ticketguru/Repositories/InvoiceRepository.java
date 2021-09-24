package fi.paikalla.ticketguru.Repositories;

import org.springframework.data.repository.CrudRepository;
import fi.paikalla.ticketguru.Entities.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

}
