package com.example.projetosd.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

import static java.util.Objects.nonNull;

@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class SpringKafkaConfig {

    @Value("${spring.kafka.producer.acks:all}")
    private String producerAckMode;

    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        final var factory = new DefaultKafkaProducerFactory<String, String>(producerConfigs());
        return new KafkaTemplate<>(factory);
    }

    private Map<String, Object> producerConfigs() {
        final var producerConfig = kafkaProperties.buildProducerProperties(new DefaultSslBundleRegistry());

        if(nonNull(producerAckMode)) {
            log.info("msg=ack-producer-defined, value={}", producerAckMode);
            producerConfig.put(ProducerConfig.ACKS_CONFIG, producerAckMode);
        }
        return producerConfig;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        final var listenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        final var consumerProperties = kafkaProperties.buildConsumerProperties(new DefaultSslBundleRegistry());
        final var consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);
        listenerContainerFactory.setConsumerFactory(consumerFactory);

        return listenerContainerFactory;
    }
}
