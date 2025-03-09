package com.journal.app.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.journal.app.model.User;

@Repository
public interface UserRepo extends MongoRepository<User, ObjectId>
{
	User findByUserName(String userName);
}
