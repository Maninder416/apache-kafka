package com.optum.labs.kafka.kafka;

import com.optum.labs.kafka.model.User;
import com.optum.labs.kafka.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer {

    @Autowired
    UserRepository userRepository;

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(User user) {
        log.info("Message received :{}", user.toString());
        userRepository.save(user);
    }


}
