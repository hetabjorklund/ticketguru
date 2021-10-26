package fi.paikalla.ticketguru.Entities;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.*;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Event {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String name; // tapahtuman nimi, esim. 'Ruisrock' tai 'Savonlinnan Oopperajuhlat'
	private String address; // tapahtuman osoite
	private Integer maxCapacity; // maksimipaikkamäärä, tickets-listan koko ei voi olla tätä suurempi
	@FutureOrPresent
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") //https://stackoverflow.com/questions/28802544/java-8-localdate-jackson-format/38051405
	//@JsonDeserialize(using = LocalDateTimeDeserializer.class)//tämä Arrayhomma liittyy kai jotenkin objectMapperiin, tämä on yksi tapa korjata, mutta voi rikkoa muuta.
	//@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime startTime; // tapahtuman alkuaika (pvm ja kellonaika)
	@FutureOrPresent
	private LocalDateTime endTime; // tapahtuman loppuaika (pvm ja kellonaika)
	@FutureOrPresent
	private LocalDateTime endOfPresale; // ennakkomyynnin loppuminen (pvm ja kellonaika)
	@ManyToOne
	private EventStatus status; // tapahtuman status
	private String description; // tapahtuman kuvaus
	//private HashMap<TicketType, Double> ticketPrices; // tähän tilaisuuteen saatavilla olevat lipputyypit ja niiden hinnat
	@OneToMany(cascade = CascadeType.ALL, mappedBy="event") @JsonIgnore // koska muuten hakukatastrofi jos ei käytä api/events/-endpointtia
	private List<TicketType> ticketTypes;
	//@JsonIgnore //@OneToMany(cascade = CascadeType.ALL, mappedBy="event") // automaatti-api kestää ongelman, mutta normi pyörii ympyrää
	//private List<Ticket> tickets; 
	
	// konstruktori jossa kaikki parametrit
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
		//this.tickets = new ArrayList<Ticket>(); 
	} 
	
	// konstruktori jossa ei status-parametriä
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
		//this.tickets = new ArrayList<Ticket>();
	} 
}
