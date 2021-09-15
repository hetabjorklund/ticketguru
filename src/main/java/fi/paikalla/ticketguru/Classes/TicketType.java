package fi.paikalla.ticketguru.Classes;

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
	private String description;
	/*@OneToMany(cascade = CascadeType.ALL, mappedBy="typeId")
	private List<EventTicket> eventTicket;*/
	
	public TicketType() {}

	public TicketType(String description) {
		super();
		this.description = description;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "TicketType [typeId=" + typeId + ", description=" + description + "]";
	}
	
	
	
}
