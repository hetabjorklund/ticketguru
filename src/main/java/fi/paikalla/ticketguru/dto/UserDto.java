package fi.paikalla.ticketguru.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDto {
	@NotNull(message = "Please enter first name")
	private String firstname;
	@NotNull(message = "Please enter last name")
	private String lastname; 
	@NotNull(message = "Please enter username")
	private String username;
	@Size(min= 8, max= 16, message="Length between 8 and 16")
	private String password;
	
	
	
	public UserDto(String firstName, String lastName, String userName, String password) {
		super();
		this.firstname = firstName;
		this.lastname = lastName;
		this.username = userName;
		this.password = password;
	}



	public UserDto() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getFirstName() {
		return firstname;
	}



	public void setFirstName(String firstName) {
		this.firstname = firstName;
	}



	public String getLastName() {
		return lastname;
	}



	public void setLastName(String lastName) {
		this.lastname = lastName;
	}



	public String getUserName() {
		return username;
	}



	public void setUserName(String userName) {
		this.username = userName;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	

}
