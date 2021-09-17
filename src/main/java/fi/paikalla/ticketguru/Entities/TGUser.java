package fi.paikalla.ticketguru.Entities;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TGUser extends AbstractPersistable<Long>{
	
	private String firstName;
	private String lastName; 
	private String userName; 
	private String password; 
	private String auth; 

}
