package com.journal.app.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.journal.app.model.ConfigJournalApp;
import com.journal.app.repository.ConfigJournalAppRepository;

import jakarta.annotation.PostConstruct;

@Component
public class AppCache 
{
	@Autowired
	private ConfigJournalAppRepository configJournalAppRepository;
	
	public Map<String, String> APP_CACHE = new HashMap<>();
	
	@PostConstruct
	public void init()
	{
		List<ConfigJournalApp> configList = configJournalAppRepository.findAll();
		for(ConfigJournalApp config : configList)
		{
			APP_CACHE.put(config.getKey(), config.getValue());
		}
	}
	
}
