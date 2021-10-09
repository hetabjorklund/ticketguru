package fi.paikalla.ticketguru.controllers;

import java.util.List;
import java.util.Optional;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.paikalla.ticketguru.Repositories.InvoiceRepository;
import fi.paikalla.ticketguru.Entities.*;

@RestController
public class InvoiceController {
	
	@Autowired
	private InvoiceRepository invoicerepo;
	
	@Autowired
	private ObjectMapper objectMapper;
	
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
	public ResponseEntity<Invoice> addInvoice(@Valid @RequestBody Invoice invoice, BindingResult bindingresult) { // luodaan uusi lasku

		if (bindingresult.hasErrors()) { // tarkistetaan onko mukana TGUser
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // jos ei, palautetaan 400
		}
		else {			
			Invoice newInvoice = new Invoice(invoice.getTGuser()); // luodaan tässä uusi, jotta konstruktori tekee a) tyhjän lippulistan ja b) timestampiksi kuluvan hetken - muuten timestamp on joko pyynnössä lähetetty tai null
			invoicerepo.save(newInvoice);			
			return new ResponseEntity<>(newInvoice, HttpStatus.CREATED); // palautetaan luotu lasku ja 201
		}
	}		
	
	// PUT
	@PutMapping("/invoices/{id}") // päivittää haluttua laskua
	public ResponseEntity<Invoice> updateInvoice(@Valid @RequestBody Invoice newInvoice, @PathVariable Long id, BindingResult bindingresult) {
		
		if (bindingresult.hasErrors()) { // tarkistetaan onko mukana TGUser	
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // jos ei, palautetaan 400
		}		
		else {
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
	}
	
	// PATCH
	/*@PatchMapping("/invoices/{id}")
	public ResponseEntity<Invoice> updateInvoice(@Valid @PathVariable Long id, @RequestBody Invoice updatedInvoice, BindingResult bindingresult) {
		
		if (bindingresult.hasErrors()) { // jos validointi epäonnistuu eli tguser on null
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // palautetaan 400 
		}
		else { // jos validointi onnistuu eli tguser ei ole null			
		
			Optional<Invoice> target = invoicerepo.findById(id); // haetaan Optional-olio id:n perusteella invoicereposta
			
			if (target.isEmpty()) { // jos id:llä ei löydy laskua
				return new ResponseEntity<>(HttpStatus.NOT_FOUND); // palautetaan 404
			}	
			
			else { // jos id:llä löytyy lasku				
				try {
					Invoice invoice = target.get(); // otetaan Invoice-olio talteen käsittelyä varten
					
					//if (updatedInvoice.getTGuser() != null) { // jos tguser ei ole null
						invoice.setTGuser(updatedInvoice.getTGuser()); // korvataan vanhan laskun myyjä Patchissa tulevalla
					//}
					//else { // jos tguser on null
						//return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // palautetaan 400 
					//}
					
					if (updatedInvoice.getTickets() != null) { // tarkistetaan tuleeko Patchissa lippulista
						invoice.setTickets(updatedInvoice.getTickets()); // jos tulee, korvataan vanha lippulista uudella
					}
					
					// muita oliomuuttujia ei tarvitse käydä läpi: id:tä eikä alkuperäistä luontiaikaa pidä voida muuttaa
					
					invoicerepo.save(invoice); // jos tämän kommentoi pois, patch onnistuu vaikka tguser on null, jos ei kommentoi pois, tulee 500 (siksi try-catch)
					return new ResponseEntity<Invoice>(invoice, HttpStatus.OK);
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // palautetaan 400 
					}
			}
		}
	}*/
	
	@PatchMapping(value = "/invoices/{id}", consumes = "application/json-patch+json")
	public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody JsonPatch patchDocument) {
		
		Optional<Invoice> target = invoicerepo.findById(id); // haetaan invoicerepositorysta Optional-olio id:n perusteella
		if (target.isEmpty()) { // jos haettua laskua ei löydy
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // palautetaan 404
		}
		else { // jos haettu lasku löytyy			
			Invoice invoice = patchInvoice(patchDocument, id); // käytetään lasku pathInvoice-metodin kautta			
			return new ResponseEntity<>(invoice, HttpStatus.OK); // palautetaan muokattu lasku ja 200
		}		
	}
	
	// siirrä invoiceserviceen jos toimii
	public Invoice patchInvoice(JsonPatch patchDocument, Long id) {
		
		//ObjectMapper objectMapper = new ObjectMapper();
		
        // Gets the original invoice from the database
        Invoice originalInvoice = invoicerepo.findById(id).get();
        
        //Converts the original invoice to a JsonStructure
        JsonStructure invoiceToBePatched = objectMapper.convertValue(originalInvoice, JsonStructure.class);

        // Applies the patch to the original invoice
        JsonValue patchedInvoice = patchDocument.apply(invoiceToBePatched);

        // Converts the JsonValue to an Invoice instance
        Invoice modifiedInvoice = objectMapper.convertValue(patchedInvoice, Invoice.class);

        // Saves the modified invoice in the repository
        invoicerepo.save(modifiedInvoice); 
        
        return modifiedInvoice;
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
