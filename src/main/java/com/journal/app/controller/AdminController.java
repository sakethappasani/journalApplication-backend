package com.journal.app.controller;

import java.util.List;

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
@RequestMapping("admin")
public class AdminController 
{
	@Autowired
	private UserService userService;
	
	@GetMapping("all-users")
	public ResponseEntity<?> getAllUser()
	{
		List<User> usersList = userService.getAllUsers();
		if(usersList != null && !usersList.isEmpty())
		{			
			return new ResponseEntity<>(usersList, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("create-admin-user")
	public ResponseEntity<?> createUser(@RequestBody User user)
	{
		try
		{
			userService.saveAdmin(user);		
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
