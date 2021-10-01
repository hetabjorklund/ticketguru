package fi.paikalla.ticketguru.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Repositories.InvoiceRepository;
import fi.paikalla.ticketguru.Entities.*;

@RestController
public class InvoiceController {
	
	@Autowired
	private InvoiceRepository invoicerepo;
	
	// GET	
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
	@PostMapping("/invoices")
	public ResponseEntity<Invoice> addInvoice(@RequestBody Invoice invoice) { // luodaan uusi lasku		
		// pitäisikö tässä tarkistaa, onko lippulistassa samoja lippuja kuin jonkun muun invoicen listassa, koska samaa lippua ei pidä myydä moneen kertaan?				
		return new ResponseEntity<>(invoicerepo.save(invoice), HttpStatus.CREATED); // palautetaan luotu lasku ja 201
	}		
	
	// PUT
	
	
	
	
	// DELETE
	@DeleteMapping("/invoices/{id}")
	public ResponseEntity<Invoice> deleteInvoiceById(@PathVariable Long id) throws Exception { // poistetaan haluttu lasku
		
		try {
			if (this.invoicerepo.findById(id) != null) { // jos haetulla id:llä löytyy lasku			
				Invoice invoice = this.invoicerepo.findById(id).get(); // haetaan lasku talteen palautusta varten
				this.invoicerepo.deleteById(id); // poistetaan haettu lasku reposta
				return new ResponseEntity<>(invoice, HttpStatus.OK); // palautetaan poistettu lasku ja 200					
			}			
			else { // eli jos haetulla id:llä ei löydy laskua
				return new ResponseEntity<>(HttpStatus.NOT_FOUND); // palautetaan 404
			}
		} catch (Exception e) { 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // jos tapahtuu jokin virhe, palautetaan 404			
		}		
		
	}

}
