package fi.paikalla.ticketguru.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Invoice {
	
	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime invoiceTimestamp; // aikaleima myyntitapahtumalle	
	@NotNull @ManyToOne
	private TGUser tguser; // laskun myyjä
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")	@JsonIgnore
	private List<Ticket> tickets; // lista samalla laskulla olevista lipuista

	public Invoice() {} // parametriton konstruktori
	
	/*public Invoice(Long invoiceId, LocalDateTime timestamp, TGUser TGUser, List<Ticket> tickets) {
		super();
		this.invoiceId = invoiceId;
		this.timestamp = timestamp;
		this.tguser = TGUser;
		this.tickets = tickets;
	}*/
	
	public Invoice(TGUser tguser) { // parametrillisessä konstruktorissa ei tarvita muuta kuin myyjä; id tulee automaattisesti
		this.tguser = tguser; 
		this.invoiceTimestamp = LocalDateTime.now(); // myyntiaika on automaattisesti nykyinen aika
		this.tickets = new ArrayList<Ticket>(); // lippulista on automaattisesti tyhjä lista; liput lisätään laskuun vasta kun ne luodaan/myydään
	}
	
	public Long getInvoiceId() {
		return this.id;
	}

	public void setInvoiceId(Long invoiceId) {
		this.id = invoiceId;
	}

	public LocalDateTime getTimestamp() {
		return this.invoiceTimestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.invoiceTimestamp = timestamp;
	}

	public TGUser getTGuser() {
		return this.tguser;
	}

	public void setTGuser(TGUser tguser) {
		this.tguser = tguser;
	}

	public List<Ticket> getTickets() {
		return this.tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public String toString() {
		return "Invoice [invoiceId=" + this.id + ", timestamp=" + this.invoiceTimestamp + "]";
	}

}
