package fi.paikalla.ticketguru.Entities;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.*;
import java.util.HashMap;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event extends AbstractPersistable<Long> {
	
	private String name;
	private String address;
	private Integer maxCapacity;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime endOfPresale;
	private EventStatus status;
	private String description;
	//@OneToMany(mappedBy="event") // tämä odottaa sitä, että on luotu Ticket-olio
	// private List<Ticket> tickets = new ArrayList<>();	
	private HashMap<TicketType, Double> ticketPrices; // tähän tilaisuuteen saatavilla olevat lipputyypit ja niiden hinnat

}
