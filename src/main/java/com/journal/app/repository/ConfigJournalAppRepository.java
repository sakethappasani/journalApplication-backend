package com.journal.app.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.journal.app.model.ConfigJournalApp;

@Repository
public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalApp, ObjectId>
{

}
