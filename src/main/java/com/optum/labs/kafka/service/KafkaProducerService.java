package com.optum.labs.kafka.service;


import com.optum.labs.kafka.model.User;
import com.optum.labs.kafka.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {

    @Value("${spring.kafka.topic}")
    private String topic;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    public void send(User user){
        log.info("Sending user object :{} ",user);
        User save = userRepository.save(user);
        kafkaTemplate.send(topic,save);
    }
}
