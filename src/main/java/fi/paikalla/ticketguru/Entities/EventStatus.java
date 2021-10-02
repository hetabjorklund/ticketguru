package fi.paikalla.ticketguru.Entities;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class EventStatus extends AbstractPersistable<Long> {	
	
	private String statusName; // tapahtuman tilanne: 'järjestetään', 'peruttu' tai 'siirretty'	
	/*
	@OneToMany(cascade = CascadeType.ALL, mappedBy="EventStatus")
	@JsonIgnore
	private Event event;
	*/
	public EventStatus() {
		super(); 
	}
	
	public EventStatus(String statusName) {
		super(); 
		this.statusName = statusName; 
		// TODO Auto-generated constructor stub
	}
}