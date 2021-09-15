package fi.paikalla.ticketguru.Classes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TicketUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	private String fName;
	private String lName;
	private String username;
	private String passhash;
	private String auth;
	/*@OneToMany(cascade = CascadeType.ALL, mappedBy="userId")
	private List<SalesEvent> salesEvent;*/
	
	public TicketUser() {}

	public TicketUser(String fName, String lName, String username, String passhash, String auth) {
		super();
		this.fName = fName;
		this.lName = lName;
		this.username = username;
		this.passhash = passhash;
		this.auth = auth;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasshash() {
		return passhash;
	}

	public void setPasshash(String passhash) {
		this.passhash = passhash;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	@Override
	public String toString() {
		return "TicketUser [userId=" + userId + ", fName=" + fName + ", lName=" + lName + ", username=" + username
				+ ", passhash=" + passhash + ", auth=" + auth + "]";
	}
	
}
