package com.journal.app.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.journal.app.service.EmailService;

@Service
public class KafkaSentimentConsumerService {

    private final EmailService emailService;

    public KafkaSentimentConsumerService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "sentiment-demo", groupId = "test-group")
    public void consume(KafkaSentiment  sentiment) {
        sendEmail(sentiment.getSentiment());
    }

    private void sendEmail(String sentiment) {
        emailService.sendEmail("sakethappasanii@gmail.com", "Kafka Test Mail", sentiment);
    }
}
