package com.journal.app.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.journal.app.model.User;
import com.journal.app.repository.UserRepo;

@Service
public class UserService 
{
	@Autowired
	private UserRepo userRepo;
	
	private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	public void saveUser(User user)
	{
		userRepo.save(user);
	}
	
	public void saveAdmin(User user)
	{
		user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
		user.setRoles(Arrays.asList("USER", "ADMIN"));
		userRepo.save(user);
	}
	
	public boolean saveNewUser(User user)
	{
		try
		{
		user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
		user.setRoles(Arrays.asList("USER"));
		userRepo.save(user);
		return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public List<User> getAllUsers()
	{
		return userRepo.findAll();
	}
	
	public Optional<User> findById(ObjectId id)
	{
		return userRepo.findById(id);
	}
	
	public void deleteById(ObjectId id)
	{
		userRepo.deleteById(id);
	}
	
	public User findByUserName(String username)
	{
		return userRepo.findByUserName(username);
	}
	
}
