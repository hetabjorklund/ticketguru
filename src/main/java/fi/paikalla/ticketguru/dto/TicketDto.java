package fi.paikalla.ticketguru.dto;

public class TicketDto {
	private double price; 
	private boolean	used; 
	private long ticketType;
	private long invoice;
	
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
