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
	@Bean
	public CommandLineRunner demo(EventRepository eventRepo, EventStatusRepository statusRepo, 
			TicketTypeRepository typerepo, TicketRepository ticketRepo, 
			InvoiceRepository invRepo, TGUserRepository userepo){
		
		return(args)-> {
			log.info("Creating status");
			statusRepo.save(new EventStatus("upcoming")); 
			
			log.info("Creating event");
			LocalDateTime start = LocalDateTime.of(2021,12,03,9,00);
			LocalDateTime end = LocalDateTime.of(2021,12,03,16,00); 
			LocalDateTime prend = LocalDateTime.of(2021,11,27,16,00); 
			
			Event ev = new Event("Ruisrock", "Savonlinnankatu 50", 600, 
					start, end, prend, "murderdeathkill"); 
			Event ev2 = new Event("Ruisrock2", "Savonlinnankatu 50", 700, 
					start, end, prend, "murderdeathkillevent"); 
			Event ev3 = new Event("Ruisrock3", "Savonlinnankatu 50", 700, 
					start, end, prend, statusRepo.findByStatusName("upcoming"),"murderdeathkillevent"); 
			
			eventRepo.save(ev);
			eventRepo.save(ev2);
			eventRepo.save(ev3); 
			
			TicketType tt1 = new TicketType(eventRepo.findByName("Ruisrock"), "aikuinen", 20.90); 
			TicketType tt2 = new TicketType(eventRepo.findByName("Ruisrock"), "eläkeläinen", 30.00); 
			TicketType tt3 = new TicketType(eventRepo.findByName("Ruisrock3"), "aikuinen", 24.75);
			
			typerepo.save(tt1);
			typerepo.save(tt2);
			typerepo.save(tt3);
			
			TGUser user = new TGUser("maikki", "menevä", "MaiMe", "salasana", "USER"); 
			
			userepo.save(user); 
			
			Invoice invoice1 = new Invoice(user); 
			
			invRepo.save(invoice1); 
			
			Ticket t1 = new Ticket(tt1, 20.9, invoice1);
			Ticket t2 = new Ticket(tt2, 30.00, invoice1);
			
			ticketRepo.save(t1); 
			ticketRepo.save(t2); 
			
			
		}; 	
	}

}
