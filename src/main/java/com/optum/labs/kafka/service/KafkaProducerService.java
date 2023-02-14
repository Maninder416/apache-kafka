package com.optum.labs.kafka.service;

import com.optum.labs.kafka.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class KafkaProducerService {

    private static final String TOPIC = "user";

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    public void send(Order order){
     log.info("Order object is :{}",order);
     kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(),order);
    }
}
