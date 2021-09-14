package fi.paikalla.ticketguru.Classes;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventStatus extends AbstractPersistable<Long> {

	private String status;
}
