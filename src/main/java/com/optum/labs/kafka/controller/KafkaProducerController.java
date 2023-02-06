package com.optum.labs.kafka.controller;

import com.optum.labs.kafka.model.User;
import com.optum.labs.kafka.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerController {
    @Autowired
    KafkaProducerService kafkaProducerService;

    @PostMapping("/producer")
    public ResponseEntity<String> sendMessage(@RequestBody User user){
        kafkaProducerService.send(user);
        return ResponseEntity.ok("Json message sent successfully");
    }
}
