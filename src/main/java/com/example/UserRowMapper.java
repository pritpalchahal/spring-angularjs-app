package com.example;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {
	
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			String userId = rs.getString("UserId");
			String password = rs.getString("Password");
			String firstName = rs.getString("FirstName");
			String lastName = rs.getString("LastName");
			
			User user = new User(userId,password,firstName,lastName);
			return user;
		}
}
