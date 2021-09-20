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
public class Event extends AbstractPersistable<Long> {
	
	private String name; // tapahtuman nimi, esim. 'Ruisrock' tai 'Savonlinnan Oopperajuhlat'
	private String address; // tapahtuman osoite
	private Integer maxCapacity; // maksimipaikkamäärä, tickets-listan koko ei voi olla tätä suurempi
	private LocalDateTime startTime; // tapahtuman alkuaika (pvm ja kellonaika)
	private LocalDateTime endTime; // tapahtuman loppuaika (pvm ja kellonaika)
	private LocalDateTime endOfPresale; // ennakkomyynnin loppuminen (pvm ja kellonaika)
	private EventStatus status; // tapahtuman status
	private String description; // tapahtuman kuvaus
	//@OneToMany(mappedBy="event")
	//private List<Ticket> tickets = new ArrayList<>(); // tätä voi vielä selventää esim. erittelemällä ennakkoon myydyt ja oviliput omille listoilleen	
	private HashMap<TicketType, Double> ticketPrices; // tähän tilaisuuteen saatavilla olevat lipputyypit ja niiden hinnat
	private List<TicketType> ticketTypes; 
}
