package com.optum.labs.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerService {

    @KafkaListener(topics = {"output-topic2"},groupId = "spring-boot-kafka")
    public void consume(ConsumerRecord<String, Long> record){
        System.out.println("recived: {}"+record.value() + "with key: "+record.key());
    }

}
