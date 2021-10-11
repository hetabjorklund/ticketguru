package fi.paikalla.ticketguru.dto;

import javax.validation.constraints.Min;

import org.springframework.validation.annotation.Validated;

@Validated
public class TicketDto {
	private double price; 
	private boolean	used;
	
	@Min(value = 0, message = "invoice should be at least 1")
	private long invoice;
	
	@Min(value = 0, message= "ticketType must be at least 1")
	private long ticketType;
	
	public TicketDto() {}

	public TicketDto(double price, boolean used, long ticketType, long invoice) {
		super();
		this.price = price;
		this.used = used;
		this.ticketType = ticketType;
		this.invoice = invoice;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public long getTicketType() {
		return ticketType;
	}

	public void setTicketType(long ticketType) {
		this.ticketType = ticketType;
	}

	public long getInvoice() {
		return invoice;
	}

	public void setInvoice(long invoice) {
		this.invoice = invoice;
	}
	
}
