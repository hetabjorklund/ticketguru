package fi.paikalla.ticketguru.Entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class Ticket extends AbstractPersistable<Long> {

	@Min(0)
	private Double price;
	
	@NotNull
	private boolean	used; 
	//@ManyToOne // tulee rekursioympyrä
	//@JsonIgnore
	//private Event event;
	
	@NotNull
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

	public Ticket(TicketType ticketType, Invoice invoice) {
		super();
		this.ticketType = ticketType;
		this.invoice = invoice;
		this.price = ticketType.getPrice();
		this.used = false;
	}
	
	/*public Ticket(TicketType ticketType, Double price, Event event, Invoice invoice) {
		super();
		this.price = price;
		//this.event = event; 
		this.used = false;
		this.ticketType = ticketType;
		this.invoice = invoice; 
	}*/
		
}
