package com.optum.labs.kafka.kafka;

import com.optum.labs.kafka.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JsonKafkaConsumer {

    @KafkaListener(topics = "#{'${spring.kafka.jsonTopic}'.split(',')}", groupId = "myGroup")
    public void consume(String user) {
        log.info("Json message received : {}", user.toString());
    }
}
