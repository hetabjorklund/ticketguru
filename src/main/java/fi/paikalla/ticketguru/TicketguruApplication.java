package fi.paikalla.ticketguru;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Entities.EventStatus;
import fi.paikalla.ticketguru.Entities.Invoice;
import fi.paikalla.ticketguru.Entities.TGUser;
import fi.paikalla.ticketguru.Entities.Ticket;
import fi.paikalla.ticketguru.Entities.TicketType;
import fi.paikalla.ticketguru.Repositories.EventRepository;
import fi.paikalla.ticketguru.Repositories.EventStatusRepository;
import fi.paikalla.ticketguru.Repositories.InvoiceRepository;
import fi.paikalla.ticketguru.Repositories.TGUserRepository;
import fi.paikalla.ticketguru.Repositories.TicketRepository;
import fi.paikalla.ticketguru.Repositories.TicketTypeRepository;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class TicketguruApplication {
	
	private static final Logger log = LoggerFactory.getLogger(TicketguruApplication.class); 

	public static void main(String[] args) {
		SpringApplication.run(TicketguruApplication.class, args);
	}
	
	// luodaan valmiiksi dataa tietokantaan jotta on jotain millä testata
	@Bean
	public CommandLineRunner createStarterData(EventRepository eventRepo, EventStatusRepository statusRepo, 
			TicketTypeRepository typeRepo, TicketRepository ticketRepo, 
			InvoiceRepository invoiceRepo, TGUserRepository userRepo){
		
		return(args)-> {
			log.info("Creating status");
			statusRepo.save(new EventStatus("upcoming")); 
			
			log.info("Creating event");
			LocalDateTime start = LocalDateTime.of(2021,12,03,9,00);
			LocalDateTime end = LocalDateTime.of(2021,12,03,16,00); 
			LocalDateTime presaleend = LocalDateTime.of(2021,11,27,16,00); 
			
			Event event1 = new Event("Ruisrock", "Savonlinnankatu 50", 600, 
					start, end, presaleend, "Ruissalossa rokataan"); 
			Event event2 = new Event("Muumirock", "Muumilaakso", 700, 
					start, end, presaleend, "Pihoo!"); 
			Event event3 = new Event("Mörkörock", "Yksinäiset vuoret", 1, 
					start, end, presaleend, statusRepo.findByStatusName("upcoming"), "Mörkö narisee yksin"); 
			
			eventRepo.save(event1);
			eventRepo.save(event2);
			eventRepo.save(event3); 
			
			TicketType tt1 = new TicketType(eventRepo.findByName("Ruisrock"), "aikuinen", 20.90); 
			TicketType tt2 = new TicketType(eventRepo.findByName("Muumirock"), "eläkeläinen", 30.00); 
			TicketType tt3 = new TicketType(eventRepo.findByName("Mörkörock"), "aikuinen", 24.75);
			
			typeRepo.save(tt1);
			typeRepo.save(tt2);
			typeRepo.save(tt3);
			
			TGUser user = new TGUser("maikki", "menevä", "MaiMe", "salasana", "USER"); 
			
			userRepo.save(user); 
			
			Invoice invoice1 = new Invoice(user); 
			
			invoiceRepo.save(invoice1); 
			
			Ticket t1 = new Ticket(typeRepo.findByTypeAndEvent("aikuinen", eventRepo.findByName("Ruisrock")), 20.9, invoice1);
			Ticket t2 = new Ticket(typeRepo.findByTypeAndEvent("eläkeläinen", eventRepo.findByName("Ruisrock")), 30.00, invoice1);
			
			ticketRepo.save(t1); 
			ticketRepo.save(t2); 			
			
		}; 	
	}

}
