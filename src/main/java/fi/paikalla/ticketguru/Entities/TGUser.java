package fi.paikalla.ticketguru.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter(value = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor

public class TGUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	private String firstName;
	private String lastName; 
	private String userName;
	@ToString.Exclude 
	@JsonIgnore // ei lähetetä  salasanaa tai auth-tasoa clientille tai paljasteta toStringissä
	private String password; 
	@ToString.Exclude @JsonIgnore
	private String auth; 	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tguser")
	@JsonIgnore
	private List<Invoice> invoices; 

	public TGUser(String firstName, String lastName, String userName, String password, String auth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		setPassword(password);
		this.auth = auth;
		this.invoices = new ArrayList<Invoice>(); 
	}
	
	public TGUser(String firstName, String lastName, String userName, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		setPassword(password);
		this.auth = "ROLE_USER";
		this.invoices = new ArrayList<Invoice>(); 
	}
	
	private void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }
	
	public void updatePassword(String password) {
		this.password = PASSWORD_ENCODER.encode(password);
	}

}


	
