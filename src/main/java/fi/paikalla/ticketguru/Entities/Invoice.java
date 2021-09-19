package fi.paikalla.ticketguru.Entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long invoiceId;
	private LocalDateTime timeOfSale; // aikaleima myyntitapahtumalle
	
	@ManyToOne
	@JoinColumn(name = "TGUserId")
	private TGUser TGuser; // laskun myyjä

	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "invoiceId")
	private List<Ticket> tickets; // lista samalla laskulla olevista lipuista

	public Invoice() {}
	
	public Invoice(Long invoiceId, LocalDateTime timestamp, TGUser TGUser, List<Ticket> tickets) {
		super();
		this.invoiceId = invoiceId;
		this.timestamp = timestamp;
		TGUser = TGUser;
		this.tickets = tickets;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public TGUser getTGuser() {
		return TGuser;
	}

	public void setTGuser(TGUser tGuser) {
		TGuser = tGuser;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public String toString() {
		return "Invoice [invoiceId=" + invoiceId + ", timestamp=" + timestamp + "]";
	}
	
	
	
	
}
