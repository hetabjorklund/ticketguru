package fi.paikalla.ticketguru.Services;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.paikalla.ticketguru.Entities.Invoice;
import fi.paikalla.ticketguru.Repositories.InvoiceRepository;

@Service
public class InvoiceService {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private InvoiceRepository invoicerepo;
	
	public Invoice patchInvoice(JsonPatch patchDocument, Long id) {
			
        // Haetaan lasku invoicereposta (InvoiceControllerin updateInvoice-metodissa on jo tarkistettu että lasku löytyy eikä tule virheilmoitusta)
        Invoice originalInvoice = invoicerepo.findById(id).get();
        
        // Muunnetaan Invoice-olio JsonStructure-olioksi
        JsonStructure invoiceToBePatched = objectMapper.convertValue(originalInvoice, JsonStructure.class);

        // Lisätään pyynnössä saatu JsonPatch haluttuun laskuun
        JsonValue patchedInvoice = patchDocument.apply(invoiceToBePatched);

        // Muunnetaan JsonValue-olio takaisin Invoice-olioksi
        Invoice modifiedInvoice = objectMapper.convertValue(patchedInvoice, Invoice.class);

        // Tallennetaan muokattu lasku invoicerepoon
        invoicerepo.save(modifiedInvoice); 
        
        // Palautetaan muokattu lasku
        return modifiedInvoice;
    }
	
	

}
