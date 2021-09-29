package fi.paikalla.ticketguru.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Repositories.InvoiceRepository;
import fi.paikalla.ticketguru.Entities.Invoice;

@RestController
public class InvoiceController {
	
	@Autowired
	private InvoiceRepository invoicerepo;
	
	@GetMapping("/invoices") // hae kaikki laskut
	public List<Invoice> getAllInvoices() {
		return (List<Invoice>) this.invoicerepo.findAll();		
	}
	
	@GetMapping("invoices/{id}") // hae tietty lasku
	public ResponseEntity<Optional<Invoice>> getInvoiceById(@PathVariable Long id) {
		Optional<Invoice> invoice = this.invoicerepo.findById(id);
		
		if (invoice.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<>(invoice, HttpStatus.OK);
		}
		
	}
	
	
	
	
	
	// POST
	
	
	
	
	// PUT
	
	
	
	
	// DELETE

}
