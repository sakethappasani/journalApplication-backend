package com.journal.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.app.model.User;
import com.journal.app.service.UserService;

@RestController
@RequestMapping("public")
public class PublicController 
{
	
	@Autowired
	private UserService userService;
	
	@GetMapping("health-check")
	public String healthCheck()
	{
		return "Ok";
	}
	
	@PostMapping("create-user")
	public ResponseEntity<?> createUser(@RequestBody User user)
	{
		boolean userCreated = userService.saveNewUser(user);
		if(userCreated)
		{
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
