package com.optum.labs.kafka.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "#{'${spring.kafka.topic}'.split(',')}",groupId = "myGroup")
    public void consumer(String message){
      log.info("Message received : {} ",message);
    }


}
