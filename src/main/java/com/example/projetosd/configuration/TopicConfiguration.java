package com.example.projetosd.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Arrays;

@Configuration
public class TopicConfiguration {

    @Bean
    public KafkaAdmin.NewTopics newTopics() {
        final var topics = Arrays.stream(Topics.values())
                .map(Topics::getTopic)
                .toArray(NewTopic[]::new);

        return new KafkaAdmin.NewTopics(topics);
    }
}
