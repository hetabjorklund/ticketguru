package fi.paikalla.ticketguru.Entities;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket extends AbstractPersistable<Long> {

	private Double price; 
	private boolean	used; 
	//@ManyToOne
	//private Event event
	@ManyToOne
	private TicketType ticketType; 
	@ManyToOne
	private Invoice invoice;
	
	
	public Ticket(TicketType ticketType, Double price) {
		super();
		this.price = price;
		this.used = false;
		this.ticketType = ticketType;
	} 
	
	public Ticket(TicketType ticketType, Double price, Invoice invoice) {
		super();
		this.price = price;
		this.used = false;
		this.ticketType = ticketType;
		this.invoice = invoice; 
	} 
	
	
}
