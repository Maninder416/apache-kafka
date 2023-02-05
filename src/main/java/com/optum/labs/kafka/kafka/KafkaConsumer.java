package com.optum.labs.kafka.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "optum-labs-topic",groupId = "myGroup")
    public void consumer(String message){
      log.info("Message received : {} ",message);
    }


}
