package com.journal.app.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.app.api.response.WeatherResponse;
import com.journal.app.model.User;
import com.journal.app.service.UserService;
import com.journal.app.service.WeatherService;

@RestController
@RequestMapping("/user")
public class UserController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private WeatherService weatherService;
	
	@PutMapping
	public ResponseEntity<?> updateUser(@RequestBody User user)
	{
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		User userInDb = userService.findByUserName(authenticatedUser.getName());
		if(userInDb != null)
		{
			userInDb.setUserName(user.getUserName());
			userInDb.setPassword(user.getPassword());
			userService.saveUser(userInDb);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteUser()
	{
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		User userInDb = userService.findByUserName(authenticatedUser.getName());
		if(userInDb != null)
		{
			ObjectId userId = userInDb.getId();
			userService.deleteById(userId);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping
	public ResponseEntity<?> greetings()
	{
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		WeatherResponse weatherReponse = weatherService.getWeather("Mumbai");
		String greeting = "";
		if(weatherReponse != null)
		{
			greeting = ", Weather Feels like "+ weatherReponse.getCurrent().getFeelslike();
		}
		return new ResponseEntity<>("Hi "+authenticatedUser.getName()+greeting, HttpStatus.OK);
	}
	
}
