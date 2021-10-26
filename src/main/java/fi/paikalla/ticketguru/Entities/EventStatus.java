package fi.paikalla.ticketguru.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class EventStatus {	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	
}
