package com.journal.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService 
{
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendEmail(String to, String subject, String body)
	{
		try
		{
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(to);
			mail.setSubject(subject);
			mail.setText(body);
			log.error("Mail is ready to be sent");
			javaMailSender.send(mail);
			log.error("Mail is sent successfully");
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
