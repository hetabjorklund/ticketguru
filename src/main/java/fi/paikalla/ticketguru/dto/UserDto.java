package fi.paikalla.ticketguru.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {
	@NotBlank
	private String firstname;
	@NotBlank
	private String lastname; 
	@NotBlank
	private String username;
	@Size(min=8, max=16)
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
