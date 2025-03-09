package com.journal.app.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.app.model.JournalEntry;
import com.journal.app.model.User;
import com.journal.app.service.JournalEntryService;
import com.journal.app.service.UserService;

@RestController
@RequestMapping("journal")
public class JournalEntryController 
{
	
	@Autowired
	private JournalEntryService journalEntryService;
	@Autowired
	private UserService userService;

	
	@GetMapping
	public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser()
	{
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		String userName = authenticatedUser.getName();
		User userInDb = userService.findByUserName(userName);
		List<JournalEntry> entryList = userInDb.getJournalEntries();
		if(entryList != null && !entryList.isEmpty())
		{
			return new ResponseEntity<>(entryList, HttpStatus.FOUND);			
		}
		else
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);	
		}
	}
	
	@PostMapping
	public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry)
	{
		try
		{
			Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
			String userName = authenticatedUser.getName();
			journalEntryService.saveEntry(myEntry, userName);
			return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("id/{myId}")
	public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId)
	{
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		String userName = authenticatedUser.getName();
		User userInDb = userService.findByUserName(userName);
		List<JournalEntry> usersEntries =  userInDb.getJournalEntries().stream().filter(entry -> entry.getId().equals(myId)).collect(Collectors.toList());
		if(!usersEntries.isEmpty())
		{
			Optional<JournalEntry> entry = journalEntryService.findById(myId);
			if(entry.isPresent())
			{
				return new ResponseEntity<>(entry.get(), HttpStatus.FOUND);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("id/{myId}")
	public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId)
	{
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		String userName = authenticatedUser.getName();
		Optional<JournalEntry> entry = journalEntryService.findById(myId);
		if(entry.isPresent())
		{
			journalEntryService.deleteJournalById(myId, userName);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("id/{id}")
	public ResponseEntity<JournalEntry> updateJournalEntryById(@PathVariable ObjectId id ,@RequestBody JournalEntry newEntry)
	{
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		String userName = authenticatedUser.getName();
		User userInDb = userService.findByUserName(userName);
		List<JournalEntry> usersEntries =  userInDb.getJournalEntries().stream().filter(entry -> entry.getId().equals(id)).collect(Collectors.toList());
		if(!usersEntries.isEmpty())
		{
			Optional<JournalEntry> entry = journalEntryService.findById(id);
			if(entry.isPresent())
			{
				JournalEntry oldEntry = entry.get();
				oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
				oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());
				journalEntryService.saveEntry(oldEntry);
				return new ResponseEntity<>(oldEntry ,HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
