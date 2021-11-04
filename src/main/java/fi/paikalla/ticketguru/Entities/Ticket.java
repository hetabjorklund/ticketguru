package fi.paikalla.ticketguru.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import fi.paikalla.ticketguru.Repositories.TicketRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.bytebuddy.utility.RandomString;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
//@Validated
public class Ticket {
	
	//@Autowired
	//private TicketRepository ticketrepo;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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
	
	private String code; // yksilöllinen koodi lipun tarkistamista varten (12 merkkiä pitkä random string)
	
	// konstruktori pelkän lipun luomiseen ilman laskua
	public Ticket(TicketType ticketType, Double price) {
		super();
		this.price = price;
		this.used = false;
		this.ticketType = ticketType;
		this.code = RandomString.make(12);
	} 
	
	// konstruktori lipun luomiseen tietylle laskulle, hinta asetetaan erikseen
	public Ticket(TicketType ticketType, Double price, Invoice invoice) {
		super();
		this.price = price;
		this.used = false;
		this.ticketType = ticketType;
		this.invoice = invoice;
		this.code = RandomString.make(12);
	}

	// konstruktori lipun luomiseen tietylle laskulle, hinta tulee suoraan TicketTypesta
	public Ticket(TicketType ticketType, Invoice invoice) {
		super();
		this.ticketType = ticketType;
		this.invoice = invoice;
		this.price = ticketType.getPrice();
		this.used = false;
		this.code = RandomString.make(12);
	}
			
}
