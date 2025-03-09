package com.journal.app.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.journal.app.cache.AppCache;
import com.journal.app.enums.Sentiment;
import com.journal.app.kafka.KafkaSentiment;
import com.journal.app.model.JournalEntry;
import com.journal.app.model.User;
import com.journal.app.repository.UserRepoImpl;

@Component
public class UserScheduler {
    
    private final UserRepoImpl userRepo;
    private final AppCache appCache;
    private final KafkaTemplate<String, KafkaSentiment> kafkaTemplate;
    
    public UserScheduler(UserRepoImpl userRepo, AppCache appCache, KafkaTemplate<String, KafkaSentiment> kafkaTemplate) {
        this.userRepo = userRepo;
        this.appCache = appCache;
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendEmails() {
		List<User> userForSA = userRepo.getUserForSA();
		for(User user : userForSA) {
			List<JournalEntry> journalEntries = user.getJournalEntries();
			List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7,ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
			Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
			
			for(Sentiment sentiment: sentiments) {
				if(sentiment != null)
					sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment,0)+1);
			}
			Sentiment mostFrequentSentiment = null;
			int maxCount = 0;
			for(Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
				if(entry.getValue() > maxCount) {
					maxCount = entry.getValue();
					mostFrequentSentiment = entry.getKey();
				}
			}
            if (mostFrequentSentiment != null) {
                KafkaSentiment sentimentData = KafkaSentiment.builder()
                        .email(user.getEmail())
                        .sentiment(String.format("This is a sample mail from the Kafka Template by Saketh: %s", mostFrequentSentiment))
                        .build();

                kafkaTemplate.send("sentiment-demo", sentimentData.getEmail(), sentimentData);
            }
        }
    }
    
    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache() {
        appCache.init();
    }
}
