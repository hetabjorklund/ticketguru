package fi.paikalla.ticketguru.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TicketTypeDto {
	@NotNull(message = "Event must be present")
	@Min(value = 1, message = "Event must be larger than 0")
	private long event; 
	@NotBlank(message = "Type must be present")
	private String type; 
	@NotNull
	private double price;

	public TicketTypeDto(long event, String type, double price) {
		super();
		this.event = event;
		this.type = type;
		this.price = price;
	}
	public TicketTypeDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public TicketTypeDto(double price) {
		this.price = price; 
	}
	
	public long getEvent() {
		return event;
	}
	public void setEvent(long event) {
		this.event = event;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	} 
	
}
