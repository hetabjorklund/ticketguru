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
import org.springframework.web.bind.annotation.PutMapping;
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
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // jos tapahtuu jokin virhe, etsittyä laskua ei ole löytynyt ja palautetaan 404			
		}
	}	
	
	// POST
	@PostMapping("/invoices")
	public ResponseEntity<Invoice> addInvoice(@RequestBody Invoice invoice) { // luodaan uusi lasku
		
		/*if (invoice.getTickets() != null) { // tarkistetaan onko pyynnön mukana tulevassa laskussa lippulista
			invoice.getTickets().clear(); // jos on, varmuuden vuoksi tyhjennetään pyynnön mukana tulevan laskun lippulista: liput lisätään tiettyyn laskuun vasta kun ne luodaan/myydään			
		}*/
		return new ResponseEntity<>(invoicerepo.save(invoice), HttpStatus.CREATED); // palautetaan luotu lasku ja 201
	}		
	
	// PUT
	@PutMapping("/invoices/{id}") // päivittää haluttua laskua
	public ResponseEntity<Invoice> updateInvoice(@RequestBody Invoice newInvoice, @PathVariable Long id) {
		
		if (this.invoicerepo.findById(id).isEmpty()) { // tarkistetaan löytyykö haetulla id:llä laskua
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // jos ei löydy, palautetaan 404
		}
		else {
			Invoice invoice = this.invoicerepo.findById(id).get(); // jos haetulla id:llä löytyy lasku, päivitetään sen tiedot
			invoice.setTGuser(newInvoice.getTGuser());
			invoice.setTickets(newInvoice.getTickets());
			invoice.setTimestamp(newInvoice.getTimestamp());

			this.invoicerepo.save(invoice); // tallennetaan päivitetty lasku
			
			return new ResponseEntity<>(invoice, HttpStatus.OK); // palautetaan uusi, päivitetty lasku ja 200				
		}
	}
		
	// DELETE
	@DeleteMapping("/invoices")
	public ResponseEntity<String> deleteAll() { // poistetaan kaikki laskut
		
		if (this.invoicerepo.count() == 0) { // tarkistetaan onko invoicerepossa ylipäätään mitään poistettavaa
			return new ResponseEntity<>("There are no invoices to delete", HttpStatus.NOT_FOUND); // jos ei ole, palautetaan viesti ja 404
		}
		
		else { // jos invoicerepossa on laskuja
			
			// tarkistetaan onko laskussa lippulistaa jonka koko on 0			
			for (Invoice invoice : this.invoicerepo.findAll()) {
				if (invoice.getTickets().size() == 0) {
					this.invoicerepo.delete(invoice); // poistetaan vain ne laskut joissa on tyhjä lippulista
				}
			}
						
			if (this.invoicerepo.count() == 0) { // jos tyhjennys onnistui
				return new ResponseEntity<>("All invoices have been deleted", HttpStatus.NO_CONTENT); // palautetaan viesti ja 204		
			}
			else {
				return new ResponseEntity<>("One or more invoices have associated tickets, deletion forbidden", HttpStatus.FORBIDDEN); // jos tyhjennys ei onnistunut, palautetaan 403
			}			
		}
				
	}	
	
	@DeleteMapping("/invoices/{id}")
	public ResponseEntity<String> deleteInvoiceById(@PathVariable Long id) { // poistetaan haluttu lasku
		
		
		if (this.invoicerepo.findById(id).isPresent()) { // jos haetulla id:llä löytyy lasku	
			
			Invoice invoice = this.invoicerepo.findById(id).get(); // otetaan lasku talteen käsittelyä varten
				
			if (invoice.getTickets().size() == 0) { // tarkistetaan onko laskulla lippuja
				this.invoicerepo.delete(invoice); // jos ei, poistetaan lasku
				return new ResponseEntity<>("Invoice deleted", HttpStatus.NO_CONTENT); // palautetaan viesti ja 204
			}
				
			else { // jos laskulla on lippuja
				return new ResponseEntity<>("Invoice has associated tickets, deletion forbidden", HttpStatus.FORBIDDEN); // palautetaan viesti ja 403
			}
				
		}			
		
		else { // eli jos haetulla id:llä ei löydy laskua
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // palautetaan 404
		}
		
	}

}