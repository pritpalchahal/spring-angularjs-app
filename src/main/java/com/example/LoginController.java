package com.example;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	@SuppressWarnings("serial")
	@RequestMapping(value = "/api/authenticate",
			method = {RequestMethod.POST},
			consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String,String> Authenticate(@RequestBody Map<String,String> vals){
		Map<String,String> result = new HashMap<String,String>(){
			{
				put("success",null);
				put("message","Username or password is incorrect");
			}
		};
		
		String username = vals.get("username");
		String password = vals.get("password");
		
		if(username == null || password==null) return result;
		
		User user = this.userService.getByUsername(username);
		
		if(user != null && user.getPassword().equals(password)){
			result.put("success", "true");
			result.put("message", "Success");
			return result;
		}
		return result;
	}
	
}
