package fi.paikalla.ticketguru.Entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

public class TicketType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long typeId;
	private String type;
	/*@OneToMany(cascade = CascadeType.ALL, mappedBy="typeId")
	private List<Ticket> ticket;*/
	
	public TicketType() {}

	public TicketType(String type) {
		super();
		this.type = type;
	}

	public Long getTypeId() {
		return typeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	/*public List<Ticket> getTicket() {
		return ticket;
	}

	public void setTicket(List<Ticket> ticket) {
		this.ticket = ticket;
	}*/
}
