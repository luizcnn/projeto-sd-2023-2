package com.example.projetosd.entrypoint.kafka.consumer;

import com.example.projetosd.domain.Fruit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.UncheckedIOException;

import static com.example.projetosd.configuration.Topics.TopicsConstants.FRUIT_TOPIC_GROUP_ID;
import static com.example.projetosd.configuration.Topics.TopicsConstants.FRUIT_TOPIC_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class FruitConsumer {

    private final ObjectMapper mapper;

    @KafkaListener(topics = {FRUIT_TOPIC_NAME}, groupId = FRUIT_TOPIC_GROUP_ID)
    public void consume(ConsumerRecord<String, String> record) {
        try {
            final var msg = record.value();
            final var fruit = mapper.readValue(msg, Fruit.class);
            log.info(
                "status=consume-success, fruit={}, partition={}, offset={}",
                fruit.name(),
                record.partition(),
                record.offset()
            );
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }

    }

}
