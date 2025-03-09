package com.journal.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class JournalAppApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(JournalAppApplication.class, args);
		System.out.println("Application Started");
	}
	
	@Bean
	 PlatformTransactionManager platformTransactionManager(MongoDatabaseFactory dbfactory)
	{
		return new MongoTransactionManager(dbfactory);
	}
	
	@Bean
	RestTemplate restTemplate() 
	{
		return new RestTemplate();
	}
	
}
