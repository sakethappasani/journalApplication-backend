package com.journal.app;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.journal.app.scheduler.UserScheduler;


@SpringBootTest
class JournalAppApplicationTests 
{
	@Autowired
	private UserScheduler userScheduler;
	
	@Test
	public void sendMail()
	{
		userScheduler.fetchUsersAndSendEmails();
	}
}
