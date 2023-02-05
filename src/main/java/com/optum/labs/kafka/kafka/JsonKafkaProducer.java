package com.optum.labs.kafka.kafka;

import com.optum.labs.kafka.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JsonKafkaProducer {

    @Value("${spring.kafka.topic}")
    private String topicName;

    /**
     * we can use autowired but when we have only one parameter it's good to use
     * parametrized constructor to pass it instead of using autowired.
     * Passing 2 parameters. Key as String and value as Object as we
     * defined these properties in application.yml properties for kafka
     * json producer.
     */
    private KafkaTemplate<String, User> kafkaTemplate;

    public JsonKafkaProducer(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(User data){
        Message<User> message= MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC,topicName)
                .build();
        kafkaTemplate.send(message);
      log.info("***** Message sent ***** :{}",data);
    }
}
