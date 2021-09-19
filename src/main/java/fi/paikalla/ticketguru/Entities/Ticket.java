package fi.paikalla.ticketguru.Entities;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.*;
import java.util.HashMap;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket extends AbstractPersistable<Long> {

	private Double price
	private boolean	used
	@ManyToOne
	private Event event
	private TicketType ticket
}
