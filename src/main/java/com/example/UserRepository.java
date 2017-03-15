package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
	
	private final String insertQuery = "INSERT INTO users (UserId,Password,FirstName,LastName) VALUES(?,?,?,?)";
	private final String checkQuery = "SELECT COUNT(*) FROM users WHERE UserId = ?";
	private final String getUserQuery = "SELECT * FROM users WHERE UserId = ?";
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
	 * Inserts user details into the users table.
	 * @param user
	 */
	public void addUser(User user){
		jdbcTemplate.update(insertQuery,user.getUsername(),user.getPassword(),user.getFirstname(),user.getLastname());
	}
	
	/**
	 * Checks if username already exists in system.
	 * @param username
	 * @return
	 */
	public boolean isTaken(String username){
		//check if username is valid
		if(username == null) { return false; }
		
		Integer count = jdbcTemplate.queryForObject(checkQuery, new Object[]{username},Integer.class);
		return count == 1;
	}
	
	/**
	 * Retrieves given user from table if possible.
	 * @param username
	 * @return
	 */
	public User getByUsername (String username){
		//check if username exists or not
		if(!isTaken(username)) { return null; }
		
		User user = (User) jdbcTemplate.queryForObject(getUserQuery, new Object[]{username},new UserRowMapper());
		return user;
	}

}
