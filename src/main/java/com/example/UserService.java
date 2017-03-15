package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.UserRepository;

@Service
public class UserService {
	private UserRepository userRepository;
	
	@Autowired
	public void setUserRepository(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	public User getByUsername(String username){
		return this.userRepository.getByUsername(username);
	}
	
	public void addUser(User user){
		this.userRepository.addUser(user);
	}

	public boolean isTaken(String username){
		return this.userRepository.isTaken(username);
	}
}
