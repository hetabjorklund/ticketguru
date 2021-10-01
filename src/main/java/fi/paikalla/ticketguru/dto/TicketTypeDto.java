package fi.paikalla.ticketguru.dto;

public class TicketTypeDto {
	private long event; 
	private String type; 
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
