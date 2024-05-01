package com.example.projetosd.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;

@Getter
@RequiredArgsConstructor
public enum Topics {

    FRUIT_TOPIC(
            TopicBuilder
                    .name(TopicsConstants.FRUIT_TOPIC_NAME)
                    .partitions(6)
                    .replicas(2)
                    .build()
    );

    private final NewTopic topic;

    public static class TopicsConstants {
        public static final String FRUIT_TOPIC_NAME = "fruit-topic";
        public static final String FRUIT_TOPIC_GROUP_ID = "sd-project_fruit-topic";
    }

}
