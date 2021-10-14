package fi.paikalla.ticketguru.Entities;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class EventStatus extends AbstractPersistable<Long> {	
	
	@NotBlank
	private String statusName; // tapahtuman tilanne: 'järjestetään', 'peruttu' tai 'siirretty'		
	@OneToMany(cascade = CascadeType.ALL, mappedBy="status")
	@JsonIgnore
	private List<Event> events;
	
	public EventStatus() {
		super();
		this.events = new ArrayList<Event>();
	}
	
	public EventStatus(String statusName) {
		super(); 
		this.statusName = statusName;
		this.events = new ArrayList<Event>();
	}
}