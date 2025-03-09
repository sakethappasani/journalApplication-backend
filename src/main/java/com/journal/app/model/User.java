package com.journal.app.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User 
{
	@Id
	private ObjectId id;
	@Indexed(unique = true)
	@NonNull
	private String userName;
	private String email;
	private boolean sentimentAnalysis;
	@NonNull
	private String password;
	@DBRef
	List<JournalEntry> journalEntries = new ArrayList<>();
	private List<String> roles;
}
