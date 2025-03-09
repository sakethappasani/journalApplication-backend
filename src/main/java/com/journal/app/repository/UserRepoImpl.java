package com.journal.app.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.journal.app.model.User;
public class UserRepoImpl 
{
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<User> getUserForSA()
	{
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is("kicku@gmail.com"));
		query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
		List<User> users= mongoTemplate.find(query, User.class);
		return users;
	}
}
