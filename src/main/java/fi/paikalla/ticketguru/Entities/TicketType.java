package fi.paikalla.ticketguru.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class TicketType extends AbstractPersistable<Long>{

	@ManyToOne (fetch= FetchType.EAGER)
	private Event event;
	private String type;
	@OneToMany (cascade = CascadeType.ALL, mappedBy="ticketType")
	@JsonIgnore
	private List<Ticket> tickets;
	private double price; 

	public TicketType(Event event, String type, Double price) {
		super();
		this.event = event;
		this.type = type;
		this.price = price;
		this.tickets = new ArrayList<Ticket>(); 
	}


	
}
