package com.example.projetosd.entrypoint.rest;

import com.example.projetosd.entrypoint.kafka.producer.FruitProducer;
import com.example.projetosd.entrypoint.vo.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

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

    @GetMapping("/{name}")
    public ResponseEntity<?> processFruitByName(@PathVariable String name) {
        try {
            final var fruit = fruitProducer.produce(name);
            return ResponseEntity.ok(fruit);
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiError.builder()
                            .errorMsg(e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .status(400)
                    );
        }
    }

}
