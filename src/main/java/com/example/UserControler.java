package com.example;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControler {
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	@RequestMapping(value = "/api/users/{userId}",
			method = {RequestMethod.GET},
			consumes = MimeTypeUtils.ALL_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public User getUser(@PathVariable String userId){
		
		if(userId == null) return null;
		
		User user = this.userService.getByUsername(userId);
		
		if(user != null){
			return user;
		}
		return null;
	}

	@SuppressWarnings("serial")
	@RequestMapping(value = "/api/users",
			method = {RequestMethod.POST},
			consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String,String> addUser(@RequestBody Map<String,String> user){
		String firstName = user.get("firstName");
		String lastName = user.get("lastName");
		String username = user.get("username");
		String password = user.get("password");
		
		Map<String,String> result = new HashMap<String,String>(){

			{
				put("success",null);
				put("message","Username '"+ username + "' is already taken.");
			}
		};
		
		if(this.userService.isTaken(username)) return result;
		
		//TODO: validate names before adding
		this.userService.addUser(new User(username,password,firstName,lastName));
		
		result.put("success", "true");
		result.put("message", "Success");
		return result;
	}
}
