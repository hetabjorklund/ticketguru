package fi.paikalla.ticketguru.Classes;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;
import java.time.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event extends AbstractPersistable<Long> {
	
	private String name;
	private String address;
	private Integer maxSeats;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime endOfPresale;
	private EventStatus status;
	//@OneToMany(mappedBy="event")
	// private List<Ticket> tickets = new ArrayList<>();

}
