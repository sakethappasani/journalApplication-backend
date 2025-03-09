package com.journal.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.journal.app.model.JournalEntry;
import com.journal.app.model.User;
import com.journal.app.repository.JournalEntryRepo;

@Service
public class JournalEntryService 
{
	@Autowired
	private JournalEntryRepo journalEntryRepo;
	@Autowired
	private UserService userService;
	
	@Transactional
	public void saveEntry(JournalEntry journalEntry, String userName)
	{
		try
		{			
			User user = userService.findByUserName(userName);
			journalEntry.setDate(LocalDateTime.now());
			JournalEntry savedEntry = journalEntryRepo.save(journalEntry);
			user.getJournalEntries().add(savedEntry);
			userService.saveUser(user);
		}
		catch (Exception e) 
		{
			System.out.println(e);
			throw new RuntimeException("An error occurred", e);
		}
	}
	
	public void saveEntry(JournalEntry journalEntry)
	{
		journalEntryRepo.save(journalEntry);
	}
	
	public List<JournalEntry> getAll()
	{
		return journalEntryRepo.findAll();
	}
	
	public Optional<JournalEntry> findById(ObjectId id)
	{
		return journalEntryRepo.findById(id);
	}
	
	@Transactional
	public void deleteJournalById(ObjectId id, String userName)
	{
		try
		{
			User user = userService.findByUserName(userName);
			boolean removed = user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
			if(removed)
			{
				userService.saveUser(user);
				journalEntryRepo.deleteById(id);			
			}			
		}
		catch (Exception e)
		{
			System.out.println(e);
			throw new RuntimeException("An error occured while deleting the entry", e);
		}
	}
}
