package fi.paikalla.ticketguru.Entities;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TGUser extends AbstractPersistable<Long>{
	@JsonIgnore
	private String firstName;
	@JsonIgnore
	private String lastName; 
	private String userName;
	@JsonIgnore //ei lähetetä kuitenkaan salasanaa tai auth tasoa clientille. 
	private String password; 
	@JsonIgnore
	private String auth; 
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "TGuser")
	@JsonIgnore
	private List<Invoice> invoices; 

	public TGUser(String firstName, String lastName, String userName, String password, String auth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.auth = auth;
		this.invoices = new ArrayList<Invoice>(); 
	}
}


	
