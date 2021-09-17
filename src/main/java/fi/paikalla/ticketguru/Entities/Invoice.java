package fi.paikalla.ticketguru.Entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long invoiceId;
	private LocalDateTime timestamp;
	
	/* odottaa TGUser-luokkaa
	@ManyToOne
	@JoinColumn(name = "userId")
	private TGUser TGuser;
	*/
	
	

	public Invoice(Long invoiceId, LocalDateTime timestamp, TGUser tGuser) {
		super();
		this.invoiceId = invoiceId;
		this.timestamp = timestamp;
		TGuser = tGuser;
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

	@Override
	public String toString() {
		return "Invoice [invoiceId=" + invoiceId + ", timestamp=" + timestamp + "]";
	}
	
	
}
