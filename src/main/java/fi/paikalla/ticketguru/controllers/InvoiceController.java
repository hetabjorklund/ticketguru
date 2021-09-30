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
import fi.paikalla.ticketguru.Entities.*;

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
		Optional<Invoice> invoice = this.invoicerepo.findById(id); // haetaan annetulla id:llä lasku
		
		if (invoice.isEmpty()) { // tarkistetaan onko haetun id:n lasku olemassa vai null
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // jos null, palautetaan 404
		}
		else {
			return new ResponseEntity<>(invoice, HttpStatus.OK); // jos haetun id:n lasku on olemassa, palautetaan se ja 200
		}
	}
	
	@GetMapping("/invoices/{id}/tickets") // hae kaikki tietyllä laskulla myydyt liput
	public ResponseEntity<List<Ticket>> getTicketsOfInvoiceById(@PathVariable Long id) throws Exception {
		
		try {
			Optional<Invoice> invoiceOption = this.invoicerepo.findById(id); // haetaan annetulla id:llä lasku
			Invoice invoice = invoiceOption.get(); // haetaan Optional-oliosta varsinainen Invoice-olio
			
			if (invoiceOption.isEmpty()) { // tarkistetaan onko haetun id:n lasku olemassa vai null
				return new ResponseEntity<>(HttpStatus.NOT_FOUND); // jos null, palautetaan 404
			}
			else {
				return new ResponseEntity<>(invoice.getTickets(), HttpStatus.OK); // jos haetun id:n lasku on olemassa, palautetaan sen liput listana ja 200	
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // jos tapahtuu jokin virhe, laskua ei ole löytynyt ja palautetaan 404			
		}
	}
	
	
	
	
	
	// POST
	
	
	
	
	// PUT
	
	
	
	
	// DELETE

}
