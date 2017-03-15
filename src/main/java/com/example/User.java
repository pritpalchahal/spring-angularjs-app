package com.example;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class User {
	private String userId;
	private String password;
	private String firstName;
	private String lastName;
	
	public User(String userId, String password, String firstname, String lastname){
		this.userId = userId;
		this.password = password;
		this.firstName = firstname;
		this.lastName = lastname;
	}
	
	public String getLastname() {
		return lastName;
	}
	public void setLastname(String lastname) {
		this.lastName = lastname;
	}
	public String getFirstname() {
		return firstName;
	}
	public void setFirstname(String firstname) {
		this.firstName = firstname;
	}
	public String getUsername() {
		return userId;
	}
	public void setUsername(String username) {
		this.userId = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
