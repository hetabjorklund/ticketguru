package fi.paikalla.ticketguru.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Invoice {
	
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long invoiceId;
	private LocalDateTime timestamp; // aikaleima myyntitapahtumalle	
	@NotNull @ManyToOne //@JoinColumn(name = "TGUserId")
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
		this.timestamp = LocalDateTime.now(); // myyntiaika on automaattisesti nykyinen aika
		this.tickets = new ArrayList<Ticket>(); // lippulista on automaattisesti tyhjä lista; liput lisätään laskuun vasta kun ne luodaan/myydään
	}
	
	public Long getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
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
		return "Invoice [invoiceId=" + this.invoiceId + ", timestamp=" + this.timestamp + "]";
	}

}
