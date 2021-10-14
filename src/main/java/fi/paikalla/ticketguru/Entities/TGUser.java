package fi.paikalla.ticketguru.Entities;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor

public class TGUser extends AbstractPersistable<Long> {
	
	@JsonIgnore
	private String firstName;
	@JsonIgnore
	private String lastName; 
	private String userName;
	@JsonIgnore // ei lähetetä kuitenkaan salasanaa tai auth tasoa clientille
	private String password; 
	@JsonIgnore
	private String auth; 	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tguser")
	@JsonIgnore
	private List<Invoice> invoices; 
	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	public TGUser(String firstName, String lastName, String userName, String password, String auth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		setPassword(password);
		this.auth = auth;
		this.invoices = new ArrayList<Invoice>(); 
	}
	
	public void setPassword(String pass) {
		this.password = PASSWORD_ENCODER.encode(pass); 
	}

}


	
