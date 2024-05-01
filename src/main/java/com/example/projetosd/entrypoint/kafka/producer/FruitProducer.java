package com.example.projetosd.entrypoint.kafka.producer;

import com.example.projetosd.domain.Fruit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.projetosd.configuration.Topics.TopicsConstants.FRUIT_TOPIC_NAME;

@Component
@RequiredArgsConstructor
public class FruitProducer {

    @Value("#{new Boolean('${kafka-ordered-producer:false}')}")
    private Boolean orderedProducer;
    private static final List<String> FRUITS;
    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    static {
        FRUITS = List.of("Banana", "Laranja", "Limao", "Morango", "Abacaxi", "Abacate", "Amora", "Figo");
    }

    public Fruit produce() {
        final var index = (int) (Math.random() * FRUITS.size());
        final var fruit = new Fruit(FRUITS.get(index));
        try {
            kafkaTemplate.send(
                    new ProducerRecord<>(
                        FRUIT_TOPIC_NAME,
                        orderedProducer ? fruit.name() : null,
                        mapper.writeValueAsString(fruit)
                    )
            );
            return fruit;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
