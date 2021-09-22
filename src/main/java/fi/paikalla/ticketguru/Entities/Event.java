package fi.paikalla.ticketguru.Entities;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.*;
import java.util.HashMap;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Event extends AbstractPersistable<Long> {
	
	private String name; // tapahtuman nimi, esim. 'Ruisrock' tai 'Savonlinnan Oopperajuhlat'
	private String address; // tapahtuman osoite
	private Integer maxCapacity; // maksimipaikkamäärä, tickets-listan koko ei voi olla tätä suurempi
	private LocalDateTime startTime; // tapahtuman alkuaika (pvm ja kellonaika)
	private LocalDateTime endTime; // tapahtuman loppuaika (pvm ja kellonaika)
	private LocalDateTime endOfPresale; // ennakkomyynnin loppuminen (pvm ja kellonaika)
	@ManyToOne
	private EventStatus status; // tapahtuman status
	private String description; // tapahtuman kuvaus
	@OneToMany(cascade = CascadeType.ALL, mappedBy="event")
	@JsonIgnore //koska muuten hakukatastrofi jos ei käytä api/events/ endpointtia
	private List<TicketType> ticketTypes;
	
	public Event(String name, String address, Integer maxCapacity, LocalDateTime startTime, LocalDateTime endTime,
			LocalDateTime endOfPresale, EventStatus status, String description) {
		super();
		this.name = name;
		this.address = address;
		this.maxCapacity = maxCapacity;
		this.startTime = startTime;
		this.endTime = endTime;
		this.endOfPresale = endOfPresale;
		this.status = status;
		this.description = description;
		this.ticketTypes = new ArrayList<TicketType>(); 
	} 
	
	public Event(String name, String address, Integer maxCapacity, LocalDateTime startTime, LocalDateTime endTime,
			LocalDateTime endOfPresale, String description) {
		super();
		this.name = name;
		this.address = address;
		this.maxCapacity = maxCapacity;
		this.startTime = startTime;
		this.endTime = endTime;
		this.endOfPresale = endOfPresale;
		this.description = description;
		this.ticketTypes = new ArrayList<TicketType>();
	} 



}
