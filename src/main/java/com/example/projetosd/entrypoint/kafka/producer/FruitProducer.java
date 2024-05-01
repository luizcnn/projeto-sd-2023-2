package com.example.projetosd.entrypoint.kafka.producer;

import com.example.projetosd.domain.Fruit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
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
    @Value("${spring.kafka.producer.acks:all}")
    private String producerAckMode;

    private static final List<String> FRUITS;
    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MeterRegistry meterRegistry;

    static {
        FRUITS = List.of("Banana", "Laranja", "Limao", "Morango", "Abacaxi", "Abacate", "Amora", "Figo");
    }

    @Counted(value = "sd_fruit_producer_production", description = "Counter of all production messages")
    @Timed(value = "sd_fruit_producer_latency", description = "Latency of producer")
    public Fruit produce() {
        final var index = (int) (Math.random() * FRUITS.size());
        final var fruit = new Fruit(FRUITS.get(index));
        final var tag = Tag.of("ack-mode", producerAckMode);
        return meterRegistry.timer("sd_fruit_producer_production", List.of(tag)).record(() -> {
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
        });
    }

}
