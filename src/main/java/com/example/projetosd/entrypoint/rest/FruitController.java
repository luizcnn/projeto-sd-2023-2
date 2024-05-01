package com.example.projetosd.entrypoint.rest;

import com.example.projetosd.entrypoint.kafka.producer.FruitProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fruits")
@RequiredArgsConstructor
public class FruitController {

    private final FruitProducer fruitProducer;

    @GetMapping
    public ResponseEntity<?> processFruit() {
        final var fruit = fruitProducer.produce();
        return ResponseEntity.ok(fruit);
    }

}
