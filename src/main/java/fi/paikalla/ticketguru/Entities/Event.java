package fi.paikalla.ticketguru.Entities;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event extends AbstractPersistable<Long> {
	
	private String name;
	private String address;
	private Integer capacity;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime endOfPresale;
	private EventStatus status;
	//@OneToMany(mappedBy="event") // tämä odottaa sitä, että on luotu Ticket-olio
	// private List<Ticket> tickets = new ArrayList<>();

}
