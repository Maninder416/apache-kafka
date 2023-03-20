package com.optum.labs.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KafkaMessageConsumer {

    private List<String> results = new ArrayList<>();

    @KafkaListener(topics = "topic10", groupId = "${kafka.application-id}")
    public void consume(String message) {
        // process the message
        String result = processMessage(message);

        // add the result to the list
        results.add(result);
    }

    public List<String> getResults() {
        return results;
    }

    private String processMessage(String message) {
        // implement the message processing logic here
        return message.toUpperCase();
    }
}

