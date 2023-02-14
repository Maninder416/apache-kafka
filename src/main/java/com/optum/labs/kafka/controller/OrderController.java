package com.optum.labs.kafka.controller;

import com.optum.labs.kafka.model.Order;
import com.optum.labs.kafka.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    KafkaProducerService producerService;

    @PostMapping("/post")
    public void order(@RequestBody Order order){
        System.out.println("ordeer is: ");
        System.out.println("ordeer is: "+order);
        producerService.send(order);
    }


}
