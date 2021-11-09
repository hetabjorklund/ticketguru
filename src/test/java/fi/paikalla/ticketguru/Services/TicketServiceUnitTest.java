package fi.paikalla.ticketguru.Services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import fi.paikalla.ticketguru.Entities.Ticket;

@RunWith(SpringRunner.class)
@SpringBootTest
class TicketServiceUnitTest {
	
	@Autowired
	TicketService ticketservicetester;
	
	@Test
	void testGetAllTickets() {
		
		List<Ticket> ticketlist = ticketservicetester.getAllTickets();
		Integer size = ticketlist.size();
		
		assertAll(
			// Palauttaako listaolion (saa olla tyhjä lista) eikä nullia
			() -> assertNotNull(ticketservicetester.getAllTickets(), "Should return at least an empty list, not null"),
			// Onko listan tyyppi ArrayList
			() -> assertEquals(ArrayList.class, ticketlist.getClass(), "Should return a list of the type ArrayList<Ticket>"),
			// Onko listan koko 0 (ei pitäisi olla koska tietokannassa on lippuja)
			() -> assertNotEquals(0, size, "Size of the ticket list should not be 0")
		);

	}

	@Test
	void testCheckTicketCodeAvailability() {
		
		assertAll(		
			// Annetaan parametrina koodi, joka on jo olemassa, eli metodin pitäisi palauttaa false
			() -> assertFalse(ticketservicetester.checkTicketCodeAvailability("hSqRzz1q8a5H"), "Should return false since the code already exists"),				
			// Annetaan parametrina koodi, jota ei ole vielä olemassa, eli metodin pitäisi palauttaa true
			() -> assertTrue(ticketservicetester.checkTicketCodeAvailability("000000000000"), "Should return true since the code doesn't yet exist")		
		);
		
	}

}
